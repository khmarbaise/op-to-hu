package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

public interface Highlighter {

  /*
{% highlight bash linenos %}
svn mkdir URL/dir1 URL/dir2 URL/dir3 -m"- Create directory structure"
{% endhighlight %}
   */

  UnaryOperator<Post> migrate = post -> {
    var content = post.content().stream().map(ApacheJiraLink.interpolate).toList();
    return new Post(post.file(), post.layout(), post.postType(), post.title(), post.publishingTime(), post.categories(),
        content);
  };

}
