package main.JSONParser.Parser;

import main.JSONParser.JSONValues.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static JSONString parseString(String s) {
        return new JSONString(s.substring(1, s.length() - 1));
    }

    public static JSONValue parseValue(String s) {
        return new JSONValue(s);
    }

    public static JSONArray parseArray(String s) {
        s = s.substring(1, s.length() - 1);
        List<JSONElement> elements = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            Object[] object = getValue(s, i);
            JSONElement value = parse((String) object[0]);
            elements.add(value);
            i = (int) object[1];
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
            Object[] o = getValue(s, i);
            String value = (String) o[0];
            object.set(key, parse(value));
            i = (int) o[1];
        }
        return object;
    }

    public static Object[] getValue(String s, int i) {
        char x = s.charAt(i);
        String value;
        if (x == '{') {
            value = matchBracket(s, i, '{', '}');
            i++;
        } else if (x == '[') {
            value = matchBracket(s, i, '[', ']');
            i++;
        } else if (x == '"') {
            value = "\"" + (matchTill(s, "\"", i + 1, true));
            i++;
        } else {
            value = (matchTill(s, ",}]\0", i, false));
            i++;
        }
        i += value.length();
        return new Object[]{value, i};
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

    private static String matchBracket(String s, int i, char l_brace, char r_brace) {
//        i++;
        int start = i;
        int a = 0;
        while (a > 0 || start == i) {
            if (i >= s.length()) {
                throw new RuntimeException(String.format("Expected %c got end of String", r_brace));
            }
            char x = s.charAt(i);
            if (x == l_brace) {
                a++;
            }
            if (x == r_brace) {
                a--;
            }
            i++;
        }
        return s.substring(start, i);
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
