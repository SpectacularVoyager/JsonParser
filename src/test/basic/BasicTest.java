package test.basic;

import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.JSONValues.JSONValue;
import main.JSONParser.Mapper;
import main.JSONParser.ParameterizedGenerics;
import main.JSONParser.Parser.Parser;
import main.ParserCombinators.Serializer.ReflectiveSerializer;
import main.ParserCombinators.Serializer.Serializer;
import test.Test.Class1;
import test.Test.Class2;
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
    public void example(){
        //Create a generics object with for Class1<Integer>
        ParameterizedGenerics generics=new ParameterizedGenerics(Class2.class);

        //Create a serializer,Here we create a reflectionSerializer.We can also use a Custom Serializer
        Serializer<Class2> serializer=new ReflectiveSerializer<>(Class2.class,generics);

        //Create a mapper
        Mapper<Class2> mapper=new Mapper<>(serializer);


        JSONObject object=Parser.parseObject("{\"a\":10,\"b\":20}");
        Class2 obj=mapper.deserialize(object, Class2.class);
        Assertions.assertEquals(obj.a,10);

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
    @Test
    public void checkQOLFeactures() throws IllegalAccessException {
        JSONObject object = (JSONObject) Parser.parse("{\"x\":{\"a\":11,\"b\":6},\"b\":[[[12,13,14],[1,2]],[[3]]],\"c3\":{\"b\":10,\"c\":30}}");
        Mapper<Class1<Integer>> mapper=new Mapper<>(Class1.class,Integer.class);

        Class1<Integer> obj=mapper.deserialize(object,Class1.class);

    }
}
