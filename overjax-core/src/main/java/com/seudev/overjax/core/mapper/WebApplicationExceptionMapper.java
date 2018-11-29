package com.seudev.overjax.core.mapper;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.Provider;

import com.seudev.overjax.annotation.ExceptionHandling;

@Provider
@ApplicationScoped
public class WebApplicationExceptionMapper extends AbstractThrowableMapper<WebApplicationException> {
    
    @Inject
    private Logger logger;
    
    @Inject
    @ExceptionHandling
    private Event<ClientErrorException> clientErrorExceptionEvent;
    
    @Inject
    @ExceptionHandling
    private Event<RedirectionException> redirectionExceptionEvent;
    
    @Inject
    @ExceptionHandling
    private Event<ServerErrorException> serverErrorExceptionEvent;
    
    @Override
    public Response toResponse(WebApplicationException ex) {
        if (ex instanceof ClientErrorException) {
            logger.log(FINE, ex.getMessage(), ex);
            clientErrorExceptionEvent.fireAsync((ClientErrorException) ex);
        } else if (ex instanceof RedirectionException) {
            logger.log(WARNING, ex.getMessage(), ex);
            redirectionExceptionEvent.fireAsync((RedirectionException) ex);
        } else if (ex instanceof ServerErrorException) {
            logger.log(SEVERE, ex.getMessage(), ex);
            serverErrorExceptionEvent.fireAsync((ServerErrorException) ex);
        }
        
        Response response = ex.getResponse();
        StatusType status = response.getStatusInfo();
        
        if (showStackTrace(status)) {
            return stackTraceMapper.toResponse(ex, status, ex.getMessage());
        }
        return response;
    }
    
}
