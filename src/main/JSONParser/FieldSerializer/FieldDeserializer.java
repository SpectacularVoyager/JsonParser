package main.JSONParser.FieldSerializer;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.JSONValues.JSONObject;

@FunctionalInterface
public interface FieldDeserializer <T>{
    T deserialize(JSONElement object);
}
