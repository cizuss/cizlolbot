package cizlolbot.twitch.irc;

import cizlolbot.twitch.oauth2.TokenRetriever;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class IrcBot {
    private static final String addressStr = "irc.chat.twitch.tv";
    private static final String nickname = "cizlolbot";

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    private String channelName;

    public IrcBot(String channelName) {
        this.channelName = channelName;
    }

    private TokenRetriever tokenRetriever = TokenRetriever.getInstance();

    public void connect() {
        try {
            socket = new Socket(InetAddress.getByName(addressStr), 6667);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            output.write(String.format("PASS %s\n", getIrcPassword()));
            output.write(String.format("NICK %s\n", nickname));
            output.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void readLine() {
        try {
            String line = input.readLine();
            if (line != null) {
                System.out.println("Read " + line);
                if (line.contains("You are in a maze")) {
                    write(String.format("JOIN #%s", channelName));
                } else if (line.contains(":End of /NAMES list")) {
                    write("CAP REQ :twitch.tv/membership");
                    write("CAP REQ :twitch.tv/commands");
                } else if (line.contains("PRIVMSG")) {
                    IrcMessage ircMessage = new IrcMessage(line);
                    System.out.println(String.format("%s: %s", ircMessage.getFrom(), ircMessage.getBody()));
                }

                Thread.sleep(50);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
