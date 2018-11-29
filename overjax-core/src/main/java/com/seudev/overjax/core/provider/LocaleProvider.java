package com.seudev.overjax.core.provider;

import static java.util.Collections.list;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.seudev.overjax.config.ConfigProperties;

@RequestScoped
public class LocaleProvider {

	@Inject
	private Logger logger;

	@Inject
	private HttpServletRequest request;

	@Inject
	@ConfigProperty(name = ConfigProperties.DEFAULT_LOCALES)
	private List<Locale> defaultLocales;

	@Produces
	public List<Locale> getLocales() {
		List<Locale> locales = list(request.getLocales());

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Request locales: " + locales);
		}
		defaultLocales.stream()
				.filter(l -> !locales.contains(l))
				.forEach(locales::add);

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Request locales + default locales: " + locales);
		}
		return locales;
	}

}
