package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

public final class SearchByTitleRequest extends Request {
    @SerializedName("title")
    private String title;

    public SearchByTitleRequest(String title) {
        this.type = "SEARCH_BY_TITLE";

        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
