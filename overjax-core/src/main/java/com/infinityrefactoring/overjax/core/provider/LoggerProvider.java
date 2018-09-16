package com.infinityrefactoring.overjax.core.provider;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class LoggerProvider {

	public static Logger getLogger(Class<?> c) {
		return Logger.getLogger(c.getName());
	}

	@Produces
	public Logger getLogger(InjectionPoint injectionPoint) {
		Class<?> c = injectionPoint.getMember().getDeclaringClass();
		return getLogger(c);
	}

}
