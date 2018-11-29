package com.infinityrefactoring.overjax.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
public class MultivaluedLinkedHashMap extends LinkedHashMap<Object, Object> {

    private static final long serialVersionUID = 5409183975561034787L;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean containsValue(Object value) {
        if (super.containsValue(value)) {
            return true;
        }
        
        return values().stream()
                .filter(v -> (v instanceof Collection))
                .map(v -> (Collection<Object>) v)
                .anyMatch(c -> c.contains(value));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object put(Object key, Object value) {
        Object oldValue = get(key);
        if (oldValue == null) {
            return super.put(key, value);
        }
        
        if (oldValue instanceof Collection<?>) {
            Collection<Object> values = (Collection<Object>) oldValue;
            values.add(value);
        } else {
            ArrayList<Object> list = new ArrayList<>();
            list.add(oldValue);
            list.add(value);
            super.put(key, list);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(Map<? extends Object, ? extends Object> m) {
        m.forEach(this::put);
    }
    
    public void putAll(Object key, Collection<?> values) {
        values.forEach(v -> put(key, v));
    }
    
    @Override
    public Object putIfAbsent(Object key, Object value) {
        if (!containsKey(key)) {
            return put(key, value);
        }
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object key, Object value) {
        if (super.remove(key, value)) {
            return true;
        }
        Object currentValue = get(key);
        if (currentValue instanceof Collection) {
            Collection<Object> values = (Collection<Object>) currentValue;
            return values.remove(value);
        }
        return false;
    }
    
}
