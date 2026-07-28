package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.Tweet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetDao {

    private BookmarkDao bookmarkDao = new BookmarkDao();
    private MediaDao mediaDao = new MediaDao();
    private HashtagDao hashtagDao = new HashtagDao();
    private TweetHashtagDao tweetHashtagDao = new TweetHashtagDao();

    public int createTweet(int userId, String content) {
        String sql = """
                INSERT INTO tweets (user_id, content)
                VALUES (?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, content);

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int tweetId = resultSet.getInt(1);
                saveHashtags(tweetId, content);
                return tweetId;
            }

        } catch (SQLException e) {
            System.out.println("Create tweet error: " + e.getMessage());
        }
        return -1;
    }

    public List<Tweet> getAllTweets(int currentUserId) {
        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                
                    (SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS like_count,
                    FALSE AS is_pinned,
                    EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t 
                JOIN users u ON t.user_id = u.id
                WHERE t.reply_to_id IS NULL
                ORDER BY t.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tweets.add(mapTweet(resultSet, currentUserId));
            }

        } catch (SQLException e) {
            System.out.println("Get tweets error: " + e.getMessage());
        }

        return tweets;
    }

    public List<Tweet> getTweetsByHashtag(String hashtag, int currentUserId) {
        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                
                    (SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS like_count,
                    FALSE AS is_pinned,
                    EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id                
                JOIN tweet_hashtags th ON th.tweet_id = t.id              
                JOIN hashtags h ON h.id = th.hashtag_id                
                
                WHERE h.name = ?
                
                ORDER BY t.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setString(3, hashtag.toLowerCase());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tweets.add(mapTweet(resultSet, currentUserId));
            }

        } catch (SQLException e) {
            System.out.println("Get tweets error: " + e.getMessage());
        }

        return tweets;
    }


    public List<Tweet> searchTweets(String keyword, int currentUserId) {
        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                
                    (SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS like_count,
                    FALSE AS is_pinned,
                    EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
                WHERE t.content ILIKE ?
                ORDER BY t.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setString(3,"%" + keyword + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tweets.add(mapTweet(resultSet, currentUserId));
            }

        } catch (SQLException e) {
            System.out.println("Get tweets error: " + e.getMessage());
        }

        return tweets;
    }

    public List<Tweet> getTweetsByUserId(int userId, int currentUserId) {
        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                    
                    (SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS like_count,
                    (t.id = u.pinned_tweet_id) AS is_pinned,
                    EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
                WHERE t.user_id = ? AND t.reply_to_id IS NULL
                ORDER BY
                    (t.id = u.pinned_tweet_id) DESC,
                    t.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tweets.add(mapTweet(resultSet, currentUserId));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return tweets;
    }

    public boolean deleteTweet(int tweetId, int userId) {
        String sql = """
                DELETE FROM tweets
                WHERE id = ? AND user_id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, tweetId);
            statement.setInt(2, userId);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Delete tweet error: " + e.getMessage());
            return false;
        }
    }

    public boolean editTweet(int tweetId, int userId, String content) {
        String sql = """
                UPDATE tweets
                SET content = ?
                WHERE id = ? AND user_id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, content);
            statement.setInt(2, tweetId);
            statement.setInt(3, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Edit tweet error: " + e.getMessage());
            return false;
        }
    }

    public List<Tweet> getLikedTweets(int userId, int currentUserId) {

        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                
                    (SELECT COUNT(*) FROM likes WHERE tweet_id = t.id) AS like_count,
                    FALSE AS is_pinned,
                     EXISTS (SELECT 1 FROM likes WHERE tweet_id = t.id AND user_id = ?) AS is_liked,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
                JOIN likes l ON t.id = l.tweet_id
                WHERE l.user_id = ?
                ORDER BY l.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tweets.add(mapTweet(resultSet, currentUserId));
                }

                return tweets;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<Tweet> getBookmarkedTweets(int userId, int currentUserId) {

        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                
                (SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.id) AS like_count,
                FALSE AS is_pinned,
                EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked,
                (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                TRUE AS is_bookmarked_by_user,
                (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
                JOIN bookmarks b ON t.id = b.tweet_id
                WHERE b.user_id = ?
                ORDER BY b.created_at DESC
                """;

        List<Tweet> tweets = new ArrayList<>();

        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, userId);

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    tweets.add(mapTweet(resultSet, currentUserId));
                }

                return tweets;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public Tweet retweet(int userId, int tweetId) {

        if(!tweetExists(tweetId)) return null;

        String sql = """
                INSERT INTO tweets (user_id, content, retweet_of_id)
                VALUES (?, NULL, ?)
                RETURNING id;         
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            try (ResultSet resultSet = statement.executeQuery()) {
                
                if (resultSet.next()) {
                    return getTweetById(resultSet.getInt("id"), userId);
                }
            }

        } catch (SQLException e) {
            System.out.println("Retweet error: " + e.getMessage());
        }
        return null;
    }

    public boolean tweetExists(int tweetId) {
        String sql = """
                SELECT id FROM tweets WHERE id = ?
                """;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, tweetId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.out.println("error finding tweet: " + e.getMessage());
            return false;
        }
    }

    public boolean isRetweet(int tweetId) {

        String sql = "SELECT retweet_of_id FROM tweets WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, tweetId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getObject("retweet_of_id") != null;
            }
        } catch (SQLException e) {
            System.out.println("Error checking retweet: " + e.getMessage());
        }
        return false;
    }

    public int getRetweetCount(int tweetId) {
        String sql = """
            SELECT COUNT(*)
            FROM tweets
            WHERE retweet_of_id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, tweetId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Get retweet count error: " + e.getMessage());
        }

        return 0;
    }

    public Tweet getTweetById(int tweetId, int currentUserId) {

        String sql = """
            SELECT t.*, 
                   u.display_name, 
                   u.username,
                   u.profile_image_url,
                   (SELECT COUNT(*) FROM likes WHERE tweet_id = t.id) AS like_count,
                   FALSE AS is_pinned,
                   (SELECT COUNT(*) > 0 FROM likes WHERE tweet_id = t.id AND user_id = ?) AS is_liked,
                   EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                   (SELECT COUNT(*) FROM tweets WHERE retweet_of_id = t.id) AS retweet_count,
                   (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
            FROM tweets t 
            JOIN users u ON t.user_id = u.id 
            WHERE t.id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, tweetId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapTweet(resultSet, currentUserId);
                }
            }
        } catch (SQLException e) {
            System.out.println("error finding tweet: " + e.getMessage());
        }
        return null;
    }

    public boolean unretweet(int userId, int rootTweetId) {

        String sql = """
                DELETE FROM tweets
                WHERE retweet_of_id = ? AND user_id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, rootTweetId);
            statement.setInt(2, userId);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("unretweet error: " + e.getMessage());
            return false;
        }
    }

    public boolean addReply(int userId, int tweetId, String content) {

        if(!tweetExists(tweetId)) return false;
        if(content == null || content.trim().isEmpty() || content.length() > 280) return false;

        String sql = """
                INSERT INTO tweets (user_id, content, reply_to_id)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, content);
            statement.setInt(3, tweetId);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Reply error: " + e.getMessage());
            return false;
        }
    }

    public List<Tweet> getTweetReplies(int tweetId , int currentUserId) {

        String sql = """
        SELECT t.*,
                u.display_name,
                u.username,
                u.profile_image_url,
                (SELECT COUNT(*) FROM likes WHERE tweet_id = t.id) AS like_count,
                FALSE AS is_pinned,
                (SELECT COUNT(*) > 0 FROM likes WHERE tweet_id = t.id AND user_id = ?) AS is_liked,
                EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                (SELECT COUNT(*) FROM tweets WHERE retweet_of_id = t.id) AS retweet_count,
                (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
        FROM tweets t
        JOIN users u ON t.user_id = u.id
        WHERE t.reply_to_id = ?
        ORDER BY t.created_at DESC
        """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, tweetId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tweets.add(mapTweet(resultSet, currentUserId));
                }
                return tweets;
            }
        } catch (SQLException e) {
            System.out.println("error getting tweet replies: " + e.getMessage());
        }
        return null;
    }

    public List<Tweet> getUserReplies(int userId, int currentUserId) {

        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_retweet,
                    t.retweet_of_id,
                    t.reply_to_id,
                    u.display_name,
                    u.username,
                    u.profile_image_url,
                
                    (SELECT COUNT(*) FROM likes WHERE tweet_id = t.id) AS like_count,
                    FALSE as is_pinned,
                     EXISTS (SELECT 1 FROM likes WHERE tweet_id = t.id AND user_id = ?) AS is_liked,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.reply_to_id = t.id) AS reply_count
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
                WHERE t.user_id = ? AND t.reply_to_id IS NOT NULL
                ORDER BY t.created_at DESC;
                """;

        List<Tweet> replies = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, currentUserId);
            statement.setInt(2, currentUserId);
            statement.setInt(3, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    replies.add(mapTweet(resultSet, currentUserId));
                }
                return replies;
            }
        } catch (SQLException e) {
            System.out.println("error getting user replies: " + e.getMessage());
        }
        return null;
    }

    private void saveHashtags(int tweetId, String content)
    {
        if (content == null || content.isBlank()) return;

        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find())
        {
            String hashtag = matcher.group(1).toLowerCase();
            int hashtagId = hashtagDao.createIfNotExists(hashtag);

            if (hashtagId != -1) tweetHashtagDao.addHashtagToTweet(tweetId, hashtagId);
        }
    }

    public boolean handlePinTweet(int userId, Integer tweetId) {

        if(tweetId == null) {

            String sql = """
                    UPDATE users
                    SET pinned_tweet_id = NULL
                    WHERE id = ?
                    """;

            try(
                    Connection connection = DatabaseConnection.getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql)
            ){
                statement.setInt(1, userId);
                return statement.executeUpdate() > 0;
            }
            catch (SQLException e) {
                System.out.println("unpin error " + e.getMessage());
                return false;
            }
        }

        String sql = """
                UPDATE users
                SET pinned_tweet_id = ?
                WHERE id = ?
                AND EXISTS(
                    SELECT 1 FROM tweets
                    WHERE id = ? AND user_id = ?
                )
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, tweetId);
            statement.setInt(2, userId);
            statement.setInt(3, tweetId);
            statement.setInt(4, userId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("pin tweet error: " + e.getMessage());
            return false;
        }
    }

    public Tweet mapTweet(ResultSet resultSet, int currentUserId) throws SQLException {

        Integer retweetOfId = resultSet.getObject("retweet_of_id", Integer.class);

        Tweet tweet = new Tweet(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("content"),
                resultSet.getTimestamp("created_at"),
                retweetOfId,
                resultSet.getObject("reply_to_id", Integer.class),
                resultSet.getString("display_name"),
                resultSet.getString("username"),
                resultSet.getInt("like_count"),
                resultSet.getBoolean("is_liked"),
                resultSet.getString("profile_image_url"),
                bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id")),
                resultSet.getInt("retweet_count"),
                retweetOfId != null,
                resultSet.getBoolean("is_retweeted_by_user"),
                resultSet.getInt("reply_count"),
                resultSet.getBoolean("is_pinned")
        );

        if (retweetOfId != null) {
            Tweet originalTweet = getTweetById(retweetOfId, currentUserId);
            tweet.setOriginalTweet(originalTweet);
        }

        if (tweet.isRetweet() && tweet.getOriginalTweet() != null) {
            tweet.setMedia(mediaDao.getMediaByTweetId(tweet.getOriginalTweet().getId()));
        } else {
            tweet.setMedia(mediaDao.getMediaByTweetId(tweet.getId()));
        }

        return tweet;
    }
}