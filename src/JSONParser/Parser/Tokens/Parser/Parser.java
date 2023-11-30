package JSONParser.Parser.Tokens.Parser;

import JSONParser.JSONValues.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static JSONString parseString(String s) {
        return new JSONString(s.substring(1, s.length() - 1));
    }

    public static JSONValue parseValue(String s) {
        return new JSONValue(s);
    }

    public static JSONArray parseArray(String s) {
        String str = s.substring(1, s.length() - 1);
        List<JSONElement> elements = new ArrayList<>();
        for (String x : str.split(",")) {
            elements.add(parse(x));
        }
        return new JSONArray(elements);
    }

    public static JSONObject parseObject(String s) {
        s = s.substring(1, s.length() - 1);
        int i = 0;
        JSONObject object = new JSONObject();
        while (i < s.length()) {
            int key_start = i + 1;

            if (s.charAt(i) != '"') {
                System.out.println(s.charAt(i));
                throw new IllegalArgumentException("Key must be enclosed within \"\"");
            }
            for (++i; s.charAt(i) != '"'; i++) {
            }
            String key = (s.substring(key_start, i));
            i++;

            if (s.charAt(i) != ':') {
                throw new IllegalArgumentException("Expected \":\", got:" + s.charAt(i));
            }
            int valueStart = ++i;
            char x = s.charAt(valueStart);
            String value;
            if (x == '{') {
                value = (matchTill(s, "}", i,true));

            } else if (x == '[') {
                value = (matchTill(s, "]", i,true));
                i++;
            } else if (x == '"') {
                value = (matchTill(s, "\"", i,true));
                i++;
            } else {
                value = (matchTill(s, ",}]\0", i,false));
                i++;
            }
            object.set(key, parse(value));
            i += value.length();
        }
        return object;
    }

    public static String matchTill(String s, String x, int start, boolean includeDelim) {
        int i = start;
//        while (i < s.length() && s.charAt(i) != x) {
        while (true) {
            if (i >= s.length()) {
                if (match(x, '\0')) {
                    break;
                }
                throw new IllegalArgumentException("Expected One Of:" + x);
            }
            if (match(x, s.charAt(i))) {
                if (includeDelim) i++;
                break;
            }
            i++;
        }
        return s.substring(start, i);
    }

    private static boolean match(String pat, char x) {
        for (char c : pat.toCharArray()) {
            if (c == x) {
                return true;
            }
        }
        return false;
    }

    public static JSONElement parse(String s) {

        char x = s.charAt(0);
        if (x == '{') {
            return parseObject(s);
        } else if (x == '[') {
            return parseArray(s);
        } else if (x == '\"') {
            return parseString(s);
        } else {
            return parseValue(s);
        }
    }
}