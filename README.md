# SDI - Simple Dependency Injector
Simple Dependency Injector is a Dependency Injector built in Kotlin/Java to be used in small projects.

### How to use it
There are two main annotations:
* Component: Used to annotate the classes that should be injected by the injector. This annotation should be used in implementation classes or in classes that the user wants to be instantiated by the injector (like a Bean)
* Inject: Used to annotate the classes to be injected by the injector.

##### Component annotation
The way to use the `@Component` annotation is the following:
```
@Component(classes = [MyService::class])
```
It is necessary to provide a class or classes that the component implements, so the injector could track the different implementations.
Also, if it is necessary to just track a class that does not implement an interface, it is possible by setting the own class
```
@Component(classes = [MyClass::class])
```
##### Inject annotation
The way to use the `@Inject` annotation is the following:
```
@Inject
or
@Inject("com.example.useraccount.services.impl.AccountServiceImpl")
``` 
If there is no package name provided in the annotation, the injector will inject the first implementation that it founds. On the other hand, if there is a canonical name in the annotation, the injector will provide that instance.

### Examples
[Java example](https://github.com/LuismiBarcos/SDI/tree/main/src/test/java/com)
* Special attention to `Animals` file as is the example of how to configure SDI in your java project.

[Kotlin example](https://github.com/LuismiBarcos/SDI/tree/main/src/test/kotlin/com)
* Special attention to `Main` file as is the example of how to configure SDI in your kotlin project.

### Used tools
[gitignore.io](https://www.toptal.com/developers/gitignore/)

[Used blog reference](https://dev.to/jjbrt/how-to-create-your-own-dependency-injection-framework-in-java-4eaj)

[Kotlin](https://kotlinlang.org/)