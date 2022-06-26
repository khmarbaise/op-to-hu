package com.soebes.tools.optohu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  DateTimeFormatter DATE_TIME_FORMATTER_WITHOUT_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    // date: 2019-01-02 23:36:42
    // date: 2019-01-06 23:37:00
    var dtStr = dateTimeLine.group(1).trim();
    var dateTime = (dtStr.length() > 16) ? LocalDateTime.parse(dtStr, DATE_TIME_FORMATTER)
        : LocalDateTime.parse(dtStr, DATE_TIME_FORMATTER_WITHOUT_SECONDS);

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
