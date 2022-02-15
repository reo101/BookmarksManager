package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * WrongPasswordException
 */
public class WrongPasswordException extends ApplicationException {

    public WrongPasswordException() {
        super(ServerLogger.getInstance());
    }

    public WrongPasswordException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public WrongPasswordException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public WrongPasswordException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public WrongPasswordException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
