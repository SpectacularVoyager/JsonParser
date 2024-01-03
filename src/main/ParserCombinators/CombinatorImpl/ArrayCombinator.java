package main.ParserCombinators.CombinatorImpl;

import main.JSONParser.JSONValues.JSONArray;
import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.Combinator;
import main.ParserCombinators.Serializer.Serializer;
import main.ParserCombinators.Serializer.SerializerUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ArrayCombinator<T> implements Combinator<T> {
    @Override
    public Object deserialize(Field f, JSONElement element, ParameterizedGenerics generics) {
        Class<?> type = f.getType();
        Object o = f.getGenericType();
        List<JSONElement> list = ((JSONArray) element).getList();
        Object arr = Array.newInstance(type.componentType(), list.size());
        for (int i = 0; i < list.size(); i++) {
//                    Array.set(arr, i, getArrayElement(type.componentType(), list.get(i), null));ParameterizedType
            Array.set(arr, i, SerializerUtils.getDeserializeField(type.componentType(), o, list.get(i), null));
        }
        return arr;
    }

    @Override
    public JSONElement serialize(Object o, Class<?> type, ParameterizedType parameterizedType, Serializer<T> serializer) {

        int[] l = (int[]) o;
        List<JSONElement> elements = new ArrayList<>();
//        JSONArray array=new JSONArray();
        for (Object element : l) {
            try {
                JSONObject jsonElement = new JSONObject();
                serializer.serialize(element, jsonElement);
                elements.add(jsonElement);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return new JSONArray(elements);

    }

    @Override
    public boolean check(Class<?> type) {
        return type.isArray();
    }


}
