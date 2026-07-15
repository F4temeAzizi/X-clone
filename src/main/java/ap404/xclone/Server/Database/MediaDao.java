package ap404.xclone.Server.Database;

import ap404.xclone.Shared.Models.Media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MediaDao
{
    public List<Media> getMediaByTweetId(int tweetId)
    {
        String sql = """
                SELECT *
                FROM media
                WHERE tweet_id = ?
                ORDER BY media_order
                """;

        List<Media> mediaList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, tweetId);

            ResultSet resultSet = statement.executeQuery();

            try (resultSet)
            {
                while (resultSet.next())
                {
                    mediaList.add(new Media(
                            resultSet.getInt("id"),
                            resultSet.getInt("tweet_id"),
                            resultSet.getString("media_url"),
                            resultSet.getString("media_type"),
                            resultSet.getInt("media_order")
                    ));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Media error :" + e.getMessage());
        }

        return mediaList;
    }

    public boolean addMedia(int tweetId, String mediaUrl, String mediaType, int mediaOrder)
    {
        String sql = """
            INSERT INTO media(tweet_id, media_url, media_type, media_order)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, tweetId);
            statement.setString(2, mediaUrl);
            statement.setString(3, mediaType);
            statement.setInt(4, mediaOrder);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Add media error: " + e.getMessage());
            return false;
        }
    }
}
