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
public class Story {
    private SubCategory subCategory;
    private int id;
    private String title;
    private Author author;
    private String description;
}
