package bstmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2016 by Josh Hug
 *
 * @author cs61b
 */
public class BSTMapTest {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<>();
            BSTMap<String, Integer> b = new BSTMap<>();
            BSTMap<Integer, String> c = new BSTMap<>();
            BSTMap<Boolean, Integer> e = new BSTMap<>();
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /**
     * assumes put/size/containsKey/get work
     */
    @Test
    public void sanityClearTest() {
        int amount = 455;
        BSTMap<String, Integer> b = new BSTMap<>();
        for (int i = 0; i < amount; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            Assertions.assertTrue(null != b.get("hi" + i) && (b.get("hi" + i).equals(1 + i))
                    && b.containsKey("hi" + i));
        }
        Assertions.assertEquals(amount, b.size());
        b.clear();
        Assertions.assertEquals(0, b.size());
        for (int i = 0; i < amount; i++) {
            Assertions.assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    /**
     * assumes put works
     */
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        Assertions.assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        Assertions.assertTrue(b.containsKey("waterYouDoingHere"));
    }

    /**
     * assumes put works
     */
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        Assertions.assertNull(b.get("starChild"));
        Assertions.assertEquals(0, b.size());
        b.put("starChild", 5);
        Assertions.assertEquals(5, (int) ((Integer) b.get("starChild")));
        b.put("KISS", 5);
        Assertions.assertEquals(5, (int) ((Integer) b.get("KISS")));
        Assertions.assertNotEquals(null, b.get("starChild"));
        Assertions.assertEquals(2, b.size());
    }

    /**
     * assumes put works
     */
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        Assertions.assertEquals(0, b.size());
        b.put("hi", 1);
        Assertions.assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        Assertions.assertEquals(456, b.size());
    }

    /**
     * assumes get/contains key work
     */
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        b.put("hi", 1);
        Assertions.assertTrue(b.containsKey("hi") && b.get("hi") != null);
    }

    /**
     * assumes put works
     */
    @Test
    public void containsKeyNullTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        b.put("hi", null);
        Assertions.assertTrue(b.containsKey("hi"));
    }

}
