package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import java.util.Objects;

/**
 * User
 */
public class User {
    private String username;
    private String hashedPassword;
    private String salt;

    public User(String username, String hashedPassword, String salt) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public String getUsername() {
        return this.username;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public String getSalt() {
        return this.salt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;

        // Compare the data members and return accordingly
        return Objects.equals(this.username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
