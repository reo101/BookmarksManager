package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * CommandParseException
 */
public class CommandParseException extends Exception {

    public CommandParseException() {
    }

    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(Throwable cause) {
        super(cause);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
