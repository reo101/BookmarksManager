package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import java.util.regex.Matcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateGroupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.DuplicateUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.ResponseParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.server.ClientRequestHandler;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.DefaultUser;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;

/**
 * RequestHandler
 */
public class RequestHandler {

    private static Gson GSON = new Gson();

    public static Request parseCommand(String input) throws CommandParseException {
        Type type = Type.of(input);
        Matcher matcher = type.getMatcher(input);

        matcher.find();

        // TODO: parseCommand
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
                yield new ListRequest();
            }
            case ADD_TO -> {
                yield new ListRequest();
            }
            case REMOVE_FROM -> {
                yield new ListRequest();
            }
            case LIST -> {
                yield new ListRequest();
            }
            case LIST_BY_GROUP -> {
                yield new ListRequest();
            }
            case SEARCH_BY_TAGS -> {
                yield new ListRequest();
            }
            case SEARCH_BY_TITLE -> {
                yield new ListRequest();
            }
            case CLEANUP -> {
                yield new ListRequest();
            }
            case IMPORT_FROM_CHROME -> {
                yield new ListRequest();
            }
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

        // TODO: executeRequest
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
                            new DefaultUser(
                                    registerRequest.getUsername(),
                                    registerRequest.getHashedPassword()));
                } catch (DuplicateUserException e) {
                    yield e.toString();
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
                                    loginRequest.getHashedPassword()));
                } catch (NoSuchUserException | WrongPasswordException e) {
                    yield e.toString();
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
                if (user == null) {
                    yield "Cannot add group, you need to login first";

                }

                try {
                    userManager.getUserBookmarksStorage(user)
                            .addGroup(newGroupRequest.getGroupName());
                } catch (NoSuchUserException | DuplicateGroupException e) {
                    yield e.toString();
                }

                yield String.format(
                        "Success. The group \"%s\" was added",
                        newGroupRequest.getGroupName());
            }
            case AddToRequest addToRequest -> {
                yield "Kek";
            }
            case RemoveFromRequest removeFromRequest -> {
                yield "Kek";
            }
            case ListRequest listRequest -> {
                yield "Kek";
            }
            case ListByGroupRequest listByGroupRequest -> {
                yield "Kek";
            }
            case SearchByTagsRequest searchByTagsRequest -> {
                yield "Kek";
            }
            case SearchByTitleRequest searchByTitleRequest -> {
                yield "Kek";
            }
            case CleanupRequest cleanupRequest -> {
                yield "Kek";
            }
            case ImportFromChromeRequest importFromChromeRequest -> {
                yield "Kek";
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
