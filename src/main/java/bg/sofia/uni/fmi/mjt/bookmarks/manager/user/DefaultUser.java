package bg.sofia.uni.fmi.mjt.bookmarks.manager.user;

import java.util.Objects;

/**
 * User
 */
public class DefaultUser implements User {
    private String username;
    private String hashedPassword;

    public DefaultUser(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public boolean login(String hashedPassword) {
        return this.hashedPassword.equals(hashedPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        DefaultUser user = (DefaultUser) obj;

        // Compare the data members and return accordingly
        return Objects.equals(this.username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
