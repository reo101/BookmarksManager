package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * DuplicateGroupException
 */
public class DuplicateGroupException extends ApplicationException {

    public DuplicateGroupException() {
        super(ServerLogger.getInstance());
    }

    public DuplicateGroupException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public DuplicateGroupException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public DuplicateGroupException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public DuplicateGroupException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
