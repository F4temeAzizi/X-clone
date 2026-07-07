package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.Tweet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TweetDao {

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

    public List<Tweet> getAllTweets() {
        String sql = """
                SELECT tweets.id,
                       tweets.user_id,
                       tweets.content,
                       tweets.created_at,
                       tweets.is_retweet,
                       tweets.retweet_of_id,
                       tweets.reply_to_id,
                       users.display_name,
                       users.username
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
                        resultSet.getString("username")
                );

                tweets.add(tweet);
            }

        } catch (SQLException e) {
            System.out.println("Get tweets error: " + e.getMessage());
        }

        return tweets;
    }

    public List<Tweet> getTweetsByUserId(int userId) {
        String sql = """
                SELECT tweets.id,
                       tweets.user_id,
                       tweets.content,
                       tweets.created_at,
                       tweets.is_retweet,
                       tweets.retweet_of_id,
                       tweets.reply_to_id,
                       users.display_name,
                       users.username
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
                            resultSet.getString("username")
                    );

                    tweets.add(tweet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Get user tweets error: " + e.getMessage());
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

    private Integer getIntegerOrNull(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }
}