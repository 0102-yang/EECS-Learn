package deque;

/**
 * @author yang
 */
public class LinkedListDeque<T> implements Deque<T> {

    private int size;

    Node<T> head = new Node<>();

    Node<T> tail = new Node<>();

    public LinkedListDeque() {
        this.size = 0;
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public void addFirst(T value) {
        Node<T> firstNode = this.head.next;
        Node<T> newNode = new Node<>(value, firstNode, this.head);
        firstNode.prev = newNode;
        this.head.next = newNode;
        this.size++;
    }

    @Override
    public void addLast(T value) {
        Node<T> lastNode = this.tail.prev;
        Node<T> newNode = new Node<>(value, this.tail, lastNode);
        lastNode.next = newNode;
        this.tail.prev = newNode;
        this.size++;
    }

    @Override
    public T removeFirst() {
        if (this.size <= 0) {
            return null;
        }

        Node<T> firstNode = this.head.next;
        Node<T> secondNode = this.head.next.next;
        this.head.next = secondNode;
        secondNode.prev = this.head;
        this.size--;
        return firstNode.value;
    }

    @Override
    public T removeLast() {
        if (this.size <= 0) {
            return null;
        }

        Node<T> lastFirstNode = this.tail.prev;
        Node<T> lastSecondNode = this.tail.prev.prev;
        this.tail.prev = lastSecondNode;
        lastSecondNode.next = this.tail;
        this.size--;
        return lastFirstNode.value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        Node<T> start = this.head.next;
        for (int i = 0; i < index; i++) {
            start = start.next;
        }
        return start.value;
    }

    public T getRecursive(int index) {
        return getRecursive(index, this.head.next);
    }

    private T getRecursive(int index, Node<T> node) {
        if (index == 0) {
            return node.value;
        }
        return getRecursive(index - 1, node.next);
    }

    @Override
    public void printDeque() {
        Node<T> node = this.head.next;
        while (node != this.tail) {
            System.out.println(node.value.toString());
            node = node.next;
        }
    }

    private static final class Node<T> {

        T value;

        Node<T> next;

        Node<T> prev;

        public Node() {
        }

        public Node(T value, Node<T> next, Node<T> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

    }

}
