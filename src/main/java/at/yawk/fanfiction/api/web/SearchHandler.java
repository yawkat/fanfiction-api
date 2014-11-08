package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.Author;
import at.yawk.fanfiction.api.Story;
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
            System.out.println("Tags: " + tagString);
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
