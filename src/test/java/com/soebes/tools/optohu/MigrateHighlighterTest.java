package com.soebes.tools.optohu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.Test;

class MigrateHighlighterTest {

  @Test
  void emptyContent() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost, new Content(List.of()));
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content().lines()).isEmpty();
  }

  @Test
  void singleLineContent() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost, new Content(List.of("Test Line")));
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content().lines()).containsExactly("Test Line");
  }

  @Test
  void replacingOldStyleWithNewStyle() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost,
        new Content(List.of("{% highlight bash linenos %}", "Highlighted", "{% endhighlight %}")));
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content().lines()).containsExactly("```bash", "Highlighted", "```");
  }

  @Test
  void javaHighlighter() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost,
        new Content(List.of("{% highlight java linenos %}", "Highlighted", "{% endhighlight %}")));
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content().lines()).containsExactly("```java", "Highlighted", "```");
  }
}