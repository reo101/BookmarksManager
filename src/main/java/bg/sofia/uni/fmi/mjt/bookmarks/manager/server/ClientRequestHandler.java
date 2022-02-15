package bg.sofia.uni.fmi.mjt.bookmarks.manager.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.RequestHandler;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.Request;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.ResponseParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.server.Server.ServerTask;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.User;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.utilities.Utilities;

/**
 * ClientRequestHandler
 */

public class ClientRequestHandler implements Runnable {
    private Socket socket;
    private User user;
    private UserManager userManager;

    public ClientRequestHandler(Socket socket, UserManager userManager) {
        this.socket = socket;
        this.user = null;
        this.userManager = userManager;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            Request request;
            String response;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Message received from client: " + inputLine);

                try {
                    request = RequestHandler.parseJson(inputLine);
                } catch (ResponseParseException e) {
                    response = e.getMessage();
                    out.println(response);
                    continue;
                }

                if (!ServerTask.isRunning()) {
                    out.print("WARNING! Server has shut down, any further changes won't be saved!"
                            + Utilities.NEWLINE_PLACEHOLDER);
                }

                response = RequestHandler.executeRequest(request, this);
                out.println(response);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
