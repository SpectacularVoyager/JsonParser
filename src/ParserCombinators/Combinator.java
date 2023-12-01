package ParserCombinators;

import JSONParser.FieldSerializer.FieldDeserializer;
import JSONParser.FieldSerializer.FieldSerializer;
import JSONParser.JSONValues.JSONElement;

import java.lang.reflect.ParameterizedType;
import java.util.function.Predicate;


public class Combinator<T> {
    private Predicate<Class<?>> predicate;
    private FieldSerializer<T> serializer;
    private FieldDeserializer<T> deserializer;

    public Combinator(Predicate<Class<?>> predicate, FieldSerializer<T> serializer, FieldDeserializer<T> deserializer) {
        this.predicate = predicate;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public boolean check(Class<?> clazz) {
        return predicate.test(clazz);
    }

    public JSONElement serialize(T o, Class<?> type, ParameterizedType parameterizedType) {
        return serializer.serialize(o, type, parameterizedType);
    }

    public T deserialize(JSONElement element) {
        return deserializer.deserialize(element);
    }
}
