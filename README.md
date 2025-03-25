# Chess

## Description

Chess is a modular and extensible chess engine built with Java. It focuses on decent performance, flexibility in board shapes, and custom game rules. This is a learning project, and large parts of the codebase are likely to be refactored as the project evolves to achieve its goals.

## Getting Started

### Dependencies

Ensure you have the following installed:

- Java 21
- Maven
- (For testing) JUnit and Mockito

The dependencies are managed via Maven and can be found in `pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.12.0-RC2</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.15.2</version>
    <scope>test</scope>
</dependency>
```

### Installing

Clone the repository and build the project using Maven:

```sh
git clone <repository_url>
cd Chess
mvn clean install
```

### Executing

You can run the program using Maven or by executing the JAR file directly:

```sh
mvn exec:java
```

or

```sh
java -jar target/Chess-1.0-SNAPSHOT.jar
```

## Help

If you encounter any issues, make sure:

- You are using Java 21
- Dependencies are installed properly via Maven

## Authors

- **Anton Schönfeld** - [Email](mailto\:antonschnfeld@gmail.com)

## Version History

- **0.1** - Initial development (No releases yet)

## License

This project is licensed under the MIT License - see the `LICENSE.md` file for details.

## Acknowledgments

- Inspired by a friend from school
- Learning project aimed at extensible chess AI

## Future Goals

- Support for modular board shapes
- Increased performance
- Continued refactoring to improve architecture and maintainability

