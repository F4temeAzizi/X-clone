package ap404.xclone.Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TweetHashtagDao
{
    public boolean addHashtagToTweet(int tweetId, int hashtagId)
    {
        String sql = """
                INSERT INTO tweet_hashtags(tweet_id, hashtag_id)
                VALUES(?, ?)
                ON CONFLICT DO NOTHING
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1,tweetId);
            statement.setInt(2, hashtagId);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Link hashtag error: " + e.getMessage());
            return false;
        }
    }
}
