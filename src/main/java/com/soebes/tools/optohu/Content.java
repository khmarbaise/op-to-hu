package com.soebes.tools.optohu;

import java.util.List;

record Content(List<String> lines) {

  Content(List<String> lines) {
    this.lines = List.copyOf(lines);
  }

}
