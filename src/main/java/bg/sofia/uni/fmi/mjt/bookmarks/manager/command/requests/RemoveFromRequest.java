package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class RemoveFromRequest extends Request {
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("url")
    private String url;

    public RemoveFromRequest(String groupName, String url) {
        this.type = "REMOVE_FROM";

        this.groupName = groupName;
        this.url = url;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUrl() {
        return url;
    }
}
