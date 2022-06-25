package com.soebes.tools.optohu;

import java.nio.file.Path;
import java.util.List;

record Post(Path file, Layout layout, PostType postType, String title, String publishingTime, List<String> categories,
            List<String> content) {

  static Post from(Post post, List<String> contentLines) {
    return new Post(post.file, post.layout, post.postType, post.title, post.publishingTime, post.categories,
        contentLines);
  }

  enum Layout {
    POST
  }

  public enum PostType {
    NONE,
    BLOG
  }

  record Content(List<String> lines) {

  }
}
