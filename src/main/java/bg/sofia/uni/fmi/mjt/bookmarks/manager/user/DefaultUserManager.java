package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.storage.BookmarksStorage;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.storage.DefaultBookmarksStorage;

/**
 * DefaultUserManager
 */
public class DefaultUserManager implements UserManager {
    private Map<String, User> users;
    private Map<User, BookmarksStorage> storage;

    public DefaultUserManager() {
        this.users = new HashMap<>();
        this.storage = new HashMap<>();
    }

    @Override
    public void addUser(User user)
            throws DuplicateUserException {
        Objects.requireNonNull(user);

        if (this.users.containsKey(user.getUsername())) {
            throw new DuplicateUserException(
                    String.format(
                            "User with username %s already exists",
                            user.getUsername()));
        }

        this.users.put(user.getUsername(), user);
        this.storage.put(user, new DefaultBookmarksStorage(user));
    }

    @Override
    public User getUser(String username, String hashedPassword)
            throws NoSuchUserException, WrongPasswordException {
        Objects.requireNonNull(username, hashedPassword);

        if (!this.users.containsKey(username)) {
            throw new NoSuchUserException(
                    String.format(
                            "User with username %s does not exist",
                            username));
        }

        User user = this.users.get(username);

        if (!user.login(hashedPassword)) {
            throw new WrongPasswordException(
                    String.format(
                            "Wrong password for username %s",
                            username));
        }

        return user;
    }

    @Override
    public BookmarksStorage getUserBookmarksStorage(User user) throws NoSuchUserException {
        Objects.requireNonNull(user);

        if (!this.storage.containsKey(user)) {
            throw new NoSuchUserException(
                    String.format("User with username %s does not exist",
                            user.getUsername()));
        }

        return this.storage.get(user);
    }
}
