package com.seudev.overjax.security;

import static java.util.Objects.requireNonNull;

import javax.ws.rs.core.UriInfo;

public class TokenAuthorization {

    private final String ENDPOINT;
    private final boolean AUTHORIZED;

    TokenAuthorization(UriInfo uriInfo, boolean authorized) {
        ENDPOINT = requireNonNull(uriInfo, "The uriInfo is required").getPath();
        AUTHORIZED = authorized;
    }

    public String getEndpoint() {
        return ENDPOINT;
    }

    public boolean isAuthorized() {
        return AUTHORIZED;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append("TokenAuthorization [ENDPOINT=").append(ENDPOINT)
                .append(", AUTHORIZED=").append(AUTHORIZED)
                .append("]").toString();
    }

}
