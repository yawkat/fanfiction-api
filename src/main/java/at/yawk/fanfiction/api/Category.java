package at.yawk.fanfiction.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@Getter
@RequiredArgsConstructor
public enum Category {
    ANIME("Anime/Manga", "anime"),
    BOOKS("Books", "book"),
    CARTOONS("Cartoons", "cartoon"),
    COMICS("Comics", "comic"),
    GAMES("Games", "game"),
    MISC("Misc", "misc"),
    MOVIES("Movies", "movie"),
    PLAYS("Plays/Musicals", "play"),
    TV_SHOWS("TV Shows", "tv");

    private final String name;
    private final String id;
}
