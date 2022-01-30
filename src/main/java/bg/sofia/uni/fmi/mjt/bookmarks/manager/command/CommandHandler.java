package bg.sofia.uni.fmi.mjt.bookmarks.manager.command;

import java.util.regex.Pattern;

/**
 * CommandHandler
 */
public class CommandHandler {

    private static Pattern urlPattern = Pattern.compile(
            "\\b(?:https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    
}

// \\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]

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
