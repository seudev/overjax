package com.seudev.overjax.core.builder;

import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_APPLICATION_VERSION;
import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_AUTHORS;
import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_COPYRIGHT;
import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_HOME_PAGE;
import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_SUPPORT_EMAIL;
import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_SUPPORT_PHONE;
import static com.seudev.overjax.config.ConfigProperties.RESPONSE_METADATA_SUPPORT_URL;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.seudev.overjax.core.model.MultivaluedLinkedHashMap;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@RequestScoped
public class ResponseMetadataBuilder {

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_HOME_PAGE)
    private Optional<String> homePage;

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_SUPPORT_URL)
    private Optional<String> supportUrl;

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_SUPPORT_EMAIL)
    private Optional<String> supportEmail;

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_SUPPORT_PHONE)
    private Optional<String> supportPhone;

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_APPLICATION_VERSION)
    private Optional<String> applicationVersion;

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_AUTHORS)
    private Optional<String> authors;

    @Inject
    @ConfigProperty(name = RESPONSE_METADATA_COPYRIGHT)
    private Optional<String> copyright;

    private Serializable metadata;

    public ResponseMetadataBuilder() {
        metadata = new MultivaluedLinkedHashMap();
    }

    public Serializable getMetadata() {
        return metadata;
    }

    public MultivaluedLinkedHashMap getMetadataAsMultivaluedHashMap() {
        return (MultivaluedLinkedHashMap) metadata;
    }

    public ResponseMetadataBuilder put(Object key, Object value) {
        getMetadataAsMultivaluedHashMap().put(key, value);
        return this;
    }

    public ResponseMetadataBuilder putAll(Map<? extends Object, ? extends Object> m) {
        getMetadataAsMultivaluedHashMap().putAll(m);
        return this;
    }

    public ResponseMetadataBuilder putAll(Object key, Collection<?> values) {
        getMetadataAsMultivaluedHashMap().putAll(key, values);
        return this;
    }

    public void setMetadata(Serializable metadata) {
        this.metadata = metadata;
    }

    @PostConstruct
    private void postConstruct() {
        if (homePage.isPresent()) {
            put("homePage", homePage.get());
        }
        if (supportUrl.isPresent()) {
            put("supportUrl", supportUrl.get());
        }
        if (supportEmail.isPresent()) {
            put("supportEmail", supportEmail.get());
        }
        if (supportPhone.isPresent()) {
            put("supportPhone", supportPhone.get());
        }
        if (applicationVersion.isPresent()) {
            put("applicationVersion", applicationVersion.get());
        }
        if (authors.isPresent()) {
            List<String> list = Stream.of(authors.get().split(","))
                    .map(String::trim)
                    .collect(toList());
            put("authors", list);
        }
        if (copyright.isPresent()) {
            put("copyright", copyright.get());
        }
    }

}
