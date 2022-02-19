package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * WebpageFetchException
 */
public class WebpageFetchException extends ApplicationException {

    public WebpageFetchException() {
        super(ServerLogger.getInstance());
    }

    public WebpageFetchException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public WebpageFetchException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }
}
