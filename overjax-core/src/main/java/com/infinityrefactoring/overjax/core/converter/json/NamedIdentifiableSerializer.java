package com.infinityrefactoring.overjax.core.converter.json;

import java.io.Serializable;

import javax.inject.Inject;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

import com.infinityrefactoring.util.Identifiable;
import com.infinityrefactoring.util.Named;
import com.infinityrefactoring.util.message.MessageInterpolator;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public abstract class NamedIdentifiableSerializer<T extends Named & Identifiable<?>> implements JsonbSerializer<T> {

    @Inject
    private MessageInterpolator messageInterpolator;

    @Override
    public void serialize(T obj, JsonGenerator generator, SerializationContext ctx) {
        if (obj != null) {
            generator.writeStartObject();
            Serializable id = obj.getId();
            if (id != null) {
                generator.write("id", id.toString());
            }
            String name = messageInterpolator.get(obj.getNameKey());
            if (name != null) {
                generator.write("name", name);
            }
            generator.writeEnd();
        }
    }

}
