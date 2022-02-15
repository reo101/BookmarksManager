package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * DuplicateUserException
 */
public class DuplicateUserException extends ApplicationException {

    public DuplicateUserException() {
        super(ServerLogger.getInstance());
    }

    public DuplicateUserException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public DuplicateUserException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public DuplicateUserException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public DuplicateUserException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
