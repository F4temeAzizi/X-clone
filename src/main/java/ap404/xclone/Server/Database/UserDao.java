package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                PreparedStatement statement = connection.prepareStatement(sql)
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
                PreparedStatement statement = connection.prepareStatement(sql)
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

    public boolean deleteAccount(String username, String password) {

        String verifySql = """
                SELECT password_hash
                FROM users
                WHERE username = ?
                """;

        String deleteAccountSql = """
                DELETE FROM users
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(verifySql)) {

                statement.setString(1, username);

                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) return false;

                String hashedPassword = resultSet.getString("password_hash");

                if (!BCrypt.checkpw(password, hashedPassword)) return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(deleteAccountSql)) {

                statement.setString(1, username);

                return statement.executeUpdate() == 1;
            }

        } catch (SQLException e) {
            System.out.println("delete account error: " + e.getMessage());
            return false;
        }
    }

    public boolean changePassword(int userId, String currentPassword, String newPassword) {

        String verifySql = """
                SELECT password_hash
                FROM users
                WHERE id = ?
                """;

        String changePasswordSql = """
                UPDATE users
                SET password_hash = ?
                WHERE id = ?
                """;


        try (Connection connection = DatabaseConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(verifySql)) {

                statement.setInt(1, userId);

                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) return false;

                String hashedPassword = resultSet.getString("password_hash");

                if (!BCrypt.checkpw(currentPassword, hashedPassword)) return false;
            }

            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            try (PreparedStatement statement = connection.prepareStatement(changePasswordSql)) {

                statement.setString(1, newHashedPassword);
                statement.setInt(2, userId);

                return statement.executeUpdate() == 1;
            }

        } catch (SQLException e) {
            System.out.println("change password error: " + e.getMessage());
            return false;
        }
    }

    public List<User> searchUser(String keyword, int currentUserId)
    {
        List<User> users = new ArrayList<>();

        String sql = """
                SELECT *
                FROM users
                WHERE id <> ?
                AND (
                    LOWER(username) LIKE LOWER(?)
                    OR LOWER(display_name) LIKE LOWER(?)
                )
                ORDER BY display_name
                LIMIT 20
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            String search = "%" + keyword + "%";

            statement.setInt(1, currentUserId);
            statement.setString(2, search);
            statement.setString(3, search);

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    users.add(new User(
                                    resultSet.getInt("id"),
                                    resultSet.getString("username"),
                                    resultSet.getString("display_name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password_hash"),
                                    resultSet.getString("bio"),
                                    resultSet.getString("profile_image_url"),
                                    resultSet.getString("banner_image_url"),
                                    resultSet.getBoolean("is_private"),
                                    resultSet.getTimestamp("created_at")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("search user error: " + e.getMessage());
        }
        return users;
    }
}