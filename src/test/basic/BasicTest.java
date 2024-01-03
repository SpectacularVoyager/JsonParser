package test.basic;

import main.JSONParser.JSONValues.JSONObject;
import main.JSONParser.JSONValues.JSONValue;
import main.JSONParser.Parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicTest {
    @Test
    public void checkParse(){
        String s1="{\"x\":5}";
        JSONObject object= Parser.parseObject(s1);
        Assertions.assertEquals(object.serialize(), s1);

        object.set("x", new JSONValue(6));
        Assertions.assertEquals(object.serialize(),"{\"x\":6}");
    }
}
