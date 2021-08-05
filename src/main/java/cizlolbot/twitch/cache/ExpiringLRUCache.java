package cizlolbot.twitch.cache;

public class ExpiringLRUCache<K, V> extends ExpiringCache<K, V> {

    private LRUCache<K, ExpiringValue> underlying;

    public ExpiringLRUCache(long ttl, int maxAllowedSize) {
        super(ttl);
        underlying = new LRUCache<>(maxAllowedSize);
    }

    @Override
    public void setExpiringValue(K key, ExpiringValue value) {
        underlying.set(key, value);
    }

    @Override
    public ExpiringValue getExpiringValue(K key) {
        return underlying.get(key);
    }

    @Override
    public void remove(K key) {
        underlying.remove(key);
    }
}
