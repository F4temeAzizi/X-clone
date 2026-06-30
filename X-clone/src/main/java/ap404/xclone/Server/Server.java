package ap404.xclone.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port = 5000;

    public void start() throws IOException {

        try(ServerSocket serverSocket = new ServerSocket(port)) {

            while (!serverSocket.isClosed()) {

                try {
                    Socket socket = serverSocket.accept();

                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
