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
                SELECT tweets.id,
                       tweets.user_id,
                       tweets.content,
                       tweets.created_at,
                       tweets.is_retweet,
                       tweets.retweet_of_id,
                       tweets.reply_to_id,
                       users.display_name,
                       users.username,
                       users.profile_image_url
                FROM tweets
                JOIN users ON tweets.user_id = users.id
                ORDER BY tweets.created_at DESC
                """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Tweet tweet = new Tweet(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getBoolean("is_retweet"),
                        getIntegerOrNull(resultSet, "retweet_of_id"),
                        getIntegerOrNull(resultSet, "reply_to_id"),
                        resultSet.getString("display_name"),
                        resultSet.getString("username"),
                        likeDao.getLikeCount(resultSet.getInt("id")),
                        likeDao.isLiked(currentUserId, resultSet.getInt("id")),
                        resultSet.getString("profile_image_url"),
                        bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id"))
                );

                tweets.add(tweet);
            }

        } catch (SQLException e) {
            System.out.println("Get tweets error: " + e.getMessage());
        }

        return tweets;
    }

    public List<Tweet> getTweetsByUserId(int userId, int currentUserId) {
        String sql = """
                SELECT tweets.id,
                       tweets.user_id,
                       tweets.content,
                       tweets.created_at,
                       tweets.is_retweet,
                       tweets.retweet_of_id,
                       tweets.reply_to_id,
                       users.display_name,
                       users.username,
                       users.profile_image_url           
                FROM tweets
                JOIN users ON tweets.user_id = users.id
                WHERE tweets.user_id = ?
                ORDER BY tweets.created_at DESC
                """;

        List<Tweet> tweets = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getBoolean("is_retweet"),
                            getIntegerOrNull(resultSet, "retweet_of_id"),
                            getIntegerOrNull(resultSet, "reply_to_id"),
                            resultSet.getString("display_name"),
                            resultSet.getString("username"),
                            likeDao.getLikeCount(resultSet.getInt("id")),
                            likeDao.isLiked(currentUserId, resultSet.getInt("id")),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id"))
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
                SELECT tweets.id,
                       tweets.user_id,
                       tweets.content,
                       tweets.created_at,
                       tweets.is_retweet,
                       tweets.retweet_of_id,
                       tweets.reply_to_id,
                       users.display_name,
                       users.username,
                       users.profile_image_url
                FROM tweets
                JOIN users ON tweets.user_id = users.id
                JOIN likes ON tweets.id = likes.tweet_id
                WHERE likes.user_id = ?
                ORDER BY likes.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getBoolean("is_retweet"),
                            getIntegerOrNull(resultSet, "retweet_of_id"),
                            getIntegerOrNull(resultSet, "reply_to_id"),
                            resultSet.getString("display_name"),
                            resultSet.getString("username"),
                            likeDao.getLikeCount(resultSet.getInt("id")),
                            likeDao.isLiked(currentUserId, resultSet.getInt("id")),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id"))
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
                SELECT tweets.id,
                       tweets.user_id,
                       tweets.content,
                       tweets.created_at,
                       tweets.is_retweet,
                       tweets.retweet_of_id,
                       tweets.reply_to_id,
                       users.display_name,
                       users.username,
                       users.profile_image_url
                FROM tweets
                JOIN users ON tweets.user_id = users.id
                JOIN bookmarks ON tweets.id = bookmarks.tweet_id
                WHERE bookmarks.user_id = ?
                ORDER BY bookmarks.created_at DESC;
                """;

        List<Tweet> tweets = new ArrayList<>();

        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getBoolean("is_retweet"),
                            getIntegerOrNull(resultSet, "retweet_of_id"),
                            getIntegerOrNull(resultSet, "reply_to_id"),
                            resultSet.getString("display_name"),
                            resultSet.getString("username"),
                            likeDao.getLikeCount(resultSet.getInt("id")),
                            likeDao.isLiked(currentUserId, resultSet.getInt("id")),
                            resultSet.getString("profile_image_url"),
                            bookmarkDao.isBookmarked(currentUserId, resultSet.getInt("id"))
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

    private Integer getIntegerOrNull(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }
}