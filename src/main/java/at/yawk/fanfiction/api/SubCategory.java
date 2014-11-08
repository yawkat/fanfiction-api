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
