package ap404.xclone.Client;

import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private final int port = 5000;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client() throws IOException {

        socket = new Socket("localhost", port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

    }

    public void sendRequest(Request request) throws IOException {
        out.writeObject(request);
        out.flush();
    }

    public Response getResponse() throws IOException, ClassNotFoundException {
        return (Response) in.readObject();
    }
}
