package com.soebes.tools.optohu;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class MigrateApacheJiraLinksTest {

  Pattern XXX = Pattern.compile(Pattern.quote("{%ajl") + " (.*) " + Pattern.quote("%}"));

  @Test
  void name() {
    String x = " * {%ajl MSHARED-818 %} - Issue management URL in maven-artifact-transfer returns 404";

    XXX.matcher(x).results().forEach(s -> System.out.println("s.group() = " + s.group()));
  }
}