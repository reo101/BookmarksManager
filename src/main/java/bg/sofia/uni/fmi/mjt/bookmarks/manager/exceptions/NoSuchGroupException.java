package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * NoSuchGroupException
 */
public class NoSuchGroupException extends ApplicationException {

    public NoSuchGroupException() {
        super(ServerLogger.getInstance());
    }

    public NoSuchGroupException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public NoSuchGroupException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public NoSuchGroupException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public NoSuchGroupException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
