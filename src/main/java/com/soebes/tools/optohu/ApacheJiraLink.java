package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Migrate the links given in posts like the following:
 * <pre>
 *   {%ajl MNG-1231 %} - xxxx
 * </pre>
 * The {@code ajl} stands for Apache Jira Link which should be replaced with
 * {@code https://issues.apache.org/jira/browse/}.
 */
interface ApacheJiraLink {

  Pattern PLACEHOLDER_PATTERN = Pattern.compile(Pattern.quote("{%") + "\\s*" + "ajl " + "(.*?)" + Pattern.quote(" %}"));

  String JIRA_ISSUE_LINK_BASE = "https://issues.apache.org/jira/browse/";

  UnaryOperator<String> interpolate = s -> {
    var matcher = PLACEHOLDER_PATTERN.matcher(s);
    var sb = new StringBuilder();
    while (matcher.find()) {
      var escaped = Matcher.quoteReplacement(
          "[" + matcher.group(1) + "](" + JIRA_ISSUE_LINK_BASE + matcher.group(1) + ")");
      matcher.appendReplacement(sb, escaped);
    }
    matcher.appendTail(sb);
    return sb.toString();
  };

}
