package com.infinityrefactoring.overjax.core.builder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import com.infinityrefactoring.overjax.core.model.MultivaluedLinkedHashMap;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@RequestScoped
public class ResponseMetadataBuilder {

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
}
