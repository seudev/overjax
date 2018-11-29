package com.infinityrefactoring.overjax.core.model.message;

import java.io.Serializable;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public interface MessageSource extends Serializable {
    
    public String getPointer();
    
    public MessageSource setPointer(String pointer);

}
