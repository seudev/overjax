package com.infinityrefactoring.overjax.core.builder;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.infinityrefactoring.overjax.config.ConfigProperties;
import com.infinityrefactoring.overjax.core.model.message.DefaultMessage;
import com.infinityrefactoring.overjax.core.model.message.DefaultMessageSource;
import com.infinityrefactoring.overjax.core.model.message.DefaultMessageType;
import com.infinityrefactoring.overjax.core.model.message.Message;
import com.infinityrefactoring.overjax.core.model.message.MessageType;
import com.infinityrefactoring.util.message.MessageInterpolator;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@RequestScoped
public class MessageBuilder {
    
    @Inject
    @ConfigProperty(name = ConfigProperties.MESSAGE_PREFIX, defaultValue = "message.")
    private String messagePrefix;
    
    @Inject
    @ConfigProperty(name = ConfigProperties.MESSAGE_TITLE_SUFFIX, defaultValue = ".title")
    private String messageTitleSuffix;
    
    @Inject
    @ConfigProperty(name = ConfigProperties.MESSAGE_DETAIL_SUFFIX, defaultValue = ".detail")
    private String messageDetailSuffix;
    
    @Inject
    @ConfigProperty(name = ConfigProperties.MESSAGE_TYPE_SUFFIX, defaultValue = ".type")
    private String messageTypeSuffix;
    
    @Inject
    private MessageInterpolator messageInterpolator;
    
    @Inject
    private Map<String, MessageType> messageTypes;
    
    private List<Message> messages;

    public MessageBuilder() {
        messages = new ArrayList<>();
    }
    
    public void add(Message message) {
        messages.add(message);
    }
    
    public MessageBuilder add(String key, Object value) {
        messageInterpolator.add(key, value);
        return this;
    }
    
    public Message fromCode(String code) {
        return fromCode(code, true);
    }
    
    public Message fromCode(String code, boolean add) {
        String messagePrefix = getMessagePrefix(code);
        
        DefaultMessage message = new DefaultMessage();
        message.setId(UUID.randomUUID())
                .setCode(code)
                .setTitle(getMessageTitle(messagePrefix))
                .setDetail(getMessageDetail(messagePrefix));
        
        String typeName = getMessageTypeName(messagePrefix);
        if (typeName != null) {
            MessageType type = messageTypes.get(typeName);
            message.setType(type);
        }
        
        if (add) {
            messages.add(message);
        }
        return message;
    }
    
    public Message fromConstraintViolation(ConstraintViolation<?> constraintViolation) {
        return fromConstraintViolation(constraintViolation, true);
    }
    
    public Message fromConstraintViolation(ConstraintViolation<?> constraintViolation, boolean add) {
        String code = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String pointer = getMessageSourcePointer(constraintViolation);

        Message message = fromCode(code, add)
                .setSource(new DefaultMessageSource().setPointer(pointer));
        
        if (message.getTitle() == null) {
            message.setTitle(constraintViolation.getMessage());
        }

        if (message.getType() == null) {
            message.setType(DefaultMessageType.ERROR);
        }
        
        return message;
    }
    
    public List<Message> getErrorMessages() {
        return getMessages(m -> (m.getType() != null) && m.getType().isError());
    }

    public List<Message> getMessages() {
        return messages;
    }
    
    public List<Message> getMessages(Predicate<? super Message> filter) {
        List<Message> messages = new ArrayList<>(this.messages.size());
        Map<Message, Integer> map = new HashMap<>(this.messages.size());
        
        int i = 0;
        for (Message message : this.messages) {
            if (filter.test(message)) {
                messages.add(message);
                map.put(message, i++);
            }
        }

        messages.sort(comparing(Message::getType, MessageType.COMPARATOR)
                .thenComparing(map::get));
        
        return messages;
    }
    
    public List<Message> getMessagesExceptErrors() {
        return getMessages(m -> (m.getType() == null) || !m.getType().isError());
    }
    
    private String getMessageDetail(String messagePrefix) {
        return messageInterpolator.get(messagePrefix.concat(messageDetailSuffix), false);
    }

    private String getMessagePrefix(String code) {
        return messagePrefix.concat(code);
    }
    
    private String getMessageSourcePointer(ConstraintViolation<?> constraintViolation) {
        String source = constraintViolation.getPropertyPath().toString();
        return ("/" + source.replaceAll("[\\.\\[\\]]", "/").replaceAll("[\\/]{2,}", "/").replaceAll("\\/+$", ""));
    }
    
    private String getMessageTitle(String messagePrefix) {
        return messageInterpolator.get(messagePrefix.concat(messageTitleSuffix), false);
    }

    private String getMessageTypeName(String messagePrefix) {
        return messageInterpolator.get(messagePrefix.concat(messageTypeSuffix), false);
    }

}
