package bg.sofia.uni.fmi.mjt.bookmarks.manager.storage;

import java.util.Collection;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;

/**
 * BookmarksStorage
 */
public interface BookmarksStorage {

    /**
     * Get the associated user
     * 
     * @return the associated user
     */
    public User getUser();

    /**
     * Add a bookmark to a certain group
     * 
     * @param groupName    Name of the group
     * @param bookmarkName Name of the bookmark
     * @param bookmarkUrl  Url of the bookmark
     * @param shorten      Wether to shorten the bookmark
     */
    public void addBookmark(String groupName, String bookmarkName, String bookmarkUrl, boolean shorten);

    /**
     * Add a new group
     * 
     * @param groupName Name of the group
     * @throws DuplicateGroupException if a group with this name already exists
     */
    public void addGroup(String groupName)
            throws DuplicateGroupException;

    // public synchronized void addGroup(String groupName) throws
    // DuplicateGroupException;

    /**
     * Remove a bookmark from a certain group
     * 
     * @param groupName    Name of the group
     * @param bookmarkName Name of the bookmark
     * @param bookmarkUrl  Url of the bookmark
     * 
     * @throws NoSuchGroupException if there is no group with the name groupName
     */
    public void removeBookmark(String groupName, String bookmarkName, String bookmarkUrl)
            throws NoSuchGroupException;

    /**
     * List all bookmarks
     * 
     * @return A Collection of all bookmarks
     */
    public Collection<Bookmark> listBookmarks();

    /**
     * List all bookmarks in a certain group
     * 
     * @param groupName Name of the group
     * 
     * @return A Collection of those bookmarks
     */
    public Collection<Bookmark> listBookmarksByGroup(String groupName);

    /**
     * List all bookmarks matching certain tags
     * 
     * @param tags Array containing the tags
     * 
     * @return A Collection of those bookmarks
     */
    public Collection<Bookmark> listBookmarksByTags(String... tags);
}
