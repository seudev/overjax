package com.infinityrefactoring.overjax.core.model;

import java.io.Serializable;
import java.util.List;

import com.infinityrefactoring.overjax.core.model.message.Message;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public interface ResponseWrapper extends Serializable {

    public Object getData();

    public List<Message> getErrors();

    public Object getMeta();

    public ResponseWrapper setData(Object data);

    public ResponseWrapper setErrors(List<Message> errors);

    public ResponseWrapper setMeta(Object meta);

}
