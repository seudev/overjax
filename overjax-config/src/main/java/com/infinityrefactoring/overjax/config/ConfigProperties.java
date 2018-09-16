package com.infinityrefactoring.overjax.config;

public class ConfigProperties {

	public static final String ENABLE_REQUEST_BODY_CACHE = "overjax.enable_request_body_cache";
	public static final String REQUEST_BODY_CACHE_BUFFER_SIZE = "overjax.request_body_cache_buffer_size";

	public static final String DEFAULT_MEDIA_TYPE = "overjax.default_media_type";
	public static final String WRAP_MEDIA_TYPES = "overjax.wrap_media_types";

	public static final String SHOW_STACK_TRACE = "overjax.stack_trace.show";
	public static final String SHOW_STACK_TRACE_FOR_STATUS_FAMILIES = "overjax.stack_trace.show.for_status_families";
	public static final String STACK_TRACE_TEMPLATE_KEY = "overjax.stack_trace.template.key";
	public static final String STACK_TRACE_TEMPLATE_TYPE = "overjax.stack_trace.template.type";

	public static final String DEFAULT_LOCALES = "overjax.default_locales";

	private ConfigProperties() {}

}
