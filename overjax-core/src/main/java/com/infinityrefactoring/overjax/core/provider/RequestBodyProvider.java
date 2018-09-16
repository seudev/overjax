package com.infinityrefactoring.overjax.core.provider;

import static com.infinityrefactoring.overjax.config.ConfigProperties.ENABLE_REQUEST_BODY_CACHE;
import static com.infinityrefactoring.overjax.config.ConfigProperties.REQUEST_BODY_CACHE_BUFFER_SIZE;
import static com.infinityrefactoring.overjax.config.ProviderPriorities.REQUEST_BODY_PROVIDER;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.infinityrefactoring.overjax.annotation.RequestBody;

@Provider
@RequestScoped
@Priority(REQUEST_BODY_PROVIDER)
public class RequestBodyProvider implements ContainerRequestFilter {

	@Inject
	private Logger logger;

	@Inject
	@ConfigProperty(name = ENABLE_REQUEST_BODY_CACHE, defaultValue = "false")
	private boolean enableRequestBodyCache;

	@Inject
	@ConfigProperty(name = REQUEST_BODY_CACHE_BUFFER_SIZE, defaultValue = "512")
	private int requestBodyCachebuferSize;

	private byte[] requestBody;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (enableRequestBodyCache) {
			logger.finest("Caching the request requestBody.");

			requestBody = readBody(requestContext);
			requestContext.setEntityStream(new ByteArrayInputStream(requestBody));
		}
	}

	@Produces
	@RequestBody
	public byte[] getRequestBody() {
		if (requestBody == null) {
			logger.warning("Request body not readed.");
		}
		return requestBody;
	}

	@PreDestroy
	public void preDestroy() {
		requestBody = null;
	}

	private byte[] readBody(ContainerRequestContext requestContext) throws IOException {
		try (InputStream in = requestContext.getEntityStream()) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			int len;
			byte[] buffer = new byte[requestBodyCachebuferSize];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			return out.toByteArray();
		}
	}

}
