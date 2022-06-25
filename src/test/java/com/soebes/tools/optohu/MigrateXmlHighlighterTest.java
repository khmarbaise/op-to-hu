package com.soebes.tools.optohu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

class MigrateXmlHighlighterTest {

  String XML_SNIPPET = """
      ``` xml
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <version>3.2.1</version>
      </plugin>
      ```
      """;

  UnaryOperator<Post> migrate = (Post post) -> {
    var contentLines = post.content().stream().map(line -> switch (line) {
      case "``` xml", "```xml" -> "```xml";
      default -> line;
    }).toList();

    return Post.from(post, contentLines);
  };

  record Content(List<String> lines) {

  }

  UnaryOperator<Content> migrateContent = content -> {
    var contentLines = content.lines.stream().map(line -> switch (line) {
      case "``` xml", "```xml" -> "```xml";
      default -> line;
    }).toList();

    return new Content(contentLines);
  };

  @Test
  void name() {
    var contentLines = Arrays.stream(XML_SNIPPET.split(System.lineSeparator())).map(String::trim).toList();
    var content = new Content(contentLines);
    var contentMigrated = migrateContent.apply(content);

    assertThat(contentMigrated.lines()).containsExactly("```xml", "<plugin>",
        "<groupId>org.apache.maven.plugins</groupId>", "<artifactId>maven-source-plugin</artifactId>",
        "<version>3.2.1</version>", "</plugin>", "```"

    );

  }
}
