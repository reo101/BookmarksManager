package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * DuplicateGroupException
 */
public class DuplicateGroupException extends Exception {

    public DuplicateGroupException() {
    }

    public DuplicateGroupException(String message) {
        super(message);
    }

    public DuplicateGroupException(Throwable cause) {
        super(cause);
    }

    public DuplicateGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
