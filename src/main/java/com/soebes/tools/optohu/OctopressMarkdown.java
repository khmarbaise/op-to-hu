package com.soebes.tools.optohu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

interface OctopressMarkdown {

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

    var layout = Post.Layout.post;
    var titleLine = TITLE.matcher(fileAndContent.content().get(2));
    if (!titleLine.matches()) {
      throw new IllegalStateException(fileAndContent.file() + " Title not found");
    }
    var title = titleLine.group(1);

    var dateTimeLine = DATETIME.matcher(fileAndContent.content().get(3));
    if (!dateTimeLine.matches()) {
      throw new IllegalStateException(fileAndContent.file() + " Date not found");
    }

    //FIXME: Convert to Instant / LocalDateTime ...
    var dateTime = dateTimeLine.group(1);

    if (fileAndContent.content().contains("comments: true")) {
      // Optional??
    }

    //    post-type: blog
    var postType = PostType.NONE;
    if (fileAndContent.content().contains("post-type: blog")) {
      postType = PostType.BLOG;
    }

    var categoryLine = fileAndContent.content()
        .stream()
        .filter(s -> s.startsWith("categories:"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(fileAndContent.file() + " Categories not found"));

    List<String> categories = List.of();
    if (categoryLine.contains("[")) {
      var categoriesMatcher = CATEGORIES.matcher(categoryLine);
      if (!categoriesMatcher.matches()) {
        throw new IllegalStateException(fileAndContent.file() + " Categories matcher does not match.");
      }
      var categoriesStr = categoriesMatcher.group(1);
      var categoriesArr = categoriesStr.split(",");
      categories = Arrays.stream(categoriesArr).map(String::trim).toList();
    }

    if (!fileAndContent.content().contains("---")) {
      throw new IllegalStateException(fileAndContent.file() + " End marker not found");
    }

    var indexOfEndMarker = fileAndContent.content().lastIndexOf("---");

    var postLines = new ArrayList<String>();
    for (int i = indexOfEndMarker + 1; i < fileAndContent.content().size(); i++) {
      postLines.add(fileAndContent.content().get(i));
    }

    return new Post(fileAndContent.file(), layout, postType, title, dateTime, categories,
        Collections.unmodifiableList(postLines));
  };

}
