package com.soebes.tools.optohu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;
import static java.nio.file.attribute.PosixFilePermission.GROUP_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

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

  static void writeSingleLine(Path file, String line) {
    try {
      Files.writeString(file, line + "\n", StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void writeLines(Path file, List<String> lines) {
    lines.forEach(line -> writeSingleLine(file, line));
  }

  static void writingPost(Path directory, Post post) throws IOException {
    var file = directory.resolve(post.file().getFileName());

    Set<PosixFilePermission> perms =
        EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, GROUP_READ);
    Files.createFile(file, PosixFilePermissions.asFileAttribute(perms));

    out.println("file = " + file);

    var lines = new ArrayList<String>();
    lines.add("---");
    lines.add("title: " + String.join("\"", post.title()));
    // 2019-03-26T08:47:11+01:00
    lines.add("date: " + DateTimeFormatter.ISO_DATE_TIME.format(post.publishingTime()));

    lines.add("lastmod: " + post.publishingTime());
    lines.add("categories:");
    post.categories().categoryTags().forEach(s -> lines.add("  - " + s));
    lines.add("---");
    lines.addAll(post.content().lines());

    writeLines(file, lines);
  }
}
