package cizlolbot.twitch.ratelimit;

public interface RateLimiter {
    void throttle(String commandResponseId, Runnable block);
}
