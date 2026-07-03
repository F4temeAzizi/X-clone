package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.User;

import java.sql.*;

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
            statement.setString(4, password);

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
                WHERE (username = ? OR email = ?)
                AND password_hash = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);
            statement.setString(3, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }
}