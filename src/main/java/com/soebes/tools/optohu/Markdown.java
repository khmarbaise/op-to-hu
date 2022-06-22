package com.soebes.tools.optohu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

interface Markdown {

  Pattern TITLE = Pattern.compile("^title: \"(.*?)\"");

  //date: 2019-01-21 19:45:57
  Pattern DATETIME = Pattern.compile("^date: (.*?)");

  // categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]
  Pattern CATEGORIES = Pattern.compile("^categories: \\[(.*?)\\]");

  enum PostType {
    NONE,
    BLOG
  }

  Function<FileAndContent, Post> intoPost = fileAndContent -> {
    // check minimum size (number of fileAndLines) 7
    if (!fileAndContent.content().get(0).equals("---")) {
      throw new IllegalStateException(fileAndContent.file() + " Beginning of the post is not correct missing ---");
    }
    if (!fileAndContent.content().get(1).equals("layout: post")) {
      throw new IllegalStateException(fileAndContent.file() + " layout: post Not found.");
    }
    var layout = Layout.post;
    var titleLine = TITLE.matcher(fileAndContent.content().get(2));
    if (!titleLine.matches()) {
      throw new IllegalStateException(fileAndContent.file() + " Title not found");
    }
    var title = titleLine.group(1);

    var dateTimeLine = DATETIME.matcher(fileAndContent.content().get(3));
    if (!dateTimeLine.matches()) {
      throw new IllegalStateException(fileAndContent.file() + " Date not found");
    }

    var dateTime = dateTimeLine.group(1);

    if (fileAndContent.content().contains("comments: true")) {
      // Optional??
    }

    //    post-type: blog
    var postType = PostType.NONE;
    if (fileAndContent.content().contains("post-type: blog")) {
      postType = PostType.BLOG;
    }

    var categoriesLine = CATEGORIES.matcher(fileAndContent.content().get(5));
    if (!categoriesLine.matches()) {
      throw new IllegalStateException(fileAndContent.file() + " Categories not found");
    }

    var categoriesStr = categoriesLine.group(1);
    var categoriesArr = categoriesStr.split(",");
    var categories = Arrays.stream(categoriesArr).map(String::trim).toList();

    if (!fileAndContent.content().contains("---")) {
      throw new IllegalStateException(fileAndContent.file() + " End marker not found");
    }

    var indexOfEndMarker = fileAndContent.content().lastIndexOf("---");

    var postLines = new ArrayList<String>();
    for (int i = indexOfEndMarker + 1; i < fileAndContent.content().size(); i++) {
      postLines.add(fileAndContent.content().get(i));
    }

    return new Post(layout, postType, title, dateTime, categories, postLines);
  };

}
