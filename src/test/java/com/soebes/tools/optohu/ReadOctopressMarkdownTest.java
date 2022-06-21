package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

  //date: 2019-01-21 19:45:57
  private Pattern DATETIME = Pattern.compile("^date: (.*?)");

  // categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]
  private Pattern CATEGORIES = Pattern.compile("categories: \\[(.*?)\\]");

  record Post(Layout layout, String title, String dateTime, List<String> categories, List<String> content) {

  }

  Post convertToXX(List<String> lines) {
    // check minimum size (number of lines) 7
    if (!lines.get(0).equals("---")) {
      throw new IllegalStateException("Beginning of the post is not correct missing ---");
    }
    if (!lines.get(1).equals("layout: post")) {
      throw new IllegalStateException("layout: post Not found.");
    }
    var layout = Layout.post;
    var titleLine = TITLE.matcher(lines.get(2));
    if (!titleLine.matches()) {
      throw new IllegalStateException("Title not found");
    }
    var title = titleLine.group(0);

    var dateTimeLine = DATETIME.matcher(lines.get(3));
    if (!dateTimeLine.matches()) {
      throw new IllegalStateException("Date not found");
    }

    var dateTime = dateTimeLine.group(0);

    if (!lines.get(4).equals("comments: true")) {
      throw new IllegalStateException("comments not found.");
    }

    var categoriesLine = CATEGORIES.matcher(lines.get(5));
    if (!categoriesLine.matches()) {
      throw new IllegalStateException("Categories not found");
    }

    var categoriesStr = categoriesLine.group(0);
    var categoriesArr = categoriesStr.split(",");
    var categories = Arrays.stream(categoriesArr).map(String::trim).toList();

    if (!lines.get(6).equals("---")) {
      throw new IllegalStateException("End marker not found");
    }

    var postLines = new ArrayList<String>();
    for (int i = 7; i < lines.size(); i++) {
      postLines.add(lines.get(i));
    }
    
    /*
s = date: 2019-01-21 19:45:57
s = comments: true
s = categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]

     */
    return new Post(layout, title, dateTime, categories, postLines);
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
