package com.soebes.tools.optohu;

import com.soebes.tools.optohu.Markdown.PostType;
import java.util.List;

record Post(Layout layout, PostType postType, String title, String publishingTime, List<String> categories,
            List<String> content) {

}
