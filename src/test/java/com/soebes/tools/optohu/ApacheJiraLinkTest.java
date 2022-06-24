package com.soebes.tools.optohu;

import org.junit.jupiter.api.Test;

class ApacheJiraLinkTest {

  @Test
  void simple() {
    String x = " * {%ajl MSHARED-818 %} - Issue management URL in maven-artifact-transfer returns 404";

    var interpolated = ApacheJiraLink.interpolate.apply(x);

    System.out.println("interpolated = " + interpolated);
  }

  @Test
  void second() {
    String x = " * {% ajl MSHARED-818 %} - Issue management URL in maven-artifact-transfer returns 404";

    var interpolated = ApacheJiraLink.interpolate.apply(x);

    System.out.println("interpolated = " + interpolated);
  }
}