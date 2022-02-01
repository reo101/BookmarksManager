package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.utils.PasswordUtils;

public final class LogoutRequest extends Request {
    public LogoutRequest() {
        this.type = "LOGOUT";
    }
}
