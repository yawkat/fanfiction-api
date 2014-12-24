package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.Author;
import at.yawk.fanfiction.api.Genre;
import at.yawk.fanfiction.api.Rating;
import at.yawk.fanfiction.api.Story;
import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author yawkat
 */
class SearchHandler extends DefaultHandler {
    private int stage = 0;

    List<Story> stories = new ArrayList<Story>();

    private Story.StoryBuilder builder = null;
    private Author.AuthorBuilder authorBuilder = null;

    private boolean readText = false;
    private StringBuilder text = new StringBuilder();

    private void startReadText() {
        readText = true;
    }

    private String endReadText() {
        String s = text.toString();
        readText = false;
        text.setLength(0);
        return s;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (stage) {
        case 0:
            if (qName.equals("div") && "z-list zhover zpointer".equals(attributes.getValue("class"))) {
                builder = Story.builder();
                stage = 1;
            }
            break;
        case 1:
            if (qName.equals("a")) {
                String href = attributes.getValue("href");
                System.out.println(href);
                builder.id(Integer.parseInt(href.substring(3, href.indexOf('/', 4))));
                startReadText();
                stage = 2;
            }
            break;
        case 3:
            if (qName.equals("a")) {
                String href = attributes.getValue("href");
                if (href.startsWith("/u/")) {
                    stage = 4;
                    authorBuilder = Author.builder();
                    authorBuilder.id(Integer.parseInt(href.substring(3, href.indexOf('/', 3))));
                    startReadText();
                }
            }
            break;
        case 4:
            if (qName.equals("div")) {
                stage = 5;
                startReadText();
            }
            break;
        case 5:
            if (qName.equals("div")) {
                builder.description(endReadText());
                startReadText();
                stage = 6;
            }
            break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (stage) {
        case 2:
            if (qName.equals("a")) {
                builder.title(endReadText());
                stage = 3;
            }
            break;
        case 4:
            builder.author(authorBuilder.name(endReadText()).build());
            break;
        case 6:
            String tagString = endReadText();
            int untitledTagIndex = 0;
            for (String tag : Splitter.on(" - ").split(tagString)) {
                if (tag.startsWith("Rated: ")) {
                    builder.rating(Rating.forName(tag.substring(7)));
                } else if (tag.startsWith("Chapters: ")) {
                    builder.chapterCount(Integer.parseInt(tag.substring(10)));
                } else if (tag.startsWith("Words: ")) {
                    builder.wordCount(Integer.parseInt(tag.substring(7).replace(",", "")));
                } else if (tag.startsWith("Favs: ")) {
                    builder.favoriteCount(Integer.parseInt(tag.substring(6).replace(",", "")));
                } else if (tag.startsWith("Follows: ")) {
                    builder.followCount(Integer.parseInt(tag.substring(9).replace(",", "")));
                } else if (tag.startsWith("Reviews: ")) {
                    builder.reviewCount(Integer.parseInt(tag.substring(9).replace(",", "")));
                } else {
                    if (untitledTagIndex == 0) {
                        builder.language(tag);
                    } else if (untitledTagIndex == 1) {
                        List<Genre> genres = new ArrayList<Genre>();
                        for (String genreName : Splitter.on('/').split(tag)) {
                            genres.add(Genre.builder().name(genreName).build());
                        }
                        builder.genres(genres);
                    } else {
                        System.out.println("Unparsed tag " + tag);
                    }
                    untitledTagIndex++;
                }
            }
            stories.add(builder.build());
            stage = 0;
            break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (readText) {
            text.append(ch, start, length);
        }
    }
}
