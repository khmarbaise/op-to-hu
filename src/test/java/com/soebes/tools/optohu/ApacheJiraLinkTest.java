package com.soebes.tools.optohu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApacheJiraLinkTest {

  @Test
  void simple() {
    String x = " * {%ajl MSHARED-818 %} - Issue management URL in maven-artifact-transfer returns 404";

    var interpolated = ApacheJiraLink.interpolate.apply(x);

    assertThat(interpolated).isEqualTo(
        " * [MSHARED-818](https://issues.apache.org/jira/browse/MSHARED-818) - Issue management URL in maven-artifact-transfer returns 404");

  }

  @Test
  void second() {
    String x = " * {% ajl MSHARED-818 %} - Issue management URL in maven-artifact-transfer returns 404";

    var interpolated = ApacheJiraLink.interpolate.apply(x);

    assertThat(interpolated).isEqualTo(
        " * [MSHARED-818](https://issues.apache.org/jira/browse/MSHARED-818) - Issue management URL in maven-artifact-transfer returns 404");
  }
}