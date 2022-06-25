package com.soebes.tools.optohu;

import static java.lang.System.out;

import java.nio.file.Paths;
import java.util.Comparator;

/**
 * Migration from Octopress to Hugo.
 */
interface OpToHu {

  static void main(String[] args) {
    out.println("Op-To-Hu (Octopress to Hugo) Converter");

    var rootDirectory = Paths.get(args[0]);
    var pathStream = DirectoryTool.readRecursively(rootDirectory);
    var markdownFiles = pathStream.stream().filter(s -> s.toString().endsWith(".md")).toList();

    var blogPosts = markdownFiles.stream()
        .map(File.intoContent)
        .map(OctopressMarkdown.intoPost)
        .map(MigrateApacheJiraLinks.resolve)
        .map(MigrateHighlighter.migrate)
        .toList();

    var sorted = blogPosts.stream().sorted(Comparator.comparing(Post::file)).toList();

    sorted.forEach(s -> out.println("s = " + s.file()));

    out.println("-----------------------------------------------------------------------");
    var relativizePosts = sorted.stream().map(s -> Post.from(s, rootDirectory.relativize(s.file()))).toList();

    relativizePosts.forEach(s -> out.println("s = " + s.file()));
    out.println("Number of blog entries: = " + blogPosts.size());

  }
}
