package com.seudev.overjax.security;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.logging.Level.FINEST;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.literal.NamedLiteral;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;

import com.seudev.overjax.annotation.Authenticated;
import com.seudev.overjax.annotation.Authorize;
import com.seudev.overjax.annotation.Property;

@ApplicationScoped
public class ResourceSecurityInfoProvider {

    @Inject
    private CachedResourceSecurityInfoProvider provider;

    @Context
    private ResourceInfo resourceInfo;

    @Produces
    public ResourceSecurityInfo getResourceSecurityInfo() {
        Method resourceMethod = resourceInfo.getResourceMethod();
        return provider.getResourceSecurityInfo(resourceMethod.toString(), resourceMethod, resourceInfo.getResourceClass());
    }

    @ApplicationScoped
    @CacheDefaults(cacheName = "resource-security-info")
    public static class CachedResourceSecurityInfoProvider {

        @Inject
        private Logger logger;

        @CacheResult
        public ResourceSecurityInfo getResourceSecurityInfo(@CacheKey String resourceMethodName, Method resourceMethod, Class<?> resourceClass) {
            validateSecurityDefinition(resourceMethod);
            validateSecurityDefinition(resourceClass);
            validateSecurityDefinition(resourceMethod, resourceClass);

            ResourceSecurityInfo resourceSecurityInfo = getResourceSecurityInfo(resourceMethod, resourceMethod, resourceClass);

            if (logger.isLoggable(FINEST)) {
                logger.finest(format("Applied security info for the %s method:\n%s\n", resourceMethodName, resourceSecurityInfo));
            }
            return resourceSecurityInfo;
        }

        private ResourceSecurityInfo getResourceSecurityInfo(AnnotatedElement annotatedElement, Method resourceMethod, Class<?> resourceClass) {
            if (annotatedElement.isAnnotationPresent(DenyAll.class)) {
                return ResourceSecurityInfo.DENIED;
            } else if (annotatedElement.isAnnotationPresent(PermitAll.class)) {
                return ResourceSecurityInfo.PUBLIC;
            }
            Authenticated authenticated = annotatedElement.getAnnotation(Authenticated.class);
            if (authenticated != null) {
                Map<String, String> authenticationProperties = Stream.of(authenticated.properties())
                        .collect(toMap(Property::key, Property::value));

                Authorize authorize = resourceMethod.getAnnotation(Authorize.class);
                Map<String, String> authorizationProperties;
                String authorizeExpression;
                if (authorize == null) {
                    authorize = resourceClass.getAnnotation(Authorize.class);
                }

                if (authorize == null) {
                    authorizeExpression = null;
                    authorizationProperties = emptyMap();
                } else {
                    authorizeExpression = authorize.value();
                    authorizationProperties = Stream.of(authenticated.properties())
                            .collect(toMap(Property::key, Property::value));
                }

                return new ResourceSecurityInfo(false, false,
                        NamedLiteral.of(authenticated.provider().value()),
                        authorizeExpression,
                        authenticationProperties, authorizationProperties);
            } else if (annotatedElement == resourceMethod) {
                return getResourceSecurityInfo(resourceClass, resourceMethod, resourceClass);
            }
            return ResourceSecurityInfo.PUBLIC;
        }

        private void validateSecurityDefinition(AnnotatedElement annotatedElement) {
            DenyAll denyAll = annotatedElement.getAnnotation(DenyAll.class);
            PermitAll permitAll = annotatedElement.getAnnotation(PermitAll.class);

            if ((denyAll != null) && (permitAll != null)) {
                throw new InternalServerErrorException(format("The %s element has oposite annotations: @DenyAll and @PermitAll."
                        + "This annotations cannot be used simultaneously.", annotatedElement));
            }

            Authenticated authenticated = annotatedElement.getAnnotation(Authenticated.class);
            if ((authenticated != null) && (permitAll != null)) {
                throw new InternalServerErrorException(format("The %s element has oposite annotations: @Authenticated and @PermitAll."
                        + "This annotations cannot be used simultaneously.", annotatedElement));
            }
        }

        private void validateSecurityDefinition(Method resourceMethod, Class<?> resourceClass) {
            if ((resourceMethod.isAnnotationPresent(Authorize.class) || resourceClass.isAnnotationPresent(Authorize.class))
                    && ((!resourceMethod.isAnnotationPresent(Authenticated.class)) && (!resourceClass.isAnnotationPresent(Authenticated.class)))) {

                throw new InternalServerErrorException(format("The @Authorize annotation on the  %s method depends of the @Authenticated annotation, "
                        + "but it is not present. Use the @Authenticated annotation on this method or on the class.",
                        resourceMethod));
            }
        }

    }

}
