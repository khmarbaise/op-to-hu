package com.soebes.tools.optohu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class MarkdownToPost {

  private static final Pattern TITLE = Pattern.compile("^title: \"(.*?)\"");

  //date: 2019-01-21 19:45:57
  private static final Pattern DATETIME = Pattern.compile("^date: (.*?)");

  // categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]
  private static final Pattern CATEGORIES = Pattern.compile("^categories: \\[(.*?)\\]");

  public static Post convertIntoPost(List<String> lines) {
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
    var title = titleLine.group(1);

    var dateTimeLine = DATETIME.matcher(lines.get(3));
    if (!dateTimeLine.matches()) {
      throw new IllegalStateException("Date not found");
    }

    var dateTime = dateTimeLine.group(1);

    if (!lines.get(4).equals("comments: true")) {
      throw new IllegalStateException("comments not found.");
    }

    var categoriesLine = CATEGORIES.matcher(lines.get(5));
    if (!categoriesLine.matches()) {
      throw new IllegalStateException("Categories not found");
    }

    var categoriesStr = categoriesLine.group(1);
    var categoriesArr = categoriesStr.split(",");
    var categories = Arrays.stream(categoriesArr).map(String::trim).toList();

    if (!lines.get(6).equals("---")) {
      throw new IllegalStateException("End marker not found");
    }

    var postLines = new ArrayList<String>();
    for (int i = 7; i < lines.size(); i++) {
      postLines.add(lines.get(i));
    }

    return new Post(layout, title, dateTime, categories, postLines);
  }
}
