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
@RequiredArgsConstructor
@Getter
public enum Rating {
    CHILDREN("K", 1),
    CHILDREN_PLUS("K+", 2),
    TEEN("T", 3),
    MATURE("M", 4);

    private final String name;
    private final int id;

    public static Rating forName(String name) {
        for (Rating rating : values()) {
            if (rating.getName().equals(name)) { return rating; }
        }
        return null;
    }
}
