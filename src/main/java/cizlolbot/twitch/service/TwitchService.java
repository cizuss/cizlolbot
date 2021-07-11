package cizlolbot.twitch.service;

import cizlolbot.twitch.oauth2.TokenRetriever;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

@Singleton
public class TwitchService {
    private static final String addressStr = "irc.chat.twitch.tv";
    private static final String nickname = "cizlolbot";

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    private String currentChannel;

    private TokenRetriever tokenRetriever;

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
        write("PRIVMSG #"+channelName+" :"+message);
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
}
