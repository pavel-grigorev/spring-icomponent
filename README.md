[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.thepavel/spring-icomponent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.thepavel/spring-icomponent)

# spring-icomponent

This library adds support for the `@Component` annotation on interfaces. It creates a dynamic proxy implementation of an interface decorated with `@Component` (by default) or any other annotation that one would choose. The proxy object delegates method invocations to the user-defined method handlers. The library provides a couple of options to map method invocations to actual method handlers. A method handler is supplied with all metadata of the method being called, which makes the method declaration itself be an extra parameter for the handler logic.

Here is an example of what can be built using this tool:

```java
@Service
public interface EmailService {

  @Subject("email.subject.confirmation")
  @Template("confirmation")
  void sendConfirmation(@Param("username") String username, @Param("link") String link, @To String email);

  @Subject("email.subject.welcome")
  @Template("welcome")
  void sendWelcome(@Param("user") @To User user);
}
```

Follow [this page on GitHub](https://github.com/pavel-grigorev/spring-icomponent-demo) for this example's source code.

An example of a library built on top of `spring-icomponent` is [spring-resource-reader](https://github.com/pavel-grigorev/spring-resource-reader): a declarative resource reader with the resource content auto-conversion capabilities.

# Motivation

This library is inspired by Spring Data and also by [spring-cloud-openfeign](https://github.com/spring-cloud/spring-cloud-openfeign). The general goal is to shift further towards a declarative approach in the code. The  problem that `spring-icomponent` attempts to solve is to provide an easy-to-use platform for small project-scoped and cross-project frameworks like the one in the example above.

# Adding to your project

Gradle:
```
dependencies {
  implementation 'org.thepavel:spring-icomponent:1.0.8'
}
```

Maven:
```
<dependency>
  <groupId>org.thepavel</groupId>
  <artifactId>spring-icomponent</artifactId>
  <version>1.0.8</version>
</dependency>
```

# Scanning packages

To activate the framework add `@InterfaceComponentScan` to a java configuration:

```java
@Configuration
@ComponentScan
@InterfaceComponentScan
public class AppConfiguration {
}
```

Usage is similar to `@ComponentScan`. This configuration will scan from the package of `AppConfiguration`. You can also specify `basePackages` or `basePackageClasses` to define specific packages to scan.

# Building method handlers

Method handler is a bean implementing the `MethodHandler` interface:

```java
@Component
public class ToStringMethodHandler implements MethodHandler {
  @Override
  public Object handle(Object[] arguments, MethodMetadata methodMetadata) {
    return arguments.length > 0 && arguments[0] != null ? arguments[0].toString() : null;
  }
}
```

Method handler receives invocation arguments and metadata of the method being called.

# Linking method handlers with methods

There are multiple options to do so:
- Declaring `@Handler` annotation on a method
- Declaring `@Handler` annotation on a class
- Implementing a default method handler
- Implementing `MehodHandlerResolver`

## Declaring `@Handler` annotation on a method

The following example demonstrates the options to define a method handler for a method:

```java
@Service
public interface ToStringService {
  @Handler("toStringMethodHandler")
  String intToString(int arg);

  @Handler(beanName = "toStringMethodHandler")
  String longToString(long arg);

  @Handler(beanType = ToStringMethodHandler.class)
  String doubleToString(double arg);
}
```

## Declaring `@Handler` annotation on a class

Method handler declared on a class will handle invocations of all methods of the class that do not have their own `@Handler` declaration. In the following example all methods except `join` trigger `toStringMethodHandler`. The `join` method triggers `commaJoiningMethodHandler`.

```java
@Service
@Handler("toStringMethodHandler")
public interface ToStringService {
  String intToString(int arg);
  String longToString(long arg);
  String doubleToString(double arg);

  @Handler("commaJoiningMethodHandler")
  String join(String... strings);
}
```

## Default method handler

If present, a default method handler would handle invocations of all methods that do not have a handler defined by other options. To declare a method handler as default add the `@DefaultMethodHandler` annotation:

```java
@Component
@DefaultMethodHandler
public class LoggingMethodHandler implements MethodHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMethodHandler.class);

  @Override
  public Object handle(Object[] arguments, MethodMetadata methodMetadata) {
    LOGGER.warn("There is no specific method handler for {}. Returning null...", methodMetadata.getSourceMethod());
    return null;
  }
}
```

There can only be one default method handler in the application.

## Implementing `MehodHandlerResolver`

Method handler resolvers is a more general way to map methods to actual method handlers. All options described above are implemented as method handler resolvers.

Method handler resolver is a bean implementing `MehodHandlerResolver`. For a given method metadata it should return a `MethodHandler` object or `null`.

Multiple method handler resolvers may be defined in the application, each being responsible for a specific type of method handler.

```java
@Component
public class ToStringMethodHandlerResolver implements MethodHandlerResolver {
  @Autowired
  private ToStringMethodHandler toStringMethodHandler;

  @Override
  public MethodHandler getMethodHandler(MethodMetadata methodMetadata) {
    if (methodMetadata.getReturnTypeMetadata().getResolvedType() == String.class &&
        methodMetadata.getParametersMetadata().size() == 1) {
      return toStringMethodHandler;
    }
    return null;
  }
}
```

Given the method handler resolver in the example above, invocation of any method in the following service will trigger `toStringMethodHandler`.

```java
@Service
public interface ToStringService {
  String intToString(int arg);
  String longToString(long arg);
  String doubleToString(double arg);
}
```

## Method handler lookup sequence

When a proxy object is being created for an interface, the framework performs a method handler lookup for every method of the interface. It does so by running all method handler resolvers one by one until it obtains a method handler. The first method handler obtained is going to be linked with the method. To control the order in which the resolvers run use the `@Order` annotation just like you normally do in Spring.

The complete method handler lookup sequence for a method is:

1. If a method handler is defined by the `@Handler` annotation declared on the method then it is used. If the specified method handler does not exist then `NoSuchBeanDefinitionException` is thrown.
1. If a method handler is defined by the `@Handler` annotation declared on a class then it is used. If the specified method handler does not exist then `NoSuchBeanDefinitionException` is thrown.
1. User-defined method handler resolvers run in an optionally ordered sequence until one of them returns a method handler object or all of them return `null`. The first obtained method handler is used, if any.
1. If the default method handler is present in the application then it is used.
1. Otherwise `BeanInstantiationException` is thrown saying `"No method handler found for method ..."`.

# Method metadata

An object of type `MethodMetadata` is passed to `MethodHandler` and `MethodHandlerResolver` providing useful information on a method being called or resolved.

Method metadata includes information about the annotations declared on the method itself, its parameters, return type and `throws` declarations. Annotation information comes in the form of Spring's `MergedAnnotations` object.

The framework makes an effort to resolve all generic variables into the concrete types in the method return type, parameter types and `throws` declarations. The information about the resolved types is included in the method metadata. There are situations when the actual type can not be resolved. In this case `getResolvedType()` returns `Object.class`.

Given the class structure:

```java
public interface MyFunction<T, R> {
  R apply(T arg);
}

public interface Superinterface<T, R> extends MyFunction<List<T>, Map<T, R>> {
}

@Component
public interface Test extends Superinterface<String, Integer> {
}
```

Assuming that the method being handled is `apply`, the following code:

```java
System.out.println(methodMetadata.getSourceMethod().getName());
System.out.println(methodMetadata.getReturnTypeMetadata().getResolvedType());
System.out.println(methodMetadata.getParametersMetadata().get(0).getResolvedType());
```

Would print:

```
apply
java.util.Map<java.lang.String,java.lang.Integer>
java.util.List<java.lang.String>
```

More specifically, `getResolvedType()` never returns an object of type `TypeVariable` but either `Class` or `ParameterizedType` or `GenericArrayType`. `ParameterizedType` and `GenericArrayType` in turn would not contain any `TypeVariable`.

If a type is not parameterized, i.e. is a concrete class, `getResolvedType()` returns the class itself. Given the component:

```java
@Component
public interface Test {
  List getByName(String name);
}
```

Assuming that the method being handled is `getByName`, the following code:

```java
System.out.println(methodMetadata.getSourceMethod().getName());
System.out.println(methodMetadata.getReturnTypeMetadata().getResolvedType());
System.out.println(methodMetadata.getParametersMetadata().get(0).getResolvedType());
```

Would print:

```
getByName
java.util.List
java.lang.String
```

# Customizing annotation

When you want to build a custom annotation to mark some type of service, you usually meta-annotate it like so:

```java
@Retention(RUNTIME)
@Target(TYPE)
@Component
public @interface Custom {
  @AliasFor(annotation = Component.class)
  String value() default "";
}

@Custom
public interface MyService {
  void doWork();
}
```

With this approach both `@Custom` and `@Component` would be available for marking interfaces. But if you want `@Custom` to be the only option, set the `annotation` attribute in `@InterfaceComponentScan`:

```java
@Retention(RUNTIME)
@Target(TYPE)
public @interface Custom {
  String value() default "";
}

@Configuration
@InterfaceComponentScan(annotation = Custom.class)
public class AppConfig {
}

@Custom // picked up
public interface MyService {
  void doWork();
}

@Component // ignored
public interface TestService {
  void doWork();
}
```

The annotation attribute name is also customizable:

```java
@Retention(RUNTIME)
@Target(TYPE)
public @interface Custom {
  String beanName() default "";
}

@Configuration
@InterfaceComponentScan(
    annotation = Custom.class,
    beanNameAnnotationAttribute = "beanName"
)
public class AppConfig {
}

@Custom(beanName = "myServiceBean")
public interface MyService {
  void doWork();
}
```

Note that even though there can not be multiple direct declarations of `@InterfaceComponentScan`, multi-declaration is supported with the meta-annotations. For example:

```java
@Retention(RUNTIME)
@Target(TYPE)
public @interface Custom {
  String value() default "";
}

@Retention(RUNTIME)
@Target(TYPE)
public @interface Dummy {
  String value() default "";
}

@Retention(RUNTIME)
@Target(TYPE)
@InterfaceComponentScan(annotation = Custom.class)
public @interface CustomScan {
}

@Retention(RUNTIME)
@Target(TYPE)
@InterfaceComponentScan(annotation = Dummy.class)
public @interface DummyScan {
}

@Configuration
@CustomScan
@DummyScan
public class AppConfig {
}
```
