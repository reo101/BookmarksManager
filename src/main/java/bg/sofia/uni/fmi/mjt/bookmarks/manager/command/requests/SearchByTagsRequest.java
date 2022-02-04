package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import java.util.Collection;
import java.util.Collections;

import com.google.gson.annotations.SerializedName;

public final class SearchByTagsRequest extends Request {
    @SerializedName("tags")
    private Collection<String> tags;

    public SearchByTagsRequest(Collection<String> tags) {
        this.type = "SEARCH_BY_TAGS";

        this.tags = tags;
    }

    public Collection<String> getTags() {
        return Collections.unmodifiableCollection(this.tags);
    }
}
