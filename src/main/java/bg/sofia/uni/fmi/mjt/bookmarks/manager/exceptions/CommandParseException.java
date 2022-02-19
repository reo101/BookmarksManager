package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ClientLogger;

/**
 * CommandParseException
 */
public class CommandParseException extends ApplicationException {

    public CommandParseException(
            String message) {
        super(ClientLogger.getInstance(), message);
    }
}
