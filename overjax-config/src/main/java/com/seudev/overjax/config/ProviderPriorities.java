package com.seudev.overjax.config;

import static javax.ws.rs.Priorities.USER;

public final class ProviderPriorities {
    
    public static final int BEFORE_OVERJAX = 0;
    
    public static final int REQUEST_BODY_PROVIDER = 0;
    
    public static final int RESPONSE_BODY_HANDLER = USER + 1;
    
    public static final int RESPONSE_BODY_WRAPPER_HANDLER = 100;
    
    public static final int AFTER_OVERJAX = 10000;
    
    private ProviderPriorities() {}
    
}
