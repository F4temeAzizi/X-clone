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

    
}