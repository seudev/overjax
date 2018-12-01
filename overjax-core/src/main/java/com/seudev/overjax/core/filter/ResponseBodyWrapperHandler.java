package com.seudev.overjax.core.filter;

import static com.seudev.overjax.config.ConfigProperties.RESPONSE_META_MESSAGES_KEY;
import static com.seudev.overjax.config.ProviderPriorities.RESPONSE_BODY_WRAPPER_HANDLER;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.WARNING;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.seudev.overjax.annotation.WrapResponse;
import com.seudev.overjax.core.builder.MessageBuilder;
import com.seudev.overjax.core.builder.ResponseMetadataBuilder;
import com.seudev.overjax.core.model.DefaultResponseWrapper;
import com.seudev.overjax.core.model.ResponseWrapper;
import com.seudev.overjax.core.model.message.Message;

@Provider
@WrapResponse
@ApplicationScoped
@Priority(RESPONSE_BODY_WRAPPER_HANDLER)
public class ResponseBodyWrapperHandler implements ContainerResponseFilter {

    public static final String SKIP_RESPONSE_WRAPPER = "com.seudev.overjax.core.filter.ResponseBodyWrapperHandler.SKIP_RESPONSE_WRAPPER";
    
    @Inject
    private Logger logger;

    @Inject
    private MessageBuilder messageBuilder;

    @Inject
    private ResponseMetadataBuilder responseMetadataBuilder;
    
    @Inject
    @ConfigProperty(name = RESPONSE_META_MESSAGES_KEY, defaultValue = "messages")
    private String metaMessagesKey;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if ((!TRUE.equals(requestContext.getProperty(SKIP_RESPONSE_WRAPPER)))
                && (responseContext.getStatus() != NO_CONTENT.getStatusCode())
                && (!(responseContext.getEntity() instanceof ResponseWrapper))) {
            Object entity = responseContext.getEntity();
            if (logger.isLoggable(FINEST)) {
                logger.finest("Wrapping the response: " + entity);
            }
            DefaultResponseWrapper body = new DefaultResponseWrapper();
            body.setData(entity);
            List<Message> errors = messageBuilder.getErrorMessages();
            body.setErrors(errors.isEmpty() ? null : errors);
            
            Serializable metadata = responseMetadataBuilder.getMetadata();
            putMetaMessages(metadata);
            body.setMeta(metadata);
            body.setMeta(metadata);
            responseContext.setEntity(body);
        }
    }

    private void putMetaMessages(Serializable metadata) {
        List<Message> messages = messageBuilder.getMessagesExceptErrors();
        if (!messages.isEmpty()) {
            if (metadata instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> map = (Map<Object, Object>) metadata;
                if (map.containsKey(metaMessagesKey) && logger.isLoggable(WARNING)) {
                    logger.warning(format("The response metadata already contains a value to the message key \"%s\", then messages will be supressed.", metaMessagesKey));
                } else {
                    map.put(metaMessagesKey, messages);
                }
            } else if (logger.isLoggable(WARNING)) {
                logger.warning("The response metadata is not an instance of Map, then messages will be supressed.");
            }
        }
    }
    
}
