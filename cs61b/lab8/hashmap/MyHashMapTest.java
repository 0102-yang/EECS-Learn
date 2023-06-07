package hashmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests by Brendan Hu, Spring 2015
 * Revised for 2016 by Josh Hug
 * Revised for 2021 by Neil Kulkarni
 *
 * @author yang
 */
public class MyHashMapTest {

    @Test
    public void sanityGenericsTest() {
        MyHashMap<String, String> a = new MyHashMap<>();
        MyHashMap<String, Integer> b = new MyHashMap<>();
        MyHashMap<Integer, String> c = new MyHashMap<>();
        MyHashMap<Boolean, Integer> d = new MyHashMap<>();
    }

    /**
     * assumes put/size/containsKey/get work
     */
    @Test
    public void sanityClearTest() {
        sanityClearTest(new MyHashMap<>());
    }

    public static void sanityClearTest(MyHashMap<String, Integer> b) {
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
            //make sure put is working via containsKey and get
            Assertions.assertTrue(null != b.get("hi" + i)
                    && b.containsKey("hi" + i));
        }
        b.clear();
        Assertions.assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            Assertions.assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    /**
     * assumes put works
     */
    @Test
    public void sanityContainsKeyTest() {
        sanityContainsKeyTest(new MyHashMap<>());
    }

    public static void sanityContainsKeyTest(MyHashMap<String, Integer> b) {
        Assertions.assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        Assertions.assertTrue(b.containsKey("waterYouDoingHere"));
    }

    /**
     * assumes put works
     */
    @Test
    public void sanityGetTest() {
        sanityGetTest(new MyHashMap<>());
    }

    public static void sanityGetTest(MyHashMap<String, Integer> b) {
        Assertions.assertNull(b.get("starChild"));
        b.put("starChild", 5);
        Assertions.assertNotEquals(null, b.get("starChild"));
        b.put("KISS", 5);
        Assertions.assertNotEquals(null, b.get("KISS"));
        Assertions.assertNotEquals(null, b.get("starChild"));
    }

    /**
     * assumes put works
     */
    @Test
    public void sanitySizeTest() {
        sanitySizeTest(new MyHashMap<>());
    }

    public static void sanitySizeTest(MyHashMap<String, Integer> b) {
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
        sanityPutTest(new MyHashMap<>());
    }

    public static void sanityPutTest(MyHashMap<String, Integer> b) {
        b.put("hi", 1);
        Assertions.assertTrue(b.containsKey("hi") && b.get("hi") != null);
    }

    @Test
    public void sanityKeySetTest() {
        sanityKeySetTest(new MyHashMap<>());
    }

    public static void sanityKeySetTest(MyHashMap<String, Integer> b) {
        HashSet<String> values = new HashSet<>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
            values.add("hi" + i);
        }
        //keys are there
        Assertions.assertEquals(455, b.size());
        Set<String> keySet = b.keySet();
        Assertions.assertTrue(values.containsAll(keySet));
        Assertions.assertTrue(keySet.containsAll(values));
    }

    /**
     * Test for general functionality and that the properties of Maps hold.
     */
    @Test
    public void functionalityTest() {
        functionalityTest(new MyHashMap<>(), new MyHashMap<>());
    }

    public static void functionalityTest(MyHashMap<String, String> dictionary,
                                         MyHashMap<String, Integer> studentIDs) {
        Assertions.assertEquals(0, dictionary.size());

        // can put objects in dictionary and get them
        dictionary.put("hello", "world");
        Assertions.assertTrue(dictionary.containsKey("hello"));
        Assertions.assertEquals("world", dictionary.get("hello"));
        Assertions.assertEquals(1, dictionary.size());

        // putting with existing key updates the value
        dictionary.put("hello", "kevin");
        Assertions.assertEquals(1, dictionary.size());
        Assertions.assertEquals("kevin", dictionary.get("hello"));

        // putting key in multiple times does not affect behavior
        studentIDs.put("sarah", 12345);
        Assertions.assertEquals(1, studentIDs.size());
        Assertions.assertEquals(12345, studentIDs.get("sarah").intValue());
        studentIDs.put("alan", 345);
        Assertions.assertEquals(2, studentIDs.size());
        Assertions.assertEquals(12345, studentIDs.get("sarah").intValue());
        Assertions.assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("alan", 345);
        Assertions.assertEquals(2, studentIDs.size());
        Assertions.assertEquals(12345, studentIDs.get("sarah").intValue());
        Assertions.assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("alan", 345);
        Assertions.assertEquals(2, studentIDs.size());
        Assertions.assertEquals(12345, studentIDs.get("sarah").intValue());
        Assertions.assertEquals(345, studentIDs.get("alan").intValue());
        Assertions.assertTrue(studentIDs.containsKey("sarah"));
        Assertions.assertTrue(studentIDs.containsKey("alan"));

        // handle values being the same
        Assertions.assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("evil alan", 345);
        Assertions.assertEquals(345, studentIDs.get("evil alan").intValue());
        Assertions.assertEquals(studentIDs.get("evil alan"), studentIDs.get("alan"));
    }

}
