package main.JSONParser.FieldSerializer;

import main.JSONParser.JSONValues.JSONElement;

import java.lang.reflect.ParameterizedType;

@FunctionalInterface
public interface FieldSerializer<T> {
    JSONElement serialize(T o, Class<?> type, ParameterizedType parameterizedType);
}
