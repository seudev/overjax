package com.seudev.overjax.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;
import javax.ws.rs.NameBinding;

@Stereotype
@Documented
@NameBinding
@Target(TYPE)
@RequestScoped
@Retention(RUNTIME)
public @interface Controller {

}
