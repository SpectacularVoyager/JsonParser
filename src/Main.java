import JSONParser.JSONValues.*;
import JSONParser.Mapper;
import JSONParser.Parser.Parser;
import ParserCombinators.CombinatorList;
import ParserCombinators.Serializer.ReflectiveSerializer;
import Test.Class1;
import Test.Class3;

import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IllegalAccessException {
//        Mapper<Class1> mapper = new Mapper<Class1>(new ReflectiveSerializer<Class1>(Class1.class));
//        String s="{\"a\":5,\"b\":6,\"c\":{\"arr\":[{\"a\":1},{\"a\":6},{\"a\":9}]}}";
//        JSONObject object = (JSONObject) Parser.parse(s);
//        System.out.println(object.serialize());
//        Class1 c1 = (Class1) mapper.deserialize(object, Class1.class);
//        System.out.println(mapper.serialize(c1).serialize());
//        test();
        JSONObject object = (JSONObject) Parser.parse("{\"x\":{\"a\":11,\"b\":6},\"b\":[[12,13,14],[1,2]],\"c3\":{\"b\":10}}");
//        JSONObject object = (JSONObject) Parser.parse("{\"c3\":{\"b\":10}}");
        Mapper<Class1<Integer>> mapper=new Mapper<>(new ReflectiveSerializer<>(Class1.class,Integer.class));
        Class1<Integer> obj=mapper.deserialize(object,Class1.class);



        System.out.println(mapper.serialize(obj));

    }

    public static void test() {
        CombinatorList<Object> combinatorList = CombinatorList.getDefault();
//        System.out.println(combinatorList.serialize("Ankush",String.class).serialize());
    }
}