package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;

interface MigrateHighlighter {

  UnaryOperator<Post> migrate = post -> {
    var contentLines = post.content().lines().stream().map(line -> switch (line) {
      case "{% highlight bash linenos %}", "{% highlight bash %}" -> "```bash";
      case "{% highlight perl linenos %}" -> "```perl";
      case "{% highlight xml linenos %}" -> "```xml";
      case "{% endhighlight %}" -> "```";
      default -> line;
    }).toList();

    return Post.from(post, new Content(contentLines));
  };

  /*
  {% codeblock Downloading Artifacts lang:xml %}

{% codeblock Iterator Maven Plugin Configuration lang:xml %}
<plugin>
  <groupId>com.soebes.maven.plugins</groupId>
  <artifactId>iterator-maven-plugin</artifactId>
  <version>0.2</version>
  <executions>
    <execution>
      <phase>package</phase>
      <goals>
        <goal>executor</goal>
      </goals>
      <configuration>
        <items>
          <item>test</item>
          <item>prod</item>
          <item>dev</item>
        </items>

        <pluginExecutors>
          <pluginExecutor>
            <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-assembly-plugin</artifactId>
              <version>2.4</version>
            </plugin>
            <goal>single</goal>
            <configuration>
              <descriptors>
                <descriptor>${project.basedir}/@item@.xml</descriptor>
              </descriptors>
            </configuration>
          </pluginExecutor>
        </pluginExecutors>
      </configuration>
    </execution>
  </executions>
</plugin>
{% endcodeblock %}

   */
}
