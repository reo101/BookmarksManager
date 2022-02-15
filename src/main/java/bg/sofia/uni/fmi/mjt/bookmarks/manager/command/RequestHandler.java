package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.bookmarks.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.Command.Type;
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
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.ShutdownRequest;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.BitlyException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.ResponseParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WebpageFetchException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.server.ClientRequestHandler;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.utilities.Utilities;

/**
 * RequestHandler
 */
public class RequestHandler {

    private static Gson GSON = new Gson();

    public static Request parseCommand(String input) throws CommandParseException {
        Type type = Type.of(input);
        Matcher matcher = type.getMatcher(input);

        if (!matcher.find()) {
            throw new CommandParseException("Couldn't parse arguments to command");
        }

        return switch (type) {
            case REGISTER -> {
                String username = matcher.group(1);
                String password = matcher.group(2);

                yield new RegisterRequest(username, password);
            }
            case LOGIN -> {
                String username = matcher.group(1);
                String password = matcher.group(2);

                yield new LoginRequest(username, password);
            }
            case LOGOUT -> {
                yield new LogoutRequest();
            }
            case NEW_GROUP -> {
                String groupName = matcher.group(1);

                yield new NewGroupRequest(groupName);
            }
            case ADD_TO -> {
                String groupName = matcher.group(1);
                String url = matcher.group(2);
                boolean shorten = matcher.group(3) != null;

                yield new AddToRequest(groupName, url, shorten);
            }
            case REMOVE_FROM -> {
                String groupName = matcher.group(1);
                String url = matcher.group(2);

                yield new RemoveFromRequest(groupName, url);
            }
            case LIST -> {
                yield new ListRequest();
            }
            case LIST_BY_GROUP -> {
                String groupName = matcher.group(1);

                yield new ListByGroupRequest(groupName);
            }
            case SEARCH_BY_TAGS -> {
                Set<String> tags = Arrays.stream(
                        matcher.group(1)
                                .split("\s"))
                        .collect(Collectors.toSet());

                yield new SearchByTagsRequest(tags);
            }
            case SEARCH_BY_TITLE -> {
                String title = matcher.group(1);

                yield new SearchByTitleRequest(title);
            }
            case SHUTDOWN -> {
                yield new ShutdownRequest();
            }
            case CLEANUP -> {
                yield new CleanupRequest();
            }
            case IMPORT_FROM_CHROME -> {
                yield new ImportFromChromeRequest();
            }
            // default -> throw new CommandParseException("Couldn't get command type");
        };
    }

    public static Request parseJson(String json) throws ResponseParseException {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        return GSON.fromJson(json, switch (obj.get("type").getAsString()) {
            case null -> throw new ResponseParseException(String.format("Coudn't parse response JSON: %s", json));
            // @formatter:off
            case "REGISTER"           -> RegisterRequest.class;
            case "LOGIN"              -> LoginRequest.class;
            case "LOGOUT"             -> LogoutRequest.class;
            case "NEW_GROUP"          -> NewGroupRequest.class;
            case "ADD_TO"             -> AddToRequest.class;
            case "REMOVE_FROM"        -> RemoveFromRequest.class;
            case "LIST"               -> ListRequest.class;
            case "LIST_BY_GROUP"      -> ListByGroupRequest.class;
            case "SEARCH_BY_TAGS"     -> SearchByTagsRequest.class;
            case "SEARCH_BY_TITLE"    -> SearchByTitleRequest.class;
            case "SHUTDOWN"           -> ShutdownRequest.class;
            case "CLEANUP"            -> CleanupRequest.class;
            case "IMPORT_FROM_CHROME" -> ImportFromChromeRequest.class;
            // @formatter:on
            default -> throw new ResponseParseException(String.format("Coudn't parse response JSON: %s", json));
        });
    }

