package com.seudev.overjax.security.keycloak;

import static com.seudev.overjax.security.AuthenticationType.AUTHENTICATED;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;

import com.seudev.overjax.annotation.Authentication;
import com.seudev.overjax.security.AuthenticationProvider;
import com.seudev.overjax.security.TokenAuthentication;

@Named("keycloak")
@ApplicationScoped
public class KeycloakAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private Logger logger;

    @Inject
    private KeycloakConfig keycloakConfig;
    
    @Inject
    @Authentication(AUTHENTICATED)
    private Event<AccessToken> accessTokenEvent;
    
    @Override
    public String getRealm() {
        return keycloakConfig.getKeycloakDeployment().getRealm();
    }
    
    public void onAuthenticationEvent(@Observes @Authentication(AUTHENTICATED) TokenAuthentication tokenAuthentication) {
        Object token = tokenAuthentication.getToken();
        if (token instanceof AccessToken) {
            accessTokenEvent.fire((AccessToken) token);
        }
    }

    @Override
    public TokenAuthentication validate(String tokenString) throws Exception {
        KeycloakDeployment keycloakDeployment = keycloakConfig.getKeycloakDeployment();

        AccessToken token = AdapterTokenVerifier.verifyToken(tokenString, keycloakDeployment);
        if (token.getIssuedAt() < keycloakDeployment.getNotBefore()) {
            logger.fine("Expired token");
            return TokenAuthentication.expired(token);
        }
        Access realmAccess = token.getRealmAccess();
        Set<String> userRoles = new HashSet<>(realmAccess.getRoles());
        
        Set<String> resourceAccess = keycloakConfig.getResourceAccess();
        token.getResourceAccess().entrySet().stream()
                .filter(entry -> resourceAccess.contains(entry.getKey()))
                .forEach(entry -> userRoles.addAll(entry.getValue().getRoles()));

        return TokenAuthentication.authenticated(token, userRoles);
    }

}
