package ap404.xclone.Server;

import ap404.xclone.Server.Database.UserDao;
import ap404.xclone.Shared.*;
import ap404.xclone.Shared.Models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

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

    public void handleRequest(Request request) throws IOException, SQLException {
        switch (request.getType()) {
            case LOGIN: {

                LoginRequest loginRequest = (LoginRequest) request.getBody();

                UserDao userDao = new UserDao();

                boolean success = userDao.login(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );

                if (success) {
                    User user = userDao.getUser(loginRequest.getUsername());
                    outputStream.writeObject(new Response(ResponseType.LOGIN_SUCCESS, user));
                } else {

                    outputStream.writeObject(new Response(ResponseType.LOGIN_FAILED));
                }

                outputStream.flush();
                break;
            }
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
