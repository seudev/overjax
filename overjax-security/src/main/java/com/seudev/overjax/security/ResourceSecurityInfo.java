package com.seudev.overjax.security;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.inject.literal.NamedLiteral;

public class ResourceSecurityInfo implements Serializable {
    
    private static final long serialVersionUID = -5411154247924380200L;

    static final ResourceSecurityInfo DENIED = new ResourceSecurityInfo(true, false, null, null, emptyMap(), emptyMap());
    static final ResourceSecurityInfo PUBLIC = new ResourceSecurityInfo(false, true, null, null, emptyMap(), emptyMap());
    
    private final boolean DENY_ALL;
    private final boolean PERMIT_ALL;
    private final NamedLiteral AUTHENTICATION_PROVIDER_QUALIFIER;
    private final String AUTHORIZE_EXPRESSION;
    private final Map<String, String> AUTHENTICATION_PROPERTIES;
    private final Map<String, String> AUTHORIZATION_PROPERTIES;

    public ResourceSecurityInfo() {
        this(true, false, null, null, emptyMap(), emptyMap());
    }
    
    ResourceSecurityInfo(
            boolean denyAll,
            boolean permitAll,
            NamedLiteral authenticationProviderQualifier,
            String authorizeExpression,
            Map<String, String> authenticationProperties,
            Map<String, String> authorizationProperties) {
        DENY_ALL = denyAll;
        PERMIT_ALL = permitAll;
        AUTHENTICATION_PROVIDER_QUALIFIER = authenticationProviderQualifier;
        AUTHORIZE_EXPRESSION = authorizeExpression;
        AUTHENTICATION_PROPERTIES = unmodifiableMap(authenticationProperties);
        AUTHORIZATION_PROPERTIES = unmodifiableMap(authorizationProperties);
    }
    
    public boolean denyAll() {
        return DENY_ALL;
    }

    public Map<String, String> getAuthenticationProperties() {
        return AUTHENTICATION_PROPERTIES;
    }

    public String getAuthenticationProperty(Object key) {
        return AUTHENTICATION_PROPERTIES.get(key);
    }

    public String getAuthenticationProperty(Object key, String defaultValue) {
        return AUTHENTICATION_PROPERTIES.getOrDefault(key, defaultValue);
    }
    
    public NamedLiteral getAuthenticationProviderQualifier() {
        return AUTHENTICATION_PROVIDER_QUALIFIER;
    }
    
    public Map<String, String> getAuthorizationProperties() {
        return AUTHORIZATION_PROPERTIES;
    }

    public String getAuthorizationProperty(Object key) {
        return AUTHORIZATION_PROPERTIES.get(key);
    }
    
    public String getAuthorizationProperty(Object key, String defaultValue) {
        return AUTHORIZATION_PROPERTIES.getOrDefault(key, defaultValue);
    }
    
    public String getAuthorizeExpression() {
        return AUTHORIZE_EXPRESSION;
    }

    public boolean permitAll() {
        return PERMIT_ALL;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append("ResourceSecurityInfo [DENY_ALL=").append(DENY_ALL)
                .append(", PERMIT_ALL=").append(PERMIT_ALL)
                .append(", AUTHENTICATION_PROVIDER_QUALIFIER=").append(AUTHENTICATION_PROVIDER_QUALIFIER)
                .append(", AUTHORIZE_EXPRESSION=").append(AUTHORIZE_EXPRESSION)
                .append("]").toString();
    }
    
}
