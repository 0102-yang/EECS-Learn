package deque;

/**
 * Deque interface.
 *
 * @author yang
 */
public interface Deque<T> {

    /**
     * Return the size of deque.
     *
     * @return The size.
     */
    int size();

    /**
     * Return true if deque is empty.
     *
     * @return True for empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Add value to the start of deque.
     *
     * @param value The value.
     */
    void addFirst(T value);

    /**
     * Add value to the end of deque.
     *
     * @param value The value.
     */
    void addLast(T value);

    /**
     * Remove first element of deque.
     *
     * @return The first element.
     */
    T removeFirst();

    /**
     * Remove last element of deque.
     *
     * @return The last element.
     */
    T removeLast();

    /**
     * Get element by index.
     *
     * @param index The index.
     * @return The element.
     */
    T get(int index);

    /**
     * Print deque.
     */
    void printDeque();

}
