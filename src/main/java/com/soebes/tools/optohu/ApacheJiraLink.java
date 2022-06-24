package com.soebes.tools.optohu;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface ApacheJiraLink {

  Pattern PLACEHOLDER_PATTERN = Pattern.compile(Pattern.quote("{%") + "\\s*" + "ajl " + "(.*?)" + Pattern.quote(" %}"));

  String JIRA_LINK_BASE = "https://issues.apache.org/jira/browse/";

  UnaryOperator<String> interpolate = s -> {
    var matcher = PLACEHOLDER_PATTERN.matcher(s);
    var sb = new StringBuilder();
    while (matcher.find()) {
      var escaped = Matcher.quoteReplacement(JIRA_LINK_BASE + matcher.group(1));
      matcher.appendReplacement(sb, escaped);
    }
    matcher.appendTail(sb);
    return sb.toString();
  };

}
