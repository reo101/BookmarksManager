package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * NoSuchGroupException
 */
public class NoSuchGroupException extends Exception {

    public NoSuchGroupException() {
    }

    public NoSuchGroupException(String message) {
        super(message);
    }

    public NoSuchGroupException(Throwable cause) {
        super(cause);
    }

    public NoSuchGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
