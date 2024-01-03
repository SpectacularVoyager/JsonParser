# A Reflection based JSON Parser for Java



### A Reflection based JSON Parser and serializer In Java to manage JSON DAOs

----

### USAGE
#### Initialize Mapper for your class

```java
    //Create a generics object with for Class1<Integer>
    ParameterizedGenerics generics=new ParameterizedGenerics(Class2.class);

    //Create a serializer,Here we create a reflectionSerializer.We can also use a Custom Serializer
    Serializer<Class2> serializer=new ReflectiveSerializer<>(Class2.class,generics);

    //Create a mapper
    Mapper<Class2> mapper=new Mapper<>(serializer);
```
#### To Parse an example json object 


```json
    {"a":10,"b":20}
```
```    java    
    //Parse Object
    JSONObject object=Parser.parseObject("{\"a\":10,\"b\":20}");
    
    //Deserialize
    Class2 obj=mapper.deserialize(object, Class2.class);
    
    //Print a (It should be 10)
    System.out.println(obj.a);
```
### Features
* Works with any class
* Fully supports generic classes
* Easy to change default behaviour using Custom Serializers

### Upcoming
- [ ] Support for Multi-dimensional Arrays
- [ ] Support for finding private/protected fields
- [ ] Caching JSONArray and JSONObjects
- [ ] Enabling Serialization for Combinators(Custom Serializers)
