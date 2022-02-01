package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class ListByGroupRequest extends Request {
    @SerializedName("groupName")
    private String groupName;

    public ListByGroupRequest(String groupName) {
        this.type = "LIST_BY_GROUP";

        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
