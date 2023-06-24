package hashmap;

import java.util.Iterator;
import java.util.Set;

/**
 * A data structure that uses a linked list to store pairs of keys and values.
 * Any key must appear at most once in the dictionary, but values may appear multiple
 * times. Key operations are get(key), put(key, value), and contains(key) methods. The value
 * associated to a key is the value in the last call to put with that key.
 *
 * @author cs61b
 */
public class ULLMap<K, V> implements Map61B<K, V> {

    int size = 0;

    /**
     * Returns the value corresponding to KEY or null if no such value exists.
     */
    @Override
    public V get(K key) {
        if (this.list == null) {
            return null;
        }
        Entry lookup = this.list.get(key);
        if (lookup == null) {
            return null;
        }
        return lookup.val;
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        this.size = 0;
        this.list = null;
    }

    /**
     * Inserts the key-value pair of KEY and VALUE into this dictionary,
     * replacing the previous value associated to KEY, if any.
     */
    @Override
    public void put(K key, V val) {
        if (this.list != null) {
            Entry lookup = this.list.get(key);
            if (lookup == null) {
                this.list = new Entry(key, val, this.list);
                this.size = this.size + 1;
            } else {
                lookup.val = val;
            }
        } else {
            this.list = new Entry(key, val, this.list);
            this.size = this.size + 1;
        }
    }

    /**
     * Returns true if and only if this dictionary contains KEY as the
     * key of some key-value pair.
     */
    @Override
    public boolean containsKey(K key) {
        if (this.list == null) {
            return false;
        }
        return this.list.get(key) != null;
    }

    @Override
    public Iterator<K> iterator() {
        return new ULLMapIter();
    }

    /**
     * Keys and values are stored in a linked list of Entry objects.
     * This variable stores the first pair in this linked list.
     */
    private Entry list;

    /**
     * Represents one node in the linked list that stores the key-value pairs
     * in the dictionary.
     */
    private class Entry {

        /**
         * Stores KEY as the key in this key-value pair, VAL as the value, and
         * NEXT as the next node in the linked list.
         */
        Entry(K k, V v, Entry n) {
            this.key = k;
            this.val = v;
            this.next = n;
        }

        /**
         * Returns the Entry in this linked list of key-value pairs whose key
         * is equal to KEY, or null if no such Entry exists.
         */
        Entry get(K k) {
            if (k != null && k.equals(this.key)) {
                return this;
            }
            if (this.next == null) {
                return null;
            }
            return this.next.get(k);
        }

        /**
         * Stores the key of the key-value pair of this node in the list.
         */
        K key;

        /**
         * Stores the value of the key-value pair of this node in the list.
         */
        V val;

        /**
         * Stores the next Entry in the linked list.
         */
        Entry next;

    }

    /**
     * An iterator that iterates over the keys of the dictionary.
     */
    private class ULLMapIter implements Iterator<K> {

        /**
         * Create a new ULLMapIter by setting cur to the first node in the
         * linked list that stores the key-value pairs.
         */
        ULLMapIter() {
            this.cur = ULLMap.this.list;
        }

        @Override
        public boolean hasNext() {
            return this.cur != null;
        }

        @Override
        public K next() {
            K ret = this.cur.key;
            this.cur = this.cur.next;
            return ret;
        }

        /**
         * Stores the current key-value pair.
         */
        private Entry cur;

    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

}

