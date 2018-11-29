package com.seudev.overjax.core.converter.json;

import static com.seudev.overjax.config.ConfigType.JSONB;
import static com.seudev.overjax.config.ObserverPriorities.MESSAGE_TYPE_SERIALIZER_JSONB_CONFIG;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.Provider;

import com.seudev.overjax.annotation.Config;
import com.seudev.overjax.config.Configs;
import com.seudev.overjax.core.model.message.MessageType;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@Provider
@RequestScoped
public class MessageTypeSerializer extends NamedIdentifiableSerializer<MessageType> {

    @Inject
    private Logger logger;

    public void register(@Observes @Priority(MESSAGE_TYPE_SERIALIZER_JSONB_CONFIG) @Config(JSONB) JsonbConfig config) {
        config.withSerializers(this);
        Configs.log(logger, JSONB, "withSerializer", MessageTypeSerializer.class.getName());
    }
    
}
