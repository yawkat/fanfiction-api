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
}
