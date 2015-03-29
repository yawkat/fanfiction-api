/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.Chapter;
import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author yawkat
 */
class ChapterHandler extends DefaultHandler {
    private static final int BEFORE = 0;
    private static final int TEXT = 1;
    private static final int AFTER = 2;

    Chapter.ChapterBuilder builder;

    private StringBuilder textBuilder;

    private int stage = BEFORE;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (stage) {
        case BEFORE:
            if (qName.equals("div") && "storytext".equals(attributes.getValue("id"))) {
                stage = TEXT;
                textBuilder = new StringBuilder();
            }
            break;
        case TEXT:
            textBuilder.append('<').append(qName);
            for (int i = 0; i < attributes.getLength(); i++) {
                textBuilder.append(" ");
                textBuilder.append(attributes.getQName(i));
                textBuilder.append("=\"");
                textBuilder.append(attributes.getValue(i).replace("\\", "\\\\").replace("\"", "\\\""));
                textBuilder.append("\"");
            }
            textBuilder.append('>');
            break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (stage == TEXT) {
            textBuilder.append(StringEscapeUtils.escapeXml(new String(ch, start, length)));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (stage == TEXT) {
            if (qName.equals("div")) {
                stage = AFTER;
                builder = Chapter.builder();
                builder.text(textBuilder.toString());
            } else {
                textBuilder.append("</").append(qName).append('>');
            }
        }
    }
}
