package at.yawk.fanfiction.api;

import java.util.List;
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
    private Rating rating;
    private int chapterCount;
    private int wordCount;
    private int favoriteCount;
    private int followCount;
    private int reviewCount;
    private String language;
    private List<Genre> genres;
}
