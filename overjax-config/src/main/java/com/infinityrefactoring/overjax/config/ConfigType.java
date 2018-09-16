package com.infinityrefactoring.overjax.config;

public enum ConfigType {

	JSONB,

	DEFAULT_MEDIA_TYPE,
	WRAP_MEDIA_TYPES,

	SHOW_STACK_TRACE,
	STACK_TRACE_ON_STATUS_FAMILY,
	STACK_TRACE_TEMPLATE,

	CACHE_REQUEST_BODY,
	CAHCE_REQUEST_BODY_WITH_BUFFER_SIZE,

	EL_PROCESSOR;

	private final String FULL_NAME;

	private ConfigType() {
		FULL_NAME = ConfigType.class.getName() + '.' + name();
	}

	@Override
	public String toString() {
		return FULL_NAME;
	}

}
