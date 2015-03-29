/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api;

import org.junit.Test;

public class SearchQueryTest {
    @Test
    public void testCompile() {
        SubCategory category =
                SubCategory.builder().category(Category.GAMES).crossover(false).name("Elder Scroll series").build();
        SearchQuery query = new SearchQuery(category);

        System.out.println(query.compile(4));
    }
}