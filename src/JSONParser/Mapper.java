package JSONParser;

import JSONParser.JSONValues.*;
import ParserCombinators.Serializer.Serializer;


import ParserCombinators.Serializer.*;

public class Mapper<T> {
    Serializer<T> serializer;

    public Mapper(Serializer<T> serializer) {
        this.serializer = serializer;
    }

    private void deserialize(JSONObject jsonObject, T object) {
        Class<?> c = object.getClass();
        serializer.deserialize(jsonObject, object);
    }

    public Object deserialize(JSONObject jsonObject, Class<?> clazz) {
        T o = (T)SerializerUtils.instantiate(clazz);
        deserialize(jsonObject, o);
        return o;
    }

    public JSONObject serialize(T object) throws IllegalAccessException {
        Class<?> c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        serializer.serialize(object, jsonObject);
        return jsonObject;
    }
}
