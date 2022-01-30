package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * WrongPasswordException
 */
public class WrongPasswordException extends UserException {

    public WrongPasswordException() {
    }

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(Throwable cause) {
        super(cause);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
