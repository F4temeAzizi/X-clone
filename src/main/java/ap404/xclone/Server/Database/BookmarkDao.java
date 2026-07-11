package ap404.xclone.Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookmarkDao {

    public boolean bookmarkTweet(int userId, int tweetId) {

        String sql = """
                INSERT INTO bookmarks(user_id, tweet_id) 
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Bookmark error :" + e.getMessage());
            return false;
        }
    }

    public boolean unBookmarkTweet(int userId, int tweetId) {

        String sql = """
                    DELETE FROM bookmarks
                    WHERE user_id = ? AND tweet_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("unBookmark error : " + e.getMessage());
            return false;
        }
    }

    public boolean isBookmarked(int userId, int tweetId) {

        String sql = """
                    SELECT 1 FROM bookmarks
                    WHERE user_id = ? AND tweet_id = ? 
                    """;
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            System.out.println("isBookmarked error: " + e.getMessage());
            return false;
        }
    }
}
