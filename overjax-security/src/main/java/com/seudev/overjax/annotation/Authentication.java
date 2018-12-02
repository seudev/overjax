package com.seudev.overjax.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import com.seudev.overjax.security.AuthenticationType;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Authentication {

    AuthenticationType value();

}
