package main.JSONParser.JSONValues;

import java.util.Objects;

public class JSONString implements JSONElement {
    private final String val;

    public JSONString(String val) {
        this.val = val;
    }

    public String serialize() {
        return String.format("\"" + val + "\"");
    }

    @Override
    public Object getValue() {
        return val;
    }
    @Override
    public String toString() {
        return serialize();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONString that = (JSONString) o;
        return val.equals(that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
