package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ReadOctopressMarkdownTest {

  private static String HEADER = """
      ---
      layout: post
      title: "Apache Maven Assembly Plugin Version 3.1.1 Released"
      date: 2019-01-02 23:36:42
      comments: true
      categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]
      ---
      """;

  @Test
  void name() {
    var split = Arrays.stream(HEADER.split(System.lineSeparator())).map(String::trim).collect(Collectors.toList());
    split.forEach(s -> System.out.println("s = " + s));
  }

  enum Layout {
    post
  }

  private Pattern TITLE = Pattern.compile("^title: \"(.*?)\"");

  void convertToXX(List<String> lines) {
    // check minimum size (number of lines) 7
    if (!lines.get(0).equals("---")) {
      throw new IllegalStateException("Beginning of the post is not correct missing ---");
    }
    if (!lines.get(1).equals("layout: post")) {
      throw new IllegalStateException("layout: post Not found.");
    }
    var matcher = TITLE.matcher(lines.get(2));
    if (!matcher.matches()) {
      throw new IllegalStateException("Title not found");
    }
    var title = matcher.group(0);


    /*
s = date: 2019-01-21 19:45:57
s = comments: true
s = categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]

     */
  }

  @Test
  void second_test() throws IOException {
    var linesOfMarkdownFile = Files.readAllLines(Path.of(
            "src/test/resources/source/_posts/2019/01/2019-01-21-apache-maven-invoker-plugin-version-3-dot-2-0-released.md"))
        .stream()
        .map(String::trim)
        .toList();

    linesOfMarkdownFile.forEach(s -> System.out.println("s = " + s));

    convertToXX(linesOfMarkdownFile);
  }
}
