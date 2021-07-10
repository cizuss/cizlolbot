package cizlolbot.twitch.utils;

public class StringUtils {
    public static boolean containsWord(String string, String word) {
        String[] words = string.split(" ");
        for (String w: words) {
            if (w.equals(word)) {
                return true;
            }
        }
        return false;
    }
}
