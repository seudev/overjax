package com.infinityrefactoring.overjax.core.model.message;

public class DefaultMessageSource implements MessageSource {

    private static final long serialVersionUID = -2876244346216356839L;
    
    private String pointer;

    @Override
    public String getPointer() {
        return pointer;
    }

    @Override
    public DefaultMessageSource setPointer(String pointer) {
        this.pointer = pointer;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorSource [pointer=" + pointer + "]";
    }

}
