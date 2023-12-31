import main.JSONParser.JSONValues.*;
import main.JSONParser.Mapper;
import main.JSONParser.ParameterizedGenerics;
import main.JSONParser.Parser.Parser;
import main.ParserCombinators.CombinatorList;
import main.ParserCombinators.Serializer.CombinatorSerializer;
import main.ParserCombinators.Serializer.ReflectiveSerializer;
import main.ParserCombinators.Serializer.Serializer;
import main.ParserCombinators.Serializer.SerializerUtils;
import test.Test.Class1;

import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        JSONObject object = (JSONObject) Parser.parse("{\"x\":{\"a\":11,\"b\":6},\"b\":[[[12,13,14],[1,2]],[[3]]],\"c3\":{\"b\":10,\"c\":30}}");
        //        JSONObject object = (JSONObject) Parser.parse("{\"c3\":{\"b\":10,\"c\":154},\"val\":15}");
        ParameterizedGenerics generics=new ParameterizedGenerics(Class1.class,Integer.class);
//        Serializer<Class1<Integer>> serializer=new CombinatorSerializer<>(Class1.class,generics,CombinatorList.getDefault());
        Serializer<Class1<Integer>> serializer=new ReflectiveSerializer<>(Class1.class,generics);


        Mapper<Class1<Integer>> mapper=new Mapper<>(serializer);

        Class1<Integer> obj=mapper.deserialize(object,Class1.class);


        System.out.println(mapper.serialize(obj));
        test();

    }
    public static void test() {
        ArrayList<Integer> x=new ArrayList<>();

    }
}