package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class LoginRequest extends Request {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public LoginRequest(String username, String password) {
        this.type = "LOGIN";

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
