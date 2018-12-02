package com.seudev.overjax.core.mapper;

import static com.seudev.overjax.config.ConfigProperties.SHOW_STACK_TRACE;
import static com.seudev.overjax.config.ConfigProperties.SHOW_STACK_TRACE_FOR_STATUS_FAMILIES;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.seudev.overjax.annotation.MappedException;

public abstract class AbstractThrowableMapper<E extends Throwable> implements ExceptionMapper<E> {

    @Inject
    private Logger logger;
    
    @Inject
    @ConfigProperty(name = SHOW_STACK_TRACE, defaultValue = "false")
    private boolean showStackTrace;

    @Inject
    @ConfigProperty(name = SHOW_STACK_TRACE_FOR_STATUS_FAMILIES, defaultValue = "REDIRECTION,SERVER_ERROR")
    private List<Family> showStackTraceForStatusFamilies;

    @Inject
    protected StackTraceMapper stackTraceMapper;

    @Inject
    @MappedException
    private Map<Throwable, Response> mappedExceptions;

    @Override
    public Response toResponse(E ex) {
        if (mappedExceptions.containsKey(ex)) {
            logger.finest("Skipping mapping of already mapped exception.");
            return mappedExceptions.get(ex);
        }
        Response response = handle(ex);
        mappedExceptions.put(ex, response);
        return response;
    }
    
    protected abstract Response handle(E ex);
    
    protected boolean showStackTrace(StatusType status) {
        return (showStackTrace && showStackTraceForStatusFamilies.contains(status.getFamily()));
    }
    
}
