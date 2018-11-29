package com.seudev.overjax.core.model.message;

import java.io.Serializable;
import java.util.UUID;

import com.seudev.util.Identifiable;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public interface Message extends Serializable, Identifiable<UUID> {

    public String getCode();

    public String getDetail();
    
    public Object getMeta();

    public MessageSource getSource();

    public String getTitle();

    public MessageType getType();
    
    public Message setCode(String code);

    public Message setDetail(String detail);
    
    public Message setMeta(Object meta);

    public Message setSource(MessageSource source);

    public Message setTitle(String title);

    public Message setType(MessageType type);

}
