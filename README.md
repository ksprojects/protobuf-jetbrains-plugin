## Protobuf Support for JetBrains IDEs

[![Join the chat at https://gitter.im/protostuff/protobuf-jetbrains-plugin](https://badges.gitter.im/protostuff/protobuf-jetbrains-plugin.svg)](https://gitter.im/protostuff/protobuf-jetbrains-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[Protobuf Support Plugin](https://plugins.jetbrains.com/plugin/8277) for IntelliJ IDEA & other JetBrains products.

Latest plugin release is compatible with IntelliJ IDEA 2017.1 (older releases are compatible with IDEA 13+). 
Other JetBrains IDEs of the same or higher version should be supported as well. 

Compatibility Matrix:

| Plugin Version  | IDE Version Range  |
|-----------------|--------------------|
| 0.9.0+          | IDEA 2017.1+       |
| 0.8.0           | IDEA 2016.1+       |
| 0.6.0           | IDEA 13 - IDEA 15  |


### Installation

IntelliJ IDEA should suggest you to install plugin automatically 
when you open `.proto` file.
You can install plugin manually by opening "Plugins" settings, 
"Browse repositories..." - search for "Protobuf Support".

Plugin page: https://plugins.jetbrains.com/plugin/8277-protobuf-support

### Configuration

The plugin does not require configuration by default, for majority of projects it should work out of the box.

#### Imports

If you see 'File not found' error mark for the import statement, it means plugin cannot resolve target file within current set of source (or resource) roots.

In order to fix it, you should tell to plugin where is the "sources root" for your proto files.

There are a couple ways of doing that:

1. Mark directory as a source/resources root (right click on folder in project structure, "Mark forlder as...."):

![image](https://user-images.githubusercontent.com/4040120/28202383-c96d6f08-687d-11e7-905d-53dfcf6e0799.png)

2. Configure directory as source/resources folder in `maven`/`gradle` or other build system (if IDEA has support for that build system). 

   For gradle you can do it like this:
   ```gradle
   idea {
       module {
           sourceDirs += file("${projectDir}/src/main/proto")
       }
   }
   ```
  
  for maven, you can add as a resource directory:
   
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>groupId</groupId>
    <artifactId>artifactId</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    
    <dependencies>
      ...
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>src/main/proto</directory>
            </resource>
        </resources>
    </build>
</project>
```
   

3. You can go to plugin settings, and add source roots there (this is also useful for external imports):

   ![image](https://user-images.githubusercontent.com/4040120/28202438-0fbe29ca-687e-11e7-964a-bb1f10dfcd4f.png)

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


