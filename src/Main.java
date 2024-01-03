import main.JSONParser.JSONValues.*;
import main.JSONParser.Mapper;
import main.JSONParser.ParameterizedGenerics;
import main.JSONParser.Parser.Parser;
import main.ParserCombinators.CombinatorList;
import main.ParserCombinators.Serializer.ReflectiveSerializer;
import test.basic.Test.Class1;
import test.basic.Test.Test1;


public class Main {
    public static void main(String[] args) throws IllegalAccessException {
//        Mapper<Class1> mapper = new Mapper<Class1>(new ReflectiveSerializer<Class1>(Class1.class));
//        String s="{\"a\":5,\"b\":6,\"c\":{\"arr\":[{\"a\":1},{\"a\":6},{\"a\":9}]}}";
//        JSONObject object = (JSONObject) Parser.parse(s);
//        System.out.println(object.serialize());
//        Class1 c1 = (Class1) mapper.deserialize(object, Class1.class);
//        System.out.println(mapper.serialize(c1).serialize());
//        test();
        JSONObject object = (JSONObject) Parser.parse("{\"x\":{\"a\":11,\"b\":6},\"b\":[[[12,13,14],[1,2]],[]],\"c3\":{\"b\":10,\"c\":30}}");


//        JSONObject object = (JSONObject) Parser.parse("{\"c3\":{\"b\":10,\"c\":154},\"val\":15}");

        ParameterizedGenerics generics=new ParameterizedGenerics(Class1.class,Integer.class);
        Mapper<Class1<Integer>> mapper=new Mapper<>(new ReflectiveSerializer<>(Class1.class,generics));
        Class1<Integer> obj=mapper.deserialize(object,Class1.class);


        System.out.println(mapper.serialize(obj));

    }
    public static void test() {
        CombinatorList<Object> combinatorList = CombinatorList.getDefault();
//        System.out.println(combinatorList.serialize("Ankush",String.class).serialize());
    }
}