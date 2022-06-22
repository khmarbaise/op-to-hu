package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

interface DirectoryTool {

  static List<Path> readRecursively(Path resources) {
    try (var stream = Files.walk(resources, Integer.MAX_VALUE)) {
      return stream.filter(Files::isRegularFile).toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
