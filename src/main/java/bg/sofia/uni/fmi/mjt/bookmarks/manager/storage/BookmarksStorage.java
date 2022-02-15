package bg.sofia.uni.fmi.mjt.bookmarks.manager.storage;

import java.io.File;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.APIKeyLoadException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.BitlyException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WebpageFetchException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;

/**
 * BookmarksStorage
 */
public class BookmarksStorage {
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

    private static final Gson GSON = new Gson();

    private User user;
    private Map<String, Set<Bookmark>> bookmarks;
    private transient HttpClient httpClient = HttpClient.newHttpClient();

    public BookmarksStorage(User user) {
        this.user = user;
        this.bookmarks = new HashMap<>();
    }

    public void setHttpClient(HttpClient httpClient) {
        Objects.requireNonNull(httpClient);

        this.httpClient = httpClient;
    }

    public synchronized User getUser() {
        return user;
    }

    private static class BitlyResponse {
        @SerializedName("link")
        private String shortennedUrl;

        public String getShortennedUrl() {
            return this.shortennedUrl;
        }
    }

    public synchronized void addBookmark(String groupName, String url, boolean shorten)
            throws WebpageFetchException, BitlyException, NoSuchGroupException {

        if (!this.bookmarks.containsKey(groupName)) {
            throw new NoSuchGroupException(String.format(
                    "There is no group named %s",
                    groupName));
        }

        Document document;

        try {
            if (url.startsWith("file:")) {
                document = Jsoup.parse(new File(url.substring(5)), "UTF-8");
            } else {
                document = Jsoup.connect(url).get();
            }
        } catch (IOException e) {
            throw new WebpageFetchException(
                    String.format(
                            "Coudn't fetch webpage '%s'",
                            url),
                    e);
        }

        // TODO: Stopwords
        // TODO: Stemming

        String title = document.title();
        Collection<String> tags = Arrays.stream(
                document.body()
                        .text()
                        .toLowerCase()
                        .split("[\\p{IsPunctuation}\\p{IsWhite_Space}]+"))
                // .filter((word) -> !stopwords.contains(word))
                // .map(Utilities:stem)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(MAX_TAGS_PER_BOOKMARK)
                .collect(Collectors.toSet());

        if (shorten) {
            URI uri;
            HttpRequest httpRequest;
            HttpResponse<String> httpResponse;

            try {
                uri = new URI(
                        BITLY_SCHEME,
                        BITLY_AUTHORITY,
                        BITLY_PATH,
                        // String.format(
                        // "%s=%s",
                        // BITLY_LONG_URL,
                        // url),
                        null);
            } catch (URISyntaxException e) {
                throw new WebpageFetchException("Couldn't shorten URL, please use full length", e);
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
                    .POST(HttpRequest.BodyPublishers.ofString(String.format(
                            "{ \"%s\": \"%s\" }",
                            BITLY_LONG_URL,
                            url)))
                    .build();

            try {
                httpResponse = this.httpClient.send(httpRequest, BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new BitlyException("Couldn't shorten URL, please use full length", e);
            }

            switch (httpResponse.statusCode()) {
                case HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> {
                    String shortennedUrl = GSON
                            .fromJson(httpResponse.body(), BitlyResponse.class)
                            .getShortennedUrl();

                    url = shortennedUrl;
                }
                default -> {
                    System.err.println(httpResponse.statusCode());
                    System.err.println(httpResponse.body());
                    throw new BitlyException("Couldn't shorten URL, please use full length");
                }
            }
        }

        this.bookmarks
                .get(groupName)
                .add(new Bookmark(
                        title,
                        url,
                        tags));
    }

    public synchronized void addGroup(String groupName) throws DuplicateGroupException {
        if (this.bookmarks.containsKey(groupName)) {
            throw new DuplicateGroupException(
                    String.format(
                            "There is already a group named %s",
                            groupName));
        }

        this.bookmarks.put(groupName, new HashSet<>());
    }

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

    public synchronized Collection<Bookmark> listBookmarks() {

        Set<Bookmark> bookmarks = new HashSet<>();

        this.bookmarks.entrySet()
                .stream()
                .map(Map.Entry<String, Set<Bookmark>>::getValue)
                .forEach(bookmarks::addAll);

        return bookmarks;
    }

    public synchronized Collection<Bookmark> listBookmarksByGroup(String groupName) {
        if (!this.bookmarks.containsKey(groupName)) {
            return Collections.emptySet();
        }

        return this.bookmarks.get(groupName);
    }

    public Collection<String> searchByTags(Collection<String> tags) {
        Objects.requireNonNull(tags);

        // TODO: normalize tags
        Collection<String> normalizedTags = tags.stream()
                // .opravi()
                .collect(Collectors.toSet());

        return this.listBookmarks().stream()
                .filter((bookmark) -> {
                    return tags.stream()
                            .anyMatch(bookmark.getTags()::contains);
                })
                .map(Bookmark::getUrl)
                .collect(Collectors.toSet());
    }

    public Collection<String> searchByTitle(String title) {
        Objects.requireNonNull(title);

        return this.listBookmarks().stream()
                .filter((bookmark) -> {
                    return bookmark.getTitle().contains(title);
                })
                .map(Bookmark::getUrl)
                .collect(Collectors.toSet());
    }
}
