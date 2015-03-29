/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fanfiction.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@Getter
@RequiredArgsConstructor
public enum SearchOrder {
    UPDATE_DATE("Update Date", 1),
    PUBLISH_DATE("Publish Date", 2),
    REVIEWS("Reviews", 3),
    FAVORITES("Favorites", 4),
    FOLLOWS("Follows", 5);

    private final String name;
    private final int id;
}
