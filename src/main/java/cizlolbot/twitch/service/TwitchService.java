package cizlolbot.twitch.service;

import cizlolbot.twitch.cache.Cache;
import cizlolbot.twitch.cache.LRUCache;
import cizlolbot.twitch.oauth2.TokenRetriever;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class TwitchService {
    private static final String addressStr = "irc.chat.twitch.tv";
    private static final String nickname = "cizlolbot";

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    private String currentChannel;

    private TokenRetriever tokenRetriever;

    private Cache<String, List<String>> modsCache = new LRUCache<>(100);

    @Inject
    public TwitchService(TokenRetriever tokenRetriever) {
        this.tokenRetriever = tokenRetriever;
    }

    public void connectToIrcChat() {
        try {
            socket = new Socket(InetAddress.getByName(addressStr), 6667);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            write(String.format("PASS %s\n", getIrcPassword()));
            write(String.format("NICK %s\n", nickname));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToChannel(String channel) {
        write(String.format("JOIN #%s", channel));
        write("CAP REQ :twitch.tv/membership");
        write("CAP REQ :twitch.tv/commands");

        currentChannel = channel;
    }

    public String readLine() {
        try {
            return input.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void sendMessage(String message, String channelName) {
        write("PRIVMSG #" + channelName + " :" + message);
    }

    private void write(String message) {
        try {
            output.write(message + "\n");
            output.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getIrcPassword() {
        return String.format("oauth:%s", tokenRetriever.getToken());
    }


    public boolean isModerator(String user, String channel) {
        return getModerators(channel).contains(user);
    }

    private List<String> getModerators(String channel) {
        List<String> moderators = modsCache.get(channel);
        if (moderators != null) {
            return moderators;
        }

        sendMessage("/mods", channel);
        String line;
        do {
            line = readLine();
            String[] words = line.split(" ");
            if (words.length > 1 && words[1].equals("NOTICE")) {
                if (line.contains("There are no moderators")) {
                    moderators = new ArrayList<>();
                } else {
                    String k = "The moderators of this channel are: ";
                    String modsArrayString = line.substring(line.indexOf(k) + k.length());
                    String[] modsArr = modsArrayString.split(", ");
                    moderators = Arrays.asList(modsArr);
                }
                return moderators;
            }
        } while (true);
    }
}
