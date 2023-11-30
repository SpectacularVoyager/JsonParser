package Serializer;

import JSONParser.JSONValues.JSONObject;

import java.lang.reflect.Field;

public interface Serializer<T> {
    void deserialize(Field[] field, JSONObject object, T o);

    void serialize(Field[] field, T o, JSONObject object) throws IllegalAccessException;
}
