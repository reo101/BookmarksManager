package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * DuplicateUserException
 */
public class DuplicateUserException extends UserException {
    public DuplicateUserException() {
    }

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(Throwable cause) {
        super(cause);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
