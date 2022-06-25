package com.soebes.tools.optohu;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

interface OctopressMarkdown {

  Pattern TITLE = Pattern.compile("^title: \"(.*?)\"");

  //date: 2019-01-21 19:45:57
  Pattern DATETIME = Pattern.compile("^date: (.*?)");

  // categories: [Neuigkeiten,BM,Maven,Maven-Plugins,Maven-Plugin-Releases]
  Pattern CATEGORIES = Pattern.compile("^categories: \\[(.*?)\\]");

  Function<FileWithContent, Post> intoPost = fileWithContent -> {
    var lines = fileWithContent.content().lines();
    // check minimum size (number of fileAndLines) 7
    if (!lines.get(0).equals("---")) {
      throw new IllegalStateException(fileWithContent.file() + " Beginning of the post is not correct missing ---");
    }

    if (!lines.get(1).equals("layout: post")) {
      throw new IllegalStateException(fileWithContent.file() + " layout: post Not found.");
    }

    var layout = Post.Layout.POST;
    var titleLine = TITLE.matcher(lines.get(2));
    if (!titleLine.matches()) {
      throw new IllegalStateException(fileWithContent.file() + " Title not found");
    }
    var title = titleLine.group(1);

    var dateTimeLine = DATETIME.matcher(lines.get(3));
    if (!dateTimeLine.matches()) {
      throw new IllegalStateException(fileWithContent.file() + " Date not found");
    }

    //FIXME: Convert to Instant / LocalDateTime ...
    var dateTime = dateTimeLine.group(1);

    if (lines.contains("comments: true")) {
      // Optional??
    }

    //    post-type: blog
    var postType = Post.PostType.NONE;
    if (lines.contains("post-type: blog")) {
      postType = Post.PostType.BLOG;
    }

    var categoryLine = lines.stream()
        .filter(s -> s.startsWith("categories:"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(fileWithContent.file() + " Categories not found"));

    List<String> categories = List.of();
    if (categoryLine.contains("[")) {
      var categoriesMatcher = CATEGORIES.matcher(categoryLine);
      if (!categoriesMatcher.matches()) {
        throw new IllegalStateException(fileWithContent.file() + " Categories matcher does not match.");
      }
      var categoriesStr = categoriesMatcher.group(1);
      var categoriesArr = categoriesStr.split(",");
      categories = Arrays.stream(categoriesArr).map(String::trim).toList();
    }

    if (!lines.contains("---")) {
      throw new IllegalStateException(fileWithContent.file() + " End marker not found");
    }

    var indexOfEndMarker = lines.lastIndexOf("---");

    var postLines = lines.subList(indexOfEndMarker + 1, lines.size()).stream().toList();

    return new Post(fileWithContent.file(), layout, postType, title, dateTime, categories, new Content(postLines));
  };

}
