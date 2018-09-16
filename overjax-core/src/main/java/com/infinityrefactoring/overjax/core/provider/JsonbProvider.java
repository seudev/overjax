package com.infinityrefactoring.overjax.core.provider;

import static com.infinityrefactoring.overjax.config.ConfigType.JSONB;
import static javax.json.bind.JsonbConfig.BINARY_DATA_STRATEGY;
import static javax.json.bind.JsonbConfig.ENCODING;
import static javax.json.bind.JsonbConfig.FORMATTING;
import static javax.json.bind.JsonbConfig.NULL_VALUES;
import static javax.json.bind.JsonbConfig.PROPERTY_NAMING_STRATEGY;
import static javax.json.bind.JsonbConfig.PROPERTY_ORDER_STRATEGY;
import static javax.json.bind.JsonbConfig.STRICT_IJSON;

import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.infinityrefactoring.overjax.annotation.Config;
import com.infinityrefactoring.overjax.config.Configs;

@Provider
@ApplicationScoped
public class JsonbProvider implements ContextResolver<Jsonb> {

	@Inject
	private Logger logger;

	@Default
	@Produces
	@Config(JSONB)
	private Jsonb jsonb;

	@Inject
	@Config(JSONB)
	private Event<JsonbConfig> jsonbConfigEvent;

	@Inject
	@ConfigProperty(name = ENCODING)
	private Optional<String> encoding;

	@Inject
	@ConfigProperty(name = FORMATTING)
	private Optional<Boolean> formatting;

	@Inject
	@ConfigProperty(name = STRICT_IJSON)
	private Optional<Boolean> strictIjson;

	@Inject
	@ConfigProperty(name = NULL_VALUES)
	private Optional<Boolean> nullValues;

	@Inject
	@ConfigProperty(name = PROPERTY_NAMING_STRATEGY)
	private Optional<String> propertyNamingStrategy;

	@Inject
	@ConfigProperty(name = PROPERTY_ORDER_STRATEGY)
	private Optional<String> propertyOrderStrategy;

	@Inject
	@ConfigProperty(name = BINARY_DATA_STRATEGY)
	private Optional<String> binaryDataStrategy;

	@Override
	public Jsonb getContext(Class<?> type) {
		return jsonb;
	}

	@PostConstruct
	public void postConstruct() {
		JsonbConfig jsonbConfig = new JsonbConfig();

		if (encoding.isPresent()) {
			jsonbConfig.withEncoding(encoding.get());
		}

		if (formatting.isPresent()) {
			jsonbConfig.withFormatting(formatting.get());
		}

		if (strictIjson.isPresent()) {
			jsonbConfig.withStrictIJSON(strictIjson.get());
		}

		if (nullValues.isPresent()) {
			jsonbConfig.withNullValues(nullValues.get());
		}

		if (propertyNamingStrategy.isPresent()) {
			jsonbConfig.withPropertyNamingStrategy(propertyNamingStrategy.get());
		}

		if (propertyOrderStrategy.isPresent()) {
			jsonbConfig.withPropertyOrderStrategy(propertyOrderStrategy.get());
		}

		if (binaryDataStrategy.isPresent()) {
			jsonbConfig.withBinaryDataStrategy(binaryDataStrategy.get());
		}

		Configs.log(logger, JSONB, jsonbConfig.getAsMap());

		jsonbConfigEvent.fire(jsonbConfig);
		jsonb = JsonbBuilder.create(jsonbConfig);
	}

}
