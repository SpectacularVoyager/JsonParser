package JSONParser.JSONValues;

import java.util.*;

//TODO: Implement caching for serialize
public class JSONArray implements JSONElement {
    private final List<JSONElement> list;

    public JSONArray(List<JSONElement> list) {
        this.list = list;
    }


    public JSONArray(JSONElement... elements) {
        list = new ArrayList<>();
        Collections.addAll(list, elements);
    }


    public List<JSONElement> getList() {
        return list;
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<JSONElement> iterator = list.listIterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().serialize()).append(",");
        }
        if (sb.length() > 1) {
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }
        return "[]";
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
        JSONArray jsonArray = (JSONArray) o;
        return list.equals(jsonArray.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
