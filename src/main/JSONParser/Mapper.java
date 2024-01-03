package main.JSONParser;

import main.JSONParser.JSONValues.*;
import main.ParserCombinators.Serializer.Serializer;


import main.ParserCombinators.Serializer.*;

public class Mapper<T> {
    Serializer<T> serializer;

    public Mapper(Serializer<T> serializer) {
        this.serializer = serializer;
    }

    private void deserialize(JSONObject jsonObject, T object, Class<?> generic) {
        Class<?> c = object.getClass();
        serializer.deserialize(jsonObject, object);
    }

    public T deserialize(JSONObject jsonObject, Class<?> clazz) {
        T o = (T) SerializerUtils.instantiate(clazz);
        serializer.deserialize(jsonObject, o);
        return o;
    }

    public JSONObject serialize(T object) throws IllegalAccessException {
        Class<?> c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        serializer.serialize(object, jsonObject);
        return jsonObject;
    }
}
