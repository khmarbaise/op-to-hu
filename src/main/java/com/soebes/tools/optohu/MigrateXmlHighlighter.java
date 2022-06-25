package com.soebes.tools.optohu;

import com.soebes.tools.optohu.Post.Content;
import java.util.function.UnaryOperator;

interface MigrateXmlHighlighter {

  UnaryOperator<Content> migrateContent = content -> {
    var contentLines = content.lines().stream().map(line -> switch (line) {
      case "``` xml", "```xml" -> "```xml";
      default -> line;
    }).toList();

    return new Content(contentLines);
  };

}
