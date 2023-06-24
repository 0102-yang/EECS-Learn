package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

/**
 * @author yang
 */
public class MaxArrayDequeTest {

    @Test
    public void maxTest() {
        int size = 2000;
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(Comparator.comparingInt(value -> value));
        for (int i = 0; i < size; i++) {
            deque.addLast(i * 2);
        }

        int max = deque.max();
        assertEquals("Default max value should be 3998.", max, 3998);
        max = deque.max(Comparator.comparingInt(value -> -value));
        assertEquals("Specify max value should be 0.", max, 0);
    }

}
