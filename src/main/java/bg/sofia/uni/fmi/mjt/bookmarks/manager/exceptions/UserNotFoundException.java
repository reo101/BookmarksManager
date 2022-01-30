package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * UserNotFoundException
 */
public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
