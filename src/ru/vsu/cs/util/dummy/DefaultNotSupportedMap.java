package ru.vsu.cs.util.dummy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Интерфейс, реализующий системный Map&lt;K, V&gt;, в виде методов с исключениями "Not
 * supported yet."
 *
 * @param <K>
 * @param <V>
 */
public interface DefaultNotSupportedMap<K, V> extends Map<K, V> {

    @Override
    default int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default V get(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default V put(K key, V value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default V remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
