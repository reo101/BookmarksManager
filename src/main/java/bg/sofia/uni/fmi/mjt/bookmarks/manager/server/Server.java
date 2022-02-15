package bg.sofia.uni.fmi.mjt.bookmarks.manager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.backup.Backup;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.BackupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.DefaultUserManager;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;

/**
 * Server
 */
public class Server {
    private static final int SERVER_PORT = 62555;
    private static final int MAX_EXECUTOR_THREADS = 10;

    private static UserManager userManager;

    static {
        try {
            userManager = Backup.load(Path.of("bookmarks/usermanager.backup.json"));
        } catch (BackupException e) {
            userManager = new DefaultUserManager();
        }
    }

    public static void main(String[] args) throws BackupException, InterruptedException, IOException {
        Scanner in = new Scanner(System.in);

        Runnable server = new ServerTask();
        Thread serverThread = new Thread(server);

        serverThread.start();

        while (!in.nextLine().trim().equalsIgnoreCase("stop")) {
        }

        ServerTask.stop();

        in.close();
    }

    public static class ServerTask implements Runnable {
        protected static ServerSocket serverSocket = null;

        public static boolean running = true;

        public static void stop() throws IOException, BackupException {
            running = false;

            try (Socket dummy = new Socket("localhost", SERVER_PORT);) {
            }

            Backup.backup((DefaultUserManager) userManager);
        }

        public static boolean isRunning() {
            return running;
        }

        @Override
        public void run() {
            ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

            try {
                serverSocket = new ServerSocket(SERVER_PORT);

                System.out.println("Server started and listening for connect requests");

                Socket clientSocket = null;

                while (ServerTask.isRunning()) {
                    clientSocket = serverSocket.accept();

                    if (ServerTask.isRunning()) {
                        System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());
                    }

                    ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, userManager);

                    executor.execute(clientHandler);
                }
            } catch (IOException e) {
                if (ServerTask.isRunning()) {
                    throw new RuntimeException("There is a problem with the server socket", e);
                }
            } finally {
                try {
                    if (!serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                executor.shutdownNow();
            }
        }
    }
}
