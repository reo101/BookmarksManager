package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * NoSuchGroupException
 */
public class NoSuchGroupException extends ApplicationException {

    public NoSuchGroupException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
