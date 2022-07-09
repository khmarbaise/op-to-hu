package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Migrate the links given in posts like the following:
 * <pre>
 *   the {% mp maven-assembly-plugin %}.
 * </pre>
 * The {@code mp} stands for Apache Maven Plugin:
 * {@code [maven-assembly-plugin](https://maven.apache.org/plugins/maven-assembly-plugin/)}.
 */
interface MPLink {

  Pattern PLACEHOLDER_PATTERN = Pattern.compile(
      Pattern.quote("{%") + "\\s*" + "(mp)" + "\\s*" + "(.*?)" + Pattern.quote(" %}"));

  String MAVEN_PLUGIN_BASE_LINK = "https://maven.apache.org/plugins/";

  UnaryOperator<String> interpolate = line -> {
    var matcher = PLACEHOLDER_PATTERN.matcher(line);
    var sb = new StringBuilder();
    while (matcher.find()) {
      var escaped = Matcher.quoteReplacement(
          "[" + matcher.group(2) + "](" + MAVEN_PLUGIN_BASE_LINK + matcher.group(2) + ")");
      matcher.appendReplacement(sb, escaped);
    }
    matcher.appendTail(sb);
    return sb.toString();
  };

}
