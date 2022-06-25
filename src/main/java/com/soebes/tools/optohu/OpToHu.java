package com.soebes.tools.optohu;

import static java.lang.System.out;

import java.nio.file.Paths;
import java.util.List;

/**
 * Migration from Octopress to Hugo.
 */
interface OpToHu {

  static void main(String[] args) {
    out.println("Op-To-Hu (Octopress to Hugo) Converter");

    var pathStream = DirectoryTool.readRecursively(Paths.get(args[0]));
    var markdownFiles = pathStream.stream().filter(s -> s.toString().endsWith(".md")).toList();

    List<Post> blogPosts = markdownFiles.stream()
        .map(File::intoLines)
        .map(OctopressMarkdown.intoPost)
        .map(MigrateApacheJiraLinks.resolve)
        .map(MigrateHighlighter.migrate)
        .toList();

    blogPosts.forEach(s -> out.println("s = " + s));

    out.println("Number of blog entries: = " + blogPosts.size());
  }
}
