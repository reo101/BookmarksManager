package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * WebpageFetchException
 */
public class BitlyException extends ApplicationException {

    public BitlyException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public BitlyException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }
}
