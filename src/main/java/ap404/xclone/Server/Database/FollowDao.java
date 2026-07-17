package ap404.xclone.Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FollowDao {

    public boolean followUser(int followerId, int followingId) {

        if (followerId == followingId) {
            return false;
        }

        String sql = """
                INSERT INTO follows(follower_id, following_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, followerId);
            statement.setInt(2, followingId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Follow error: " + e.getMessage());
            return false;
        }
    }

    public boolean unfollowUser(int followerId, int followingId) {

        String sql = """
                DELETE FROM follows
                WHERE follower_id = ? AND following_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, followerId);
            statement.setInt(2, followingId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Unfollow error: " + e.getMessage());
            return false;
        }
    }

    public boolean isFollowing(int followerId, int followingId) {

        String sql = """
                SELECT 1
                FROM follows
                WHERE follower_id = ? AND following_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, followerId);
            statement.setInt(2, followingId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            System.out.println("Check follow error: " + e.getMessage());
            return false;
        }
    }

    public int getFollowersCount(int userId) {

        String sql = """
                SELECT COUNT(*)
                FROM follows
                WHERE following_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Followers count error: " + e.getMessage());
        }

        return 0;
    }

    public int getFollowingCount(int userId) {

        String sql = """
                SELECT COUNT(*)
                FROM follows
                WHERE follower_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Following count error: " + e.getMessage());
        }

        return 0;
    }
}