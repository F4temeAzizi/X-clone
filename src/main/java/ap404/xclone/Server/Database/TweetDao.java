package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.Tweet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TweetDao {

    private LikeDao likeDao = new LikeDao();
    private BookmarkDao bookmarkDao = new BookmarkDao();

    public boolean createTweet(int userId, String content) {
        String sql = """
                INSERT INTO tweets (user_id, content)
                VALUES (?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, content);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Create tweet error: " + e.getMessage());
            return false;
        }
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
                    EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
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

                Integer retweetOfId = getIntegerOrNull(resultSet, "retweet_of_id");

                Tweet tweet = new Tweet(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created_at"),
                        retweetOfId,
                        getIntegerOrNull(resultSet, "reply_to_id"),
                        resultSet.getString("display_name"),
                        resultSet.getString("username"),
                        resultSet.getInt("like_count"),
                        resultSet.getBoolean("is_liked_by_user"),
                        resultSet.getString("profile_image_url"),
                        bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id")),
                        resultSet.getInt("retweet_count"),
                        retweetOfId != null,
                        resultSet.getBoolean("is_retweeted_by_user")
                );

                if (retweetOfId != null) {
                    Tweet originalTweet = getTweetById(retweetOfId, currentUserId);
                    tweet.setOriginalTweet(originalTweet);
                }

                tweets.add(tweet);
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
                    EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user
                
                FROM tweets t
                JOIN users u ON t.user_id = u.id
                WHERE t.user_id = ?
                ORDER BY t.created_at DESC;
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
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            getIntegerOrNull(resultSet, "retweet_of_id"),
                            getIntegerOrNull(resultSet, "reply_to_id"),
                            resultSet.getString("display_name"),
                            resultSet.getString("username"),
                            resultSet.getInt("like_count"),
                            resultSet.getBoolean("is_liked_by_user"),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id")),
                            resultSet.getInt("retweet_count"),
                            isRetweet(resultSet.getInt("id")),
                            resultSet.getBoolean("is_retweeted_by_user")
                    );

                    tweets.add(tweet);
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
                     EXISTS (SELECT 1 FROM likes WHERE tweet_id = t.id AND user_id = ?) AS is_liked_by_user,
                    (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                    EXISTS (SELECT 1 FROM retweets WHERE tweet_id = t.id AND user_id = ?) AS is_retweeted_by_user
                
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
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            getIntegerOrNull(resultSet, "retweet_of_id"),
                            getIntegerOrNull(resultSet, "reply_to_id"),
                            resultSet.getString("display_name"),
                            resultSet.getString("username"),
                            resultSet.getInt("like_count"),
                            resultSet.getBoolean("is_liked_by_user"),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id")),
                            resultSet.getInt("retweet_count"),
                            isRetweet(resultSet.getInt("id")),
                            resultSet.getBoolean("is_retweeted_by_user")
                    );

                    tweets.add(tweet);
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
                EXISTS (SELECT 1 FROM likes l WHERE l.tweet_id = t.id AND l.user_id = ?) AS is_liked_by_user,
                (SELECT COUNT(*) FROM tweets r WHERE r.retweet_of_id = t.id) AS retweet_count,
                EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                TRUE AS is_bookmarked_by_user
                
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
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            getIntegerOrNull(resultSet, "retweet_of_id"),
                            getIntegerOrNull(resultSet, "reply_to_id"),
                            resultSet.getString("display_name"),
                            resultSet.getString("username"),
                            resultSet.getInt("like_count"),
                            resultSet.getBoolean("is_liked_by_user"),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id")),
                            resultSet.getInt("retweet_count"),
                            isRetweet(resultSet.getInt("id")),
                            resultSet.getBoolean("is_retweeted_by_user")
                    );

                    tweets.add(tweet);
                }

                return tweets;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public boolean retweet(int userId, int tweetId) {

        if(!tweetExists(tweetId)) return false;

        String sql = """
                INSERT INTO tweets (user_id, content, retweet_of_id)
                VALUES (?, NULL, ?)               
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Retweet error: " + e.getMessage());
            return false;
        }
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
                   u.display_name AS name, 
                   u.username,
                   u.profile_image_url,
                   (SELECT COUNT(*) FROM likes WHERE tweet_id = t.id) AS like_count,
                   (SELECT COUNT(*) > 0 FROM likes WHERE tweet_id = t.id AND user_id = ?) AS is_liked,
                   EXISTS (SELECT 1 FROM tweets r WHERE r.retweet_of_id = t.id AND r.user_id = ?) AS is_retweeted_by_user,
                   (SELECT COUNT(*) FROM tweets WHERE retweet_of_id = t.id) AS retweet_count
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
                    Integer retweetOfId = resultSet.getObject("retweet_of_id", Integer.class);

                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            retweetOfId,
                            resultSet.getObject("reply_to_id", Integer.class),
                            resultSet.getString("name"),
                            resultSet.getString("username"),
                            resultSet.getInt("like_count"),
                            resultSet.getBoolean("is_liked"),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id")),
                            resultSet.getInt("retweet_count"),
                            retweetOfId != null,
                            resultSet.getBoolean("is_retweeted_by_user")

                    );

                    if (retweetOfId != null) {
                        Tweet originalTweet = getTweetById(retweetOfId, currentUserId);
                        tweet.setOriginalTweet(originalTweet);
                    }

                    return tweet;
                }
            }
        } catch (SQLException e) {
            System.out.println("error finding tweet: " + e.getMessage());
        }
        return null;
    }

    private Integer getIntegerOrNull(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }
}