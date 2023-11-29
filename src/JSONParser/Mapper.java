package JSONParser;

import JSONParser.JSONValues.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static void serialize(JSONObject jsonObject, Object object) throws IllegalAccessException {
        Class<?> c = object.getClass();
        for (Field f : c.getFields()) {
            setField(f, object, jsonObject.get(f.getName()));
        }
    }

    private static void setField(Field field, Object object, JSONElement jsonElement) throws IllegalAccessException {
        field.set(object, getField(field.getType(), jsonElement, (field.getGenericType())));
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
                throw new UnsupportedOperationException("Arrays not supported yet use lists");
            } else {
                //Probably an Object
                Object child = type.getConstructor().newInstance();
                serialize((JSONObject) jsonElement, child);
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

    public static Object getNumericFromString(Object value, Class<?> type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (type.isPrimitive()) {
            type = toWrapper(type);
        }
        if (Void.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException("VOID DETECTED");
        }

        return type.getMethod("valueOf", String.class).invoke(type, (String) value);

    }
}
