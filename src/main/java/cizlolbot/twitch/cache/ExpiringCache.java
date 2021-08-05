package cizlolbot.twitch.cache;

public abstract class ExpiringCache<K, V> implements Cache<K, V> {

    private long ttl;

    ExpiringCache(long ttl) {
        this.ttl = ttl;
    }

    public abstract void setExpiringValue(K key, ExpiringValue value);

    public void set(K key, V value) {
        setExpiringValue(key, new ExpiringValue(value, System.currentTimeMillis() + ttl));
    }

    public abstract ExpiringValue getExpiringValue(K key);

    public abstract void remove(K key);

    public V get(K key) {
        ExpiringValue expiringValue = getExpiringValue(key);
        if (expiringValue == null) {
            return null;
        }
        if (System.currentTimeMillis() >= expiringValue.getExpiresAT()) {
            remove(key);
            return null;
        }
        return expiringValue.getValue();
    }

    protected class ExpiringValue {
        private final V value;
        private final long expiresAT;

        ExpiringValue(V value, long expiresAT) {
            this.value = value;
            this.expiresAT = expiresAT;
        }

        public V getValue() {
            return value;
        }

        public long getExpiresAT() {
            return expiresAT;
        }
    }
}
