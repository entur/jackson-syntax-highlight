![Build Status](https://github.com/entur/jackson-syntax-highlight/actions/workflows/maven.yml/badge.svg) 
[![Maven Central](https://img.shields.io/maven-central/v/org.entur.jackson/jackson-syntax-highlight.svg)](https://mvnrepository.com/artifact/org.entur.jackson/jackson-syntax-highlight)

# jackson-syntax-highlight
Simple utility for generating syntax-highlighted [JSON] text using the [Jackson](https://github.com/FasterXML/jackson) library. Inlines [ANSI] color-codes visible in ANSI-enabled consoles.

Features: 
  * works with the popular [Jackson] JSON library.
  * configurable color schemes
     * datatype
       * string
       * number
       * boolean
       * null
     * field name
     * comma
     * brackets
     * colon
     * whitespace

The library is primarily intended for adding coloring while doing minimal changes to existing applications. For example, coloring of status codes during unit testing.

## License
[Apache 2.0]

## Obtain
The project is built with [Maven] and is available on the central Maven repository. 

<details>
  <summary>Maven coordinates</summary>

Add the property
```xml
<jackson-syntax-highlight.version>1.1.0</jackson-syntax-highlight.version>
```

then add

```xml
<dependency>
    <groupId>org.entur.jackson</groupId>
    <artifactId>jackson-syntax-highlight</artifactId>
    <version>${jackson-syntax-highlight.version}</version>
</dependency>
```
</details>

or

<details>
  <summary>Gradle coordinates</summary>

For

```groovy
ext {
  jacksonSyntaxHighlightVersion = '1.1.0'
}
```

add

```groovy
api ("org.entur.jackson:jackson-syntax-highlight:${jacksonSyntaxHighlightVersion}")
```
</details>

# Usage
The highlighter wraps a normal [JsonGenerator]. Pretty-printing is enabled by default.

```java
// construct output generator
JsonGenerator delegate = new JsonFactory().createGenerator(writer);

// wrap with syntax highlighter
JsonGenerator jsonGenerator = new SyntaxHighlightingJsonGenerator(delegate);

// write JSON output
jsonGenerator.writeStartObject(); // start root object
jsonGenerator.writeFieldName("name");
jsonGenerator.writeNumber(123);
jsonGenerator.writeEndObject();
// .. etc
```
Supply an instance of `SyntaxHighlighter` using the builder:

```java
SyntaxHighlighter highlighter = DefaultSyntaxHighlighter
                                    .newBuilder()
                                    .withNumber(AnsiSyntaxHighlight.BLUE)
                                    .build();
		
JsonGenerator jsonGenerator = new SyntaxHighlightingJsonGenerator(delegate, highlighter);
```

In addition, the JSON structure can be tracked via [JsonStreamContextListener](src/main/java/org/entur/jackson/jsh/JsonStreamContextListener.java), for stateful coloring of subtrees. 

## Highlighting an object
Write a full object using `writeObject`, i.e.

```java
JsonGenerator jsonGenerator = new SyntaxHighlightingJsonGenerator(delegate, highlighter, prettyprint);
jsonGenerator.writeObject(obj);
```

## See also

 * [logback-logstash-syntax-highlighting-decorators]

# History

 - 1.1.0: Forked from [jackson-syntax-highlight](https://github.com/skjolber/jackson-syntax-highlight) due to too few maintainers.

[Apache 2.0]:          	http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:       	https://github.com/entur/jackson-syntax-highlight/issues
[Maven]:                http://maven.apache.org/
[SyntaxHighlighter]:	src/main/java/org/entur/jackson/jsh/SyntaxHighlighter.java
[Jackson]:				https://github.com/FasterXML/jackson
[ANSI]:					https://en.wikipedia.org/wiki/ANSI_escape_code
[JSON]:					https://no.wikipedia.org/wiki/JSON
[JsonGenerator]:		https://github.com/FasterXML/jackson-core/blob/master/src/main/java/com/fasterxml/jackson/core/JsonGenerator.java
[logback-logstash-syntax-highlighting-decorators]: https://github.com/entur/logback-logstash-syntax-highlighting-decorators
