package Serializer;

import JSONParser.JSONValues.JSONObject;

import java.lang.reflect.Field;

public abstract class Serializer<T> {
    public abstract Object deserialize(Field[] field,JSONObject object, Class<?> clazz);

    public abstract JSONObject serialize(Field[] field, Object o);
}
