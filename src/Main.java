import JSONParser.JSONValues.*;
import JSONParser.Mapper;
import JSONParser.Parser.Tokens.Parser.Parser;
import Test.Class1;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Mapper mapper = new Mapper();
        JSONObject object = (JSONObject) Parser.parse("{\"a\":1,\"arr\":[1,2,3],\"obj\":{\"a\":5}}");
        Class1 c1 = (Class1) mapper.deserialize(object, Class1.class);
//        c1.obj.a="123";
        System.out.println(mapper.serialize(c1).serialize());
    }
}