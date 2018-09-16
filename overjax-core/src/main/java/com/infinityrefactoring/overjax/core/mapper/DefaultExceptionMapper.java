package com.infinityrefactoring.overjax.core.mapper;

import static java.util.logging.Level.SEVERE;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.infinityrefactoring.overjax.annotation.ExceptionHandling;

@Provider
@ApplicationScoped
public class DefaultExceptionMapper extends AbstractThrowableMapper<Exception> {

	private static final Status STATUS = INTERNAL_SERVER_ERROR;

	@Inject
	private Logger logger;

	@Inject
	@ExceptionHandling
	private Event<Exception> exceptionEvent;

	@Override
	public Response toResponse(Exception ex) {
		logger.log(SEVERE, ex.getMessage(), ex);

		exceptionEvent.fireAsync(ex);

		if (showStackTrace(STATUS)) {
			return stackTraceMapper.toResponse(ex, STATUS, ex.getMessage());
		}
		return Response.status(STATUS).build();
	}

}
