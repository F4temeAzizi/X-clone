package ap404.xclone.Server;

import ap404.xclone.Shared.Request;
import ap404.xclone.Shared.RequestType;
import ap404.xclone.Shared.Response;
import ap404.xclone.Shared.ResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void run() {
        while (true){

            try {
                Object object = inputStream.readObject();
                if (object instanceof Request request) {
                    handleRequest(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRequest(Request request) throws IOException {
        switch (request.getType()) {
            case LOGIN :
                //TODO: check with database
                outputStream.writeObject(new Response(ResponseType.LOGIN_SUCCESS));
                outputStream.flush();
                break;

            //TODO
        }
    }
}
