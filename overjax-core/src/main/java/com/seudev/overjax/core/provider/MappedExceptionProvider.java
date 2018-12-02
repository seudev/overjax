package com.seudev.overjax.core.provider;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.core.Response;

import com.seudev.overjax.annotation.MappedException;

@RequestScoped
public class MappedExceptionProvider {
    
    private Map<Throwable, Response> mappedExceptions;

    @Produces
    @MappedException
    public Map<Throwable, Response> getMappedExceptions() {
        if (mappedExceptions == null) {
            mappedExceptions = new HashMap<>();
        }
        return mappedExceptions;
    }

}
