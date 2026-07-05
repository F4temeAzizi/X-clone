package ap404.xclone.Client.Managers;

import ap404.xclone.Shared.Models.User;

public class Session
{
    private static User currentUser;

    public static User getCurrentUser() { return currentUser; }

    public static void setCurrentUser(User user) { currentUser = user; }
}
