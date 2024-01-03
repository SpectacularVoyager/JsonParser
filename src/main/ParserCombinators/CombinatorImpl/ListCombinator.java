package main.ParserCombinators.CombinatorImpl;

import main.JSONParser.JSONValues.JSONArray;
import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.Combinator;
import main.ParserCombinators.Serializer.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static main.ParserCombinators.Serializer.SerializerUtils.*;

public class ListCombinator<T> implements Combinator<T> {
    @Override
    public Object deserialize(Field f, JSONElement element, ParameterizedGenerics generics) {
        Class<?> type=f.getType();
        Object o=f.getGenericType();
        Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
        List<JSONElement> list = ((JSONArray) element).getList();
        List<Object> resList = getList(type);
        for (JSONElement e : list) {
            if (__myType instanceof ParameterizedType) {
                //IF LIST<GENERIC> FOUND
                if (List.class.isAssignableFrom((Class<?>) ((ParameterizedType) __myType).getRawType())) {
                    resList.add(addList(__myType, (JSONArray) e, o, generics));
                } else {
                    resList.add(getDeserializeField((Class<?>) ((ParameterizedType) __myType).getRawType(), o, e, generics));
                }

            } else {
//                        //IF GENERIC FOUND
                resList.add(getDeserializeField((Class<?>) (__myType), o, e, generics));
            }
        }
        return resList;

    }

    @Override
    public JSONElement serialize(Object o, Class<?> type, ParameterizedType parameterizedType, Serializer<T> serializer) {
        List<JSONElement> list = new ArrayList<>();
        for (Object obj : (List) o) {
            JSONObject element = new JSONObject();
            try {
                 serializer.serialize(obj,element);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            list.add(element);
        }
        return new JSONArray(list);
    }
    @Override
    public boolean check(Class<?> type){
        return List.class.isAssignableFrom(type);
    }


}
