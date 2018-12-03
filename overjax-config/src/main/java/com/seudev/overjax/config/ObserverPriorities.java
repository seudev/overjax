package com.seudev.overjax.config;

public class ObserverPriorities {

    public static final int BEFORE_OVERJAX = 0;

    public static final int UUID_ADAPTER_JSONB_CONFIG = BEFORE_OVERJAX + 100;

    public static final int MESSAGE_TYPE_SERIALIZER_JSONB_CONFIG = BEFORE_OVERJAX + 200;

    public static final int ENUMERATION_SERIALIZER_JSONB_CONFIG = BEFORE_OVERJAX + 300;

    public static final int SECURITY_INFO_EL_PROCESSOR_CONFIG = BEFORE_OVERJAX + 400;

    public static final int AFTER_OVERJAX = 10000;

    private ObserverPriorities() {}

}
