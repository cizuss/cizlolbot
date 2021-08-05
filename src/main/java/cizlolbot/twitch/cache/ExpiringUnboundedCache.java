package cizlolbot.twitch.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpiringUnboundedCache<K, V> extends ExpiringCache<K, V> {

    private Map<K, ExpiringValue> map = new ConcurrentHashMap<>();

    public ExpiringUnboundedCache(long ttl) {
        super(ttl);
    }

    @Override
    public void setExpiringValue(K key, ExpiringValue value) {
        map.put(key, value);
    }

    @Override
    public ExpiringValue getExpiringValue(K key) {
        return map.get(key);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }
}
