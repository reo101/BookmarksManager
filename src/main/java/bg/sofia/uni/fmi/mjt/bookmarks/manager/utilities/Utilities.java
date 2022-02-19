
package bg.sofia.uni.fmi.mjt.bookmarks.manager.utilities;

/**
 * InnerUtilities
 */
public class Utilities {
    public static final String NEWLINE_PLACEHOLDER = "NEWLINE_PLACEHOLDER";

    public enum ANSI_CODES {
        BLACK_FOREGROUND("\u001B[30m"),
        BLACK_BACKGROUND("\u001B[40m"),
        RED_FOREGROUND("\u001B[31m"),
        RED_BACKGROUND("\u001B[41m"),
        GREEN_FOREGROUND("\u001B[32m"),
        GREEN_BACKGROUND("\u001B[42m"),
        YELLOW_FOREGROUND("\u001B[33m"),
        YELLOW_BACKGROUND("\u001B[43m"),
        BLUE_FOREGROUND("\u001B[34m"),
        BLUE_BACKGROUND("\u001B[44m"),
        PURPLE_FOREGROUND("\u001B[35m"),
        PURPLE_BACKGROUND("\u001B[45m"),
        CYAN_FOREGROUND("\u001B[36m"),
        CYAN_BACKGROUND("\u001B[46m"),
        WHITE_FOREGROUND("\u001B[37m"),
        WHITE_BACKGROUND("\u001B[47m"),
        RESET("\u001B[0m"),
        ITALIC("\033[3m"),
        RESET_ITALIC("\033[0m");

        private final String code;

        ANSI_CODES(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return this.code;
        }
    }
}
