package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * APIKeyLoadException
 */
public class APIKeyLoadException extends RuntimeException {

    public APIKeyLoadException() {
    }

    public APIKeyLoadException(String message) {
        super(message);
    }

    public APIKeyLoadException(Throwable cause) {
        super(cause);
    }

    public APIKeyLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
