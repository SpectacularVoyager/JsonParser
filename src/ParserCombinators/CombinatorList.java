package ParserCombinators;

import JSONParser.JSONValues.JSONElement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CombinatorList<T> {
    private final List<Combinator<T>> combinators;

    public CombinatorList(List<Combinator<T>> combinators) {
        this.combinators = combinators;
    }

    public CombinatorList() {
        this.combinators = new ArrayList<>();
    }


    public List<Combinator<T>> getCombinators() {
        return combinators;
    }

    public void add(Combinator<T> combinator) {
        combinators.add(combinator);
    }

    public JSONElement serialize(T object, Field field) {
        Class<?> type=field.getType();
        for (Combinator<T> combinator : combinators) {
            if (combinator.check(type)) {
                return combinator.serialize(object,type,(ParameterizedType) field.getGenericType());
            }
        }
        throw new RuntimeException("NO COMBINATORS MATCHED");
    }

    public T deserialize(JSONElement element, Class<?> type) {
        for (Combinator<T> combinator : combinators) {
            if (combinator.check(type)) {
                return combinator.deserialize(element);
            }
        }
        throw new RuntimeException("NO COMBINATORS MATCHED");
    }

    public static <T> CombinatorList<T> getDefault() {
        CombinatorList<T> combinatorList=new CombinatorList<>();

        return combinatorList;
    }

}
