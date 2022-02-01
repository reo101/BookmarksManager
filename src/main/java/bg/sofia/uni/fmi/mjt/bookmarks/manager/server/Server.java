package bg.sofia.uni.fmi.mjt.bookmarks.manager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.DefaultUserManager;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;

/**
 * Server
 */
public class Server {

    private static final int SERVER_PORT = 62555;
    private static final int MAX_EXECUTOR_THREADS = 10;

    private static final UserManager userManager = new DefaultUserManager();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);) {

            System.out.println("Server started and listening for connect requests");

            Socket clientSocket = null;

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, userManager);

                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }

}
