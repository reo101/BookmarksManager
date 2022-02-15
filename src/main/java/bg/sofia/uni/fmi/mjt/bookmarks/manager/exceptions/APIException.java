package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * APIException
 */
public class APIException extends ApplicationException {

    public APIException() {
        super(ServerLogger.getInstance());
    }

    public APIException(String message) {
        super(ServerLogger.getInstance(), message);
    }

    public APIException(Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public APIException(String message, Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }
}
