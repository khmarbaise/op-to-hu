package com.soebes.tools.optohu;

import com.soebes.tools.optohu.OctopressMarkdown.PostType;
import java.nio.file.Path;
import java.util.List;

record Post(Path file, Layout layout, PostType postType, String title, String publishingTime, List<String> categories,
            List<String> content) {

  static Post from(Post post) {
    return new Post(post.file(), post.layout(), post.postType(), post.title(), post.publishingTime(), post.categories(),
        post.content());
  }

  static Post from(Post post, List<String> content) {
    return new Post(post.file(), post.layout(), post.postType(), post.title(), post.publishingTime(), post.categories(),
        content);
  }
}
