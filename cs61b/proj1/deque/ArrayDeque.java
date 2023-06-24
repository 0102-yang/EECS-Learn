package deque;

/**
 * @author yang
 */
public class ArrayDeque<T> implements Deque<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private int size;

    private int capacity;

    private Object[] array;

    public ArrayDeque() {
        this.size = 0;
        this.capacity = DEFAULT_CAPACITY;
        this.array = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void addFirst(T value) {
        checkSize(this.size + 1);
        if (this.size >= 0) {
            System.arraycopy(this.array, 0, this.array, 1, this.size++);
        }
        this.array[0] = value;
    }

    @Override
    public void addLast(T value) {
        checkSize(this.size + 1);
        this.array[this.size++] = value;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T value = get(0);
        System.arraycopy(this.array, 1, this.array, 0, this.size - 1);
        this.array[--this.size] = null;
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T value = get(this.size - 1);
        this.array[--this.size] = null;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (!validIndex(index)) {
            return null;
        }
        return (T) this.array[index];
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < this.size; i++) {
            T v = get(i);
            System.out.println(v.toString());
        }
    }

    private void checkSize(int newSize) {
        if (newSize > this.capacity) {
            int newCapacity = (int) (this.capacity * 1.5);
            expand(newCapacity);
        }
    }

    private void expand(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(this.array, 0, newArray, 0, this.size);
        this.array = newArray;
        this.capacity = newCapacity;
    }

    private boolean validIndex(int index) {
        return index >= 0 && index < this.size;
    }

}
