package bg.sofia.uni.fmi.mjt.bookmarks.manager.logging;

/**
 * ClientLogger
 */
public class ClientLogger {
    private static ClientLogger clientLogger = null;

    private ClientLogger() {
        // init
    }

    public static ClientLogger getInstance() {
        if (ClientLogger.clientLogger == null) {
            ClientLogger.clientLogger = new ClientLogger();
        }

        return clientLogger;
    }
}
