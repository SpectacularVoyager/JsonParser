package ParserCombinators.Serializer;

import JSONParser.JSONValues.JSONObject;

public interface Serializer<T> {
    void deserialize(JSONObject object, T o);

    void serialize(T o, JSONObject object) throws IllegalAccessException;
}
