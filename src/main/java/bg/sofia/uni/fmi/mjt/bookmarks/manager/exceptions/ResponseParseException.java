package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * ResponseParseException
 */
public class ResponseParseException extends ApplicationException {

    public ResponseParseException() {
        super(ServerLogger.getInstance());
    }

    public ResponseParseException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public ResponseParseException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public ResponseParseException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public ResponseParseException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
