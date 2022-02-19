package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * WrongPasswordException
 */
public class WrongPasswordException extends ApplicationException {

    public WrongPasswordException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
