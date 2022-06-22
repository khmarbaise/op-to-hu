package com.soebes.tools.optohu;

import static com.soebes.tools.optohu.Markdown.intoPost;
import static java.lang.System.out;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Migration from Octopress to Hugo.
 */
class OpToHu {

  public static void main(String[] args) {
    out.println("Op-To-Hu (Octopress to Hugo) Converter");

    var pathStream = DirectoryTool.readFilesRecursively(Paths.get(args[0]));
    var listOfFiles = pathStream.stream()
        .filter(Files::isRegularFile)
        .filter(s -> s.toString().endsWith(".md"))
        .toList();

    var blogPosts = listOfFiles.stream().map(File::intoLines).map(intoPost).toList();
    blogPosts.forEach(s -> out.println("s = " + s));

    out.println("Number of blog entries: = " + blogPosts.size());
  }
}
