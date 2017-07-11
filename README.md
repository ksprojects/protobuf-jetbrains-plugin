## Protobuf Support for JetBrains IDEs

[![Join the chat at https://gitter.im/protostuff/protobuf-jetbrains-plugin](https://badges.gitter.im/protostuff/protobuf-jetbrains-plugin.svg)](https://gitter.im/protostuff/protobuf-jetbrains-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[Protobuf Support Plugin](https://plugins.jetbrains.com/plugin/8277) for IntelliJ IDEA & other JetBrains products.

Latest plugin release is compatible with IntelliJ IDEA 2017.1 (older releases are compatible with IDEA 13+). 
Other JetBrains IDEs of the same or higher version should be supported as well. 

Compatibility Matrix:

| Plugin Version  | IDE Version Range  |
|-----------------|--------------------|
| 0.9.0           | IDEA 2017.1+       |
| 0.8.0           | IDEA 2016.1+       |
| 0.6.0           | IDEA 13 - IDEA 15  |


### Installation

IntelliJ IDEA should suggest you to install plugin automatically 
when you open `.proto` file.
You can install plugin manually by opening "Plugins" settings, 
"Browse repositories..." - search for "Protobuf Support".

Plugin page: https://plugins.jetbrains.com/plugin/8277-protobuf-support


### Roadmap

https://github.com/protostuff/protobuf-jetbrains-plugin/wiki/Roadmap

### Build

Run following command in the shell:

```
./gradlew build
```

It should be possible to run build on any platform (Linux, Windows, MacOS) where
[Gradle](https://gradle.org/) is supported.

JDK 8 must be installed and available on PATH in order to build plugin.

### Run IntelliJ IDEA with enabled plugin (for development)

```
./gradlew runIdea
```

### Screenshots

![image](https://github.com/protostuff/protostuff-jetbrains-plugin/wiki/sample-2016-04-11.png)


