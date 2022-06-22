package com.soebes.tools.optohu;

import static com.soebes.tools.optohu.OctopressMarkdown.intoPost;
import static java.lang.System.out;

import java.nio.file.Paths;

/**
 * Migration from Octopress to Hugo.
 */
class OpToHu {

  public static void main(String[] args) {
    out.println("Op-To-Hu (Octopress to Hugo) Converter");

    var pathStream = DirectoryTool.readRecursively(Paths.get(args[0]));
    var markdownFiles = pathStream.stream().filter(s -> s.toString().endsWith(".md")).toList();

    var blogPosts = markdownFiles.stream().map(File::intoLines).map(intoPost).toList();

    blogPosts.forEach(s -> out.println("s = " + s));

    out.println("Number of blog entries: = " + blogPosts.size());
  }
}
