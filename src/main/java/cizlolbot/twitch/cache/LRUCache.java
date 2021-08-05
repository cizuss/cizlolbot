package cizlolbot.twitch.cache;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LRUCache<K, V> implements Cache<K, V> {
    private Map<K, V> map = new ConcurrentHashMap<>();
    private Set<K> accessSet = new LinkedHashSet<>();

    private int maxAllowedSize;

    public LRUCache(int maxAllowedSize) {
        this.maxAllowedSize = maxAllowedSize;
    }

    public void set(K key, V value) {
        if (map.size() >= maxAllowedSize) {
            removeLeastRecentlyUsed();
        }
        map.put(key, value);
        recordAccess(key);

    }

    public V get(K key) {
        V value = map.get(key);
        if (value != null) {
            recordAccess(key);
        }
        return value;
    }


    public void remove(K key) {
        accessSet.remove(key);
        map.remove(key);
    }

    private void removeLeastRecentlyUsed() {
        K keyToRemove = accessSet.iterator().next();
        remove(keyToRemove);
    }

    private void recordAccess(K key) {
        accessSet.remove(key);
        accessSet.add(key);
    }
}
