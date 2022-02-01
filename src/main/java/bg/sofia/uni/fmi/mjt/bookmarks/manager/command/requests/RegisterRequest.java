package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.utils.PasswordUtils;

public final class RegisterRequest extends Request {
    @SerializedName("username")
    private String username;
    @SerializedName("salt")
    private String salt;
    @SerializedName("hashedPassword")
    private String hashedPassword;

    public RegisterRequest(String username, String password) {
        this.type = "REGISTER";

        this.username = username;
        // this.salt = PasswordUtils.getSalt(30); // FIXME: magic number
        this.salt = PasswordUtils.HARDCODED_SALT;
        this.hashedPassword = PasswordUtils.generateSecurePassword(password, salt);
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
