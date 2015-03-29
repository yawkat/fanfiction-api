/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import lombok.Cleanup;
import lombok.Getter;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author yawkat
 */
public class WebContext implements Context {
    @Getter private static final Context instance = new WebContext();

    private WebContext() {}

    public static void main(String[] args) throws Exception {
        if (args[0].equals("chapter")) {
            Chapter chapter = getInstance().getChapter(Story.builder().id(Integer.parseInt(args[1])).build(),
                                                       Integer.parseInt(args[2]));
            System.out.print(chapter.getText());
        }
    }

    @Override
    public List<SubCategory> getSubCategories(Category category, boolean crossover) throws Exception {
        String url = (crossover ? "https://www.fanfiction.net/crossovers/" : "https://www.fanfiction.net/")
                     + category.getId() + "/"; // trailing slash required for crossovers for some reason

        return request(url, new SubCategoryHandler(category, crossover)).getSubCategories();
    }

    @Override
    public SubCategory getSubCategoryInfo(SubCategory subCategory) throws Exception {
        String url =
                (subCategory.isCrossover() ? "https://www.fanfiction.net/crossovers/" : "https://www.fanfiction.net/")
                + subCategory.getCategory().getId() + "/" +
                URLEncoder.encode(subCategory.getName().replace(' ', '-'), "UTF-8");

        return request(url, new SubCategoryInfoHandler(subCategory)).builder.build();
    }

    @Override
    public SearchResult search(String url) throws Exception {
        return SearchResult.builder().stories(request(url, new SearchHandler()).stories).build();
    }

    @Override
    public Chapter getChapter(Story story, int chapter) throws Exception {
        String url = "https://www.fanfiction.net/s/" + story.getId() + "/" + (chapter + 1);

        return request(url, new ChapterHandler()).builder.build();
    }

    static <H extends ContentHandler> H request(String url, H handler) throws Exception {
        @Cleanup InputStream stream = new URL(url).openStream();

        Parser parser = new Parser();
        parser.setContentHandler(handler);
        parser.parse(new InputSource(stream));

        return handler;
    }
}
