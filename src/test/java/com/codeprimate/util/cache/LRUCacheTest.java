/*
 * LRUCacheTest.java (c) 23 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.2.27
 */

package com.codeprimate.util.cache;

import com.cp.common.util.cache.AbstractCacheFactory;
import com.codeprimate.util.cache.*;
import com.codeprimate.util.cache.LRUCache;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class LRUCacheTest extends com.codeprimate.util.cache.AbstractCacheTest {

  private static final Logger logger = Logger.getLogger(LRUCacheTest.class);

  public LRUCacheTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LRUCacheTest.class);
    //suite.addTest(new LRUCacheTest("testName"));
    return suite;
  }

  public void testLRUCache() throws Exception {
    final User jonDoe = new User(new Integer(101), "Jon", "Doe");
    final User janeDoe = new User(new Integer(202), "Jane", "Doe");
    final User jackHandy = new User(new Integer(303), "Jack", "Handy");
    final User sandyHandy = new User(new Integer(404), "Sandy", "Handy");
    final User jakeSimpson = new User(new Integer(505), "Jake", "Simpson");
    final User danRather = new User(new Integer(606), "Dan", "Rather");
    final User tonyPark = new User(new Integer(707), "Tony", "Park");

    // Configure an LFU Cache instance.
    final LRUCache cache = (LRUCache) AbstractCacheFactory.getInstance().getLRUCacheInstance();
    cache.setMaxSize(4);

    assertEquals(4, cache.getMaxSize());
    assertEquals(0, cache.getSize());

    // Do write operations...
    cache.writeCacheable(jackHandy);
    cache.writeCacheable(sandyHandy);
    cache.writeCacheable(danRather);

    assertEquals(3, cache.getSize());
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(sandyHandy));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(tonyPark));

    cache.purgeCache(); // purge the cache

    assertEquals(3, cache.getSize());
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(sandyHandy));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(jonDoe));

    // Do read and write operations...
    cache.readCacheable(danRather.getKey());
    Thread.sleep(200);
    cache.writeCacheable(jakeSimpson);
    Thread.sleep(200);
    cache.writeCacheable(tonyPark);
    Thread.sleep(200);
    readCacheable(cache, jackHandy, 20);

    cache.purgeCache(); // purge the cache

    assertEquals(4, cache.getSize());
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(tonyPark));
    assertTrue(cache.containsCacheable(jakeSimpson));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(jonDoe));
    assertFalse(cache.containsCacheable(janeDoe));
    assertFalse(cache.containsCacheable(sandyHandy));

    cache.setMaxSize(2); // shrink the size

    assertEquals(2, cache.getMaxSize());
    assertEquals(4, cache.getSize());

    readCacheable(cache, jackHandy, 29);
    Thread.sleep(200);
    cache.writeCacheable(jonDoe);
    Thread.sleep(200);
    readCacheable(cache, jakeSimpson, 10);

    cache.purgeCache(); // purge the cache.

    assertTrue(cache.containsCacheable(jakeSimpson));
    assertTrue(cache.containsCacheable(jonDoe));
    assertFalse(cache.containsCacheable(janeDoe));
    assertFalse(cache.containsCacheable(jackHandy));
    assertFalse(cache.containsCacheable(sandyHandy));
    assertFalse(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(tonyPark));

    cache.setMaxSize(3); // grow the cache.

    assertEquals(3, cache.getMaxSize());
    assertEquals(2, cache.getSize());

    cache.writeCacheable(janeDoe);
    Thread.sleep(200);
    readCacheable(cache, jonDoe, 51);
    Thread.sleep(200);
    cache.writeCacheable(tonyPark);
    Thread.sleep(200);
    readCacheable(cache, janeDoe, 2);
    Thread.sleep(200);
    cache.writeCacheable(danRather);
    Thread.sleep(200);
    cache.writeCacheable(sandyHandy);
    Thread.sleep(200);
    readCacheable(cache, janeDoe, 2);

    cache.purgeCache(); // purge the cache

    assertEquals(3, cache.getSize());
    assertTrue(cache.containsCacheable(janeDoe));
    assertTrue(cache.containsCacheable(sandyHandy));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(jonDoe));
    assertFalse(cache.containsCacheable(jackHandy));
    assertFalse(cache.containsCacheable(jakeSimpson));
    assertFalse(cache.containsCacheable(tonyPark));

    cache.writeCacheable(jonDoe);
    Thread.sleep(200);
    cache.writeCacheable(tonyPark);
    readCacheable(cache, tonyPark, 16);

    cache.purgeCache();  // purge the cache

    assertEquals(3, cache.getSize());
    assertTrue(cache.containsCacheable(tonyPark));
    assertTrue(cache.containsCacheable(jonDoe));
    assertTrue(cache.containsCacheable(janeDoe));
    assertFalse(cache.containsCacheable(jackHandy));
    assertFalse(cache.containsCacheable(sandyHandy));
    assertFalse(cache.containsCacheable(jakeSimpson));
    assertFalse(cache.containsCacheable(danRather));
  }

}
