import JSONParser.JSONValues.*;
import JSONParser.Mapper;
import JSONParser.Parser.Tokens.Parser.Parser;
import Serializer.DefaultSerializer;
import Test.Class1;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Mapper<Class1> mapper = new Mapper<Class1>(new DefaultSerializer<Class1>());
        JSONObject object = (JSONObject) Parser.parse("{\"a\":1,\"arr\":[1,2,3],\"obj\":{\"a\":5}}");
        Class1 c1 = (Class1) mapper.deserialize(object, Class1.class);
        System.out.println(mapper.serialize(c1).serialize());
    }
}