package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

interface MigrateJavaHighlighter {

  UnaryOperator<Post> migrateContent = post -> {
    var content = post.content();
    var contentLines = content.lines().stream().map(line -> switch (line) {
      case "``` java", "```java" -> "```java";
      default -> line;
    }).toList();

    return Post.from(post, new Content(contentLines));
  };

}