    public static String executeRequest(
            Request request,
            ClientRequestHandler clientRequestHandler) {
        User user = clientRequestHandler.getUser();
        UserManager userManager = clientRequestHandler.getUserManager();

        return switch (request) {
            case RegisterRequest registerRequest -> {
                // Already logged in
                if (user != null) {
                    yield String.format(
                            "Cannot register, you're already logged in as %s",
                            user.getUsername());
                }

                try {
                    userManager.addUser(
                            new User(
                                    registerRequest.getUsername(),
                                    registerRequest.getHashedPassword(),
                                    registerRequest.getSalt()));
                } catch (DuplicateUserException e) {
                    yield e.getMessage();
                }

                yield String.format(
                        "Success. A new account with username %s created",
                        registerRequest.getUsername());
            }
            case LoginRequest loginRequest -> {
                // Already logged in
                if (user != null) {
                    yield String.format(
                            "Cannot login, you're already logged in as %s",
                            user.getUsername());
                }

                try {
                    clientRequestHandler.setUser(
                            userManager.getUser(
                                    loginRequest.getUsername(),
                                    loginRequest.getPassword()));
                } catch (NoSuchUserException | WrongPasswordException e) {
                    yield e.getMessage();
                }

                yield String.format(
                        "Success. You have logged in as %s",
                        loginRequest.getUsername());
            }
            case LogoutRequest logoutRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot logout, you're not logged in";
                }

                clientRequestHandler.setUser(null);

                yield "Success, you've logged out";
            }
            case NewGroupRequest newGroupRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot add group, you need to log in first";
                }

                try {
                    userManager.getUserBookmarksStorage(user.getUsername())
                            .addGroup(
                                    newGroupRequest.getGroupName());
                } catch (NoSuchUserException | DuplicateGroupException e) {
                    yield e.getMessage();
                }

                yield String.format(
                        "Success. The group \"%s\" was added",
                        newGroupRequest.getGroupName());
            }
            case AddToRequest addToRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot add a bookmark, you need to log in first";
                }

                try {
                    userManager.getUserBookmarksStorage(user.getUsername())
                            .addBookmark(
                                    addToRequest.getGroupName(),
                                    addToRequest.getUrl(),
                                    addToRequest.isShorten());
                } catch (NoSuchUserException | WebpageFetchException | BitlyException | NoSuchGroupException e) {
                    yield e.getMessage();
                }

                yield String.format(
                        "Success. A bookmark with the URL '%s' was added in the %s group",
                        addToRequest.getUrl(), addToRequest.getGroupName());
            }
            case RemoveFromRequest removeFromRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot remove a bookmark, you need to log in first";
                }

                try {
                    userManager.getUserBookmarksStorage(user.getUsername())
                            .removeBookmark(
                                    removeFromRequest.getGroupName(),
                                    removeFromRequest.getUrl());
                } catch (NoSuchUserException | NoSuchGroupException e) {
                    yield e.getMessage();
                }

                yield String.format(
                        "Success. The bookmark with URL '%s' was removed from the %s group",
                        removeFromRequest.getUrl(), removeFromRequest.getGroupName());
            }
            case ListRequest listRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot list all bookmarks, you need to log in first";
                }

                String bookmarks;

                try {
                    bookmarks = userManager.getUserBookmarksStorage(user.getUsername())
                            .listBookmarks()
                            .stream()
                            .map(Bookmark::toString)
                            .collect(Collectors.joining(
                                    System.lineSeparator()));
                } catch (NoSuchUserException e) {
                    yield e.getMessage();
                }

                yield String.format("Here is the list of all bookmarks:%s%s",
                        System.lineSeparator(),
                        bookmarks)
                        .replaceAll(System.lineSeparator(), Utilities.NEWLINE_PLACEHOLDER);
            }
            case ListByGroupRequest listByGroupRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot list bookmarks by group, you need to log in first";
                }

                String bookmarks;

                try {
                    bookmarks = userManager.getUserBookmarksStorage(user.getUsername())
                            .listBookmarksByGroup(listByGroupRequest.getGroupName())
                            .stream()
                            .map(Bookmark::toString)
                            .collect(Collectors.joining(
                                    System.lineSeparator()));
                } catch (NoSuchUserException e) {
                    yield e.getMessage();
                }

                yield String.format("Here is the list of the selected bookmarks:%s%s",
                        System.lineSeparator(),
                        bookmarks)
                        .replaceAll(System.lineSeparator(), "GAGURI");
            }
            case SearchByTagsRequest searchByTagsRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot search bookmarks by tags, you need to log in first";
                }

                String urls;

                try {
                    urls = userManager.getUserBookmarksStorage(user.getUsername())
                            .searchByTags(searchByTagsRequest.getTags())
                            .stream()
                            .collect(Collectors.joining(
                                    System.lineSeparator()));
                } catch (NoSuchUserException e) {
                    yield e.getMessage();
                }

                yield String.format("Here is the list of the selected bookmarks:%s%s",
                        System.lineSeparator(),
                        urls);
            }
            case SearchByTitleRequest searchByTitleRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot search bookmarks by title, you need to log in first";
                }

                String urls;

                try {
                    urls = userManager.getUserBookmarksStorage(user.getUsername())
                            .searchByTitle(searchByTitleRequest.getTitle())
                            .stream()
                            .collect(Collectors.joining(
                                    System.lineSeparator()));
                } catch (NoSuchUserException e) {
                    yield e.getMessage();
                }

                yield String.format("Here is the list of the URLs of the selected bookmarks:%s%s",
                        System.lineSeparator(),
                        urls);
            }
            case ShutdownRequest shutdownRequest -> {
                // try {
                //     ServerTask.stop();
                // } catch (BackupException | IOException e) {
                //     e.printStackTrace();
                // }

                yield "Server shut down (NOPE)";
            }
            case CleanupRequest cleanupRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot issue a cleanup, you need to log in first";
                }

                // TODO: cleanupRequest

                yield "Not yet implemented";
            }
            case ImportFromChromeRequest importFromChromeRequest -> {
                // Not logged in
                if (user == null) {
                    yield "Cannot issue an import from chrome, you need to log in first";
                }

                // TODO: importFromChromeRequest

                yield "Not yet implemented";
            }
            default -> throw new IllegalStateException("Unexpected value: " + request);
        };
    }
}

/*
 * ArrayList<Object> result = new ArrayList<>();
 *
 * Gson g = new Gson();
 *
 * JsonArray e = new JsonParser().parse(json).getAsJsonArray();
 *
 * for(int i = 0; i < e.size(); i++){
 * JsonObject o = e.get(i).getAsJsonObject();
 * if (o.get("code") != null)
 * result.add(g.fromJson(o, Class1.class));
 * else if (o.get("id") != null)
 * result.add(g.fromJson(o, Class2.class));
 * else result.add(g.fromJson(o, Object.class));
 * }
 */
