package bg.sofia.uni.fmi.mjt.bookmarks.manager.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * ClientLogger
 */
public class ClientLogger implements Logger {
    private static ClientLogger clientLogger = null;
    private static Path CLIENT_LOGS_PATH = Path.of("logs/CLIENT_LOGS.log");

    private ClientLogger() {
        try {
            if (!Files.exists(CLIENT_LOGS_PATH.getParent())) {
                Files.createDirectory(CLIENT_LOGS_PATH.getParent());
            }
        } catch (IOException e) {
            System.err.println(String.format(
                    "Couldn't create logs directory: %s",
                    e));
            e.printStackTrace();
        }

        try {
            if (!Files.exists(CLIENT_LOGS_PATH)) {
                Files.createFile(CLIENT_LOGS_PATH);
            }
        } catch (IOException e) {
            System.err.println(String.format(
                    "Couldn't create client logs file: %s",
                    e));
            e.printStackTrace();
        }
    }

    public static ClientLogger getInstance() {
        if (ClientLogger.clientLogger == null) {
            ClientLogger.clientLogger = new ClientLogger();
        }

        return clientLogger;
    }

    @Override
    public void log(String message) {
        try {
            Files.writeString(
                    CLIENT_LOGS_PATH,
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
