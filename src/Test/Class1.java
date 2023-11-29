package Test;

import JSONParser.JSONValues.JSONString;
import JSONParser.JSONValues.JSONValue;

import java.util.Arrays;
import java.util.List;

public class Class1 {
    public String str1;
    public int val;
    public Class2 c;
    public List<List<Integer>> list;

    public Class1() {
    }

    @Override
    public String toString() {
        return String.format("[%s\t%s\t%s\t%s]", str1, val, c, list.toString());
    }
}

