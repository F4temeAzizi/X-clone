package ap404.xclone.Shared.Models;

public class Tweet
{
    private String name;
    private String username;
    private String content;

    public Tweet(String name, String username, String content)
    {
        this.name = name;
        this.username = username;
        this.content = content;
    }

    public String getName() { return name; }

    public String getUsername() { return username; }

    public String getContent() { return content; }
}