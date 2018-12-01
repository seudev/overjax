package com.seudev.overjax.config;

public class ConfigProperties {
    
    public static final String ENABLE_REQUEST_BODY_CACHE = "overjax.enable_request_body_cache";
    public static final String REQUEST_BODY_CACHE_BUFFER_SIZE = "overjax.request_body_cache_buffer_size";
    
    public static final String DEFAULT_RESPONSE_MEDIA_TYPE = "overjax.response.default.media_type";
    public static final String DEFAULT_RESPONSE_ENTITY = "overjax.response.default.entity";
    
    public static final String SHOW_STACK_TRACE = "overjax.stack_trace.show";
    public static final String SHOW_STACK_TRACE_FOR_STATUS_FAMILIES = "overjax.stack_trace.show.for_status_families";
    public static final String STACK_TRACE_TEMPLATE_KEY = "overjax.stack_trace.template.key";
    public static final String STACK_TRACE_TEMPLATE_TYPE = "overjax.stack_trace.template.type";
    
    public static final String RESPONSE_META_MESSAGES_KEY = "overjax.response.meta.messages.key";
    
    public static final String DEFAULT_LOCALES = "overjax.default_locales";

    public static final String MESSAGE_PREFIX = "overjax.message.prefix";
    public static final String MESSAGE_TITLE_SUFFIX = "overjax.message.title.suffix";
    public static final String MESSAGE_DETAIL_SUFFIX = "overjax.message.detail.suffix";
    public static final String MESSAGE_TYPE_SUFFIX = "overjax.message.type.suffix";
    
    private ConfigProperties() {}
    
}
