package ap404.xclone.Client.Managers;

import javafx.scene.Scene;

public class ThemeManager
{
    private static boolean isDark = true;

    public static boolean isDarkTheme() { return isDark; }
    public static void setIsDark(boolean dark) { isDark = dark; }

    public static void applyTheme(Scene scene)
    {
        scene.getStylesheets().removeIf(css -> css.contains("theme.css") || css.contains("light.css"));

        if (isDark)
        {
            scene.getStylesheets().add(ThemeManager.class.getResource("/css/theme.css").toExternalForm());
        }
        else
        {
            scene.getStylesheets().add(ThemeManager.class.getResource("/css/light.css").toExternalForm());
        }
    }

    public static String getThemeCss()
    {
        if (isDark) return ThemeManager.class.getResource("/css/theme.css").toExternalForm();
        return ThemeManager.class.getResource("/css/light.css").toExternalForm();
    }
}
