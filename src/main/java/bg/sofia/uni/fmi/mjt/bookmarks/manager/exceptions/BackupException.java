package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * BackupException
 */
public class BackupException extends ApplicationException {

    public BackupException() {
        super(ServerLogger.getInstance());
    }

    public BackupException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public BackupException(
            Throwable cause) {
        super(ServerLogger.getInstance(), cause);
    }

    public BackupException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }

    public BackupException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(ServerLogger.getInstance(), message, cause, enableSuppression, writableStackTrace);
    }
}
