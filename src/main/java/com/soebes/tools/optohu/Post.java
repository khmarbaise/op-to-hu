package com.soebes.tools.optohu;

import java.util.List;

record Post(Layout layout, String title, String publishingTime, List<String> categories, List<String> content) {

}
