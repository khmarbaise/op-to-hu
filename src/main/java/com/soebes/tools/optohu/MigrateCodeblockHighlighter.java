package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface MigrateCodeblockHighlighter {

  Pattern START_HIGHLIGTHER_PATTERN = Pattern.compile(
      "^" + Pattern.quote("{%") + "\\s+" + "codeblock" + "\\s+" + "(.*)" + "\\s+" + "(.*?:.*?)" + "\\s+"
          + Pattern.quote("%}"));

  // {% codeblock Downloading Artifacts lang:xml %}
  UnaryOperator<String> filter = s -> {
    var matcher = START_HIGHLIGTHER_PATTERN.matcher(s);
    var sb = new StringBuilder();
    while (matcher.find()) {
      var replace = switch (matcher.group(2)) {
        case "lang:xml" -> "```xml";
        default -> "```"; //??
      };

      var escaped = Matcher.quoteReplacement(replace);
      matcher.appendReplacement(sb, escaped);
    }
    matcher.appendTail(sb);
    return sb.toString();
  };

  Pattern START_HIGHLIGTHER_WITHOUT_TYPE_PATTERN = Pattern.compile(
      "^" + Pattern.quote("{%") + "\\s+" + "codeblock" + "\\s+" + "(.*)" + "\\s+" + Pattern.quote("%}"));

  // {% codeblock Downloading Artifacts %}
  UnaryOperator<String> filterWithoutType = s -> {
    var matcher = START_HIGHLIGTHER_WITHOUT_TYPE_PATTERN.matcher(s);
    var sb = new StringBuilder();
    while (matcher.find()) {
      var escaped = Matcher.quoteReplacement("```");
      matcher.appendReplacement(sb, escaped);
    }
    matcher.appendTail(sb);
    return sb.toString();
  };

  UnaryOperator<String> endCodeBlock = s -> {
    if (s.equals("{% codeblock %}")) {
      return "```";
    } else {
      return s;
    }
  };

  UnaryOperator<Post> highlighter = post -> {
    var resultLines = post.content().lines().stream().map(filter).map(filterWithoutType).map(endCodeBlock).toList();
    return Post.from(post, new Content(resultLines));
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
