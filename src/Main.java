import JSONParser.JSONValues.*;
import JSONParser.Mapper;
import JSONParser.Parser.Tokens.Parser.Parser;
import ParserCombinators.Combinator;
import ParserCombinators.CombinatorList;
import Serializer.DefaultSerializer;
import Test.Class1;

import java.util.function.Predicate;


public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Mapper<Class1> mapper = new Mapper<Class1>(new DefaultSerializer<Class1>());
        String s="{\"a\":1,\"arr\":[{\"a\":1,\"b\":2},1],\"obj\":{\"a\":5,\"b\":10}}";
        System.out.println(s);
        JSONObject object = (JSONObject) Parser.parse(s);
        System.out.println(object.serialize());
//        Class1 c1 = (Class1) mapper.deserialize(object, Class1.class);
//        System.out.println(mapper.serialize(c1).serialize());
        test();
    }
    public static void test(){
        CombinatorList<Object> combinatorList=CombinatorList.getDefault();
//        System.out.println(combinatorList.serialize("Ankush",String.class).serialize());
    }
}