package com.soebes.tools.optohu;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

class OpToHuTest {

  @Test
  void first() {
    var resources = Path.of("src/test/resources");

    var pathStream = DirectoryTool.readRecursively(resources);
    var listOfFiles = pathStream.stream().filter(Files::isRegularFile).toList();

    var collect = listOfFiles.stream().map(File.intoContent).map(OctopressMarkdown.intoPost).toList();
    collect.forEach(s -> System.out.println("s = " + s));
  }

  @Test
  void readAllLines() {
    var markdownFile = Path.of(
        "src/test/resources/source/_posts/2019/01/2019-01-21-apache-maven-invoker-plugin-version-3-dot-2-0-released.md");
    var fileLines = File.intoContent.apply(markdownFile);
  }
}