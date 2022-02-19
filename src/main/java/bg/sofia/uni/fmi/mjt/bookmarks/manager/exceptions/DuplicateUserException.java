package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * DuplicateUserException
 */
public class DuplicateUserException extends ApplicationException {

    public DuplicateUserException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
