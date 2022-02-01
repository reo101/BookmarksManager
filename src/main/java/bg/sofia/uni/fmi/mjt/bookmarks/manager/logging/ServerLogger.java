package bg.sofia.uni.fmi.mjt.bookmarks.manager.logging;

/**
 * ClientLogger
 */
public class ServerLogger {
    private static ServerLogger serverLogger = null;

    private ServerLogger() {
        // init
    }

    public static ServerLogger getInstance() {
        if (ServerLogger.serverLogger == null) {
            ServerLogger.serverLogger = new ServerLogger();
        }

        return serverLogger;
    }
}
