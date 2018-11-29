package com.infinityrefactoring.overjax.core.mapper;

import static java.lang.String.format;
import static java.util.logging.Level.FINE;
import static java.util.stream.Collectors.joining;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;

import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.infinityrefactoring.overjax.annotation.ExceptionHandling;
import com.infinityrefactoring.overjax.core.builder.MessageBuilder;

@Provider
@ApplicationScoped
public class ConstraintViolationMapper extends AbstractThrowableMapper<ConstraintViolationException> {
    
    private static final Status STATUS = PRECONDITION_FAILED;
    
    @Inject
    private Logger logger;
    
    @Inject
    private MessageBuilder messageBuilder;
    
    @Inject
    @ExceptionHandling
    private Event<ConstraintViolationException> constraintViolationExceptionEvent;
    
    @Override
    public Response toResponse(ConstraintViolationException ex) {
        if (logger.isLoggable(FINE)) {
            logger.log(FINE, getConcatenatedConstraintViolations(ex.getConstraintViolations()), ex);
        }
        
        constraintViolationExceptionEvent.fireAsync(ex);
        
        if (showStackTrace(STATUS)) {
            return stackTraceMapper.toResponse(ex, STATUS, getConcatenatedConstraintViolations(ex.getConstraintViolations()));
        }
        
        ex.getConstraintViolations().forEach(messageBuilder::fromConstraintViolation);
        
        return Response.status(STATUS).build();
    }
    
    private String getConcatenatedConstraintViolations(Set<ConstraintViolation<?>> constraintViolations) {
        String concatenatedConstraintViolations = constraintViolations.stream().map(Object::toString).collect(joining(",\n    "));
        return format("ConstraintViolations = [\n    %s\n]", concatenatedConstraintViolations);
    }
    
}
