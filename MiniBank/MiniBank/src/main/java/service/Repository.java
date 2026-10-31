package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import model.annotation.Id;

/** A small generic repository that uses an @Id field as each entity's key. */
public final class Repository<T> {
    private final HashMap<String, T> entities = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }
        String id = readId(entity);
        entities.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null.");
        }
        for (T item : items) {
            save(item);
        }
    }

    public T findById(String id) {
        return entities.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean delete(String id) {
        return entities.remove(id) != null;
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must contain at least one number.");
        }
        double total = 0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values cannot contain null.");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }

    private String readId(T entity) {
        Field idField = null;
        Class<?> type = entity.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (idField != null) {
                        throw new IllegalArgumentException("Entity has multiple @Id fields: "
                                + entity.getClass().getName());
                    }
                    idField = field;
                }
            }
            type = type.getSuperclass();
        }
        if (idField == null) {
            throw new IllegalArgumentException("Entity has no @Id field: "
                    + entity.getClass().getName());
        }

        try {
            idField.setAccessible(true);
            Object value = idField.get(entity);
            if (value == null) {
                throw new IllegalArgumentException("Entity @Id value cannot be null.");
            }
            return value.toString();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read the entity @Id field.", e);
        }
    }
}
