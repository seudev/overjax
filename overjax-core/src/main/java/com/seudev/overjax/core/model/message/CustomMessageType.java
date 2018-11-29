package com.seudev.overjax.core.model.message;

import java.util.Objects;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public class CustomMessageType implements MessageType {
    
    private static final long serialVersionUID = -2289979904631267170L;

    private final String ID;
    private final String NAME_KEY;
    private final int PRIORITY;
    private final boolean ERROR;

    public CustomMessageType(String id, String nameKey, int priority, boolean error) {
        ID = id;
        NAME_KEY = nameKey;
        PRIORITY = priority;
        ERROR = error;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof CustomMessageType) && Objects.equals(ID, ((CustomMessageType) obj).ID));
    }
    
    @Override
    public String getId() {
        return ID;
    }
    
    @Override
    public String getNameKey() {
        return NAME_KEY;
    }
    
    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
    
    @Override
    public boolean isError() {
        return ERROR;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append("CustomMessageType [ID=").append(ID)
                .append(", NAME_KEY=").append(NAME_KEY)
                .append(", PRIORITY=").append(PRIORITY)
                .append(", ERROR=").append(ERROR)
                .append("]").toString();
    }
    
}
