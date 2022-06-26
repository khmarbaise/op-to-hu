package com.soebes.tools.optohu;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

record Post(Path file, Layout layout, PostType postType, String title, LocalDateTime publishingTime,
            List<String> categories, Content content) {

  static Post from(Post post, Content content) {
    return new Post(post.file, post.layout, post.postType, post.title, post.publishingTime, post.categories, content);
  }

  static Post from(Post post, Path newFile) {
    return new Post(newFile, post.layout, post.postType, post.title, post.publishingTime, post.categories,
        post.content);
  }

  enum Layout {
    POST
  }

  public enum PostType {
    NONE,
    BLOG
  }

}
