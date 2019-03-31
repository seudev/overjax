package com.seudev.overjax.security;

import static com.seudev.overjax.security.AuthenticationType.AUTHENTICATED;
import static com.seudev.overjax.security.AuthenticationType.DENIED_ACCESS;
import static com.seudev.overjax.security.AuthenticationType.EXPIRED;
import static com.seudev.overjax.security.AuthenticationType.INVALID;
import static com.seudev.overjax.security.AuthenticationType.NO_AUTHENTICATION;
import static com.seudev.overjax.security.AuthenticationType.PUBLIC_ACCESS;
import static com.seudev.overjax.security.TokenAuthentication.deniedAccess;
import static com.seudev.overjax.security.TokenAuthentication.invalid;
import static com.seudev.overjax.security.TokenAuthentication.noAuthentication;
import static java.util.logging.Level.FINE;
import static javax.ws.rs.Priorities.AUTHENTICATION;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.WWW_AUTHENTICATE;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.seudev.overjax.annotation.Authenticated;
import com.seudev.overjax.annotation.Authentication;

@Provider
@Authenticated
@RequestScoped
@Priority(AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    
    @Inject
    private Logger logger;
    
    @Inject
    private ResourceSecurityInfo resourceSecurityInfo;
    
    @Inject
    private SecurityInfo securityInfo;

    @Inject
    private Instance<AuthenticationProvider> authenticationProvider;
    
    @Inject
    @Authentication(DENIED_ACCESS)
    private Event<TokenAuthentication> deniedAccessEvent;
    
    @Inject
    @Authentication(PUBLIC_ACCESS)
    private Event<TokenAuthentication> publicAccessEvent;
    
    @Inject
    @Authentication(NO_AUTHENTICATION)
    private Event<TokenAuthentication> noAuthenticationEvent;
    
    @Inject
    @Authentication(INVALID)
    private Event<TokenAuthentication> invalidTokenEvent;
    
    @Inject
    @Authentication(EXPIRED)
    private Event<TokenAuthentication> expiredTokenEvent;
    
    @Inject
    @Authentication(AUTHENTICATED)
    private Event<TokenAuthentication> authenticatedUserEvent;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (resourceSecurityInfo.denyAll()) {
            denyRequest(requestContext);
        } else if (resourceSecurityInfo.permitAll()) {
            publicAccess();
        } else {
            AuthenticationProvider authenticationProvider = getAuthenticationProvider();
            
            if (logger.isLoggable(FINE)) {
                logger.log(FINE, "AuthenticationProvider: {0}", authenticationProvider.getClass().getName());
            }
            
            try {
                String tokenString = authenticationProvider.getToken(requestContext.getHeaderString(AUTHORIZATION));
                if ((tokenString == null) || tokenString.isEmpty()) {
                    abortWithUnauthorized(requestContext, authenticationProvider, noAuthentication());
                } else {
                    TokenAuthentication tokenAuthentication = authenticationProvider.validate(tokenString);
                    AuthenticationType resultType = tokenAuthentication.getResultType();

                    switch (resultType) {
                        case AUTHENTICATED:
                            logger.fine("Authentication successful");
                            securityInfo.setTokenAuthentication(tokenAuthentication);
                            authenticatedUserEvent.fire(tokenAuthentication);
                            break;
                        case DENIED_ACCESS:
                            denyRequest(requestContext);
                            break;
                        case PUBLIC_ACCESS:
                            publicAccess();
                            break;
                        default:
                            abortWithUnauthorized(requestContext, authenticationProvider, tokenAuthentication);
                            break;
                    }
                }
            } catch (Exception ex) {
                abortWithUnauthorized(requestContext, authenticationProvider, invalid(ex));
            }
        }
    }
    
    private void abortWithUnauthorized(ContainerRequestContext requestContext, AuthenticationProvider authenticationProvider, TokenAuthentication tokenAuthentication) {
        securityInfo.setTokenAuthentication(tokenAuthentication);
        
        AuthenticationType resultType = tokenAuthentication.getResultType();
        if (resultType == AUTHENTICATED) {
            throw new IllegalArgumentException("Invalid result type.");
        }
        
        if (logger.isLoggable(FINE)) {
            logger.fine(tokenAuthentication.toString());
            
            Throwable throwable = tokenAuthentication.getException();
            if (throwable != null) {
                logger.log(FINE, throwable.getMessage(), throwable);
            }
            logger.fine("Aborting request");
        }
        
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(authenticationProvider.getAuthenticationScheme())
                .append(" realm=\"").append(authenticationProvider.getRealm()).append("\"")
                .append(", error=\"").append(resultType).append("\"");
        
        String errorDetail = tokenAuthentication.getErrorDescription();
        if (errorDetail != null) {
            headerBuilder.append(", error_description=\"").append(errorDetail).append("\"");
        }
        
        Response response = status(UNAUTHORIZED)
                .header(WWW_AUTHENTICATE, headerBuilder.toString())
                .build();
        
        switch (resultType) {
            case NO_AUTHENTICATION:
                noAuthenticationEvent.fire(tokenAuthentication);
            case INVALID:
                invalidTokenEvent.fire(tokenAuthentication);
                break;
            case EXPIRED:
                expiredTokenEvent.fire(tokenAuthentication);
                break;
        }
        requestContext.abortWith(response);
    }
    
    private void denyRequest(ContainerRequestContext requestContext) {
        logger.fine("Denied request");
        TokenAuthentication tokenAuthentication = deniedAccess();
        securityInfo.setTokenAuthentication(tokenAuthentication);
        deniedAccessEvent.fire(tokenAuthentication);
        requestContext.abortWith(status(UNAUTHORIZED).build());
    }
    
    private AuthenticationProvider getAuthenticationProvider() {
        Named qualifier = resourceSecurityInfo.getAuthenticationProviderQualifier();
        if ((qualifier == null) || qualifier.value().isEmpty()) {
            return authenticationProvider.get();
        }
        return authenticationProvider.select(qualifier).get();
    }
    
    private void publicAccess() {
        logger.fine("Public access");
        TokenAuthentication tokenAuthentication = TokenAuthentication.publicAccess();
        securityInfo.setTokenAuthentication(tokenAuthentication);
        publicAccessEvent.fire(tokenAuthentication);
    }
    
}
