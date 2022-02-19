package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * UserNotFoundException
 */
public class NoSuchUserException extends ApplicationException {

    public NoSuchUserException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
