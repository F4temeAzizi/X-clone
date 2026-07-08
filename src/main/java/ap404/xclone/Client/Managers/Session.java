package ap404.xclone.Client.Managers;

import ap404.xclone.Client.Client;
import ap404.xclone.Shared.Models.User;

public class Session
{
    private static User currentUser;
    private static Client client;

    public static Client getClient() { return client; }
    public void setClient(Client client) { this.client = client;}

    public static User getCurrentUser() { return currentUser; }

    public static void setCurrentUser(User user) { currentUser = user; }
}
