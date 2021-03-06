package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * DuplicateGroupException
 */
public class DuplicateGroupException extends ApplicationException {

    public DuplicateGroupException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }
}
