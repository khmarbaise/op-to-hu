package com.soebes.tools.optohu;

import org.junit.jupiter.api.Test;

class MigrateApacheJiraLinksTest {

  @Test
  void simplyReplacement() {
    String x = " * {%ajl MSHARED-818 %} - Issue management URL in maven-artifact-transfer returns 404";

    var apply = MigrateApacheJiraLinks.resolve.apply(x);

    System.out.println("apply = " + apply);

  }
}