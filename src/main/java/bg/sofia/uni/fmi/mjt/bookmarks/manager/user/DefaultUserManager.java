package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import java.util.Objects;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;

/**
 * DefaultUserManager
 */
public class DefaultUserManager implements UserManager {
    private Set<User> users;

    @Override
    public void addUser(User user)
            throws DuplicateUserException {
        Objects.requireNonNull(user);

        if (this.users.contains(user)) {
            throw new DuplicateUserException(
                    String.format(
                            "User with username %s already exists",
                            user.getUsername()));
        }
    }

    @Override
    public User getUser(String username, String hashedPassword)
            throws UserNotFoundException, WrongPasswordException {
        User user = this.users.stream()
                .filter((u) -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        if (!user.login(hashedPassword)) {
            throw new WrongPasswordException(
                    String.format(
                            "Wrong password for username %s",
                            username));
        }

        return user;
    }
}
