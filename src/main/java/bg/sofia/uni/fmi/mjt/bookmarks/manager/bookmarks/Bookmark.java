package bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks;

import java.util.Objects;

/**
 * Bookmark
 */
public class Bookmark {
    private String name;
    private String url;

    public Bookmark(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
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

        // Compare the data members and return accordingly
        return Objects.equals(this.name, bookmark.name) &&
                Objects.equals(this.url, bookmark.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.url);
    }
}
