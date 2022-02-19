package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.AddToRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.CleanupRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.ImportFromChromeRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.ListByGroupRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.ListRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.LoginRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.LogoutRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.NewGroupRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.RegisterRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.RemoveFromRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.Request;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.SearchByTagsRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.SearchByTitleRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.ApplicationException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchBookmarkException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.ResponseParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WebpageFetchException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.server.ClientRequestHandler;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.storage.BookmarksStorage;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;

/**
 * RequestHandlerTest
 */
public class RequestHandlerTest {

    @Mock
    private ClientRequestHandler clientRequestHandlerMock = Mockito.mock(ClientRequestHandler.class);

    @Mock
    private UserManager userManagerMock = Mockito.mock(UserManager.class);

    @Mock
    private BookmarksStorage bookmarksStorageMock = Mockito.mock(BookmarksStorage.class);

    @Test
    public void testRequestHandlerParseCommand() throws CommandParseException {

        Map<String, ? extends Request> map = new HashMap<>() {
            {
                put("register user password",
                        new RegisterRequest("user", "password"));
                put("login user password",
                        new LoginRequest("user", "password"));
                put("logout",
                        new LogoutRequest());
                put("new-group def",
                        new NewGroupRequest("def"));
                put("add-to def https://www.gitlab.com",
                        new AddToRequest("def", "https://www.gitlab.com", false));
                put("add-to def https://www.gitlab.com --shorten",
                        new AddToRequest("def", "https://www.gitlab.com", true));
                put("remove-from def https://www.gitlab.com",
                        new RemoveFromRequest("def", "https://www.gitlab.com"));
                put("list",
                        new ListRequest());
                put("list --group-name kek",
                        new ListByGroupRequest("kek"));
                put("search --tags tag1 tag2",
                        new SearchByTagsRequest(Set.of("tag1", "tag2")));
                put("search --title kek",
                        new SearchByTitleRequest("kek"));
                put("cleanup",
                        new CleanupRequest());
                put("import-from-chrome",
                        new ImportFromChromeRequest());
            }
        };

        map.entrySet().forEach((entry) -> {
            try {
                assertEquals(
                        entry.getValue().getClass(),
                        RequestHandler.parseCommand(entry.getKey()).getClass(),
                        String.format(
                                "Command '%s' should be parsed correctly",
                                entry.getValue().getClass().getName()));
            } catch (CommandParseException e) {
                assertTrue(
                        false,
                        String.format(
                                "Command '%s' should be parsed correctly",
                                entry.getValue().getClass().getName()));
            }
        });
    }

    @Test
    public void testRequestHandlerParseJson() {
        Map<String, Class<? extends Request>> map = new HashMap<>() {
            {
                put("REGISTER", RegisterRequest.class);
                put("LOGIN", LoginRequest.class);
                put("LOGOUT", LogoutRequest.class);
                put("NEW_GROUP", NewGroupRequest.class);
                put("ADD_TO", AddToRequest.class);
                put("REMOVE_FROM", RemoveFromRequest.class);
                put("LIST", ListRequest.class);
                put("LIST_BY_GROUP", ListByGroupRequest.class);
                put("SEARCH_BY_TAGS", SearchByTagsRequest.class);
                put("SEARCH_BY_TITLE", SearchByTitleRequest.class);
                put("CLEANUP", CleanupRequest.class);
                put("IMPORT_FROM_CHROME", ImportFromChromeRequest.class);
            }
        };

        map.entrySet().stream()
                .forEach((entry) -> {
                    try {
                        assertTrue(
                                entry.getValue().isInstance(
                                        RequestHandler.parseJson(
                                                String.format("{ \"type\": \"%s\" }", entry.getKey()))),
                                String.format(
                                        "JSON request %s should be identified correctly",
                                        entry.getValue().getName()));
                    } catch (ResponseParseException e) {
                        assertTrue(
                                false,
                                String.format(
                                        "JSON request %s should be identified correctly",
                                        entry.getValue().getName()));
                    }
                });
    }

