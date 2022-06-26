package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

interface MigrateHighlighter {

  UnaryOperator<Post> migrate = post -> {
    var contentLines = post.content().lines().stream().map(line -> switch (line) {
      case "{% highlight bash linenos %}", "{% highlight bash %}" -> "```bash";
      case "{% highlight perl linenos %}" -> "```perl";
      case "{% highlight xml linenos %}" -> "```xml";
      case "{% highlight java linenos %}" -> "```java";
      case "{% endhighlight %}" -> "```";
      default -> line;
    }).toList();

    return Post.from(post, new Content(contentLines));
  };

}
