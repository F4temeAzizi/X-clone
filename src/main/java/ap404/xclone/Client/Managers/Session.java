package ap404.xclone.Client.Managers;

import ap404.xclone.Client.Client;
import ap404.xclone.Shared.Models.User;

public class Session
{
    private static User currentUser;
    private static Client client;

    public static Client getClient() { return client; }
    public static void setClient(Client client) { Session.client = client;}

    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User user) { currentUser = user; }
}
