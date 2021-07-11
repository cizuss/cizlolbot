package cizlolbot.twitch.ratelimit;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRateLimiter implements RateLimiter {
    private int nrOfMessagesPerMinute;

    private Map<String, Long> windows = new HashMap<>();
    private Map<String, Integer> nrMessagesMap = new HashMap<>();

    public InMemoryRateLimiter(int nrOfMessagesPerMinute) {
        this.nrOfMessagesPerMinute = nrOfMessagesPerMinute;
    }

    public void throttle(String commandResponseId, Runnable block) {
        long now = System.currentTimeMillis() / 60000;
        initMapsIfNeeded(commandResponseId);
        long window = windows.get(commandResponseId);
        int nrMessages = nrMessagesMap.get(commandResponseId);

        if (now > window) {
            windows.put(commandResponseId, now);
            nrMessagesMap.put(commandResponseId, 0);
        }

        if (nrMessages < nrOfMessagesPerMinute) {
            block.run();
            nrMessagesMap.put(commandResponseId, nrMessages + 1);
        } else {
            throw new RuntimeException("Nope, try again later...");
        }
    }

    private void initMapsIfNeeded(String channel) {
        if (!windows.containsKey(channel)) {
            windows.put(channel, 0L);
        }
        if (!nrMessagesMap.containsKey(channel)) {
            nrMessagesMap.put(channel, 0);
        }
    }
}
