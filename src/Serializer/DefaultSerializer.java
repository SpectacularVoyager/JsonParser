package Serializer;

import JSONParser.JSONValues.JSONObject;

import java.lang.reflect.Field;

public class DefaultSerializer<T> implements Serializer<T> {
    @Override
    public void deserialize(Field[] field, JSONObject jsonObject, Object object) {
        for (Field f : field) {
            SerializerUtils.setField(f, object, jsonObject.get(f.getName()));
        }
    }

    @Override
    public void serialize(Field[] field, T o, JSONObject jsonObject) throws IllegalAccessException {
        for (Field f : field) {
            jsonObject.set(f.getName(), SerializerUtils.serializeField(f.get(jsonObject)));
        }
    }
}
