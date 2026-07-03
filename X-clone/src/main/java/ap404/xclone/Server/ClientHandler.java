package ap404.xclone.Server;

import ap404.xclone.Server.Database.UserDao;
import ap404.xclone.Shared.Request;
import ap404.xclone.Shared.Response;
import ap404.xclone.Shared.ResponseType;
import ap404.xclone.Shared.SignupRequest;

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
        try {
            while (true){
                Object object = inputStream.readObject();
                if (object instanceof Request request) {
                    handleRequest(request);
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }

    public void handleRequest(Request request) throws IOException {
        switch (request.getType()) {
            case LOGIN :
                //TODO: check with database
                outputStream.writeObject(new Response(ResponseType.LOGIN_SUCCESS));
                outputStream.flush();
                break;
            case SIGNUP: {

                SignupRequest signupRequest = (SignupRequest) request.getBody();

                UserDao userDao = new UserDao();

                boolean success = userDao.signup(
                        signupRequest.getName(),
                        signupRequest.getUsername(),
                        signupRequest.getEmail(),
                        signupRequest.getPassword()
                );

                if (success) {
                    outputStream.writeObject(
                            new Response(ResponseType.SIGNUP_SUCCESS));
                } else {
                    outputStream.writeObject(
                            new Response(ResponseType.SIGNUP_FAILED));
                }

                outputStream.flush();
                break;
            }
        }
    }
}
