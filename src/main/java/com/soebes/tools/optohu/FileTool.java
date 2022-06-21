package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

interface FileTool {

  static List<String> intoLines(Path markdownFile) {
    try {
      return Files.readAllLines(markdownFile).stream().map(String::trim).toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
