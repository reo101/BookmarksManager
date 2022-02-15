package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.storage.BookmarksStorage;

/**
 * UserManager
 */
public interface UserManager {

    /**
     * Add a user to the repository
     *
     * @param user The user
     *
     * @throws DuplicateUserException if there is already a user with the same username in the database
     */
    public void addUser(User user)
            throws DuplicateUserException;

    /**
     * Get user by username and password
     *
     * @param username
     * @param password
     *
     * @return The user
     *
     * @throws NoSuchUserException    if there is no user in the database with the
     *                                specified username
     * @throws WrongPasswordException if there is a user in the database with the
     *                                specified name but the password is wrong
     */
    public User getUser(String username, String password)
            throws NoSuchUserException, WrongPasswordException;

    /**
     * Get {@link BookmarksStorage} associated with a certain user
     *
     * @param username The username for that user
     *
     * @return The {@link BookmarksStorage}
     *
     * @throws NoSuchUserException if there is no user in the database with the
     *                             specified username
     */
    public BookmarksStorage getUserBookmarksStorage(String username)
            throws NoSuchUserException;
}
