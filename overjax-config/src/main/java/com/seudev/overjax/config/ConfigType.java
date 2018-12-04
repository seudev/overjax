package com.seudev.overjax.config;

import com.seudev.util.data.Enums;

public enum ConfigType {
    
    JSONB,
    EL_PROCESSOR;
    
    @Override
    public String toString() {
        return Enums.getFullName(this);
    }
    
}
