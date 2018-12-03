package com.seudev.overjax.security.keycloak;

import static com.seudev.overjax.config.ConfigProperties.SECURITY_KEYCLOAK_DEPLOYMENT;
import static com.seudev.overjax.config.ConfigProperties.SECURITY_KEYCLOAK_RESOURCES_ACCESS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;

import com.seudev.overjax.security.ResourceSecurityInfo;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@ApplicationScoped
public class KeycloakConfig {

    private Map<String, KeycloakDeployment> DEPLOYMENTS = new HashMap<>();
    private Map<String, Set<String>> RESOURCE_ACCESS = new HashMap<>();

    @Inject
    private ResourceSecurityInfo resourceSecurityInfo;

    @Inject
    private Config config;

    public KeycloakDeployment getKeycloakDeployment() {
        String properyKey = resourceSecurityInfo.getAuthenticationProperty(SECURITY_KEYCLOAK_DEPLOYMENT, SECURITY_KEYCLOAK_DEPLOYMENT);
        KeycloakDeployment keycloakDeployment = DEPLOYMENTS.get(properyKey);
        if (keycloakDeployment == null) {
            String keycloakConfig = config.getValue(properyKey, String.class);
            InputStream in = new ByteArrayInputStream(keycloakConfig.getBytes(UTF_8));
            keycloakDeployment = KeycloakDeploymentBuilder.build(in);
            DEPLOYMENTS.put(properyKey, keycloakDeployment);
        }
        return keycloakDeployment;
    }

    public Set<String> getResourceAccess() {
        String properyKey = resourceSecurityInfo.getAuthenticationProperty(SECURITY_KEYCLOAK_RESOURCES_ACCESS, SECURITY_KEYCLOAK_RESOURCES_ACCESS);

        Set<String> resourceAccess = RESOURCE_ACCESS.get(properyKey);

        if (resourceAccess == null) {
            Optional<String> optional = config.getOptionalValue(properyKey, String.class);
            if (optional.isPresent()) {
                String resources = config.getValue(properyKey, String.class);

                resourceAccess = Stream.of(resources.split(","))
                        .map(String::trim)
                        .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
            } else {
                resourceAccess = emptySet();
            }
            RESOURCE_ACCESS.put(properyKey, resourceAccess);
        }
        return resourceAccess;
    }

}
