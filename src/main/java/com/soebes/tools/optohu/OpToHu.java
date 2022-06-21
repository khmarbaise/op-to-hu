package com.soebes.tools.optohu;

import static com.soebes.tools.optohu.Markdown.intoPost;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Migration from Octopress to Hugo.
 */
class OpToHu {

  public static void main(String[] args) {
    System.out.println("Op-To-Hu (Octopress to Hugo) Converter");

    var pathStream = DirectoryTool.readFilesRecursively(Paths.get(args[0]));
    var listOfFiles = pathStream.stream().filter(Files::isRegularFile).toList();

    var collect = listOfFiles.stream().map(FileTool::intoLines).map(intoPost).toList();
    collect.forEach(s -> System.out.println("s = " + s));
  }
}
