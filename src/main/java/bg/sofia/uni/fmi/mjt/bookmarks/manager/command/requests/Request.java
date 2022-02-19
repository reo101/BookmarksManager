package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Request
 */
public sealed class Request permits RegisterRequest,LoginRequest,LogoutRequest,NewGroupRequest,AddToRequest,RemoveFromRequest,ListRequest,ListByGroupRequest,SearchByTagsRequest,SearchByTitleRequest,CleanupRequest,ImportFromChromeRequest {
    @SerializedName("type")
    protected String type;
}
