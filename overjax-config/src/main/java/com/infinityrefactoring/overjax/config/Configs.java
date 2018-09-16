package com.infinityrefactoring.overjax.config;

import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.WARNING;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class Configs {

	private static final Logger LOGGER = Logger.getLogger(Configs.class.getName());

	public static <K, V> String getLogMessage(String configKey, Map<K, V> configDetails) {
		if ((configDetails == null) || configDetails.isEmpty()) {
			return configKey;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("\n").append(configKey).append(" = {\n    ");

		Iterator<Entry<K, V>> iterator = configDetails.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<K, V> entry = iterator.next();

			builder.append('"').append(entry.getKey()).append("\": \"").append(entry.getValue()).append('"');
			if (iterator.hasNext()) {
				builder.append(",\n    ");
			}
		}
		return builder.append("\n}").toString();
	}

	public static String getLogMessage(String configKey, Object... configDetails) {
		if ((configDetails.length % 2) != 0) {
			LOGGER.log(WARNING, "Malformatted configuration details for \"{0}\". Use example:\n"
					+ "Configs.getLogMessage(\"MyConfigKey\",\n"
					+ "\tconfigDetailKey1\", configDetailValue1,\n"
					+ "\tconfigDetailKey2\", configDetailValue2,\n"
					+ "\tconfigDetailKey3, configDetailValue3,\n"
					+ "\t...)", configKey);
		}

		if (configDetails.length == 0) {
			return configKey;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("\n").append(configKey).append(" = {\n    ");
		for (int i = 0; i < configDetails.length; i += 2) {
			builder.append('"').append(configDetails[i]).append("\": \"");
			if ((i + 1) < configDetails.length) {
				builder.append(configDetails[i + 1]);
			}
			builder.append('"');
			if ((i + 2) < configDetails.length) {
				builder.append(",\n    ");
			}
		}
		return builder.append("\n}").toString();
	}

	public static <K, V> void log(Logger logger, ConfigType configType, Map<K, V> configDetails) {
		log(logger, configType.toString(), configDetails);
	}

	public static void log(Logger logger, ConfigType configType, Object... configDetails) {
		log(logger, configType.toString(), configDetails);
	}

	public static <K, V> void log(Logger logger, String configKey, Map<K, V> configDetails) {
		if (logger.isLoggable(CONFIG)) {
			logger.config(getLogMessage(configKey, configDetails));
		}
	}

	public static void log(Logger logger, String configKey, Object... configDetails) {
		if (logger.isLoggable(CONFIG)) {
			logger.config(getLogMessage(configKey, configDetails));
		}
	}

	private Configs() {}

}
