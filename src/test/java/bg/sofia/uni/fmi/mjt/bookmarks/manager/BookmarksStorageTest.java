package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.BitlyException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WebpageFetchException;
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
    public void testBookmarksStorage() throws DuplicateGroupException, WebpageFetchException, BitlyException, NoSuchGroupException {
        bookmarksStorage.addGroup("GROUP 1");
        bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);

        assertEquals(1, bookmarksStorage.listBookmarks().size(),
                "There should be 1 bookmark after adding");
    }

    @Test
    public void testBookmarksStorageListByGroup() throws DuplicateGroupException, WebpageFetchException, BitlyException, NoSuchGroupException {
        bookmarksStorage.addGroup("GROUP 1");
        bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);

        assertEquals(1, bookmarksStorage.listBookmarksByGroup("GROUP 1").size(),
                "There should be 1 bookmark in GROUP 1");
        assertEquals(0, bookmarksStorage.listBookmarksByGroup("GROUP 2").size(),
                "There should be no bookmarks in GROUP 2");
    }

    @Test
    public void testBookmarksStorageSearchByTags() throws DuplicateGroupException, WebpageFetchException, BitlyException, NoSuchGroupException {
        bookmarksStorage.addGroup("GROUP 1");
        bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);

        assertEquals(1, bookmarksStorage.searchByTags(Set.of("illustrative", "example")).size(),
                "Bookmarks should pickup the correct keywords");
    }

    @Test
    public void testBookmarksStorageSearchByTitle() throws DuplicateGroupException, WebpageFetchException, BitlyException, NoSuchGroupException {
        bookmarksStorage.addGroup("GROUP 1");
        bookmarksStorage.addBookmark("GROUP 1", "file:res/example.html", false);

        assertEquals(1, bookmarksStorage.searchByTitle("Example").size(),
                "Bookmarks should pickup the correct title");
    }
}
