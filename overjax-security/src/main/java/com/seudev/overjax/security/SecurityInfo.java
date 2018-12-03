package com.seudev.overjax.security;

import static com.seudev.overjax.config.ConfigType.EL_PROCESSOR;
import static com.seudev.overjax.config.ObserverPriorities.SECURITY_INFO_EL_PROCESSOR_CONFIG;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.el.ELProcessor;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.seudev.overjax.annotation.Config;
import com.seudev.overjax.config.Configs;

@RequestScoped
public class SecurityInfo {
    
    @Inject
    private Logger logger;
    
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
    
    public boolean hasAllRoles(Collection<String> roles) {
        Set<String> userRoles = getTokenAuthentication().getUserRoles();
        if ((roles == null) || roles.isEmpty() || (roles.size() > userRoles.size())) {
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

    public boolean hasAnyRoles(Collection<String> roles) {
        Set<String> userRoles = getTokenAuthentication().getUserRoles();
        if ((roles == null) || roles.isEmpty()) {
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
    
    public boolean hasRole(String role) {
        Set<String> userRoles = getTokenAuthentication().getUserRoles();
        if ((role != null) && userRoles.contains(role)) {
            addAccessRole(role);
            return true;
        }
        return false;
    }
    
    void setTokenAuthentication(TokenAuthentication tokenAuthentication) {
        this.tokenAuthentication = tokenAuthentication;
    }
    
    void setTokenAuthorization(TokenAuthorization tokenAuthorization) {
        this.tokenAuthorization = tokenAuthorization;
    }

    private void addAccessRole(Collection<String> roles) {
        if (accessRoles == null) {
            accessRoles = new HashSet<>();
        }
        for (String role : roles) {
            accessRoles.add(role);
        }
    }

    private void addAccessRole(String role) {
        if (accessRoles == null) {
            accessRoles = new HashSet<>();
        }
        accessRoles.add(role);
    }

    @SuppressWarnings("unused")
    private void register(@Observes @Priority(SECURITY_INFO_EL_PROCESSOR_CONFIG) @Config(EL_PROCESSOR) ELProcessor processor) {
        processor.defineBean("$", this);
        Configs.log(logger, EL_PROCESSOR, "defineBean", "$=" + SecurityInfo.class.getName());
    }

}
