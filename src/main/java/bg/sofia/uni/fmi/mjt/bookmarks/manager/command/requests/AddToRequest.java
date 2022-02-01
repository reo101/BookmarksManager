package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class AddToRequest extends Request {
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("url")
    private String url;
    @SerializedName("shorten")
    private boolean shorten;

    public AddToRequest(String groupName, String url, boolean shorten) {
        this.type = "ADD_TO";

        this.groupName = groupName;
        this.url = url;
        this.shorten = shorten;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUrl() {
        return url;
    }

    public boolean isShorten() {
        return shorten;
    }
}

