package com.soebes.tools.optohu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.Test;

class MigrateHighlighterTest {

  @Test
  void emptyContent() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost, List.of());
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content()).isEmpty();
  }

  @Test
  void singleLineContent() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost, List.of("Test Line"));
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content()).containsExactly("Test Line");
  }

  @Test
  void replacingOldStyleWithNewStyle() {
    var mockPost = mock(Post.class);
    var post = Post.from(mockPost, List.of("{% highlight bash linenos %}", "Highlighted", "{% endhighlight %}"));
    var result = MigrateHighlighter.migrate.apply(post);
    assertThat(result.content()).containsExactly("```bash", "Highlighted", "```");
  }
}