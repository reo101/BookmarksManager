package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class NewGroupRequest extends Request {
    @SerializedName("groupName")
    private String groupName;

    public NewGroupRequest(String groupName) {
        this.type = "NEW_GROUP";

        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
