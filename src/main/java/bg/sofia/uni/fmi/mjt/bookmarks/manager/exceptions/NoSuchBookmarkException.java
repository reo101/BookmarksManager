package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * NoSuchGroupException
 */
public class NoSuchBookmarkException extends ApplicationException {

    public NoSuchBookmarkException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
