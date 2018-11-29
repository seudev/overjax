package com.seudev.overjax.core.mapper;

import static com.seudev.overjax.config.ConfigProperties.SHOW_STACK_TRACE;
import static com.seudev.overjax.config.ConfigProperties.SHOW_STACK_TRACE_FOR_STATUS_FAMILIES;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public abstract class AbstractThrowableMapper<E extends Throwable> implements ExceptionMapper<E> {

	@Inject
	@ConfigProperty(name = SHOW_STACK_TRACE, defaultValue = "false")
	private boolean showStackTrace;

	@Inject
	@ConfigProperty(name = SHOW_STACK_TRACE_FOR_STATUS_FAMILIES, defaultValue = "REDIRECTION,SERVER_ERROR")
	private List<Family> showStackTraceForStatusFamilies;

	@Inject
	protected StackTraceMapper stackTraceMapper;

	protected boolean showStackTrace(StatusType status) {
		return (showStackTrace && showStackTraceForStatusFamilies.contains(status.getFamily()));
	}

}
