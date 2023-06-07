package hashmap;

import org.junit.jupiter.api.Test;

/**
 * Tests by Brendan Hu, Spring 2015
 * Revised for 2016 by Josh Hug
 * Revised for 2021 by Neil Kulkarni
 *
 * @author yang
 */
public class MyHashMapBucketsTest {

    @Test
    public void sanityGenericsTest() {
        MyHashMap<String, Integer> a = new MyHashMapALBuckets<>();
        MyHashMap<String, Integer> b = new MyHashMapALBuckets<>();
        MyHashMap<Integer, String> c = new MyHashMapALBuckets<>();
        MyHashMap<Boolean, Integer> d = new MyHashMapALBuckets<>();

        a = new MyHashMapLLBuckets<>();
        b = new MyHashMapLLBuckets<>();
        c = new MyHashMapLLBuckets<>();
        d = new MyHashMapLLBuckets<>();

        a = new MyHashMapTSBuckets<>();
        b = new MyHashMapTSBuckets<>();
        c = new MyHashMapTSBuckets<>();
        d = new MyHashMapTSBuckets<>();

        a = new MyHashMapHSBuckets<>();
        b = new MyHashMapHSBuckets<>();
        c = new MyHashMapHSBuckets<>();
        d = new MyHashMapHSBuckets<>();

        a = new MyHashMapPQBuckets<>();
        b = new MyHashMapPQBuckets<>();
        c = new MyHashMapPQBuckets<>();
        d = new MyHashMapPQBuckets<>();
    }

    /**
     * assumes put/size/containsKey/get work
     */
    @Test
    public void sanityClearTest() {
        MyHashMapTest.sanityClearTest(new MyHashMapALBuckets<>());
        MyHashMapTest.sanityClearTest(new MyHashMapLLBuckets<>());
        MyHashMapTest.sanityClearTest(new MyHashMapTSBuckets<>());
        MyHashMapTest.sanityClearTest(new MyHashMapHSBuckets<>());
        MyHashMapTest.sanityClearTest(new MyHashMapPQBuckets<>());
    }

    /**
     * assumes put works
     */
    @Test
    public void sanityContainsKeyTest() {
        MyHashMapTest.sanityContainsKeyTest(new MyHashMapALBuckets<>());
        MyHashMapTest.sanityContainsKeyTest(new MyHashMapLLBuckets<>());
        MyHashMapTest.sanityContainsKeyTest(new MyHashMapTSBuckets<>());
        MyHashMapTest.sanityContainsKeyTest(new MyHashMapHSBuckets<>());
        MyHashMapTest.sanityContainsKeyTest(new MyHashMapPQBuckets<>());
    }

    /**
     * assumes put works
     */
    @Test
    public void sanityGetTest() {
        MyHashMapTest.sanityGetTest(new MyHashMapALBuckets<>());
        MyHashMapTest.sanityGetTest(new MyHashMapLLBuckets<>());
        MyHashMapTest.sanityGetTest(new MyHashMapTSBuckets<>());
        MyHashMapTest.sanityGetTest(new MyHashMapHSBuckets<>());
        MyHashMapTest.sanityGetTest(new MyHashMapPQBuckets<>());
    }

    /**
     * assumes put works
     */
    @Test
    public void sanitySizeTest() {
        MyHashMapTest.sanitySizeTest(new MyHashMapALBuckets<>());
        MyHashMapTest.sanitySizeTest(new MyHashMapLLBuckets<>());
        MyHashMapTest.sanitySizeTest(new MyHashMapTSBuckets<>());
        MyHashMapTest.sanitySizeTest(new MyHashMapHSBuckets<>());
        MyHashMapTest.sanitySizeTest(new MyHashMapPQBuckets<>());
    }

    /**
     * assumes get/contains key work
     */
    @Test
    public void sanityPutTest() {
        MyHashMapTest.sanityPutTest(new MyHashMapALBuckets<>());
        MyHashMapTest.sanityPutTest(new MyHashMapLLBuckets<>());
        MyHashMapTest.sanityPutTest(new MyHashMapTSBuckets<>());
        MyHashMapTest.sanityPutTest(new MyHashMapHSBuckets<>());
        MyHashMapTest.sanityPutTest(new MyHashMapPQBuckets<>());
    }

    @Test
    public void sanityKeySetTest() {
        MyHashMapTest.sanityKeySetTest(new MyHashMapALBuckets<>());
        MyHashMapTest.sanityKeySetTest(new MyHashMapLLBuckets<>());
        MyHashMapTest.sanityKeySetTest(new MyHashMapTSBuckets<>());
        MyHashMapTest.sanityKeySetTest(new MyHashMapHSBuckets<>());
        MyHashMapTest.sanityKeySetTest(new MyHashMapPQBuckets<>());
    }

    /**
     * Test for general functionality and that the properties of Maps hold.
     */
    @Test
    public void functionalityTest() {
        MyHashMapTest.functionalityTest(new MyHashMapALBuckets<>(), new MyHashMapALBuckets<>());
        MyHashMapTest.functionalityTest(new MyHashMapLLBuckets<>(), new MyHashMapLLBuckets<>());
        MyHashMapTest.functionalityTest(new MyHashMapTSBuckets<>(), new MyHashMapTSBuckets<>());
        MyHashMapTest.functionalityTest(new MyHashMapHSBuckets<>(), new MyHashMapHSBuckets<>());
        MyHashMapTest.functionalityTest(new MyHashMapPQBuckets<>(), new MyHashMapPQBuckets<>());
    }

}
