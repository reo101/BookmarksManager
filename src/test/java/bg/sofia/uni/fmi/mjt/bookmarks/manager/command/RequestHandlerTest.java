package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.LoginRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;

/**
 * RequestHandlerTest
 */
public class RequestHandlerTest {

    @Test
    public void testRequestHandlerParseCommand() throws CommandParseException {
        LoginRequest expected = new LoginRequest("user", "password");
        LoginRequest actual = (LoginRequest) RequestHandler.parseCommand("login user password");

        assertEquals(expected.getUsername(), actual.getUsername(),
                "Login command (username) should be parsed correctly");
        assertEquals(expected.getPassword(), actual.getPassword(),
                "Login command (password) should be parsed correctly");
    }
}
