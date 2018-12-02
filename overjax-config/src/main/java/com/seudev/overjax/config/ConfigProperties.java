package com.seudev.overjax.config;

public class ConfigProperties {

    public static final String REQUEST_BODY_CACHE_ENABLED = "overjax.request.body.cache.enabled";
    public static final String REQUEST_BODY_CACHE_BUFFER_SIZE = "overjax.request.body.cache.buffer_size";

    public static final String DEFAULT_LOCALES = "overjax.default.locales";
    public static final String DEFAULT_RESPONSE_MEDIA_TYPE = "overjax.default.response.media_type";
    public static final String DEFAULT_RESPONSE_ENTITY = "overjax.default.response.entity";

    public static final String DELEGATE_EXCEPTION_CAUSE_TO_OTHER_MAPPER = "overjax.delegate.exception.cause.to_other_mapper";
    public static final String SHOW_STACK_TRACE = "overjax.stack.trace.show";
    public static final String SHOW_STACK_TRACE_FOR_STATUS_FAMILIES = "overjax.stack.trace.show.for_status_families";
    public static final String STACK_TRACE_TEMPLATE_KEY = "overjax.stack.trace.template.key";
    public static final String STACK_TRACE_TEMPLATE_TYPE = "overjax.stack.trace.template.type";
    
    public static final String MESSAGE_PREFIX = "overjax.message.prefix";
    public static final String MESSAGE_TITLE_SUFFIX = "overjax.message.title.suffix";
    public static final String MESSAGE_DETAIL_SUFFIX = "overjax.message.detail.suffix";
    public static final String MESSAGE_TYPE_SUFFIX = "overjax.message.type.suffix";
    
    public static final String RESPONSE_METADATA_MESSAGES_KEY = "overjax.response.metadata.messages.key";
    public static final String RESPONSE_METADATA_HOME_PAGE = "overjax.response.metadata.home_page";
    public static final String RESPONSE_METADATA_SUPPORT_URL = "overjax.response.metadata.support.url";
    public static final String RESPONSE_METADATA_SUPPORT_EMAIL = "overjax.response.metadata.support.email";
    public static final String RESPONSE_METADATA_SUPPORT_PHONE = "overjax.response.metadata.support.phone";
    public static final String RESPONSE_METADATA_APPLICATION_VERSION = "overjax.response.metadata.application_version";
    public static final String RESPONSE_METADATA_AUTHORS = "overjax.response.metadata.authors";
    public static final String RESPONSE_METADATA_COPYRIGHT = "overjax.response.metadata.copyright";

    private ConfigProperties() {}

}
