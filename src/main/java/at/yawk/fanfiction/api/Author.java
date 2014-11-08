package at.yawk.fanfiction.api;

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
public class Author {
    private int id;
    private String name;
}