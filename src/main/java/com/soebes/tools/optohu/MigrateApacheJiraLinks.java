package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

interface MigrateApacheJiraLinks {

  UnaryOperator<Post> resolve = post -> {
    var content = post.content().lines().stream().map(ApacheJiraLink.interpolate).toList();
    return new Post(post.file(), post.layout(), post.postType(), post.title(), post.publishingTime(), post.categories(),
        new Content(content));
  };

}

