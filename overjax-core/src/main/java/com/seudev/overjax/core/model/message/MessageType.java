package com.seudev.overjax.core.model.message;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

import java.io.Serializable;
import java.util.Comparator;

import com.seudev.util.Identifiable;
import com.seudev.util.Named;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public interface MessageType extends Serializable, Identifiable<String>, Named {

    public static final Comparator<MessageType> COMPARATOR = nullsLast(comparing(MessageType::getPriority));
    
    public int getPriority();
    
    public boolean isError();

}
