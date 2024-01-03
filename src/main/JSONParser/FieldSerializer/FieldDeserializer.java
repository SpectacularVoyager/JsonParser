package main.JSONParser.FieldSerializer;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.ParameterizedGenerics;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface FieldDeserializer {
    Object deserialize(Class<?> type, Object o, JSONElement element, ParameterizedGenerics generics) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
