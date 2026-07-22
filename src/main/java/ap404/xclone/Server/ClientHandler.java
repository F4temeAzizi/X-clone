package ap404.xclone.Server;

import ap404.xclone.Server.Database.*;
import ap404.xclone.Shared.DTO.request.*;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.Models.Media;
import ap404.xclone.Shared.Models.Tweet;
import ap404.xclone.Shared.Models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import ap404.xclone.Server.Database.TweetDao;
import ap404.xclone.Server.Database.FollowDao;

public class ClientHandler implements Runnable {

    private Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private TweetDao tweetDao;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        tweetDao = new TweetDao();
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

    public void handleRequest(Request request) throws IOException{
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
                MediaDao mediaDao = new MediaDao();

                int id = tweetDao.createTweet(
                        createTweetRequest.getUserId(),
                        createTweetRequest.getContent()
                );

                if (id != -1) {
                    for (Media media : createTweetRequest.getMedia()) {
                        mediaDao.addMedia(id, media.getMediaUrl(), media.getMediaType(), media.getMediaOrder());
                    }
                    outputStream.writeObject(new Response(ResponseType.CREATE_TWEET_SUCCESS));
                } else {
                    outputStream.writeObject(new Response(ResponseType.CREATE_TWEET_FAILED));
                }

                outputStream.flush();
                break;
            }

