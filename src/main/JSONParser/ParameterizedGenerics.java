package main.JSONParser;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;

public class ParameterizedGenerics {
    private final HashMap<String, Class<?>> map;

    public ParameterizedGenerics() {
        this.map = new HashMap<>();
    }

    public ParameterizedGenerics(HashMap<String, Class<?>> map) {
        this.map = map;
    }

    public<V> ParameterizedGenerics(Class<?> clazz,Class<?>... types) {
        this();
        for(int i=0;i<clazz.getTypeParameters().length;i++){
            map.put(clazz.getTypeParameters()[i].getName(),types[i]);
        }
    }

    public ParameterizedGenerics(Field f, ParameterizedType parameterizedType, ParameterizedGenerics generics) {
        this();
        Type[] types = (((ParameterizedType) f.getGenericType()).getActualTypeArguments());
        Type t = ((ParameterizedType) f.getGenericType()).getRawType();
//        System.out.println(((ParameterizedType) f.getGenericType()).getActualTypeArguments());
        TypeVariable<?>[] vars = ((Class<?>) t).getTypeParameters();
        for (int i = 0; i < vars.length; i++) {
            String key = vars[i].getName();
            Class<?> val = generics.get(types[i].getTypeName());
            if (val == null) {
                val = (Class<?>) types[i];
            }
            map.put(key, val);
//            System.out.printf("%s:\t%s\n",key,val);
        }
    }
    public ParameterizedGenerics(Object genericType, ParameterizedGenerics generics) {
        this();
        Type[] types = (((ParameterizedType) genericType).getActualTypeArguments());
        Type t = ((ParameterizedType) genericType).getRawType();
//        System.out.println(((ParameterizedType) f.getGenericType()).getActualTypeArguments());
        TypeVariable<?>[] vars = ((Class<?>) t).getTypeParameters();
        for (int i = 0; i < vars.length; i++) {
            String key = vars[i].getName();
            Class<?> val = generics.get(types[i].getTypeName());
            if (val == null) {
                val = (Class<?>) types[i];
            }
            map.put(key, val);
        }
    }

    public Class<?> get(String str) {
        return map.get(str);
    }

    public void put(String str, Class<?> clazz) {
        map.put(str, clazz);
    }
}
