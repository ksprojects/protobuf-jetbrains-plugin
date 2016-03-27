## Google Protocol Buffers support for JetBrains IDEs

Google Protobuf Plugin for IntelliJ IDEA & other JetBrains products.

Based on [antlr4-jetbrains-adapter](https://github.com/antlr/jetbrains/) and ANTLR 4 grammar from [protostuff-compiler](https://github.com/protostuff/protostuff-compiler/tree/master/protostuff-parser/src/main/antlr4/io/protostuff/compiler/parser).

Plugin is compatible with IntelliJ IDEA 2016.1. Other JetBrains IDEs of the same or higher version should be supported as well. 

### Development Checklist

| #  | Feature                      | Status        | Planned Release |
|----|------------------------------|---------------|------------------|
| 1  | Syntax Highligh              | In progress.  | 0.1, April 2016  |
| 2  | Code Formatting              |               |                  |
| 3  |                              |               |                  |
| 4  |                              |               |                  |

#### Syntax Highligh

- [x] keywords
- [ ] enum constants 
- [ ] numbers
- [ ] scalar value types (currently they are not keywords)
- [ ] color settings

### Build

```
./gradlew build
```

Requirements:

1. JDK 8

### Run IntelliJ IDEA with enabled plugin (for development)

```
./gradlew runIdea
```

### Screenshots

![image](https://cloud.githubusercontent.com/assets/4040120/14066979/0e94a4b6-f462-11e5-90ca-27e12a198169.png)

### Links

1. https://github.com/vsch/ide-examples/blob/wiki/IntelliJIdeaPsiCookbook.md
2. [Custom Language Support Tutorial](http://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support_tutorial.html)
3. [Building plugins with Gradle](http://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system.html)
4. https://github.com/antlr/jetbrains
5. https://github.com/antlr/jetbrains/blob/master/doc/plugin-dev-notes.md
6. https://github.com/antlr/jetbrains-plugin-sample
7. https://bintray.com/antlr/maven
