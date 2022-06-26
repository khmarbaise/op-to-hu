package com.soebes.tools.optohu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MigrateCodeblockHighlighterTest {

  @Test
  void second() {
    String x = "{% codeblock parent POM file %}";
    var result = MigrateCodeblockHighlighter.filterWithoutType.apply(x);
    assertThat(result).isEqualTo("```");
  }

  @Test
  void first() {
    String x = "{% codeblock Downloading Artifacts lang:xml %}";
    var result = MigrateCodeblockHighlighter.filter.apply(x);
    assertThat(result).isEqualTo("```xml");
  }

  @Test
  void third() {
    String lines = """
        {% codeblock Downloading Artifacts lang:xml %}
        Test code block.
        Test code block.
        {% endcodeblock %}
        """;

    String expectedContentResult = """
        ```xml
        Test code block.
        Test code block.
        ```
        """;

    var mockPost = mock(Post.class);

    var content = new Content(Arrays.stream(lines.split(System.lineSeparator())).toList());
    var expectedContent = Arrays.stream(expectedContentResult.split(System.lineSeparator())).toList();

    var post = Post.from(mockPost, content);
    var contentMigrated = MigrateCodeblockHighlighter.highlighter.apply(post);

    assertThat(contentMigrated.content().lines()).containsExactlyElementsOf(expectedContent);

  }
}