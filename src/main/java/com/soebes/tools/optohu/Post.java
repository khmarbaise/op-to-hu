package com.soebes.tools.optohu;

import com.soebes.tools.optohu.Markdown.PostType;
import java.nio.file.Path;
import java.util.List;

record Post(Path file, Layout layout, PostType postType, String title, String publishingTime, List<String> categories,
            List<String> content) {

}