    @Test
    public void testRequestHandlerExecuteRequestNotLoggedIn()
            throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        doNothing().when(userManagerMock).addUser(any(User.class));
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));

        List<Request> notLoggedInRequests = List.of(
                new RegisterRequest("", ""),
                new LoginRequest("", ""));

        notLoggedInRequests.forEach((request) -> {
            assertTrue(RequestHandler
                    .executeRequest(request, clientRequestHandlerMock)
                    .contains("already logged"),
                    String.format(
                            "Command %s shouldn't be executed when user is logged in",
                            request.getClass().getName()));
        });
    }

    @Test
    public void testRequestHandlerExecuteRequestLoggedIn()
            throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        doNothing().when(userManagerMock).addUser(any(User.class));
        when(clientRequestHandlerMock.getUser()).thenReturn(null);

        List<Request> loggedInRequests = List.of(
                new LogoutRequest(),
                new NewGroupRequest(""),
                new AddToRequest("", "", false),
                new RemoveFromRequest("", ""),
                new ListRequest(),
                new ListByGroupRequest(""),
                new SearchByTagsRequest(Collections.emptySet()),
                new SearchByTitleRequest(""),
                new CleanupRequest());

        loggedInRequests.forEach((request) -> {
            assertTrue(RequestHandler
                    .executeRequest(request, clientRequestHandlerMock)
                    .contains("need to log in"),
                    String.format(
                            "Command %s shouldn't be executed when user is not logged in",
                            request.getClass().getName()));
        });
    }

    @Test
    public void testRequestHandlerExecuteRequestRegisterSuccess()
            throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        doNothing().when(userManagerMock).addUser(any(User.class));

        assertDoesNotThrow(() -> {
            RequestHandler.executeRequest(new RegisterRequest("user", "password"), clientRequestHandlerMock);
        });

        verify(userManagerMock, atLeastOnce()).addUser(any(User.class));
    }

    @Test
    public void testRequestHandlerExecuteRequestRegisterFailure()
            throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        doThrow(new DuplicateUserException("User already exists")).when(userManagerMock).addUser(any(User.class));

        assertTrue(
                RequestHandler
                        .executeRequest(
                                new RegisterRequest("user", "password"),
                                clientRequestHandlerMock)
                        .contains("already exists"),
                "Duplicate User should be caught");

        verify(userManagerMock, atLeastOnce()).addUser(any(User.class));
    }

    @Test
    public void testRequestHandlerExecuteRequestLoginSuccess()
            throws ApplicationException {
        doNothing().when(clientRequestHandlerMock).setUser(null);
        when(clientRequestHandlerMock.getUser()).thenReturn(null);
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(userManagerMock.getUser(anyString(), anyString())).thenReturn(null);

        assertDoesNotThrow(() -> {
            RequestHandler.executeRequest(new LoginRequest("user", "password"), clientRequestHandlerMock);
        });

        verify(clientRequestHandlerMock, atLeastOnce()).setUser(null);
    }

    @Test
    public void testRequestHandlerExecuteRequestLoginFailure()
            throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        doThrow(new NoSuchUserException("There is no such user")).when(userManagerMock).getUser(anyString(),
                anyString());

        assertTrue(
                RequestHandler
                        .executeRequest(
                                new LoginRequest("user", "password"),
                                clientRequestHandlerMock)
                        .contains("no such"),
                "Missing user should be caught");
    }

    @Test
    public void testRequestHandlerExecuteRequestLogoutSuccess() {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        doNothing().when(clientRequestHandlerMock).setUser(null);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));

        assertDoesNotThrow(() -> {
            RequestHandler.executeRequest(new LogoutRequest(), clientRequestHandlerMock);
        });

        verify(clientRequestHandlerMock, atLeastOnce()).setUser(null);
    }

    @Test
    public void testRequestHandlerExecuteRequestNewGroupSuccess() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString())).thenReturn(bookmarksStorageMock);
        doNothing().when(bookmarksStorageMock).addGroup(anyString());

        assertDoesNotThrow(() -> {
            RequestHandler.executeRequest(new NewGroupRequest(""), clientRequestHandlerMock);
        });

        verify(bookmarksStorageMock, atLeastOnce()).addGroup(anyString());
    }

    @Test
    public void testRequestHandlerExecuteRequestNewGroupFailure() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        doThrow(new NoSuchUserException("There is no such user"))
                .when(userManagerMock).getUserBookmarksStorage(anyString());

        assertTrue(
                RequestHandler.executeRequest(new NewGroupRequest(""), clientRequestHandlerMock).contains("no such"),
                "Missing user should be caught");
    }

    @Test
    public void testRequestHandlerExecuteRequestAddToSuccess() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString())).thenReturn(bookmarksStorageMock);
        doNothing().when(bookmarksStorageMock).addBookmark(anyString(), anyString(), any(boolean.class));

        assertTrue(
                RequestHandler.executeRequest(new AddToRequest("", "", false), clientRequestHandlerMock)
                        .contains("Success"),
                "Bookmark should be added successfully");
    }

    @Test
    public void testRequestHandlerExecuteRequestAddToFailure() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString())).thenReturn(bookmarksStorageMock);
        doThrow(new WebpageFetchException("Webpage fetch error")).when(bookmarksStorageMock).addBookmark(anyString(),
                anyString(), any(boolean.class));

        assertTrue(
                RequestHandler.executeRequest(new AddToRequest("", "", false), clientRequestHandlerMock)
                        .contains("fetch error"),
                "Bookmark should not be added when there is an error");
    }

    @Test
    public void testRequestHandlerExecuteRequestRemoveFromSuccess() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString())).thenReturn(bookmarksStorageMock);
        doNothing().when(bookmarksStorageMock).removeBookmark(anyString(), anyString());

        assertTrue(
                RequestHandler.executeRequest(new RemoveFromRequest("", ""), clientRequestHandlerMock)
                        .contains("Success"),
                "Bookmark should be removed successfully");
    }

    @Test
    public void testRequestHandlerExecuteRequestRemoveFromFailure() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString())).thenReturn(bookmarksStorageMock);
        doThrow(new NoSuchBookmarkException("There is no such bookmark"))
                .when(bookmarksStorageMock).removeBookmark(anyString(), anyString());

        assertTrue(
                RequestHandler.executeRequest(new RemoveFromRequest("", ""), clientRequestHandlerMock)
                        .contains("no such bookmark"),
                "Bookmark should not be removed when there is an error");
    }

    @Test
    public void testRequestHandlerExecuteRequestListSuccess() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString())).thenReturn(bookmarksStorageMock);
        when(bookmarksStorageMock.listBookmarks()).thenReturn(Collections.emptySet());

        assertTrue(
                RequestHandler.executeRequest(new ListRequest(), clientRequestHandlerMock)
                        .contains("Success"),
                "There should be a list when there is no error");
    }

    @Test
    public void testRequestHandlerExecuteRequestListFailure() throws ApplicationException {
        when(clientRequestHandlerMock.getUserManager()).thenReturn(userManagerMock);
        when(clientRequestHandlerMock.getUser()).thenReturn(new User("", "", ""));
        when(userManagerMock.getUserBookmarksStorage(anyString()))
                .thenThrow(new NoSuchUserException("There is no such user"));

        assertTrue(
                RequestHandler.executeRequest(new ListRequest(), clientRequestHandlerMock)
                        .contains("no such"),
                "There should be no list when there is an error");
    }
}
