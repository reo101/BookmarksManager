package bg.sofia.uni.fmi.mjt.bookmarks.manager.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.RequestHandler;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.command.requests.Request;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.CommandParseException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.utilities.Utilities;

/**
 * Client
 */
public class Client {
    private static final int SERVER_PORT = 62555;

    private static final Gson GSON = new Gson();

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            String command;
            Request request;
            String requestJson;
            String response;

            while (true) {
                System.out.print("Enter message: ");
                command = scanner.nextLine();

                if ("disconnect".equals(command)) {
                    break;
                }

                try {
                    request = RequestHandler.parseCommand(command);
                } catch (CommandParseException e) {
                    System.out.println(e.toString());
                    continue;
                }

                requestJson = GSON.toJson(request);

                writer.println(requestJson);

                // Using a newline placeholder to preserve newlines while still using readLine()
                response = reader.readLine().replaceAll(Utilities.NEWLINE_PLACEHOLDER, System.lineSeparator());
                System.out.printf("The server replied:%s%s%s",
                        System.lineSeparator(),
                        response,
                        System.lineSeparator());
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
