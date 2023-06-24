package hashmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of optional parts of lab 8.
 *
 * @author yang
 */
public class MyHashMapExtraTest {

    @Test
    public void testRemove() {
        MyHashMap<String, String> q = new MyHashMap<>();
        // a b c d e
        q.put("c", "a");
        q.put("b", "a");
        q.put("a", "a");
        q.put("d", "a");
        q.put("e", "a");
        Assertions.assertNotNull(q.remove("c"));
        Assertions.assertFalse(q.containsKey("c"));
        Assertions.assertTrue(q.containsKey("a"));
        Assertions.assertTrue(q.containsKey("b"));
        Assertions.assertTrue(q.containsKey("d"));
        Assertions.assertTrue(q.containsKey("e"));
    }

    /**
     * Remove Test 2
     * Test the 3 different cases of remove
     */
    @Test
    public void testRemoveThreeCases() {
        MyHashMap<String, String> q = new MyHashMap<>();
        // a b c d e
        q.put("c", "a");
        q.put("b", "a");
        q.put("a", "a");
        q.put("d", "a");
        q.put("e", "a");
        // a b c d
        Assertions.assertNotNull(q.remove("e"));
        Assertions.assertTrue(q.containsKey("a"));
        Assertions.assertTrue(q.containsKey("b"));
        Assertions.assertTrue(q.containsKey("c"));
        Assertions.assertTrue(q.containsKey("d"));
        // a b d
        Assertions.assertNotNull(q.remove("c"));
        Assertions.assertTrue(q.containsKey("a"));
        Assertions.assertTrue(q.containsKey("b"));
        Assertions.assertTrue(q.containsKey("d"));
        // a b d f
        q.put("f", "a");
        // a b f
        Assertions.assertNotNull(q.remove("d"));
        Assertions.assertTrue(q.containsKey("a"));
        Assertions.assertTrue(q.containsKey("b"));
        Assertions.assertTrue(q.containsKey("f"));
    }

}
