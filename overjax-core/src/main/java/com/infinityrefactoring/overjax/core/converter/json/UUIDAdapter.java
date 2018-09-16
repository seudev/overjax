package com.infinityrefactoring.overjax.core.converter.json;

import static com.infinityrefactoring.overjax.config.ConfigType.JSONB;
import static com.infinityrefactoring.overjax.config.ObserverPriorities.UUID_ADAPTER_JSONB_CONFIG;
import static java.util.logging.Level.FINEST;

import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import javax.ws.rs.ext.Provider;

import com.infinityrefactoring.overjax.annotation.Config;
import com.infinityrefactoring.overjax.config.Configs;

@Provider
@ApplicationScoped
public class UUIDAdapter implements JsonbAdapter<UUID, String> {

	@Inject
	private Logger logger;

	@Override
	public UUID adaptFromJson(String uuid) throws Exception {
		if (logger.isLoggable(FINEST)) {
			logger.finest("Parsing String to UUID: " + uuid);
		}
		return ((uuid == null) ? null : UUID.fromString(uuid));
	}

	@Override
	public String adaptToJson(UUID uuid) throws Exception {
		if (logger.isLoggable(FINEST)) {
			logger.finest("Parsing UUID to String: " + uuid);
		}
		return ((uuid == null) ? null : uuid.toString());
	}

	public void register(@Observes @Priority(UUID_ADAPTER_JSONB_CONFIG) @Config(JSONB) JsonbConfig config) {
		config.withAdapters(this);
		Configs.log(logger, JSONB, "withAdapter", UUIDAdapter.class.getName());
	}

}
