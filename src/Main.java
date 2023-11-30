import JSONParser.JSONValues.*;
import JSONParser.Mapper;
import Test.Class1;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
//        System.out.println("Hello world!");
//        Class1 class1 = new Class1("Hello", 123);
//        JSONString String = new JSONString("Ankush");
//        JSONValue value = new JSONValue(1);
////        System.out.println(String.serialize());
////        System.out.println(value.serialize());
//        System.out.println(new JSONArray(new JSONElement[]{String, value}).serialize());
//
//        JSONObject obj = new JSONObject();
//        obj.set("Name", String);
////        obj.set("marks", value);
//        System.out.println(obj.serialize());
        Class1 c = new Class1();
        JSONObject obj = new JSONObject();
        JSONObject obj2 = new JSONObject();
        obj2.set(new JSONString("a"),new JSONString("hello"));


        obj.set(new JSONString("str1"), new JSONString("name"));
        obj.set(new JSONString("val"), new JSONValue(123));
        obj.set(new JSONString("c"), obj2);
        obj.set(new JSONString("list"), new JSONArray(new JSONArray(new JSONValue(1),new JSONValue(2)),new JSONArray(new JSONValue(3))));
        obj.set(new JSONString("arr"), new JSONArray(new JSONValue(1),new JSONValue(2)));
        Mapper mapper=new Mapper();
        mapper.deserialize(obj, c);
        System.out.println(mapper.serialize(c).serialize());
        System.out.println(obj.serialize());
        System.out.println(c);
    }
}