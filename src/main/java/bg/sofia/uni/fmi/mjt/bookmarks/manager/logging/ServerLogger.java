package bg.sofia.uni.fmi.mjt.bookmarks.manager.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * ServerLogger
 */
public class ServerLogger implements Logger {
    private static ServerLogger clientLogger = null;
    private static Path SERVER_LOGS_PATH = Path.of("logs/SERVER_LOGS.log");

    private ServerLogger() {
        try {
            if (!Files.exists(SERVER_LOGS_PATH.getParent())) {
                Files.createDirectory(SERVER_LOGS_PATH.getParent());
            }
        } catch (IOException e) {
            System.err.println(String.format(
                    "Couldn't create logs directory: %s",
                    e));
            e.printStackTrace();
        }

        try {
            if (!Files.exists(SERVER_LOGS_PATH)) {
                Files.createFile(SERVER_LOGS_PATH);
            }
        } catch (IOException e) {
            System.err.println(String.format(
                    "Couldn't create server logs file: %s",
                    e));
            e.printStackTrace();
        }
    }

    public static ServerLogger getInstance() {
        if (ServerLogger.clientLogger == null) {
            ServerLogger.clientLogger = new ServerLogger();
        }

        return clientLogger;
    }

    @Override
    public void log(String message) {
        try {
            Files.writeString(
                    SERVER_LOGS_PATH,
                    message,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(String.format(
                    "Couldn't append to logs file: %s",
                    e));
        }
    }
}
