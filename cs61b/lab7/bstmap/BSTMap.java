package bstmap;

import java.util.*;

/**
 * @author yang
 */
public class BSTMap<K extends Comparable<K>, V extends Comparable<V>> implements Map61B<K, V> {

    private int size;

    private Entry root;

    private Entry nodeToDelete;

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(this.root, key) != null;
    }

    @Override
    public V get(K key) {
        Entry res = get(this.root, key);
        return res != null ? res.value : null;
    }

    private Entry get(Entry node, K key) {
        if (node == null) {
            return null;
        } else if (node.key.compareTo(key) > 0) {
            return get(node.left, key);
        } else if (node.key.compareTo(key) < 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        this.root = put(this.root, key, value);
    }

    private Entry put(Entry node, K key, V value) {
        if (node == null) {
            this.size++;
            node = new Entry(key, value);
        } else if (node.key.compareTo(key) > 0) {
            node.left = put(node.left, key, value);
        } else if (node.key.compareTo(key) < 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    @Override
    public Set<K> keySet() {
        Set<K> res = new TreeSet<>();
        for (K k : this) {
            res.add(k);
        }
        return res;
    }

    @Override
    public V remove(K key) {
        return remove(key, get(key));
    }

    @Override
    public V remove(K key, V value) {
        this.root = remove(this.root, key, value);
        this.size--;
        return this.nodeToDelete.value;
    }

    private Entry remove(Entry root, K key, V value) {
        if (root == null) {
            return null;
        } else if (root.key.compareTo(key) > 0) {
            root.left = remove(root.left, key, value);
        } else if (root.key.compareTo(key) < 0) {
            root.right = remove(root.right, key, value);
        } else if (root.value.compareTo(value) == 0 && root.left != null && root.right != null) {
            Entry tmp = findMinEntry(root.right);
            root.key = tmp.key;
            root.value = tmp.value;
            root.right = remove(root.right, tmp.key, tmp.value);
        } else {
            this.nodeToDelete = new Entry(root.key, root.value);
            if (root.left == null) {
                root = root.right;
            } else {
                root = root.left;
            }
        }
        return root;
    }

    private Entry findMinEntry(Entry root) {
        if (root != null) {
            while (root.left != null) {
                root = root.left;
            }
        }
        return root;
    }

    @Override
    public Iterator<K> iterator() {
        return new BstMapIterator();
    }

    private class BstMapIterator implements Iterator<K> {

        private final Queue<K> keyQueue = new LinkedList<>();

        public BstMapIterator() {
            Entry root = BSTMap.this.root;
            init(root);
        }

        private void init(Entry root) {
            if (root == null) {
                return;
            }
            init(root.left);
            this.keyQueue.add(root.key);
            init(root.right);
        }

        @Override
        public boolean hasNext() {
            return !this.keyQueue.isEmpty();
        }

        @Override
        public K next() {
            return this.keyQueue.remove();
        }

    }

    private class Entry {

        K key;

        V value;

        Entry left;

        Entry right;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

}
