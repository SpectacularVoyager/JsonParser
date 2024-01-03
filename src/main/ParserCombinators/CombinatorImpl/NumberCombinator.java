package main.ParserCombinators.CombinatorImpl;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONValue;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.Combinator;
import main.ParserCombinators.Serializer.Serializer;
import main.ParserCombinators.Serializer.SerializerUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public class NumberCombinator<T> implements Combinator<T> {
    @Override
    public Object deserialize(Field f, JSONElement element, ParameterizedGenerics generics) {
        Class<?> type=f.getType();
        Object o=f.getGenericType();
        try {
            return SerializerUtils.getNumericFromString(element.getValue(), type);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONElement serialize(Object o, Class<?> type, ParameterizedType parameterizedType, Serializer<T> serializer) {
        return new JSONValue(String.valueOf(o));
    }
    @Override
    public boolean check(Class<?> type){
        return Number.class.isAssignableFrom(type) || type.isPrimitive();
    }


}
