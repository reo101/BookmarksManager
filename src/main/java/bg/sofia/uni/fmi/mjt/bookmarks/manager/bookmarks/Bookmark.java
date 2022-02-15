package bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;

import static bg.sofia.uni.fmi.mjt.bookmarks.manager.utilities.Utilities.ANSI.*;

/**
 * Bookmark
 */
public class Bookmark {
    @Expose
    private String title;

    @Expose
    private String url;

    @Expose
    private Collection<String> tags;

    public Bookmark(String name, String url, Collection<String> tags) {
        this.title = name;
        this.url = url;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Collection<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return String.format(
        //@formatter:off
                        "%s%sBookmark Title:%s%s %s"   + "%s" +
                        "%s%sURL:%s%s %s"              + "%s" +
                        "%s%sTags:%s%s %s[%s %s %s]%s" + "%s",
        //@formatter:on
                YELLOW_FOREGROUND, ITALIC,
                RESET, RESET_ITALIC,
                this.title,
                System.lineSeparator(),
                PURPLE_FOREGROUND, ITALIC,
                RESET, RESET_ITALIC,
                this.url,
                System.lineSeparator(),
                GREEN_FOREGROUND, ITALIC,
                RESET, RESET_ITALIC,
                RED_FOREGROUND, RESET,
                this.tags.stream()
                        .collect(Collectors.joining(", ")),
                RED_FOREGROUND, RESET,
                System.lineSeparator());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Bookmark bookmark = (Bookmark) obj;

        return Objects.equals(this.url, bookmark.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url);
    }
}
