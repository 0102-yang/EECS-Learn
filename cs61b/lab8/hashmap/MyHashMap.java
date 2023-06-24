package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author yang
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {

        K key;

        V value;

        Node(K k, V v) {
            this.key = k;
            this.value = v;
        }

    }

    /**
     * Instance Variables.
     */
    private Collection<Node>[] buckets;

    // You should probably define some more!

    /**
     * Load factor.
     */
    private double loadFactor;

    private int size;

    private Set<K> keySet = new HashSet<>();

    /**
     * Constructors
     */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] buckets = new Collection[tableSize];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
        return buckets;
    }

    // DONE: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int hash(K key, int size) {
        return Math.abs(key.hashCode()) & (size - 1);
    }

    @Override
    public void clear() {
        this.buckets = createTable(16);
        this.loadFactor = 0.75;
        this.keySet = new HashSet<>();
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return this.keySet.contains(key);
    }

    @Override
    public V get(K key) {
        if (!this.keySet.contains(key)) {
            return null;
        }

        int index = hash(key, this.buckets.length);
        Collection<Node> bucket = this.buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    private void checkSize(int newSize) {
        if ((double) newSize / this.buckets.length > this.loadFactor) {
            expand();
        }
    }

    /**
     * Expand table (new size) by (old size) * 2.
     */
    private void expand() {
        int newSize = this.buckets.length << 1;
        Collection<Node>[] newBuckets = createTable(newSize);

        // Transfer data of old table to new table.
        for (var bucket : this.buckets) {
            for (var node : bucket) {
                int index = hash(node.key, newSize);
                newBuckets[index].add(node);
            }
        }

        this.buckets = newBuckets;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        checkSize(this.size + 1);
        int index = hash(key, this.buckets.length);
        Collection<Node> bucket = this.buckets[index];

        if (this.keySet.contains(key)) {
            for (var node : bucket) {
                if (node.key.equals(key)) {
                    node.value = value;
                }
            }
        } else {
            bucket.add(createNode(key, value));
            this.keySet.add(key);
            this.size++;
        }
    }

    @Override
    public Set<K> keySet() {
        return new HashSet<>(this.keySet);
    }

    @Override
    public V remove(K key) {
        return remove(key, get(key));
    }

    @Override
    public V remove(K key, V value) {
        int index = hash(key, this.buckets.length);
        Collection<Node> bucket = this.buckets[index];
        V res = null;
        for (var node : bucket) {
            if (node.key.equals(key) && node.value.equals(value)) {
                res = node.value;
                bucket.remove(node);
                break;
            }
        }
        return res;
    }

    @Override
    public Iterator<K> iterator() {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<K> {

        private final Queue<K> keyQueue = new LinkedList<>();

        public KeyIterator() {
            Set<K> keySet = MyHashMap.this.keySet();
            this.keyQueue.addAll(keySet);
        }

        @Override
        public boolean hasNext() {
            return this.keyQueue.isEmpty();
        }

        @Override
        public K next() {
            return this.keyQueue.remove();
        }

    }

}
