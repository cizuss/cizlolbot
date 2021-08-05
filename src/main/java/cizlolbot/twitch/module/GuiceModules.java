package cizlolbot.twitch.module;

import com.google.inject.Module;

import java.util.Arrays;
import java.util.List;

public class GuiceModules {
    public static List<Module> all() {
        return Arrays.asList(new GodModule());
    }
}
