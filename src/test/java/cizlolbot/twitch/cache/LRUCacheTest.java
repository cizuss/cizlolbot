package cizlolbot.twitch.cache;

import org.junit.Test;

import static org.junit.Assert.*;

public class LRUCacheTest {
    private LRUCache<String, String> cache = new LRUCache<>(3);

    @Test
    public void test() {
        cache.set("k1", "v1");
        cache.set("k2", "v2");
        cache.set("k3", "v3");

        cache.get("k1");
        cache.get("k2");
        cache.get("k3");
        cache.get("k1");
        cache.get("k3");

        cache.set("k4", "v4");
        cache.set("k5", "v5");

        assertNull(cache.get("k2"));
        assertNull(cache.get("k1"));
    }
}