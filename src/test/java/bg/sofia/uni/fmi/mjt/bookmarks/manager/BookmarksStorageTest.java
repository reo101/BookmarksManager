package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.storage.BookmarksStorage;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;

/**
 * BookmarksStorageTest
 */
@ExtendWith(MockitoExtension.class)
public class BookmarksStorageTest {

    @Spy
    private static HttpClient httpClientSpy;

    @Mock
    @SuppressWarnings("unchecked")
    private static HttpResponse<String> httpResponseMock = Mockito.mock(HttpResponse.class);

    private static User user;
    private static BookmarksStorage bookmarksStorage;

    @BeforeAll
    public static void init() {
        user = new User("Test", "TestPass", "TestSalt");
    }

    @BeforeEach
    public void setup() {
        bookmarksStorage = new BookmarksStorage(user);
        bookmarksStorage.setHttpClient(httpClientSpy);
    }

    @Test
    public void testBookmarksStorage() {
        assertDoesNotThrow(() -> {
            bookmarksStorage.addGroup("GROUP 1");
            bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);
        });

        assertEquals(1, bookmarksStorage.listBookmarks().size(),
                "There should be 1 bookmark after adding");
    }

    @Test
    public void testBookmarksStorageListByGroup() {
        assertDoesNotThrow(() -> {
            bookmarksStorage.addGroup("GROUP 1");
            bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);
        });

        assertEquals(1, bookmarksStorage.listBookmarksByGroup("GROUP 1").size(),
                "There should be 1 bookmark in GROUP 1");
        assertEquals(0, bookmarksStorage.listBookmarksByGroup("GROUP 2").size(),
                "There should be no bookmarks in GROUP 2");
    }

    @Test
    public void testBookmarksStorageSearchByTags() {
        assertDoesNotThrow(() -> {
            bookmarksStorage.addGroup("GROUP 1");
            bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);
        });

        assertEquals(1, bookmarksStorage.searchByTags(Set.of("illustrative", "example")).size(),
                "Bookmarks should pickup the correct keywords");
    }

    @Test
    public void testBookmarksStorageSearchByTitle() {
        assertDoesNotThrow(() -> {
            bookmarksStorage.addGroup("GROUP 1");
            bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);
        });

        assertEquals(1, bookmarksStorage.searchByTitle("Example").size(),
                "Bookmarks should pickup the correct title");
    }

    @Test
    public void testBookmarksStorageRemoveBookmark() {
        assertDoesNotThrow(() -> {
            bookmarksStorage.addGroup("GROUP 1");
            bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);
        });

        assertEquals(1, bookmarksStorage.listBookmarks().size(), "There should be 1 bookmark before removing");

        assertDoesNotThrow(() -> {
            bookmarksStorage.removeBookmark("GROUP 1", "file:res/example.html");
        });

        assertEquals(0, bookmarksStorage.listBookmarks().size(), "There should be no bookmarks after removing");
    }

    @Test
    public void testBookmarksStorageShortenLink() throws IOException, InterruptedException {
        when(httpClientSpy
                .send(any(HttpRequest.class),
                        ArgumentMatchers.<BodyHandler<String>>any()))
                                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn("{ \"link\": \"https://bit.ly/testing\" }");

        assertDoesNotThrow(() -> {
            bookmarksStorage.addGroup("test");
            bookmarksStorage.addBookmark("test", "file:res/example.html", true);
        });

        assertTrue(bookmarksStorage.listBookmarks().stream().findFirst().get().getUrl().contains("bit.ly"),
                "Shortened link should have 'bit.ly' inside of it");
        assertFalse(bookmarksStorage.listBookmarks().stream().findFirst().get().getUrl().contains("example"),
                "Shortened link should not have 'example' inside of it");
    }
}
