package com.soebes.tools.optohu;

import static com.soebes.tools.optohu.MigrateXmlHighlighter.migrateContent;
import static org.assertj.core.api.Assertions.assertThat;

import com.soebes.tools.optohu.Post.Content;
import java.util.Arrays;
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

  String EXPECTED_XML_SNIPPET = """
      ```xml
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <version>3.2.1</version>
      </plugin>
      ```
      """;

  @Test
  void name() {
    var content = new Content(Arrays.stream(XML_SNIPPET.split(System.lineSeparator())).toList());
    var expectedContent = Arrays.stream(EXPECTED_XML_SNIPPET.split(System.lineSeparator())).toList();
    var contentMigrated = migrateContent.apply(content);

    assertThat(contentMigrated.lines()).containsExactlyElementsOf(expectedContent);

  }
}
