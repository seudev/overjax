package com.seudev.overjax.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import com.seudev.overjax.config.ConfigType;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Config {

	ConfigType value();

}
