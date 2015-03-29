/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class SearchQuery {
    private final String category;
    private final String subCategorySanitized;

    private int order = SearchOrder.UPDATE_DATE.getId();
    private final List<Integer> genresIncluded = new ArrayList<Integer>(2);
    private final List<Integer> genresExcluded = new ArrayList<Integer>(1);
    private int minRating = 0;
    private int maxRating = 0;
    private final List<Integer> worldsIncluded = new ArrayList<Integer>(1);
    private final List<Integer> worldsExcluded = new ArrayList<Integer>(1);
    private final List<Integer> charactersIncluded = new ArrayList<Integer>(4);
    private final List<Integer> charactersExcluded = new ArrayList<Integer>(2);
    private int timeRange = 0;
    private int language = 0;
    private int status = 0;
    private int words = 0;
    private boolean wordsIsMinimum = true;
    private boolean pairingIncluded = false;
    private boolean pairingExcluded = false;

    public SearchQuery(SubCategory subCategory) {
        this(subCategory.getCategory().getId(), subCategory.getName().replace(' ', '-'));
    }

    public SearchQuery order(SearchOrder order) {
        return order(order.getId());
    }

    public SearchQuery order(int order) {
        this.order = order;
        return this;
    }

    public SearchQuery includeGenre(int genre) {
        this.genresIncluded.add(genre);
        return this;
    }

    public SearchQuery includeGenre(Genre genre) {
        return includeGenre(genre.getId());
    }

    public SearchQuery rating(int from, int to) {
        this.minRating = from;
        this.maxRating = to;
        return this;
    }

    public SearchQuery rating(Rating from, Rating to) {
        return rating(from.getId(), to.getId());
    }

    public SearchQuery rating(Rating rating) {
        return rating(rating, rating);
    }

    private SearchQuery words(int words, boolean minimum) {
        this.words = words;
        this.wordsIsMinimum = minimum;
        return this;
    }

    public SearchQuery minWords(int words) {
        return words(words, true);
    }

    public SearchQuery maxWords(int words) {
        return words(words, false);
    }

    public SearchQuery includeWorld(int world) {
        worldsIncluded.add(world);
        return this;
    }

    public SearchQuery includeWorld(World world) {
        return includeWorld(world.getId());
    }

    public SearchQuery excludeWorld(int world) {
        worldsExcluded.add(world);
        return this;
    }

    public SearchQuery excludeWorld(World world) {
        return excludeWorld(world.getId());
    }

    public SearchQuery includeCharacter(int character) {
        this.charactersIncluded.add(character);
        return this;
    }

    public SearchQuery includeCharacter(Character character) {
        return includeCharacter(character.getId());
    }

    public SearchQuery timeRange(int timeRange) {
        this.timeRange = timeRange;
        return this;
    }

    public SearchQuery timeRange(TimeRange timeRange) {
        return timeRange(timeRange.getId());
    }

    public SearchQuery language(int language) {
        this.language = language;
        return this;
    }

    public SearchQuery language(Language language) {
        return language(language.getId());
    }

    public SearchQuery status(int status) {
        this.status = status;
        return this;
    }

    public SearchQuery status(Status status) {
        return status(status.getId());
    }

    public SearchQuery pairingIncluded(boolean pairing) {
        this.pairingIncluded = pairing;
        return this;
    }

    public SearchQuery pairingExcluded(boolean pairing) {
        this.pairingExcluded = pairing;
        return this;
    }

    public CompiledSearchQuery compile() {
        final StringBuilder builder = new StringBuilder("https://www.fanfiction.net/");
        builder.append(category).append('/');
        try {
            builder.append(URLEncoder.encode(subCategorySanitized, "UTF-8")).append("/?");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        if (order != 0) {
            builder.append("srt=").append(order).append('&');
        }
        for (int i = 0; i < genresIncluded.size(); i++) {
            int genre = genresIncluded.get(i);
            builder.append('g').append((char) ('a' + i)).append('=').append(genre).append('&');
        }
        if (minRating != 0 || maxRating != 0) {
            builder.append("r=").append(minRating);
            if (minRating != maxRating) {
                builder.append('0').append(maxRating);
            }
            builder.append('&');
        }
        if (words != 0 || !wordsIsMinimum) {
            builder.append("len=");
            int thousands = words / 1000;
            builder.append(thousands);
            if (!wordsIsMinimum) { builder.append('1'); }
            builder.append('&');
        }
        if (language != 0) {
            builder.append("lan=").append(language).append('&');
        }
        if (status != 0) {
            builder.append("s=").append(status).append('&');
        }
        if (timeRange != 0) {
            builder.append("t=").append(timeRange).append('&');
        }
        if (pairingIncluded) {
            builder.append("pm=1&");
        }
        if (pairingExcluded) {
            builder.append("_pm=1&");
        }
        appendItemList(builder, "g", genresIncluded);
        appendItemList(builder, "_g", genresExcluded);
        appendItemList(builder, "c", charactersIncluded);
        appendItemList(builder, "_c", charactersExcluded);
        appendItemList(builder, "v", worldsIncluded);
        appendItemList(builder, "_v", worldsExcluded);

        builder.append("p=");

        final String s = builder.toString();
        return new CompiledSearchQuery(s);
    }

    private static void appendItemList(StringBuilder builder, String tag, List<Integer> l) {
        for (int i = 0; i < l.size(); i++) {
            int genre = l.get(i);
            builder.append(tag).append((char) ('a' + i)).append('=').append(genre).append('&');
        }
    }

    public String compile(int page) {
        return compile().finalize(page);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CompiledSearchQuery {
        private final String query;

        public String finalize(int page) {
            return query + (page + 1);
        }
    }
}