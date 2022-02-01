package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class SearchByTagsRequest extends Request {
    @SerializedName("groupName")
    private String groupName;

    public SearchByTagsRequest(String groupName) {
        this.type = "SEARCH_BY_TAGS";

        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
