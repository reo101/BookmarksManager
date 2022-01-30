package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

/**
 * User
 */
public interface User {

    public String getUsername();

    public boolean login(String hashedPassword);
}
