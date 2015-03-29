/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Builder;

/**
 * @author yawkat
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubCategory {
    private final Category category;
    private final boolean crossover;

    private String name;
    private int id;
    private int estimatedStoryCount;
    private Collection<Character> characters;
    private Collection<Genre> genres;
    private Collection<Language> languages;
    private Collection<World> worlds;
}
