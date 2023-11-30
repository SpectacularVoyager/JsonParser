package Serializer;

import JSONParser.JSONValues.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class SerializerUtils {
    public static void deserialize(JSONObject jsonObject, Object object) {
        Class<?> c = object.getClass();
        for (Field f : c.getFields()) {
            setField(f, object, jsonObject.get(f.getName()));
        }
    }

    public static Object deserialize(JSONObject jsonObject, Class<?> clazz) {
        Object o = instantiate(clazz);
        deserialize(jsonObject, o);
        return o;
    }


    public static JSONObject serialize(Object object) throws IllegalAccessException {
        Class<?> c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field f : c.getFields()) {
            jsonObject.set(f.getName(), serializeField(f.get(object)));
        }
        return jsonObject;
    }

    public static JSONElement serializeField(Object object) throws IllegalAccessException {
        Class<?> type = object.getClass();
        if (object instanceof CharSequence) {
            return new JSONString(String.valueOf(object));
        } else if (object instanceof Number || type.isPrimitive()) {
            return new JSONValue(String.valueOf(object));
        } else if (object instanceof List) {
            List<JSONElement> list = new ArrayList<>();
            for (Object obj : (List) object) {
                JSONElement element = serializeField(obj);
                list.add(element);
            }
            return new JSONArray(list);
        } else if (type.isArray()) {
            int[] l = (int[]) object;
            List<JSONElement> elements = new ArrayList<>();
            for (Object element : l) {
                elements.add(serializeField(element));
            }
            return new JSONArray(elements);
        } else {
            return serialize(object);
        }
    }

    private static Object instantiate(Class<?> clazz) {
        try {
            Constructor constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Please provide a default constructor for:\t" + clazz.getName());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot Instantiate class:\t" + clazz.getName());
        }

    }


    public static void setField(Field field, Object object, JSONElement jsonElement) {
        field.setAccessible(true);
        try {
            field.set(object, getField(field.getType(), jsonElement, (field.getGenericType())));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Cannot acces %s.%s()", object.getClass().getName(), field.getName()));
        }
    }

    private static Object getField(Class<?> type, JSONElement jsonElement, Type parameterizedType) {
        try {
            if (String.class.isAssignableFrom(type)) {
                return jsonElement.getValue();
            } else if (CharSequence.class.isAssignableFrom(type)) {
                return type.getConstructor(CharSequence.class).newInstance(jsonElement.getValue());
            } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                return getNumericFromString(jsonElement.getValue(), type);
            } else if (List.class.isAssignableFrom(type)) {
                List<JSONElement> list = ((JSONArray) jsonElement).getList();
                List<Object> resList = new ArrayList<>();
                for (JSONElement o : list) {
                    Type __myType = (((ParameterizedType) parameterizedType).getActualTypeArguments()[0]);
                    if (__myType instanceof ParameterizedType) {
                        //IF LIST<GENERIC> FOUND
                        resList.add(getField(type, o, __myType));
                    } else {
                        //IF GENERIC FOUND
                        resList.add(getField((Class<?>) __myType, o, __myType));
                    }

                }
                return resList;
            } else if (type.isArray()) {
                List<JSONElement> list = ((JSONArray) jsonElement).getList();
                Object arr = Array.newInstance(type.componentType(), list.size());
                for (int i = 0; i < list.size(); i++) {
                    Array.set(arr, i, 1);
                }
                return arr;
            } else {
                //Probably an Object
                Object child = instantiate(type);
                deserialize((JSONObject) jsonElement, child);
                return child;
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation exception", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("CAUGHT EXCEPTIONS");
    }

    private static Class<?> toWrapper(Class<?> clazz) {
        if (!clazz.isPrimitive())
            return clazz;

        if (clazz == Integer.TYPE)
            return Integer.class;
        if (clazz == Long.TYPE)
            return Long.class;
        if (clazz == Boolean.TYPE)
            return Boolean.class;
        if (clazz == Byte.TYPE)
            return Byte.class;
        if (clazz == Character.TYPE)
            return Character.class;
        if (clazz == Float.TYPE)
            return Float.class;
        if (clazz == Double.TYPE)
            return Double.class;
        if (clazz == Short.TYPE)
            return Short.class;
        if (clazz == Void.TYPE)
            return Void.class;

        return clazz;
    }

    private static Object getNumericFromString(Object value, Class<?> type) throws InvocationTargetException {

        if (type.isPrimitive()) {
            type = toWrapper(type);
        }
        if (Void.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException("VOID DETECTED");
        }
        try {
            return type.getMethod("valueOf", String.class).invoke(type, (String) value);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Cannot call %s.valueOf()", type.getName()));
        }
    }
}
