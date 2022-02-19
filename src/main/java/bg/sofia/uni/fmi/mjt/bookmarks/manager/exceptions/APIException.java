package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * APIException
 */
public class APIException extends ApplicationException {

    public APIException(String message) {
        super(ServerLogger.getInstance(), message);
    }
}
