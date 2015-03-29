/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.Category;
import at.yawk.fanfiction.api.SearchQuery;
import at.yawk.fanfiction.api.SubCategory;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebContextTest {
    @Test
    public void testGetSubCategories() throws Exception {
        // we only check for exceptions here. maybe think of a better check in the future?
        WebContext.getInstance().getSubCategories(Category.GAMES, false);
        WebContext.getInstance().getSubCategories(Category.GAMES, true);
    }

    @Test
    public void testSearch() throws Exception {
        List<SubCategory> li = WebContext.getInstance().getSubCategories(Category.GAMES, false);
        System.out.println(WebContext.getInstance().search(new SearchQuery(li.get(0)).compile(0)));
    }

    @Test
    public void testGetSubCategoryInfo() throws Exception {
        List<SubCategory> li = WebContext.getInstance().getSubCategories(Category.GAMES, false);
        SubCategory info = WebContext.getInstance().getSubCategoryInfo(li.get(0));
        System.out.println(info);
    }
}