            case GET_ALL_TWEETS: {

                TweetDao tweetDao = new TweetDao();
                int currentUserId = (Integer) request.getBody();

                outputStream.writeObject(
                        new Response(ResponseType.GET_TWEETS_SUCCESS, tweetDao.getAllTweets(currentUserId))
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

                if (likeDao.unlikeTweet(likeRequest.getUserId(), likeRequest.getTweetId())) {
                    outputStream.writeObject(
                            new Response(ResponseType.UNLIKE_SUCCESS)
                    );
                } else {
                    outputStream.writeObject(
                            new Response(ResponseType.UNLIKE_FAILED)
                    );
                }
                outputStream.flush();
                break;
            }

            case GET_USER_BY_ID: {

                GetUserByIdRequest getUserByIdRequest = (GetUserByIdRequest) request.getBody();

                UserDao userDao = new UserDao();

               User user = userDao.getUserById(getUserByIdRequest.getUserId());

               if (user != null) outputStream.writeObject(new Response(ResponseType.GET_USER_BY_ID_SUCCESS, user));
               else outputStream.writeObject(new Response(ResponseType.GET_USER_BY_ID_FAILED));

               outputStream.flush();
               break;
            }

            case GET_TWEETS_BY_USER: {

                GetProfileTweetsRequest getProfileTweetsRequest = (GetProfileTweetsRequest) request.getBody();

                TweetDao tweetDao = new TweetDao();


                List<Tweet> tweets = tweetDao.getTweetsByUserId(getProfileTweetsRequest.getUserId(),
                                                                getProfileTweetsRequest.getCurrentUserId());

                if (tweets != null)
                {
                    outputStream.writeObject(new Response(ResponseType.GET_TWEETS_BY_USER_SUCCESS, tweets));
                }
                else
                {
                    outputStream.writeObject(new Response(ResponseType.GET_TWEETS_BY_USER_FAILED));
                }

                outputStream.flush();
                break;
            }

            case DELETE_TWEET: {

                DeleteTweetRequest deleteTweetRequest = (DeleteTweetRequest) request.getBody();

                TweetDao tweetDao = new TweetDao();

                boolean success = tweetDao.deleteTweet(deleteTweetRequest.getTweetId(), deleteTweetRequest.getUserId());

                if (success)
                {
                    outputStream.writeObject(new Response(ResponseType.DELETE_TWEET_SUCCESS));
                }
                else
                {
                    outputStream.writeObject(new Response(ResponseType.DELETE_TWEET_FAILED));
                }

                outputStream.flush();
                break;
            }

            case EDIT_TWEET: {

                EditTweetRequest editTweetRequest = (EditTweetRequest) request.getBody();

                TweetDao tweetDao = new TweetDao();
                MediaDao mediaDao = new MediaDao();

                boolean success = tweetDao.editTweet(editTweetRequest.getTweetId(),
                        editTweetRequest.getUserId(), editTweetRequest.getContent());

                if (success)
                {
                    mediaDao.deleteMediaByTweetId(editTweetRequest.getTweetId());

                    for (int i = 0; i < editTweetRequest.getMedia().size(); i++)
                    {
                        Media media = editTweetRequest.getMedia().get(i);
                        mediaDao.addMedia(editTweetRequest.getTweetId(), media.getMediaUrl(),
                                media.getMediaType(), i);
                    }
                    outputStream.writeObject(new Response(ResponseType.EDIT_TWEET_SUCCESS));
                }
                else
                {
                    outputStream.writeObject(new Response(ResponseType.EDIT_TWEET_FAILED));
                }

                outputStream.flush();
                break;
            }

            case GET_LIKED_TWEETS: {

                try {
                    TweetDao tweetDao = new TweetDao();
                    GetProfileTweetsRequest getProfileTweetsRequest = (GetProfileTweetsRequest) request.getBody();

                    List<Tweet> tweets = tweetDao.getLikedTweets(getProfileTweetsRequest.getUserId(), getProfileTweetsRequest.getCurrentUserId());
                    outputStream.writeObject(new Response(ResponseType.GET_LIKED_TWEETS_SUCCESS, tweets));

                } catch (RuntimeException e) {
                    outputStream.writeObject(new Response(ResponseType.GET_LIKED_TWEETS_FAILED));
                }

                outputStream.flush();
                break;
            }

            case BOOKMARK: {

                BookmarkDao bookmarkDao = new BookmarkDao();
                BookmarkRequest bookmarkRequest = (BookmarkRequest) request.getBody();

                if (bookmarkDao.bookmarkTweet(bookmarkRequest.getUserId(), bookmarkRequest.getTweetId()))
                {
                    outputStream.writeObject(new Response(ResponseType.BOOKMARK_SUCCESS));
                }
                else
                {
                    outputStream.writeObject(new Response(ResponseType.BOOKMARK_FAILED));
                }

                outputStream.flush();
                break;
            }

            case UNBOOKMARK: {

                BookmarkDao bookmarkDao = new BookmarkDao();
                BookmarkRequest bookmarkRequest = (BookmarkRequest) request.getBody();

                boolean success = bookmarkDao.unBookmarkTweet(bookmarkRequest.getUserId(), bookmarkRequest.getTweetId());

                if (success)
                {
                    outputStream.writeObject(new Response(ResponseType.UNBOOKMARK_SUCCESS));
                }
                else
                {
                    outputStream.writeObject(new Response(ResponseType.UNBOOKMARK_FAILED));
                }

                outputStream.flush();
                break;
            }

            case GET_BOOKMARKED_TWEETS: {

                try
                {
                    TweetDao tweetDao = new TweetDao();
                    GetProfileTweetsRequest getProfileTweetsRequest = (GetProfileTweetsRequest) request.getBody();

                    List<Tweet> tweets = tweetDao.getBookmarkedTweets(getProfileTweetsRequest.getUserId(),
                            getProfileTweetsRequest.getCurrentUserId());

                    outputStream.writeObject(new Response(ResponseType.GET_BOOKMARKED_TWEETS_SUCCESS, tweets));
                }
                catch (RuntimeException e)
                {
                    outputStream.writeObject(new Response(ResponseType.GET_BOOKMARKED_TWEETS_FAILED));
                }

                outputStream.flush();
                break;
            }

            case RETWEET: {

                RetweetRequest retweetRequest = (RetweetRequest) request.getBody();

                boolean retweeted = tweetDao.retweet(retweetRequest.getUserId(), retweetRequest.getTweetId());

                if(retweeted)
                    outputStream.writeObject(new Response(ResponseType.RETWEET_SUCCESS));
                else
                    outputStream.writeObject(new Response(ResponseType.RETWEET_FAILED));

                outputStream.flush();
                break;
            }

            case UNRETWEET: {

                UnretweetRequest unretweetRequest = (UnretweetRequest) request.getBody();

                boolean unretweet = tweetDao.unretweet(unretweetRequest.getUserId(), unretweetRequest.getRootTweetId());

                if(unretweet)
                    outputStream.writeObject(new Response(ResponseType.UNRETWEET_SUCCESS));
                else
                    outputStream.writeObject(new Response(ResponseType.UNRETWEET_FAILED));

                outputStream.flush();
                break;
            }
            case FOLLOW: {

                FollowRequest followRequest = (FollowRequest) request.getBody();

                FollowDao followDao = new FollowDao();

                boolean success = followDao.followUser(
                        followRequest.getFollowerId(),
                        followRequest.getFollowingId()
                );

                if (success) {
                    outputStream.writeObject(
                            new Response(ResponseType.FOLLOW_SUCCESS)
                    );
                } else {
                    outputStream.writeObject(
                            new Response(ResponseType.FOLLOW_FAILED)
                    );
                }

                outputStream.flush();
                break;
            }

            case UNFOLLOW: {

                FollowRequest followRequest = (FollowRequest) request.getBody();

                FollowDao followDao = new FollowDao();

                boolean success = followDao.unfollowUser(
                        followRequest.getFollowerId(),
                        followRequest.getFollowingId()
                );

                if (success) {
                    outputStream.writeObject(
                            new Response(ResponseType.UNFOLLOW_SUCCESS)
                    );
                } else {
                    outputStream.writeObject(
                            new Response(ResponseType.UNFOLLOW_FAILED)
                    );
                }

                outputStream.flush();
                break;
            }

            case CHECK_FOLLOW: {

                FollowRequest followRequest = (FollowRequest) request.getBody();

                FollowDao followDao = new FollowDao();

                boolean isFollowing = followDao.isFollowing(
                        followRequest.getFollowerId(),
                        followRequest.getFollowingId()
                );

                outputStream.writeObject(
                        new Response(
                                ResponseType.CHECK_FOLLOW_SUCCESS,
                                isFollowing
                        )
                );

                outputStream.flush();
                break;
            }

            case REPLY: {

                ReplyRequest replyRequest = (ReplyRequest) request.getBody();

                boolean replied = tweetDao.addReply(replyRequest.getUserId(), replyRequest.getTweetId(), replyRequest.getContent());

                if(replied)
                    outputStream.writeObject(new Response(ResponseType.REPLY_SUCCESS));
                else
                    outputStream.writeObject(new Response(ResponseType.REPLY_FAILED));

                outputStream.flush();
                break;
            }

            case GET_TWEET_REPLIES: {

                GetTweetRepliesRequest getTweetRepliesRequest = (GetTweetRepliesRequest) request.getBody();

                List<Tweet> tweets = tweetDao.getTweetReplies(getTweetRepliesRequest.getTweetId(), getTweetRepliesRequest.getCurrentUserId());

                if(tweets != null) {
                    outputStream.writeObject(new Response(ResponseType.GET_TWEET_REPLIES_SUCCESS, tweets));
                }
                else outputStream.writeObject(new Response(ResponseType.GET_TWEET_REPLIES_FAILED));

                outputStream.flush();
                break;
            }

            case GET_USER_REPLIES: {

                GetProfileTweetsRequest getProfileTweetsRequest = (GetProfileTweetsRequest) request.getBody();

                List<Tweet> tweets = tweetDao.getUserReplies(getProfileTweetsRequest.getUserId(), getProfileTweetsRequest.getCurrentUserId());

                if(tweets != null) {
                    outputStream.writeObject(new Response(ResponseType.GET_USER_REPLIES_SUCCESS, tweets));
                }
                else outputStream.writeObject(new Response(ResponseType.GET_TWEET_REPLIES_FAILED));

                outputStream.flush();
                break;
            }
        }
    }
}
