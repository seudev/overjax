package com.seudev.overjax.core.model.message;

import java.util.Objects;
import java.util.UUID;

import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.annotation.JsonbTypeSerializer;

import com.seudev.overjax.core.converter.json.MessageTypeSerializer;

@JsonbPropertyOrder({"id", "code", "type", "title", "detail", "source", "meta"})
public class DefaultMessage implements Message {
    
    private static final long serialVersionUID = 6787593725524066182L;
    
    private UUID id;
    
    private String code;
    
    @JsonbTypeSerializer(MessageTypeSerializer.class)
    private MessageType type;
    
    private String title;
    
    private String detail;
    
    private MessageSource source;
    
    private Object meta;
    
    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof DefaultMessage) && Objects.equals(id, ((DefaultMessage) obj).id));
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getDetail() {
        return detail;
    }
    
    @Override
    public UUID getId() {
        return id;
    }
    
    @Override
    public Object getMeta() {
        return meta;
    }
    
    @Override
    public MessageSource getSource() {
        return source;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public MessageType getType() {
        return type;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
    @Override
    public DefaultMessage setCode(String code) {
        this.code = code;
        return this;
    }
    
    @Override
    public DefaultMessage setDetail(String detail) {
        this.detail = detail;
        return this;
    }
    
    public DefaultMessage setId(UUID id) {
        this.id = id;
        return this;
    }
    
    @Override
    public DefaultMessage setMeta(Object meta) {
        this.meta = meta;
        return this;
    }
    
    @Override
    public DefaultMessage setSource(MessageSource source) {
        this.source = source;
        return this;
    }

    @Override
    public DefaultMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public DefaultMessage setType(MessageType type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Message [id=").append(id)
                .append(", code=").append(code)
                .append(", type=").append(type)
                .append(", title=").append(title)
                .append(", detail=").append(detail)
                .append(", source=").append(source)
                .append(", meta=").append(meta)
                .append("]").toString();
    }

}
