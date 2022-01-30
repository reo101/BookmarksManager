package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import java.util.Map;
import java.util.Objects;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;

/**
 * DefaultUserManager
 */
public class DefaultUserManager implements UserManager {
    private Map<String, User> users;

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
    }

    @Override
    public User getUser(String username, String hashedPassword)
            throws UserNotFoundException, WrongPasswordException {
        if (!this.users.containsKey(username)) {
            throw new UserNotFoundException(
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
}
