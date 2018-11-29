package com.infinityrefactoring.overjax.core.converter.json;

import static com.infinityrefactoring.overjax.config.ConfigType.JSONB;
import static com.infinityrefactoring.overjax.config.ObserverPriorities.ENUMERATION_SERIALIZER_JSONB_CONFIG;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.Provider;

import com.infinityrefactoring.overjax.annotation.Config;
import com.infinityrefactoring.overjax.config.Configs;
import com.infinityrefactoring.util.Enumeration;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@Provider
@RequestScoped
public class EnumerationSerializer extends NamedIdentifiableSerializer<Enumeration<?, ?>> {

    @Inject
    private Logger logger;

    public void register(@Observes @Priority(ENUMERATION_SERIALIZER_JSONB_CONFIG) @Config(JSONB) JsonbConfig config) {
        config.withSerializers(this);
        Configs.log(logger, JSONB, "withSerializer", EnumerationSerializer.class.getName());
    }

}
