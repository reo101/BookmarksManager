package bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.logging.Logger;

/**
 * ApplicationException
 */
public abstract class ApplicationException extends Exception {

    private Logger logger;

    public ApplicationException(
            Logger logger) {
        super();
        this.logger = logger;
        this.log();
    }

    public ApplicationException(
            Logger logger,
            String message) {
        super(message);
        this.logger = logger;
        this.log();
    }

    public ApplicationException(
            Logger logger,
            Throwable cause) {
        super(cause);
        this.logger = logger;
        this.log();
    }

    public ApplicationException(
            Logger logger,
            String message,
            Throwable cause) {
        super(message, cause);
        this.logger = logger;
        this.log();
    }

    public ApplicationException(
            Logger logger,
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.logger = logger;
        this.log();
    }

    private void log() {
        this.logger.log(
                String.format(
                //@formatter:off
                        "Exception: %s"   + "%s" +
                        "Timestamp: %s"   + "%s" +
                        "Stack Trace: %s" + "%s%s",
                //@formatter:on
                        this.toString(),
                        System.lineSeparator(),
                        LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy HH:mm:ss")),
                        System.lineSeparator(),
                        ((Supplier<String>) () -> {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            this.printStackTrace(pw);
                            return sw.toString();
                        }).get(),
                        System.lineSeparator(), System.lineSeparator()));
    }
}
