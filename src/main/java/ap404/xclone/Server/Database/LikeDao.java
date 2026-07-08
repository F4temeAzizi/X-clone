package ap404.xclone.Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LikeDao {

    public boolean likeTweet(int userId, int tweetId) {

        String sql = """
                INSERT INTO likes(user_id, tweet_id)
                VALUES(?, ?)
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Like error: " + e.getMessage());
            return false;
        }
    }

    public boolean unlikeTweet(int userId, int tweetId) {

        String sql = """
                DELETE FROM likes 
                WHERE user_id = ? AND tweet_id = ?
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("unlike error: " + e.getMessage());
            return false;
        }
    }
}
