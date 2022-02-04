package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * APIException
 */
public class APIException extends Exception {

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(Throwable cause) {
        super(cause);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
