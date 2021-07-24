# Simple Spring Framework

## Package Structure

- `core`: Bean container and dependency injection
  - `annotation`
    
- `aop`: Aspect-oriented Programming support with AspectJ syntax
  - `annotation`
  - `aspect`: Class for user-defined aspect to extend
    
- `web`: MVC-Serializer pattern web framework
  - `annotation`
  - `constant`: Constant values (e.g. Http methods, status cods, etc)
  - `exception`: Base exception class to be caught by framework
  - `processor`: Components of request processor chain
  - `renderer`: Renderers for generate response
  - `serializer`: Base serializers for customizable request body serialization.
  - `type`: Classes for the framework
    
- `orm`: Object relation mapping

- `boot`: Starter of the app
  - `annotation`
    
- `util`: Utility classes for the framework

## Build Instruction

### Maven

```
<dependency>
    <groupId>com.jialehu</groupId>
    <artifactId>SimpleSpringFramework</artifactId>
<!--If using JAR instead of Maven repo, uncomment following-->
<!--    <scope>system</scope>-->
<!--    <systemPath>${project.basedir}/PATH/TO/SimpleSpringFramework-jar-with-dependencies.jar</systemPath>-->
</dependency>
```

### Build JAR

`mvn install`
