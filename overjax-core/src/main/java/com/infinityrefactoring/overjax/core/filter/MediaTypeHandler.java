package com.infinityrefactoring.overjax.core.filter;

import static com.infinityrefactoring.overjax.config.ConfigProperties.DEFAULT_MEDIA_TYPE;
import static com.infinityrefactoring.overjax.config.ProviderPriorities.MEDIA_TYPE_HANDLER;
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
@Priority(MEDIA_TYPE_HANDLER)
public class MediaTypeHandler implements ContainerResponseFilter {

	@Inject
	private Logger logger;

	@Inject
	@ConfigProperty(name = DEFAULT_MEDIA_TYPE, defaultValue = APPLICATION_JSON)
	private MediaType defaultMediaType;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if ((responseContext.getStatus() != NO_CONTENT.getStatusCode()) && (responseContext.getMediaType() == null)) {
			if (logger.isLoggable(FINEST)) {
				logger.finest("Using the default media type: " + defaultMediaType);
			}
			responseContext.setEntity(responseContext.getEntity(), responseContext.getEntityAnnotations(), defaultMediaType);
		}
	}

}
