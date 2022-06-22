package com.soebes.tools.optohu;

import java.nio.file.Files;
import java.nio.file.Path;

interface File {

  static FileAndContent intoLines(Path markdownFile) {
    try {
      var contentOfFile = Files.readAllLines(markdownFile).stream().map(String::trim).toList();
      return new FileAndContent(markdownFile, contentOfFile);
    } catch (Exception e) {
      throw new RuntimeException(markdownFile + " " + e.getMessage(), e);
    }
  }

}
