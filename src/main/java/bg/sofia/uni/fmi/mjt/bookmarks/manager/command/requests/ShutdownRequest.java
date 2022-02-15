package bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests;

public final class ShutdownRequest extends Request {
    public ShutdownRequest() {
        this.type = "SHUTDOWN";
    }
}
