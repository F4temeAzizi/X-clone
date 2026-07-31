package ap404.xclone.Server.Database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HashtagDao
{
    public Integer getIdByName(String name)
    {
        String sql = """
                SELECT id
                FROM hashtags
                WHERE name = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, name.toLowerCase());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return resultSet.getInt("id");
        }
        catch (SQLException e)
        {
            System.out.println("Get hashtag error: " + e.getMessage());
        }
        return null;
    }

    public int createIfNotExists(String name)
    {
        Integer id = getIdByName(name);

        if (id != null) return id;

        String sql = """
                INSERT INTO hashtags(name)
                VALUES(?)
                RETURNING id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, name.toLowerCase());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return resultSet.getInt("id");
        }
        catch (SQLException e)
        {
            System.out.println("Create hashtag error: " + e.getMessage());
        }
        return -1;
    }
}
