package com.soebes.tools.optohu;

import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        .map(MigrateLinks.resolve)
        .map(MigrateHighlighter.migrate)
        .map(MigrateXmlHighlighter.migrateContent)
        .map(MigrateJavaHighlighter.migrateContent)
        .map(MigrateCodeblockHighlighter.highlighter)
        .toList();

    var sorted = blogPosts.stream().sorted(Comparator.comparing(Post::file)).toList();

    sorted.forEach(s -> out.println("s = " + s.file()));

    out.println("-----------------------------------------------------------------------");
    var relativizePosts = sorted.stream().map(s -> Post.from(s, rootDirectory.relativize(s.file()))).toList();

    relativizePosts.forEach(s -> out.println("s = " + s.file()));
    out.println("Number of blog entries: = " + blogPosts.size());

    var target = Path.of("target", "posts");

    relativizePosts.stream().forEach(post -> {
      try {
        Path writing = target.resolve(post.file().getParent());
        Files.createDirectories(writing);
        writingPost(writing, post);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

  }

  static void writingPost(Path directory, Post post) throws IOException {
    var file = directory.resolve(post.file().getFileName());
    out.println("file = " + file);
    Files.writeString(file, "---\n", StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    Files.writeString(file, "title: " + post.title() + "\n", StandardOpenOption.APPEND);
    Files.writeString(file, "date: " + post.publishingTime() + "\n", StandardOpenOption.APPEND);
    Files.writeString(file, "---\n", StandardOpenOption.APPEND);
    Files.write(file, post.content().lines(), StandardOpenOption.APPEND);
  }
}
