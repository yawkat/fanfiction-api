/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api;

import java.io.IOException;
import java.util.List;
import org.xml.sax.SAXException;

/**
 * @author yawkat
 */
public interface Context {
    List<SubCategory> getSubCategories(Category category, boolean crossover) throws Exception;

    SubCategory getSubCategoryInfo(SubCategory subCategory) throws Exception;

    SearchResult search(String url) throws Exception;

    Chapter getChapter(Story story, int chapter) throws Exception;
}
