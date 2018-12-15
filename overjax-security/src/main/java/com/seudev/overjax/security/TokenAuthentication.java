package com.seudev.overjax.security;

import static com.seudev.overjax.security.AuthenticationType.AUTHENTICATED;
import static com.seudev.overjax.security.AuthenticationType.EXPIRED;
import static com.seudev.overjax.security.AuthenticationType.INVALID;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.util.Set;

public class TokenAuthentication {
    
    private static final TokenAuthentication LIBERTATED_ACCESS = new TokenAuthentication(AuthenticationType.LIBERATED_ACCESS, null, null, null, null);
    private static final TokenAuthentication NO_AUTHENTICATION = new TokenAuthentication(AuthenticationType.NO_AUTHENTICATION, null, null, null, null);
    private static final TokenAuthentication DENIED_ACCESS = new TokenAuthentication(AuthenticationType.DENIED_ACCESS, null, null, null, null);
    
    private final AuthenticationType RESULT_TYPE;
    private final Object TOKEN;
    private final String ERROR_DESCRIPTION;
    private final Exception EXCEPTION;
    private final Set<String> USER_ROLES;
    
    public static TokenAuthentication authenticated(Object token, Set<String> userRoles) {
        return new TokenAuthentication(AUTHENTICATED, token, null, null, userRoles);
    }
    
    public static TokenAuthentication deniedAccess() {
        return DENIED_ACCESS;
    }
    
    public static TokenAuthentication expired(Object token) {
        return new TokenAuthentication(EXPIRED, token, null, null, null);
    }
    
    public static TokenAuthentication invalid(Exception exception) {
        return invalid(null, exception);
    }
    
    public static TokenAuthentication invalid(Object token, Exception exception) {
        return invalid(token, null, exception);
    }
    
    public static TokenAuthentication invalid(Object token, String errorDescription, Exception exception) {
        return new TokenAuthentication(INVALID, token, errorDescription, exception, null);
    }
    
    public static TokenAuthentication liberatedAccess() {
        return LIBERTATED_ACCESS;
    }
    
    public static TokenAuthentication noAuthentication() {
        return NO_AUTHENTICATION;
    }
    
    private TokenAuthentication(AuthenticationType resultType, Object token, String errorDescription, Exception exception, Set<String> userRoles) {
        requireNonNull(resultType, "The result type is required");
        RESULT_TYPE = resultType;
        if (resultType == AUTHENTICATED) {
            TOKEN = requireNonNull(token, "The token is required");
            ERROR_DESCRIPTION = null;
            EXCEPTION = null;
            USER_ROLES = ((userRoles == null) ? emptySet() : unmodifiableSet(userRoles));
        } else {
            TOKEN = token;
            ERROR_DESCRIPTION = errorDescription;
            EXCEPTION = exception;
            USER_ROLES = emptySet();
        }
    }
    
    public String getErrorDescription() {
        return ERROR_DESCRIPTION;
    }
    
    public Exception getException() {
        return EXCEPTION;
    }
    
    public AuthenticationType getResultType() {
        return RESULT_TYPE;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getToken() {
        return (T) TOKEN;
    }
    
    public Set<String> getUserRoles() {
        return USER_ROLES;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TokenAuthentication [\n\tRESULT_TYPE=").append(RESULT_TYPE);
        
        if (TOKEN != null) {
            builder.append("\n\tTOKEN=\"").append(TOKEN).append("\"");
        }
        if (ERROR_DESCRIPTION != null) {
            builder.append("\n\tERROR_DETAIL=\"").append(ERROR_DESCRIPTION).append("\"");
        }
        if ((USER_ROLES != null) && (!USER_ROLES.isEmpty())) {
            builder.append("\n\tUSER_ROLES=").append(USER_ROLES);
        }
        if (EXCEPTION != null) {
            builder.append("\n\tEXCEPTION=\"").append(EXCEPTION.getMessage()).append("\"");
        }
        return builder.append("\n]").toString();
    }
    
}
