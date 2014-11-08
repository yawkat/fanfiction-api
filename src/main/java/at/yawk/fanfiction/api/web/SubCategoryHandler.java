package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.Category;
import at.yawk.fanfiction.api.SubCategory;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
class SubCategoryHandler extends DefaultHandler {
    private static final int BEFORE = 0;
    private static final int IN_TABLE = 1;
    private static final int IN_LINK = 2;
    private static final int IN_STORY_COUNT = 3;
    private static final int AFTER = 4;

    private final Category category;
    private final boolean crossover;

    @Getter private final List<SubCategory> subCategories = new ArrayList<SubCategory>();

    private int stage = BEFORE;

    private String name;
    private int id;
    /**
     * Story count of the current category. Since characters may be fragmented, this value must be 0 before anything
     * was
     * read and will be modified while new characters come in.
     */
    private int storyCount;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (stage) {
        case BEFORE:
            if (qName.equals("div") && "list_output".equals(attributes.getValue("id"))) {
                stage = IN_TABLE;
            }
            break;
        case IN_TABLE:
            if (qName.equals("a")) {
                name = attributes.getValue("title");
                String href = attributes.getValue("href");
                if (crossover) {
                    id = Integer.parseInt(href.substring(href.lastIndexOf('/', href.length() - 2) + 1,
                                                         href.length() - 1));
                } else {
                    id = -1;
                }
                stage = IN_LINK;
            }
            break;
        case IN_LINK:
            if (qName.equals("span")) {
                stage = IN_STORY_COUNT;
            }
            break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (stage == IN_STORY_COUNT) {
            storyCount = parseInt(storyCount, ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (stage) {
        case IN_STORY_COUNT:
            assert qName.equals("span") : qName;
            stage = IN_TABLE;
            SubCategory subCategory = SubCategory.builder()
                    .category(category).crossover(crossover).name(name).id(id).estimatedStoryCount(storyCount).build();
            subCategories.add(subCategory);
            storyCount = 0; // clear story count (see comments)
            break;
        case IN_TABLE:
            if (qName.equals("tr")) {
                stage = AFTER;
            }
            break;
        }
    }

    static int parseInt(int previous, char[] chars, int off, int len) {
        for (int i = 0; i < len; i++) {
            char c = chars[off + i];
            int v = c - (int) '0';
            if (v >= 0 && v < 10) {
                previous *= 10;
                previous += v;
            } else if (c == 'K') {
                previous *= 1000;
            }
        }
        return previous;
    }
}
