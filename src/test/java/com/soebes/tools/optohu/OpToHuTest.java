package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OpToHuTest {

  List<Path> readFilesRecursively(Path resources) {
    try (var stream = Files.walk(resources, Integer.MAX_VALUE)) {
      return stream.filter(Files::isRegularFile).collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void first() {
    var resources = Path.of("src/test/resources");

    var pathStream = readFilesRecursively(resources);
    var listOfFiles = pathStream.stream().filter(Files::isRegularFile).toList();

    listOfFiles.stream().forEach(s -> System.out.println("s = " + s));
    //    try (DirectoryStream<Path> stream = Files.newDirectoryStream(resources)) {
    //      for (Path entry : stream) {
    //        System.out.println("entry = " + entry);
    //        var isDirectory = Files.isDirectory(entry);
    //
    //      }
    //    }
    //
    //    var fileCollection = Files.list(resources).filter(Files::isRegularFile).toList();
    //
    //    fileCollection.stream().forEach(s -> System.out.println("s = " + s));
  }
}