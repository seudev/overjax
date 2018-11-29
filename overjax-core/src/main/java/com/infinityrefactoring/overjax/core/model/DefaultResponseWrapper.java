package com.infinityrefactoring.overjax.core.model;

import java.util.List;

import com.infinityrefactoring.overjax.core.model.message.Message;

public class DefaultResponseWrapper implements ResponseWrapper {
    
    private static final long serialVersionUID = -4873934144266548013L;
    
    private Object data;
    private List<Message> errors;
    private Object meta;
    
    @Override
    public Object getData() {
        return data;
    }
    
    @Override
    public List<Message> getErrors() {
        return errors;
    }
    
    @Override
    public Object getMeta() {
        return meta;
    }

    @Override
    public DefaultResponseWrapper setData(Object data) {
        this.data = data;
        return this;
    }
    
    @Override
    public DefaultResponseWrapper setErrors(List<Message> errors) {
        this.errors = errors;
        return this;
    }
    
    @Override
    public DefaultResponseWrapper setMeta(Object meta) {
        this.meta = meta;
        return this;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append("DefaultResponseWrapper [data=").append(data)
                .append(", errors=").append(errors)
                .append(", meta=").append(meta)
                .append("]").toString();
    }
    
}
