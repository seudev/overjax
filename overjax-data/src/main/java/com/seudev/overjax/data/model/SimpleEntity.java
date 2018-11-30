package com.seudev.overjax.data.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@MappedSuperclass
public abstract class SimpleEntity<T extends SimpleEntity<T>> extends AbstractEntity<T, UUID> {
    
    private static final long serialVersionUID = -1921331366284657616L;
    
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;
    
    @Version
    private int version;
    
    public SimpleEntity(Class<T> entityClass) {
        super(entityClass);
    }
    
    @Override
    public UUID getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
}
