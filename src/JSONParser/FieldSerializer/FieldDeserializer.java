package JSONParser.FieldSerializer;

import JSONParser.JSONValues.JSONElement;
import JSONParser.JSONValues.JSONObject;

@FunctionalInterface
public interface FieldDeserializer <T>{
    T deserialize(JSONElement object);
}
