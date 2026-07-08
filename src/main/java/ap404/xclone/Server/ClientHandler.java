package ap404.xclone.Server;

import ap404.xclone.Server.Database.LikeDao;
import ap404.xclone.Server.Database.UserDao;
import ap404.xclone.Shared.DTO.request.*;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.Models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import ap404.xclone.Server.Database.TweetDao;

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
            case UPDATE_PROFILE: {

                UpdateProfileRequest updateRequest = (UpdateProfileRequest) request.getBody();

                UserDao userDao = new UserDao();

                boolean success = userDao.updateProfile(
                        updateRequest.getId(),
                        updateRequest.getName(),
                        updateRequest.getUsername(),
                        updateRequest.getBio(),
                        updateRequest.getBannerImage(),
                        updateRequest.getAvatarImage()
                );

                if (success) {
                    User user = userDao.getUserById(updateRequest.getId());
                    outputStream.writeObject(new Response(ResponseType.UPDATE_PROFILE_SUCCESS, user));

                } else {
                    outputStream.writeObject(new Response(ResponseType.UPDATE_PROFILE_FAILED));
                }

                outputStream.flush();
                break;
            }
            case CREATE_TWEET: {

                CreateTweetRequest createTweetRequest = (CreateTweetRequest) request.getBody();

                TweetDao tweetDao = new TweetDao();

                boolean success = tweetDao.createTweet(
                        createTweetRequest.getUserId(),
                        createTweetRequest.getContent()
                );

                if (success) {
                    outputStream.writeObject(new Response(ResponseType.CREATE_TWEET_SUCCESS));
                } else {
                    outputStream.writeObject(new Response(ResponseType.CREATE_TWEET_FAILED));
                }

                outputStream.flush();
                break;
            }

            case GET_ALL_TWEETS: {

                TweetDao tweetDao = new TweetDao();

                outputStream.writeObject(
                        new Response(ResponseType.GET_TWEETS_SUCCESS, tweetDao.getAllTweets())
                );

                outputStream.flush();
                break;
            }

            case LIKE: {
                LikeDao likeDao = new LikeDao();
                LikeRequest likeRequest = (LikeRequest) request.getBody();

                if (likeDao.likeTweet(likeRequest.getUserId(), likeRequest.getTweetId())) {
                    outputStream.writeObject(
                            new Response(ResponseType.LIKE_SUCCESS)
                    );
                }
                else {
                    outputStream.writeObject(
                            new Response(ResponseType.LIKE_FAILED)
                    );
                }

                outputStream.flush();
                break;
            }

            case UNLIKE: {
                LikeDao likeDao = new LikeDao();
                LikeRequest likeRequest = (LikeRequest) request.getBody();

                if(likeDao.unlikeTweet(likeRequest.getUserId(), likeRequest.getTweetId())) {
                    outputStream.writeObject(
                            new Response(ResponseType.UNLIKE_SUCCESS)
                    );
                }
                else {
                    outputStream.writeObject(
                            new Response(ResponseType.UNLIKE_FAILED)
                    );
                }
                outputStream.flush();
                break;
            }
        }
    }
}
