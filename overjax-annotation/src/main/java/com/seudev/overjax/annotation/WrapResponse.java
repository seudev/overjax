package com.seudev.overjax.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

@Documented
@NameBinding
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface WrapResponse {

}
