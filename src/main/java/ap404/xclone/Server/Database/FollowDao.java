package ap404.xclone.Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ap404.xclone.Shared.Models.User;
import java.util.ArrayList;
import java.util.List;


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
    public List<User> getFollowers(int userId)
    {
        List<User> followers = new ArrayList<>();

        String sql = """
            SELECT users.*
            FROM follows
            JOIN users
              ON follows.follower_id = users.id
            WHERE follows.following_id = ?
            ORDER BY follows.created_at DESC
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();

                PreparedStatement statement = connection.prepareStatement(sql)
        )
        {
            statement.setInt(1, userId);

            try (
                    ResultSet resultSet = statement.executeQuery()
            )
            {
                while (resultSet.next())
                {
                    followers.add(mapResultSetToUser(resultSet));
                }
            }
        }
        catch (SQLException e)
        {
            System.err.println("Get followers error: " + e.getMessage());
        }

        return followers;
    }
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException
    {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("display_name"),
                resultSet.getString("email"),
                resultSet.getString("password_hash"),
                resultSet.getString("bio"),
                resultSet.getString("profile_image_url"),
                resultSet.getString("banner_image_url"),
                resultSet.getBoolean("is_private"),
                resultSet.getTimestamp("created_at")
        );
    }
}