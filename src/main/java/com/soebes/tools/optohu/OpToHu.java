package com.soebes.tools.optohu;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Migration from Octopress to Hugo.
 */
class OpToHu {

  public static void main(String[] args) {

    System.out.println("Op-To-Hu (Octopress to Hugo) Converter");
    try {

      var fileCollection = Files.list(Paths.get(args[0]))
          .filter(Files::isRegularFile)
          .collect(toList());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
