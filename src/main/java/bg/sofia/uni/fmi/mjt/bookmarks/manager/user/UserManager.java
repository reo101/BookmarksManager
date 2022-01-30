package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;

/**
 * UserManager
 */
public interface UserManager {

    /**
     * Add a user to the repository
     *
     * @param user The user
     * @throws DuplicateUserException
     */
    public void addUser(User user) throws DuplicateUserException;

    /**
     * Get user by username and hashed password
     * 
     * @param username 
     * @param hashedPassword 
     * 
     * @return The user
     * 
     * @throws     UserNotFoundException if there is no user in the database with the specified username
     * @throws WrongPasswordException if there is a user in the database with the specified name but the password is wrong
     */
    public User getUser(String username, String hashedPassword)
     throws UserNotFoundException, WrongPasswordException;
}
