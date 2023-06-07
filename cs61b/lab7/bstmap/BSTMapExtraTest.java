package bstmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests of optional parts of lab 7.
 *
 * @author cs61b
 */
public class BSTMapExtraTest {

    /**
     * Sanity test for keySet, only here because it's optional
     */
    @Test
    public void sanityKeySetTest() {
        int amount = 455;
        BSTMap<String, Integer> b = new BSTMap<>();
        HashSet<String> values = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            b.put("hi" + i, 1);
            values.add("hi" + i);
        }
        //keys are there
        Assertions.assertEquals(amount, b.size());
        Set<String> keySet = b.keySet();
        Assertions.assertTrue(values.containsAll(keySet));
        Assertions.assertTrue(keySet.containsAll(values));
    }

    /**
     * Remove Test
     * <p>
     * Note for testRemoveRoot:
     * <p>
     * Just checking that c is gone (perhaps incorrectly)
     * assumes that remove is BST-structure preserving.
     * <p>
     * More exhaustive tests could be done to verify
     * implementation of remove, but that would require doing
     * things like checking for inorder vs. preorder swaps,
     * and is unnecessary in this simple BST implementation.
     */
    @Test
    public void testRemoveRoot() {
        BSTMap<String, String> q = new BSTMap<String, String>();
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
     * test the 3 different cases of remove
     */
    @Test
    public void testRemoveThreeCases() {
        BSTMap<String, String> q = new BSTMap<>();
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

    /**
     * Remove Test 3
     * Checks that remove works correctly on root nodes
     * when the node has only 1 or 0 children on either side.
     */
    @Test
    public void testRemoveRootEdge() {
        BSTMap rightChild = new BSTMap();
        rightChild.put('A', 1);
        rightChild.put('B', 2);
        Integer result = (Integer) rightChild.remove('A');
        Assertions.assertEquals(1, (int) result);
        for (int i = 0; i < 10; i++) {
            rightChild.put((char) ('C' + i), 3 + i);
        }
        rightChild.put('A', 100);
        Assertions.assertEquals(((Integer) rightChild.remove('D')), 4);
        Assertions.assertEquals(((Integer) rightChild.remove('G')), 7);
        Assertions.assertEquals(((Integer) rightChild.remove('A')), 100);
        Assertions.assertEquals(9, rightChild.size());

        BSTMap leftChild = new BSTMap();
        leftChild.put('B', 1);
        leftChild.put('A', 2);
        Assertions.assertEquals(1, (int) ((Integer) leftChild.remove('B')));
        Assertions.assertEquals(1, leftChild.size());
        Assertions.assertNull(leftChild.get('B'));

        BSTMap noChild = new BSTMap();
        noChild.put('Z', 15);
        Assertions.assertEquals(15, (int) ((Integer) noChild.remove('Z')));
        Assertions.assertEquals(0, noChild.size());
        Assertions.assertNull(noChild.get('Z'));
    }

}
