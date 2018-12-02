package com.seudev.overjax.security;

import static javax.ws.rs.Priorities.AUTHORIZATION;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.seudev.overjax.annotation.Authorization;
import com.seudev.overjax.annotation.Authorize;
import com.seudev.util.el.ExpressionEvaluator;

@Provider
@Authorize("")
@RequestScoped
@Priority(AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    
    @Inject
    private Logger logger;
    
    @Context
    private UriInfo uriInfo;
    
    @Inject
    private ResourceSecurityInfo resourceSecurityInfo;
    
    @Inject
    private SecurityInfo securityInfo;
    
    @Inject
    private ExpressionEvaluator expressionEvaluator;
    
    @Inject
    @Authorization(true)
    private Event<TokenAuthorization> authorizedEvent;
    
    @Inject
    @Authorization(false)
    private Event<TokenAuthorization> unauthorizedEvent;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (resourceSecurityInfo.denyAll()) {
            denyRequest(requestContext);
        } else if (resourceSecurityInfo.permitAll()) {
            liberatedAccess();
        } else {
            String authorizeExpression = resourceSecurityInfo.getAuthorizeExpression();
            if (expressionEvaluator.evalAsBoolean(authorizeExpression)) {
                logger.fine("Authorization successful!");
                authorized();
            } else {
                logger.fine("Unsuccessful authorization.");
                abortWithUnauthorized(requestContext);
            }
        }
    }
    
    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        TokenAuthorization tokenAuthorization = new TokenAuthorization(uriInfo, false);
        securityInfo.setTokenAuthorization(tokenAuthorization);
        unauthorizedEvent.fire(tokenAuthorization);
        requestContext.abortWith(status(UNAUTHORIZED).build());
    }
    
    private void authorized() {
        TokenAuthorization tokenAuthorization = new TokenAuthorization(uriInfo, true);
        securityInfo.setTokenAuthorization(tokenAuthorization);
        authorizedEvent.fire(tokenAuthorization);
    }
    
    private void denyRequest(ContainerRequestContext requestContext) {
        logger.fine("Denied request");
        abortWithUnauthorized(requestContext);
    }
    
    private void liberatedAccess() {
        logger.fine("Liberated access");
        authorized();
    }
    
}
