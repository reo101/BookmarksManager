package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * ResponseParseException
 */
public class ResponseParseException extends ApplicationException {

    public ResponseParseException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
