package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

abstract class MigrateXmlHighlighter {

  public static final UnaryOperator<Post> migrateContent = post -> {
    var content = post.content();
    var contentLines = content.lines().stream().map(line -> switch (line) {
      case "``` xml", "```xml" -> "```xml";
      default -> line;
    }).toList();

    return Post.from(post, new Content(contentLines));
  };

}
