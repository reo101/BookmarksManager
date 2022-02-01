package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * UserNotFoundException
 */
public class NoSuchUserException extends UserException {

    public NoSuchUserException() {
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
