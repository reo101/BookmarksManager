package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.ServerLogger;

/**
 * BackupException
 */
public class BackupException extends ApplicationException {

    public BackupException(
            String message) {
        super(ServerLogger.getInstance(), message);
    }

    public BackupException(
            String message,
            Throwable cause) {
        super(ServerLogger.getInstance(), message, cause);
    }
}
