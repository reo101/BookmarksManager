package bg.sofia.uni.fmi.mjt.bookmarks.manager.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;

/**
 * DefaultBookmarksStorage
 */
public class DefaultBookmarksStorage implements BookmarksStorage {
    private User user;
    private Map<String, Set<Bookmark>> bookmarks;

    public DefaultBookmarksStorage(User user) {
        this.user = user;
        this.bookmarks = new HashMap<>();
    }

    @Override
    public synchronized User getUser() {
        return user;
    }

    @Override
    public synchronized void addBookmark(String groupName, String bookmarkName, String bookmarkUrl, boolean shorten) {
        this.bookmarks
                .get(groupName)
                .add(new Bookmark(bookmarkName, bookmarkUrl));
    }

    @Override
    public synchronized void addGroup(String groupName) throws DuplicateGroupException {
        if (this.bookmarks.containsKey(groupName)) {
            throw new DuplicateGroupException(
                    String.format(
                            "There is already a group named %s",
                            groupName));
        }
    }

    @Override
    public synchronized void removeBookmark(String groupName, String bookmarkName, String bookmarkUrl)
            throws NoSuchGroupException {
        if (this.bookmarks.containsKey(groupName)) {
            throw new NoSuchGroupException(
                    String.format(
                            "There isn't a group named %s",
                            groupName));
        }

        // if (this.bookmarks.get(groupName).contains)

        this.bookmarks
                .get(groupName)
                .remove(new Bookmark(bookmarkName, bookmarkUrl));

        // TODO: takovata
    }

    @Override
    public synchronized Collection<Bookmark> listBookmarks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public synchronized Collection<Bookmark> listBookmarksByGroup(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public synchronized Collection<Bookmark> listBookmarksByTags(String... tags) {
        // TODO Auto-generated method stub
        return null;
    }
}
