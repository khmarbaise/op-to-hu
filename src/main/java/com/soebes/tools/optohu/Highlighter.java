package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

interface Highlighter {

  UnaryOperator<Post> migrate = post -> {
    var contentLines = post.content().stream().map(line -> switch (line) {
      case "{% highlight bash linenos %}" -> "```bash";
      case "{% endhighlight %}" -> "```";
      default -> line;
    }).toList();

    return Post.from(post, contentLines);
  };

}
