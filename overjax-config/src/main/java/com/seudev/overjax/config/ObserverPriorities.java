package com.seudev.overjax.config;

public class ObserverPriorities {

    public static final int BEFORE_OVERJAX = 0;

    public static final int UUID_ADAPTER_JSONB_CONFIG = BEFORE_OVERJAX + 1;

    public static final int MESSAGE_TYPE_SERIALIZER_JSONB_CONFIG = BEFORE_OVERJAX + 2;

    public static final int ENUMERATION_SERIALIZER_JSONB_CONFIG = BEFORE_OVERJAX + 3;

    public static final int AFTER_OVERJAX = 10000;

    private ObserverPriorities() {}

}
