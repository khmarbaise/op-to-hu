package com.soebes.tools.optohu;

import java.nio.file.Path;
import java.util.List;

record Post(Path file, Layout layout, PostType postType, String title, String publishingTime, List<String> categories,
            Content content) {

  static Post from(Post post, Content content) {
    return new Post(post.file, post.layout, post.postType, post.title, post.publishingTime, post.categories, content);
  }

  enum Layout {
    POST
  }

  public enum PostType {
    NONE,
    BLOG
  }

}
