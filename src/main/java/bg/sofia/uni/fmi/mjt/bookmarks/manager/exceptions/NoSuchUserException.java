package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * UserNotFoundException
 */
public class NoSuchUserException extends ApplicationException {

    public NoSuchUserException() {
        super(ServerLogger.getInstance());
    }

    public NoSuchUserException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public NoSuchUserException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public NoSuchUserException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public NoSuchUserException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
