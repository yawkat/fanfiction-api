/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api.web;

import at.yawk.fanfiction.api.*;
import at.yawk.fanfiction.api.Character;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author yawkat
 */
class SubCategoryInfoHandler extends DefaultHandler {
    private static final int BEFORE = 0;
    private static final int INSIDE = 1;
    private static final int INSIDE_SELECT = 2;
    private static final int INSIDE_OPTION = 3;
    private static final int AFTER = 4;

    private static final String GENRE = "genreid1".intern();
    private static final String LANGUAGE = "languageid".intern();
    private static final String WORLD = "verseid1".intern();
    private static final String CHARACTER = "characterid1".intern();

    final SubCategory.SubCategoryBuilder builder = SubCategory.builder();

    private int stage = BEFORE;

    private String select;
    private String optionId;
    private StringBuilder nameBuilder = new StringBuilder();

    private Map<String, String> selectEntries = new HashMap<String, String>();

    public SubCategoryInfoHandler(SubCategory category) {
        builder.category(category.getCategory())
                .crossover(category.isCrossover())
                .name(category.getName())
                .id(category.getId())
                .estimatedStoryCount(category.getEstimatedStoryCount());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (stage) {
        case BEFORE:
            if (qName.equals("div") && "filters".equals(attributes.getValue("id"))) {
                stage = INSIDE;
            }
            break;
        case INSIDE:
            if (qName.equals("select")) {
                stage = INSIDE_SELECT;
                select = attributes.getValue("name");
            }
            break;
        case INSIDE_SELECT:
            if (qName.equals("option")) {
                stage = INSIDE_OPTION;
                optionId = attributes.getValue("value");
            }
            break;
        }
    }

    @SuppressWarnings("StringEquality")
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (stage) {
        case INSIDE_OPTION:
            stage = INSIDE_SELECT;
            int start = selectEntries.isEmpty() ? nameBuilder.indexOf(":") + 2 : 0;
            selectEntries.put(optionId, nameBuilder.substring(start));
            nameBuilder.setLength(0);
            break;
        case INSIDE_SELECT:
            if (qName.equals("select")) {
                select = select.intern();
                // compare interned (todo: is this actually worth it?)
                if (select == GENRE) {
                    List<Genre> genres = new ArrayList<Genre>(selectEntries.size());
                    for (Map.Entry<String, String> genre : selectEntries.entrySet()) {
                        genres.add(Genre.builder().id(Integer.parseInt(genre.getKey())).name(genre.getValue()).build());
                    }
                    builder.genres(genres);
                } else if (select == LANGUAGE) {
                    List<Language> languages = new ArrayList<Language>(selectEntries.size());
                    for (Map.Entry<String, String> language : selectEntries.entrySet()) {
                        languages.add(
                                Language.builder()
                                        .id(Integer.parseInt(language.getKey()))
                                        .name(language.getValue())
                                        .build()
                        );
                    }
                    builder.languages(languages);
                } else if (select == WORLD) {
                    List<World> worlds = new ArrayList<World>(selectEntries.size());
                    for (Map.Entry<String, String> language : selectEntries.entrySet()) {
                        worlds.add(
                                World.builder()
                                        .id(Integer.parseInt(language.getKey()))
                                        .name(language.getValue())
                                        .build()
                        );
                    }
                    builder.worlds(worlds);
                } else if (select == CHARACTER) {
                    List<Character> characters = new ArrayList<Character>(selectEntries.size());
                    for (Map.Entry<String, String> language : selectEntries.entrySet()) {
                        characters.add(
                                Character.builder()
                                        .id(Integer.parseInt(language.getKey()))
                                        .name(language.getValue())
                                        .build()
                        );
                    }
                    builder.characters(characters);
                }
                selectEntries.clear();
                stage = INSIDE;
            }
            break;
        case INSIDE:
            if (qName.equals("form")) {
                stage = AFTER;
            }
            break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (stage == INSIDE_OPTION) {
            nameBuilder.append(ch, start, length);
        }
    }
}
