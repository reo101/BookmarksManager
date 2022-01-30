package bg.sofia.uni.fmi.mjt.bookmarks.manager.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

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

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                System.out.println("Sending message <" + message + "> to the server...");

                writer.println(message);

                String reply = reader.readLine();
                System.out.println("The server replied <" + reply + ">");

                if ("disconnect".equals(message)) {
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
