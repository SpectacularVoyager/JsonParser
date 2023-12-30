package JSONParser.JSONValues;

import java.util.Objects;

public class JSONValue implements JSONElement {
    private final String val;

    public JSONValue(Object val) {
        this.val = String.valueOf(val);
    }

    public String serialize() {
        return val;
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
        JSONValue that = (JSONValue) o;

        return val.equals(that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
