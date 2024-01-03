package ParserCombinators.Serializer;

import JSONParser.JSONValues.JSONObject;
import JSONParser.ParameterizedGenerics;

import java.lang.reflect.Field;


public class ReflectiveSerializer<T> implements Serializer<T> {

    private final Class<?> clazz;
    private ParameterizedGenerics generics = null;

    //ENSURE CLASS IS NOT GENERIC
    public ReflectiveSerializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    //FOR GENERIC CLASSES
    public ReflectiveSerializer(Class<?> clazz, ParameterizedGenerics generics) {
        this(clazz);
        this.generics = generics;
    }

    public Field[] getFields() {
        return clazz.getFields();
    }

    @Override
    public void deserialize(JSONObject jsonObject, Object object) {
        for (Field f : getFields()) {
            if (generics == null)
                SerializerUtils.deserializeField(f, object, jsonObject.get(f.getName()));
            else
                SerializerUtils.deserializeField(f, object, jsonObject.get(f.getName()),generics);
        }
    }


    @Override
    public void serialize(Object o, JSONObject jsonObject) throws IllegalAccessException {
        for (Field f : getFields()) {
            jsonObject.set(f.getName(), SerializerUtils.serializeField(f.get(o)));
        }
    }
}
