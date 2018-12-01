package com.seudev.overjax.core.filter;

import static com.seudev.overjax.config.ConfigProperties.DEFAULT_RESPONSE_ENTITY;
import static com.seudev.overjax.config.ConfigProperties.DEFAULT_RESPONSE_MEDIA_TYPE;
import static com.seudev.overjax.config.ProviderPriorities.RESPONSE_BODY_HANDLER;
import static java.util.logging.Level.FINEST;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
@ApplicationScoped
@Priority(RESPONSE_BODY_HANDLER)
public class ResponseBodyHandler implements ContainerResponseFilter {

    @Inject
    private Logger logger;

    @Inject
    @ConfigProperty(name = DEFAULT_RESPONSE_MEDIA_TYPE, defaultValue = APPLICATION_JSON)
    private MediaType defaultMediaType;
    
    @Inject
    @ConfigProperty(name = DEFAULT_RESPONSE_ENTITY, defaultValue = "")
    private String defaultEntity;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (responseContext.getStatus() != NO_CONTENT.getStatusCode()) {
            if (responseContext.getMediaType() == null) {
                if (logger.isLoggable(FINEST)) {
                    logger.finest("Using the default response media type: " + defaultMediaType);
                }
                responseContext.setEntity(responseContext.getEntity(), responseContext.getEntityAnnotations(), defaultMediaType);
            }
            
            if (responseContext.getEntity() == null) {
                if (logger.isLoggable(FINEST)) {
                    logger.finest("Using the default response entity: " + defaultEntity);
                }
                responseContext.setEntity(defaultEntity, responseContext.getEntityAnnotations(), responseContext.getMediaType());
            }
        }
    }

}
