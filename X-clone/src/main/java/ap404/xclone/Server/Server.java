package ap404.xclone.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port = 5000;

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public void start() throws IOException{
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server started on port " + port);

            while (!serverSocket.isClosed()) {


                Socket socket = serverSocket.accept();
                System.out.println("new client accepted");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
    }
}
