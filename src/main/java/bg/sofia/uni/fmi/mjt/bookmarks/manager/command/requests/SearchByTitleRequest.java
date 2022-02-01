package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class SearchByTitleRequest extends Request {
    @SerializedName("groupName")
    private String groupName;

    public SearchByTitleRequest(String groupName) {
        this.type = "SEARCH_BY_TITLE";

        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
