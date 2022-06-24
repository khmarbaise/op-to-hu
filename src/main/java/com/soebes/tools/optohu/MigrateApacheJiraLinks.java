package com.soebes.tools.optohu;

import java.util.function.Function;

interface MigrateApacheJiraLinks {

  Function<Post, Post> resolve = post -> {
    var content = post.content().stream().map(ApacheJiraLink.interpolate).toList();
    return new Post(post.file(), post.layout(), post.postType(), post.title(), post.publishingTime(), post.categories(),
        content);
  };

}

