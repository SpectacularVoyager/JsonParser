package main.ParserCombinators;

import main.JSONParser.JSONValues.JSONElement;
import main.JSONParser.ParameterizedGenerics;
import main.ParserCombinators.Serializer.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;


public interface Combinator<T> {




     public boolean check(Class<?> type);

     JSONElement serialize(Object o, Class<?> type, ParameterizedType parameterizedType, Serializer<T> serializer) ;

     Object deserialize(Field f, JSONElement element, ParameterizedGenerics generics)  ;

}
