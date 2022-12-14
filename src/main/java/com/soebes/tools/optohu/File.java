package com.soebes.tools.optohu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

interface File {

  Function<Path, FileAndContent> intoContent = markdownFile -> {
    try {
      var contentOfFile = Files.readAllLines(markdownFile).stream().toList();
      return new FileAndContent(markdownFile, new Content(contentOfFile));
    } catch (Exception e) {
      throw new RuntimeException(markdownFile + " " + e.getMessage(), e);
    }
  };

}
