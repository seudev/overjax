package com.seudev.overjax.data.model;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.seudev.util.Identifiable;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@MappedSuperclass
public abstract class AbstractEntity<T extends AbstractEntity<T, I>, I extends Serializable> implements Identifiable<I>, Serializable {

    private static final long serialVersionUID = 1897140936071670057L;
    
    @Transient
    @JsonbTransient
    private final Class<T> ENTITY_CLASS;

    public AbstractEntity(Class<T> entityClass) {
        ENTITY_CLASS = requireNonNull(entityClass);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (ENTITY_CLASS.isInstance(obj)) {
            return Objects.equals(getId(), ENTITY_CLASS.cast(obj).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
