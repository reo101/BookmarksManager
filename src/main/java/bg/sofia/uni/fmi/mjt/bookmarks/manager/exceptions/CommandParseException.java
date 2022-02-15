package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ClientLogger;

/**
 * CommandParseException
 */
public class CommandParseException extends ApplicationException {

    public CommandParseException() {
        super(ClientLogger.getInstance());
    }

    public CommandParseException(
            String message) {
        super(ClientLogger.getInstance(), message);
    }

    public CommandParseException(
            Throwable cause) {
        super(ClientLogger.getInstance(), cause);
    }

    public CommandParseException(
            String message,
            Throwable cause) {
        super(ClientLogger.getInstance(), message, cause);
    }

    public CommandParseException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ClientLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
