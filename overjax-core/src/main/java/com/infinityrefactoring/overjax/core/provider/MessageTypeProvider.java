package com.infinityrefactoring.overjax.core.provider;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;

import java.util.Collection;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.infinityrefactoring.overjax.core.model.message.DefaultMessageType;
import com.infinityrefactoring.overjax.core.model.message.MessageType;
import com.infinityrefactoring.util.data.Enums;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@ApplicationScoped
public class MessageTypeProvider {
    
    private final Map<String, MessageType> TYPES;
    
    @Inject
    public MessageTypeProvider(Instance<MessageType> messageTypes, Instance<Collection<MessageType>> collectionMessageTypes) {
        TYPES = concat(concat(messageTypes.stream(),
                collectionMessageTypes.stream().flatMap(c -> c.stream())),
                Enums.getValues(DefaultMessageType.class).stream())
                        .distinct()
                        .collect(toMap(MessageType::getId, identity()));
    }
    
    @Produces
    public Map<String, MessageType> getMessageTypes() {
        return TYPES;
    }
    
}
