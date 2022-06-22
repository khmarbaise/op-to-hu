package com.soebes.tools.optohu;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class MarkdownTest {

  private static String HEADER = """
      ---
      layout: post
      title: "Apache Maven Assembly Plugin Version 3.1.1 Released"
      date: 2019-01-02 23:36:42
      comments: true
      categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]
      ---
      First Line of Content.
      """;

  @Test
  void basicConversion() {
    var split = Arrays.stream(HEADER.split(System.lineSeparator())).map(String::trim).collect(Collectors.toList());
    var fileAndLines = new FileAndContent(Path.of("test.md"), split);
    var post = Markdown.intoPost.apply(fileAndLines);
    assertThat(post.layout()).isEqualTo(Layout.post);
    assertThat(post.title()).isEqualTo("Apache Maven Assembly Plugin Version 3.1.1 Released");
    assertThat(post.publishingTime()).isEqualTo("2019-01-02 23:36:42");
    assertThat(post.categories()).containsExactly("Neuigkeiten", "BM", "Maven", "Maven-Plugins",
        "Maven-Plugin-Releases");
    assertThat(post.content()).containsExactly("First Line of Content.");
  }

  @Test
  void second_test() throws IOException {
    var markdownPost = Path.of(
        "src/test/resources/source/_posts/2019/01/2019-01-21-apache-maven-invoker-plugin-version-3-dot-2-0-released.md");
    var fileContent = File.intoLines(markdownPost);

    var post = Markdown.intoPost.apply(fileContent);
    System.out.println("post = " + post);
  }
}
