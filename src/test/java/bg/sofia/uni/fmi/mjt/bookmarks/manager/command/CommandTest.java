package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;

/**
 * CommandTest
 */
public class CommandTest {

    @Test
    public void testCommandParsing() throws CommandParseException {
        assertEquals(Command.Type.LOGIN, Command.Type.of("login user pass"),
                "Login command should be parsed correctly");
    }

    @Test
    public void testCommandParsingFailure() throws CommandParseException {
        assertThrows(CommandParseException.class, () -> {
            Command.Type.of("wrong command");
        }, "Login command should be parsed correctly");
    }
}
