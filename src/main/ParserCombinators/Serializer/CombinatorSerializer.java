package main.ParserCombinators.Serializer;

import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.CombinatorList;

import java.lang.reflect.Field;

public class CombinatorSerializer<T> implements Serializer<T> {
    private final Class<?> clazz;
    private ParameterizedGenerics generics = null;
    CombinatorList<T> list;

    public CombinatorSerializer(Class<?> clazz, ParameterizedGenerics generics, CombinatorList<T> list) {
        this.clazz = clazz;
        this.generics = generics;
        this.list = list;
        list.setSerializer(this);
    }
    public CombinatorSerializer(Class<?> clazz,CombinatorList<T> list,Class<?>[] args) {
        this(clazz,new ParameterizedGenerics(clazz,args),list);
    }

    public Field[] getFields() {
        return clazz.getFields();
    }


    @Override
    public void deserialize(JSONObject jsonObject, T o) {
        for (Field f : getFields()) {
            try {
                Object val=list.deserialize(f, jsonObject.get(f.getName()), generics);
                f.set(o,val);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public void serialize(Object o, JSONObject object) throws IllegalAccessException {
        for (Field f : getFields()) {
            object.set(f.getName(), SerializerUtils.serializeField(f.get(o)));
//            list.serialize(o,f);
        }


    }
}
