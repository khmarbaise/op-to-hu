package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

interface File {

  static FileAndContent intoLines(Path markdownFile) {
    try {
      return new FileAndContent(markdownFile, Files.readAllLines(markdownFile).stream().map(String::trim).toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
