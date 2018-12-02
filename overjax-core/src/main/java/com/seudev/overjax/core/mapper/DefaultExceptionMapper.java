package com.seudev.overjax.core.mapper;

import static com.seudev.overjax.config.ConfigProperties.DELEGATE_EXCEPTION_CAUSE_TO_OTHER_MAPPER;
import static java.util.logging.Level.SEVERE;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.seudev.overjax.annotation.ExceptionHandling;

@Provider
@ApplicationScoped
public class DefaultExceptionMapper extends AbstractThrowableMapper<Exception> {
    
    private static final Status STATUS = INTERNAL_SERVER_ERROR;
    
    @Inject
    private Logger logger;
    
    @Inject
    @ConfigProperty(name = DELEGATE_EXCEPTION_CAUSE_TO_OTHER_MAPPER, defaultValue = "true")
    private boolean delegateExceptionCauseToOtherMapper;
    
    @Inject
    @ExceptionHandling
    private Event<Exception> exceptionEvent;
    
    @Inject
    private ConstraintViolationMapper constraintViolationMapper;
    
    @Inject
    private WebApplicationExceptionMapper webApplicationExceptionMapper;

    @Override
    protected Response handle(Exception ex) {
        logger.log(SEVERE, ex.getMessage(), ex);
        exceptionEvent.fireAsync(ex);
        
        if (delegateExceptionCauseToOtherMapper) {
            Throwable cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                logger.finest("Delegating the exception cause to the ConstraintViolationMapper.");
                return constraintViolationMapper.toResponse((ConstraintViolationException) cause);
            } else if (cause instanceof WebApplicationException) {
                logger.finest("Delegating the exception cause to the WebApplicationExceptionMapper.");
                return webApplicationExceptionMapper.toResponse((WebApplicationException) cause);
            }
        }
        
        if (showStackTrace(STATUS)) {
            return stackTraceMapper.toResponse(ex, STATUS, ex.getMessage());
        }
        return Response.status(STATUS).build();
    }

}
