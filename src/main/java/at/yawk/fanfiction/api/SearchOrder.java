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
