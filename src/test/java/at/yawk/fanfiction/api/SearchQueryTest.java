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