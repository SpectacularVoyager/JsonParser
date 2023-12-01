package Serializer;

import JSONParser.JSONValues.JSONObject;

import java.lang.reflect.Field;


public class ReflectiveSerializer<T> implements Serializer<T> {

    private Class<?> clazz;

    public ReflectiveSerializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Field[] getFields() {
        return clazz.getFields();
    }

    @Override
    public void deserialize(JSONObject jsonObject, Object object) {
        for (Field f : getFields()) {
            SerializerUtils.setField(f, object, jsonObject.get(f.getName()));
        }
    }

    @Override
    public void serialize(Object o, JSONObject jsonObject) throws IllegalAccessException {
        for (Field f : getFields()) {
            jsonObject.set(f.getName(), SerializerUtils.serializeField(f.get(o)));
        }
    }
}
