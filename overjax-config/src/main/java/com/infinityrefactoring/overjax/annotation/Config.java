package com.infinityrefactoring.overjax.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import com.infinityrefactoring.overjax.config.ConfigType;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Config {

	ConfigType value();

}
