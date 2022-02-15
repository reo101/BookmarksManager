package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * WebpageFetchException
 */
public class BitlyException extends ApplicationException {

    public BitlyException() {
        super(ServerLogger.getInstance());
    }

    public BitlyException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public BitlyException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public BitlyException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public BitlyException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
