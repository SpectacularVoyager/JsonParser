package JSONParser.JSONValues;

import java.util.*;
import java.util.Map.*;
//TODO: Implement caching for serialize

public class JSONObject implements JSONElement {
    private final HashMap<JSONString, JSONElement> map;

    public JSONObject() {
        map = new HashMap<>();
    }

    public JSONElement get(String string) {
        return get(new JSONString(string));
    }


    public JSONElement get(JSONString string) {
        if (map.containsKey(string)) {
            return map.get(string);
        }
        throw new RuntimeException("Element not found");
    }

    public void set(String string, JSONElement element) {
        set(new JSONString(string), element);
    }

    public void set(JSONString string, JSONElement element) {
        if (map.containsKey(string)) {
            map.replace(string, element);
        } else {
            map.put(string, element);
        }
    }
    public Map<JSONString,JSONElement> getMap(){
        return map;
    }


    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder("{");
        for (Entry<JSONString, JSONElement> entry : map.entrySet()) {
            sb.append(entry.getKey().serialize()).append(":").append(entry.getValue().serialize()).append(",");
        }
        if (sb.length() > 1) {
            sb.setCharAt(sb.length() - 1, '}');
            return sb.toString();
        }
        return "{}";

    }

    @Override
    public String toString() {
        return serialize();
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONObject that = (JSONObject) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
