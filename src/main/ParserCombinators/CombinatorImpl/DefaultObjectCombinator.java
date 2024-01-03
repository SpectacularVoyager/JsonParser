package main.ParserCombinators.CombinatorImpl;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.Mapper;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.Combinator;
import main.ParserCombinators.Serializer.ReflectiveSerializer;
import main.ParserCombinators.Serializer.Serializer;

import java.lang.reflect.*;

public class DefaultObjectCombinator<T> implements Combinator<T> {
    @Override
    public Object deserialize(Field f, JSONElement element, ParameterizedGenerics generics) {
        Class<?> type = f.getType();
        Object o = f.getGenericType();
        if (o instanceof ParameterizedType) {
            Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
            if (__myType instanceof TypeVariable<?>) {
                return new Mapper<>(new ReflectiveSerializer<>(type, new ParameterizedGenerics(o, generics))).deserialize((JSONObject) element, type);
            } else if (__myType instanceof ParameterizedType) {
                return new Mapper<>(new ReflectiveSerializer<>(type, new ParameterizedGenerics(__myType, generics))).deserialize((JSONObject) element, type);
            } else {
                return new Mapper<>(new ReflectiveSerializer<>(type, generics)).deserialize((JSONObject) element, type);
            }
        }
        return new Mapper<>(new ReflectiveSerializer<>(type, generics)).deserialize((JSONObject) element, type);
    }


    @Override
    public JSONElement serialize(Object o, Class<?> type, ParameterizedType parameterizedType, Serializer<T> serializer) {
        JSONObject object = new JSONObject();
        try {
            serializer.serialize(o, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    @Override
    public boolean check(Class<?> type) {
        return true;
    }


}
