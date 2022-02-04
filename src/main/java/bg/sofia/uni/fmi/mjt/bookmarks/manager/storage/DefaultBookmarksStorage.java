package bg.sofia.uni.fmi.mjt.bookmarks.manager.storage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.APIKeyLoadException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WebpageFetchException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;

/**
 * DefaultBookmarksStorage
 */
public class DefaultBookmarksStorage implements BookmarksStorage {
    private static final Path API_KEY_PATH = Path.of("apikey");
    private static final String API_KEY;

    static {
        // Load API_KEY
        try {
            API_KEY = Files
                    .lines(API_KEY_PATH)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException | IOException e) {
            throw new APIKeyLoadException("Couldn't load API_KEY", e);
        }
    }

    private static final String BITLY_SCHEME = "https";
    private static final String BITLY_AUTHORITY = "api-ssl.bitly.com";
    private static final String BITLY_PATH = "/v4/shorten";
    private static final String BITLY_LONG_URL = "long_url";

    private static int MAX_TAGS_PER_BOOKMARK = 25;

    private User user;
    private Map<String, Set<Bookmark>> bookmarks;

    public DefaultBookmarksStorage(User user) {
        this.user = user;
        this.bookmarks = new HashMap<>();
    }

    @Override
    public synchronized User getUser() {
        return user;
    }

    @Override
    public synchronized void addBookmark(String groupName, String url, boolean shorten)
            throws WebpageFetchException {

        Document document;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new WebpageFetchException(
                    String.format(
                            "Coudn't fetch webpage '%s'",
                            url),
                    e);
        }

        // TODO: Stopwords

        String title = document.title();
        Collection<String> tags = Arrays.stream(
                document.body()
                        .text()
                        .toLowerCase()
                        .split("[\\p{IsPunctuation}\\p{IsWhite_Space}]+"))
                // .filter((word) -> !stopwords.contains(word))
                .collect(Collectors.groupingBy((word) -> word, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(MAX_TAGS_PER_BOOKMARK)
                .collect(Collectors.toSet());

        if (shorten) {
            URI uri;
            HttpClient httpClient;
            HttpRequest httpRequest;
            HttpResponse<String> httpResponse;

            httpClient = HttpClient
                    .newBuilder()
                    .build();

            try {
                uri = new URI(
                        BITLY_SCHEME,
                        BITLY_AUTHORITY,
                        BITLY_PATH,
                        String.format(
                                "%s=%s",
                                BITLY_LONG_URL,
                                url),
                        null);
            } catch (URISyntaxException e) {
                // TODO: handle exception
                throw new RuntimeException("Couldn't create URI for request", e);
            }

            httpRequest = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .header(
                            "Authorization",
                            String.format(
                                    "Bearer %s",
                                    API_KEY))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                // TODO: handle exception
                throw new RuntimeException("Couldn't send http request to BitLy", e);
            }

            switch (httpResponse.statusCode()) {
                case HttpURLConnection.HTTP_OK -> {
                    // TODO: Map to SuccessThing
                }
                default -> {
                    // TODO: Propagate Error
                }
            }

            // TODO: Bitly
            // https://dev.bitly.com/
            // POST https://api-ssl.bitly.com/v4/shorten

            // {
            // "group_guid": "Ba1bc23dE4F",
            // "domain": "bit.ly",
            // "long_url": "https://dev.bitly.com/"
            // }
        }

        this.bookmarks
                .get(groupName)
                .add(new Bookmark(
                        title,
                        url,
                        tags));
    }

    @Override
    public synchronized void addGroup(String groupName) throws DuplicateGroupException {
        if (this.bookmarks.containsKey(groupName)) {
            throw new DuplicateGroupException(
                    String.format(
                            "There is already a group named %s",
                            groupName));
        }

        this.bookmarks.put(groupName, new HashSet<>());
    }

    @Override
    public synchronized void removeBookmark(String groupName, String bookmarkUrl)
            throws NoSuchGroupException {
        if (this.bookmarks.containsKey(groupName)) {
            throw new NoSuchGroupException(
                    String.format(
                            "There isn't a group named %s",
                            groupName));
        }

        // if (this.bookmarks.get(groupName).contains)

        // this.bookmarks
        // .get(groupName)
        // .remove(new Bookmark(bookmarkName, bookmarkUrl));

        // TODO: takovata
    }

    @Override
    public synchronized Collection<Bookmark> listBookmarks() {

        Set<Bookmark> bookmarks = new HashSet<>();

        this.bookmarks.entrySet()
            .stream()
            .map(Map.Entry<String, Set<Bookmark>>::getValue)
            .forEach(bookmarks::addAll);

        return bookmarks;
    }

    @Override
    public synchronized Collection<Bookmark> listBookmarksByGroup(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> searchByTags(Collection<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> searchByTitle(String title) {
        // TODO Auto-generated method stub
        return null;
    }
}
