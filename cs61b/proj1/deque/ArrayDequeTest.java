package deque;

import jh61b.junit.In;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test array deque.
 *
 * @author yang
 */
public class ArrayDequeTest {

    @Test
    public void genericTest() {
        ArrayDeque<Double> a1 = new ArrayDeque<>();
        ArrayDeque<Integer> a2 = new ArrayDeque<>();

        a1.addFirst(1.0);
        a2.addLast(1);
    }

    @Test
    public void emptyTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertTrue("Deque should be empty.", deque.isEmpty());
        deque.addLast(1);
        assertFalse("Deque should not be empty.", deque.isEmpty());
    }

    @Test
    public void addRemoveTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(10);
        deque.addLast(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(2);
        int first = deque.removeFirst();
        assertEquals("First element should be 1.", first, 10);
        int last = deque.removeLast();
        assertEquals("Last element should be 2.", last, 2);
    }

    @Test
    public void hugeData() {
        int size = 10000;
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            deque.addLast(i);
        }
        int first = deque.removeFirst();
        assertEquals("First element should be 0.", first, 0);
        int last = deque.removeLast();
        assertEquals("Last element should be 9999.", last, 9999);
    }

    @Test
    public void sizeTest() {
        int size = 12;
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertEquals("Size should be 0.", deque.size(), 0);
        for (int i = 0; i < size; i++) {
            deque.addLast(i);
        }
        assertEquals("Size should be 6.", deque.size(), size);
    }

}
