package com.seudev.overjax.core.model.message;

import com.seudev.util.Enumeration;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public enum DefaultMessageType implements MessageType, Enumeration<String, DefaultMessageType> {

    ERROR,
    WARNING,
    INFO,
    SUCCESS;
    
    @Override
    public String getId() {
        return name();
    }
    
    @Override
    public int getPriority() {
        return ordinal();
    }

    @Override
    public boolean isError() {
        return (this == ERROR);
    }

}
