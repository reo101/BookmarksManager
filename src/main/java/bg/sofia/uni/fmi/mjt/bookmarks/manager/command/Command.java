package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;

/**
 * Command
 */
public class Command {
    private static String urlRegex = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static enum Type {
        // @formatter:off
        REGISTER           ("^register\s(\\w+)\s(\\w+)$"),
        LOGIN              ("^login\s(\\w+)\s(\\w+)$"),
        LOGOUT             ("^logout$"),
        NEW_GROUP          ("^new-group\s(\\w+)$"),
        ADD_TO             ("^add-to\s(\\w+)\s(URL)(?:\s(--shorten))?$"),
        REMOVE_FROM        ("^remove-from\s(\\w+)\s(URL)$"),
        LIST               ("^list$"),
        LIST_BY_GROUP      ("^list\s--group-name\s(\\w+)$"),
        SEARCH_BY_TAGS     ("^search\s--tags\s(\\w+(?:\s\\w+)+)$"),
        SEARCH_BY_TITLE    ("^search\s--title\s(\\w+)$"),
        SHUTDOWN           ("^shutdown$"),
        CLEANUP            ("^cleanup$"),
        IMPORT_FROM_CHROME ("^import-from-chrome$");
        // @formatter:on

        private String regex;

        private Type(String regex) {
            this.regex = regex.replaceAll("URL", urlRegex);
        }

        public Matcher getMatcher(String input) {
            return Pattern.compile(this.regex).matcher(input);
        }

        public String getRegex() {
            return this.regex;
        }

        public static Type of(String input) throws CommandParseException {
            return Arrays.stream(Type.values())
                    .filter((Type type) -> input.matches(type.regex))
                    .findFirst()
                    .orElseThrow(() -> {
                        return new CommandParseException(
                                String.format(
                                        "Couldn't parse command '%s'",
                                        input));
                    });
        }
    }
}
