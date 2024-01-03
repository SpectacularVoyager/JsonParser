package main.ParserCombinators.Serializer;

import main.JSONParser.JSONValues.*;
import main.JSONParser.Mapper;
import main.JSONParser.ParameterizedGenerics;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class SerializerUtils {


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

    public static Object instantiate(Class<?> clazz) {
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

    public static void deserializeField(Field field, Object object, JSONElement element) {
        deserializeField(field, object, element, null);
    }

    public static void deserializeField(Field field, Object object, JSONElement element, ParameterizedGenerics generic) {
        field.setAccessible(true);
        try {
            field.set(object, getDeserializeField(field, element, generic));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    //need type for generics
    public static Object getDeserializeField(Field field, JSONElement element, ParameterizedGenerics generics) {
        Class<?> type = field.getType();
        Object o = field.getGenericType();
        return getDeserializeField(type, o, element, generics);
    }

    /**
     * public static Object getDeserializeField(Field field, JSONElement element, ParameterizedGenerics generics) {
     * Class<?> type = field.getType();
     * Object o = field.getGenericType();
     * if (o instanceof TypeVariable<?>) {
     * if (generics != null) {
     * TypeVariable<?> var = (TypeVariable<?>) o;
     * type = generics.get(var.getName());
     * }
     * }
     * try {
     * if (CharSequence.class.isAssignableFrom(type)) {
     * if (String.class.isAssignableFrom(type)) {
     * return element.getValue();
     * } else {
     * return type.getConstructor(CharSequence.class).newInstance((CharSequence) element.getValue());
     * }
     * } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
     * return getNumericFromString(element.getValue(), type);
     * } else if (List.class.isAssignableFrom(type)) {
     * Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
     * List<JSONElement> list = ((JSONArray) element).getList();
     * List<Object> resList = new ArrayList<>();
     * for (JSONElement e : list) {
     * if (__myType instanceof ParameterizedType) {
     * //IF LIST<GENERIC> FOUND
     * //                        resList.add(getArrayElement(type, e, generics));
     * //                        resList.add(getDeserializeField(type,o, e, generics));
     * resList.add(getDeserializeField((Class<?>) ((ParameterizedType)__myType).getRawType(),o, e, generics));
     * <p>
     * } else {
     * //                        //IF GENERIC FOUND
     * //                        resList.add(getArrayElement((Class<?>) __myType, e, null));
     * resList.add(getDeserializeField((Class<?>) __myType,o, e, generics));
     * }
     * }
     * return resList;
     * //                throw new UnsupportedOperationException();
     * } else if (type.isArray()) {
     * List<JSONElement> list = ((JSONArray) element).getList();
     * Object arr = Array.newInstance(type.componentType(), list.size());
     * for (int i = 0; i < list.size(); i++) {
     * //                    Array.set(arr, i, getArrayElement(type.componentType(), list.get(i), null));
     * Array.set(arr, i, getDeserializeField(type.componentType(),o, list.get(i), null));
     * }
     * return arr;
     * } else {
     * if (o instanceof ParameterizedType) {
     * <p>
     * Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
     * if (__myType instanceof TypeVariable<?>) {
     * return new Mapper<>(new ReflectiveSerializer<>(type, new ParameterizedGenerics(field, (ParameterizedType) o, generics))).deserialize((JSONObject) element, field.getType());
     * //                        return new Mapper<>(new ReflectiveSerializer<>(field.getType(),generics)).deserialize((JSONObject) element, field.getType());
     * } else if (__myType instanceof ParameterizedType) {
     * throw new UnsupportedOperationException("HOW");
     * //                        return getDeserializeField(type, element, (ParameterizedType) __myType);
     * } else {
     * System.out.println(o);
     * throw new UnsupportedOperationException("KURWA MAC");
     * <p>
     * //                        return new Mapper<>(new ReflectiveSerializer<>(field.getType(), (Class<?>) __myType)).deserialize((JSONObject) element, type);
     * <p>
     * }
     * }
     * return new Mapper<>(new ReflectiveSerializer<>(type)).deserialize((JSONObject) element, type);
     * }
     * } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
     * InvocationTargetException e) {
     * throw new RuntimeException(e);
     * }
     * }
     */

    public static List<Object> getList(Class<?> type) {
        if (type.isInterface()) {
            return new ArrayList<>();
        }
        return (List<Object>) instantiate(type);
    }

    public static Object addList(Type __myType, JSONArray array, Object o, ParameterizedGenerics generics) {
        if (__myType instanceof ParameterizedType parameterizedType) {
            Class<?> myType = (Class<?>) parameterizedType.getRawType();
            if (List.class.isAssignableFrom(myType)) {
                List<Object> list = getList(myType);
                List<JSONElement> jsonList = array.getList();
                int i = 0;
                for (JSONElement element : jsonList) {
                    Type child = parameterizedType.getActualTypeArguments()[0];
                    if (child instanceof ParameterizedType) {
                        if ((((Class<?>) ((ParameterizedType) child).getRawType()).isAssignableFrom(List.class))) {
                            list.add(addList(parameterizedType.getActualTypeArguments()[0], (JSONArray) element, o, generics));
                        } else {
                            list.add(getDeserializeField((Class<?>) child, o, element, generics));
                        }
                    } else {
                        list.add(getDeserializeField((Class<?>) child, o, element, generics));
                    }
                    i++;
                }
                return list;
            }
        }
        throw new UnsupportedOperationException("SHOULD NOT HAVE HAPPENED\\2");

    }

    public static Object getDeserializeField(Class<?> type, Object o, JSONElement element, ParameterizedGenerics generics) {
        if (o instanceof TypeVariable<?>) {
            if (generics != null) {
                TypeVariable<?> var = (TypeVariable<?>) o;
                type = generics.get(var.getName());
            }
        }
        try {
            if (CharSequence.class.isAssignableFrom(type)) {
                if (String.class.isAssignableFrom(type)) {
                    return element.getValue();
                } else {
                    return type.getConstructor(CharSequence.class).newInstance((CharSequence) element.getValue());
                }
            } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                return getNumericFromString(element.getValue(), type);
            } else if (List.class.isAssignableFrom(type)) {
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
//                throw new UnsupportedOperationException();
            } else if (type.isArray()) {
                List<JSONElement> list = ((JSONArray) element).getList();
                Object arr = Array.newInstance(type.componentType(), list.size());
                for (int i = 0; i < list.size(); i++) {
//                    Array.set(arr, i, getArrayElement(type.componentType(), list.get(i), null));ParameterizedType
                    Array.set(arr, i, getDeserializeField(type.componentType(), o, list.get(i), null));
                }
                return arr;
            } else {
                if (o instanceof ParameterizedType) {
                    Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
                    if (__myType instanceof TypeVariable<?>) {
                        return new Mapper<>(new ReflectiveSerializer<>(type, new ParameterizedGenerics(o, generics))).deserialize((JSONObject) element, type);
                    } else if (__myType instanceof ParameterizedType) {
                        return new Mapper<>(new ReflectiveSerializer<>(type, new ParameterizedGenerics(__myType, generics))).deserialize((JSONObject) element, type);
                    } else {
                        return new Mapper<>(new ReflectiveSerializer<>(type, null)).deserialize((JSONObject) element, type);
                    }
                }
                return new Mapper<>(new ReflectiveSerializer<>(type,null)).deserialize((JSONObject) element, type);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * public static Object getDeserializeField(Class<?> type, JSONElement element, ParameterizedType generics) {
     * try {
     * if (CharSequence.class.isAssignableFrom(type)) {
     * if (String.class.isAssignableFrom(type)) {
     * return element.getValue();
     * } else {
     * return type.getConstructor(CharSequence.class).newInstance((CharSequence) element.getValue());
     * }
     * } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
     * return getNumericFromString(element.getValue(), type);
     * } else if (List.class.isAssignableFrom(type)) {
     * Type __myType = generics.getActualTypeArguments()[0];
     * List<JSONElement> list = ((JSONArray) element).getList();
     * List<Object> resList = new ArrayList<>();
     * for (JSONElement e : list) {
     * if (__myType instanceof ParameterizedType) {
     * //IF LIST<GENERIC> FOUND
     * resList.add(getDeserializeField(type, e, (ParameterizedType) __myType));
     * } else {
     * //                        //IF GENERIC FOUND
     * resList.add(getDeserializeField((Class<?>) __myType, e, null));
     * }
     * }
     * return resList;
     * //                throw new UnsupportedOperationException();
     * } else if (type.isArray()) {
     * List<JSONElement> list = ((JSONArray) element).getList();
     * Object arr = Array.newInstance(type.componentType(), list.size());
     * for (int i = 0; i < list.size(); i++) {
     * Array.set(arr, i, getField(type.componentType(), list.get(i), null));
     * }
     * return arr;
     * } else {
     * //                if (o instanceof ParameterizedType) {
     * //
     * //                    Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
     * //                    if (__myType instanceof ParameterizedType) {
     * //                        return getDeserializeField(type, element, (ParameterizedType) __myType);
     * //                    } else if (__myType instanceof TypeVariable<?>) {
     * //                        System.out.println("OH KURWA");
     * //                        return new Mapper<>(new ReflectiveSerializer<>(type,generics)).deserialize((JSONObject) element, type);
     * //                    } else {
     * ////                        return getDeserializeField(field, element, (Class<?>) __myType);
     * //                        return new Mapper<>(new ReflectiveSerializer<>(type, (Class<?>) __myType)).deserialize((JSONObject) element, type);
     * //
     * //                    }
     * //                }
     * return new Mapper<>(new ReflectiveSerializer<>(type)).deserialize((JSONObject) element, type);
     * //                throw new UnsupportedOperationException("HOW DID WE GET HERE");
     * //                System.out.println("PLS FIX");
     * //                return new Mapper<>(new ReflectiveSerializer<>(type)).deserialize((JSONObject) element, type);
     * <p>
     * }
     * } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
     * InvocationTargetException e) {
     * throw new RuntimeException(e);
     * }
     * }
     */
    @Deprecated
    public static Object getArrayElement(Class<?> type, JSONElement element, ParameterizedGenerics generics) {
        try {
            if (CharSequence.class.isAssignableFrom(type)) {
                if (String.class.isAssignableFrom(type)) {
                    return element.getValue();
                } else {
                    return type.getConstructor(CharSequence.class).newInstance((CharSequence) element.getValue());
                }
            } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                return getNumericFromString(element.getValue(), type);
            } else if (List.class.isAssignableFrom(type)) {
//                Type __myType = generics.getActualTypeArguments()[0];
//                List<JSONElement> list = ((JSONArray) element).getList();
//                List<Object> resList = new ArrayList<>();
//                for (JSONElement e : list) {
//                    if (__myType instanceof ParameterizedType) {
//                        //IF LIST<GENERIC> FOUND
//                        resList.add(getDeserializeField(type, e, (ParameterizedType) __myType));
//                    } else {
////                        //IF GENERIC FOUND
//                        resList.add(getDeserializeField((Class<?>) __myType, e, null));
//                    }
//                }
//                return resList;
                throw new UnsupportedOperationException();
            } else if (type.isArray()) {
//                List<JSONElement> list = ((JSONArray) element).getList();
//                Object arr = Array.newInstance(type.componentType(), list.size());
//                for (int i = 0; i < list.size(); i++) {
//                    Array.set(arr, i, getField(type.componentType(), list.get(i), null));
//                }
//                return arr;
                throw new UnsupportedOperationException();
            } else {
//                if (o instanceof ParameterizedType) {
//
//                    Type __myType = (((ParameterizedType) o).getActualTypeArguments()[0]);
//                    if (__myType instanceof ParameterizedType) {
//                        return getDeserializeField(type, element, (ParameterizedType) __myType);
//                    } else if (__myType instanceof TypeVariable<?>) {
//                        System.out.println("OH KURWA");
//                        return new Mapper<>(new ReflectiveSerializer<>(type,generics)).deserialize((JSONObject) element, type);
//                    } else {
////                        return getDeserializeField(field, element, (Class<?>) __myType);
//                        return new Mapper<>(new ReflectiveSerializer<>(type, (Class<?>) __myType)).deserialize((JSONObject) element, type);
//
//                    }
//                }
                return new Mapper<>(new ReflectiveSerializer<>(type)).deserialize((JSONObject) element, type);
//                throw new UnsupportedOperationException("HOW DID WE GET HERE");
//                System.out.println("PLS FIX");
//                return new Mapper<>(new ReflectiveSerializer<>(type)).deserialize((JSONObject) element, type);

            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public static void setField(Field field, Object object, JSONElement jsonElement) {
        field.setAccessible(true);
        try {
            field.set(object, getField(field.getType(), jsonElement, (field.getGenericType())));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Cannot access %s.%s()", object.getClass().getName(), field.getName()));
        }
    }

    @Deprecated
    private static Object getField(Class<?> type, JSONElement jsonElement, Type parameterizedType) {
        try {
            if (String.class.isAssignableFrom(type)) {
                return jsonElement.getValue();
            } else if (CharSequence.class.isAssignableFrom(type)) {
                return type.getConstructor(CharSequence.class).newInstance(jsonElement.getValue());
            } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                return getNumericFromString(jsonElement.getValue(), type);
            } else if (List.class.isAssignableFrom(type)) {
                Type __myType = (((ParameterizedType) parameterizedType).getActualTypeArguments()[0]);

                List<JSONElement> list = ((JSONArray) jsonElement).getList();
                List<Object> resList = new ArrayList<>();
                for (JSONElement o : list) {
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
                throw new UnsupportedOperationException();
                //Probably an Object
//                Object child = instantiate(type);
//                deserialize((JSONObject) jsonElement, child);
//                return child;
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
