package Serializer;

import JSONParser.JSONValues.JSONObject;

import java.lang.reflect.Field;

public interface Serializer<T> {
    public abstract void deserialize(Field[] field, JSONObject object, T o);

    public abstract void serialize(Field[] field, T o, JSONObject object) throws IllegalAccessException;
}
