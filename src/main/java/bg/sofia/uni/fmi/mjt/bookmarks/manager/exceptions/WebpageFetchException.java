package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

/**
 * WebpageFetchException
 */
public class WebpageFetchException extends Exception {

    public WebpageFetchException() {
    }

    public WebpageFetchException(String message) {
        super(message);
    }

    public WebpageFetchException(Throwable cause) {
        super(cause);
    }

    public WebpageFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
