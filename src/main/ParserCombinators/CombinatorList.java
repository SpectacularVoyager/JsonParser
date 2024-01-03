package main.ParserCombinators;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.CombinatorImpl.*;
import main.ParserCombinators.Serializer.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class CombinatorList<T> {
    private final List<Combinator<T>> combinators;
    private Serializer<T> serializer;

    public CombinatorList(List<Combinator<T>> combinators) {
        this.combinators = combinators;
    }
    public void setSerializer(Serializer<T> serializer){
        this.serializer=serializer;
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

    public JSONElement serialize(Object object, Field field) {
        Class<?> type=field.getType();
        for (Combinator<T> combinator : combinators) {
            if (combinator.check(type)) {
//                return combinator.serialize(object,type,null,this.serializer);
                return combinator.serialize(object,type,(ParameterizedType) field.getGenericType(),this.serializer);
            }
        }
        throw new RuntimeException("NO COMBINATORS MATCHED");
    }

    public T deserialize(Field f,JSONElement element, ParameterizedGenerics generics) {
        Class<?> type=f.getType();
        Object o=f.getGenericType();
        if (o instanceof TypeVariable<?>) {
            if (generics != null) {
                TypeVariable<?> var = (TypeVariable<?>) o;
                type = generics.get(var.getName());
            }
        }

        for (Combinator<T> combinator : combinators) {
            if (combinator.check(type)) {
                return (T)combinator.deserialize(f,element,generics);
            }
        }
        throw new RuntimeException("NO COMBINATORS MATCHED");
    }

    public static <T> CombinatorList<T> getDefault() {
        CombinatorList<T> combinatorList=new CombinatorList<>();
        combinatorList.add(new StringCombinator<>());
        combinatorList.add(new NumberCombinator<>());
        combinatorList.add(new ArrayCombinator<>());
        combinatorList.add(new ListCombinator<>());
        combinatorList.add(new DefaultObjectCombinator<>());
        return combinatorList;
    }

}
