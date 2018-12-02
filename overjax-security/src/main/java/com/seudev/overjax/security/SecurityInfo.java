package com.seudev.overjax.security;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SecurityInfo {
    
    private TokenAuthentication tokenAuthentication;
    
    private TokenAuthorization tokenAuthorization;
    
    private Set<String> accessRoles;
    private Set<String> unmodifiableAccessRoles;

    public Set<String> getAccessRoles() {
        if (accessRoles == null) {
            return emptySet();
        }
        if (unmodifiableAccessRoles == null) {
            unmodifiableAccessRoles = unmodifiableSet(accessRoles);
        }
        return unmodifiableAccessRoles;
    }
    
    public TokenAuthentication getTokenAuthentication() {
        if (tokenAuthentication == null) {
            throw new IllegalStateException("The authentication still was not processed.");
        }
        return tokenAuthentication;
    }
    
    public TokenAuthorization getTokenAuthorization() {
        if (tokenAuthorization == null) {
            throw new IllegalStateException("The authorization still was not processed.");
        }
        return tokenAuthorization;
    }
    
    public boolean hasAllRoles(String... roles) {
        Set<String> userRoles = getTokenAuthentication().getUserRoles();
        if ((roles == null) || (roles.length == 0) || (roles.length > userRoles.size())) {
            return false;
        }
        for (String role : roles) {
            if (!userRoles.contains(role)) {
                return false;
            }
        }
        addAccessRole(roles);
        return true;
    }
    
    public boolean hasAnyRoles(String... roles) {
        Set<String> userRoles = getTokenAuthentication().getUserRoles();
        if ((roles == null) || (roles.length == 0)) {
            return false;
        }
        for (String role : roles) {
            if (userRoles.contains(role)) {
                addAccessRole(role);
                return true;
            }
        }
        return false;
    }
    
    void setTokenAuthentication(TokenAuthentication tokenAuthentication) {
        this.tokenAuthentication = tokenAuthentication;
    }

    void setTokenAuthorization(TokenAuthorization tokenAuthorization) {
        this.tokenAuthorization = tokenAuthorization;
    }

    private void addAccessRole(String... roles) {
        if (accessRoles == null) {
            accessRoles = new HashSet<>();
        }
        for (String role : roles) {
            accessRoles.add(role);
        }
    }

}
