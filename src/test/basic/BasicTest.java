package test.basic;

import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.JSONValues.JSONValue;
import main.JSONParser.Mapper;
import main.JSONParser.ParameterizedGenerics;
import main.JSONParser.Parser.Parser;
import main.ParserCombinators.Serializer.ReflectiveSerializer;
import test.Test.Test1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.Test.Test3;

import java.util.List;

public class BasicTest {
    @Test
    public void checkParse() {
        String s1 = "{\"x\":5}";
        JSONObject object = Parser.parseObject(s1);
        Assertions.assertEquals(object.serialize(), s1);

        object.set("x", new JSONValue(6));
        Assertions.assertEquals(object.serialize(), "{\"x\":6}");
    }

    @Test
    public void checkTestInMultiList() throws IllegalAccessException {


        String s = "{\"list\":[[{\"x\":5}]]}";
        JSONObject object = (JSONObject) Parser.parse(s);
        ParameterizedGenerics generics = new ParameterizedGenerics(Test3.class, Integer.class);
        Mapper<Test3<Integer>> mapper = new Mapper<>(new ReflectiveSerializer<>(Test3.class, generics));
        Test3<Integer> obj = mapper.deserialize(object, Test3.class);
        Assertions.assertEquals(s, mapper.serialize(obj).serialize());
    }

    @Test
    public void checkTest() throws IllegalAccessException {
        String s = "{\"list\":[{\"x\":5}]}";
        JSONObject object = (JSONObject) Parser.parse(s);
        ParameterizedGenerics generics = new ParameterizedGenerics(Test1.class, Integer.class);
        Mapper<Test1<Integer>> mapper = new Mapper<>(new ReflectiveSerializer<>(Test1.class, generics));
        Test1<Integer> obj = mapper.deserialize(object, Test1.class);
        Assertions.assertEquals(s, mapper.serialize(obj).serialize());
    }
}
