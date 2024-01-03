package main.ParserCombinators.CombinatorImpl;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONString;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.Combinator;
import main.ParserCombinators.Serializer.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public class StringCombinator<T> implements Combinator<T> {
    @Override
    public boolean check(Class<?> type) {
        return CharSequence.class.isAssignableFrom(type);
    }

    @Override
    public JSONElement serialize(Object o, Class<?> type, ParameterizedType parameterizedType, Serializer<T> serializer) {
        return new JSONString(String.valueOf(o));
    }

    @Override
    public Object deserialize(Field f, JSONElement element, ParameterizedGenerics generics) {
        Class<?> type = f.getType();
        Object o = f.getGenericType();
        if (String.class.isAssignableFrom(type)) {
            return element.getValue();
        } else {
            try {
                return type.getConstructor(CharSequence.class).newInstance((CharSequence) element.getValue());
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
