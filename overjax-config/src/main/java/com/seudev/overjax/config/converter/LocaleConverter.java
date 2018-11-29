package com.seudev.overjax.config.converter;

import static org.eclipse.microprofile.config.inject.ConfigProperty.UNCONFIGURED_VALUE;

import java.util.Locale;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public class LocaleConverter implements Converter<Locale> {

	@Override
	public Locale convert(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.equals(UNCONFIGURED_VALUE) || value.equals("null")) {
			return null;
		}
		return Locale.forLanguageTag(value);
	}

}
