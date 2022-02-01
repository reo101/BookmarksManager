package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * ResponseParseException
 */
public class ResponseParseException extends Exception {

    public ResponseParseException() {
    }

    public ResponseParseException(String message) {
        super(message);
    }

    public ResponseParseException(Throwable cause) {
        super(cause);
    }

    public ResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
