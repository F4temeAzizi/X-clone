package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.User;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {

    public boolean signup(String name, String username, String email, String password) {
        String sql = """
                INSERT INTO users (username, display_name, email, password_hash)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, name);
            statement.setString(3, email);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            statement.setString(4, hashedPassword);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Signup error: " + e.getMessage());
            return false;
        }
    }

    public boolean login(String usernameOrEmail, String password) {
        String sql = """
            SELECT * FROM users
            WHERE username = ? OR email = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password_hash");

                return BCrypt.checkpw(password, hashedPassword);
            }

            return false;

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }
    public User getUser (String usernameOrEmail) {
        String sql = """
            SELECT * FROM users
            WHERE username = ? OR email = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
         return null;
    }

    public boolean updateProfile (int userId, String name, String username, String bio, String bannerImage, String avatarImage)
    {
        String sql = """
                UPDATE users
                SET display_name = ?,
                username = ?,
                bio = ?,
                banner_image_url = ?,
                profile_image_url = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, bio);
            statement.setString(4, bannerImage);
            statement.setString(5, avatarImage);
            statement.setInt(6, userId);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Update profile error: " + e.getMessage());
            return false;
        }
    }

    public User getUserById(int id) {

        String sql = """
            SELECT * FROM users
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}