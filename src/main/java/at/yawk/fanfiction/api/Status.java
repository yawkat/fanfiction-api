package at.yawk.fanfiction.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
@Getter
public enum Status {
    IN_PROGRESS(1, "In-Progress"),
    COMPLETE(2, "Complete");

    private final int id;
    private final String name;
}
