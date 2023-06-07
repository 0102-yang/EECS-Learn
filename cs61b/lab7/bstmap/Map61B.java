package bstmap;

import java.util.Set;

/**
 * Your implementation BSTMap should implement this interface. To do so,
 * append "implements Map61B<K,V>" to the end of your "public class..."
 * declaration, though you can use other formal type parameters if you'd like.
 *
 * @author cs61b
 */
public interface Map61B<K, V> extends Iterable<K> {

    /**
     * Removes all of the mappings from this map.
     */
    void clear();

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key The key.
     * @return True if contains key.
     */
    boolean containsKey(K key);

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key The key.
     * @return The value.
     */
    V get(K key);

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return The size.
     */
    int size();

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key   The key.
     * @param value The value.
     */
    void put(K key, V value);

    /**
     * Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException.
     *
     * @return The key set.
     */
    Set<K> keySet();

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key key.
     * @return The value.
     */
    V remove(K key);

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.
     *
     * @param key   The key.
     * @param value The value.
     * @return The value.
     */
    V remove(K key, V value);

}
