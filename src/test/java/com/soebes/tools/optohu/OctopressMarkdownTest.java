package com.soebes.tools.optohu;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class OctopressMarkdownTest {

  private static String FIRST_MARKDOWN = """
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
  void firstMarkdownConversion() {
    var split = Arrays.stream(FIRST_MARKDOWN.split(System.lineSeparator())).collect(Collectors.toList());
    var fileAndLines = new FileAndContent(Path.of("test.md"), new Content(split));
    var post = OctopressMarkdown.intoPost.apply(fileAndLines);
    assertThat(post.layout()).isEqualTo(Post.Layout.POST);
    assertThat(post.title()).isEqualTo("Apache Maven Assembly Plugin Version 3.1.1 Released");
    assertThat(post.publishingTime()).isEqualTo(
        LocalDateTime.parse("2019-01-02 23:36:42", OctopressMarkdown.DATE_TIME_FORMATTER));
    assertThat(post.categories().categoryTags()).containsExactly("Neuigkeiten", "BM", "Maven", "Maven-Plugins",
        "Maven-Plugin-Releases");
    assertThat(post.content().lines()).containsExactly("First Line of Content.");
  }

  private static String SECOND_MARKDOWN = """
      ---
      layout: post
      title: "Apache Maven Assembly Plugin Version 3.1.1 Released"
      date: 2019-01-02 23:36:42
      comments: true
      categories: 
      ---
      First Line of Content.
      Second Line of Content.
      """;

  @Test
  void secondMarkdownConversion() {
    var split = Arrays.stream(SECOND_MARKDOWN.split(System.lineSeparator())).collect(Collectors.toList());
    var fileAndLines = new FileAndContent(Path.of("test.md"), new Content(split));
    var post = OctopressMarkdown.intoPost.apply(fileAndLines);
    assertThat(post.layout()).isEqualTo(Post.Layout.POST);
    assertThat(post.title()).isEqualTo("Apache Maven Assembly Plugin Version 3.1.1 Released");
    assertThat(post.publishingTime()).isEqualTo(
        LocalDateTime.parse("2019-01-02 23:36:42", OctopressMarkdown.DATE_TIME_FORMATTER));
    assertThat(post.categories().categoryTags()).isEmpty();
    assertThat(post.content().lines()).containsExactly("First Line of Content.", "Second Line of Content.");
  }

  @Test
  void second_test() {
    var markdownPost = Path.of(
        "src/test/resources/source/_posts/2019/01/2019-01-21-apache-maven-invoker-plugin-version-3-dot-2-0-released.md");
    var fileContent = File.intoContent.apply(markdownPost);

    var post = OctopressMarkdown.intoPost.apply(fileContent);
    System.out.println("post = " + post);
  }
}
