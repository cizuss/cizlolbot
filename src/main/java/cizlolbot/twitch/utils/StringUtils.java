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

    public static boolean startsWithWord(String string, String word) {
        String[] words = string.split(" ");
        return words.length >= 1 && words[0].equals(word);
    }
}
