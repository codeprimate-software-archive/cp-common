/*
 * LFUCacheTest.java (c) 23 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.2.27
 */

package com.codeprimate.util.cache;

import com.cp.common.util.cache.AbstractCacheFactory;
import com.codeprimate.util.cache.*;
import com.codeprimate.util.cache.LFUCache;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class LFUCacheTest extends com.codeprimate.util.cache.AbstractCacheTest {

  private static final Logger logger = Logger.getLogger(LFUCacheTest.class);

  public LFUCacheTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LFUCacheTest.class);
    //suite.addTest(new LFUCacheTest("testName"));
    return suite;
  }

  public void testLFUCache() throws Exception {
    final User jonDoe = new User(new Integer(101), "Jon", "Doe");
    final User janeDoe = new User(new Integer(202), "Jane", "Doe");
    final User jackHandy = new User(new Integer(303), "Jack", "Handy");
    final User sandyHandy = new User(new Integer(404), "Sandy", "Handy");
    final User jakeSimpson = new User(new Integer(505), "Jake", "Simpson");
    final User danRather = new User(new Integer(606), "Dan", "Rather");
    final User tonyPark = new User(new Integer(707), "Tony", "Park");

    // Configure an LFU Cache instance.
    final LFUCache cache = (LFUCache) com.cp.common.util.cache.AbstractCacheFactory.getInstance().getLFUCacheInstance();
    cache.setMaxSize(5);

    assertEquals(5, cache.getMaxSize());
    assertEquals(0, cache.getSize());

    cache.writeCacheable(jonDoe);
    cache.writeCacheable(jackHandy);
    cache.writeCacheable(jakeSimpson);
    cache.writeCacheable(danRather);

    assertEquals(4, cache.getSize());
    assertTrue(cache.containsCacheable(jonDoe));
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(jakeSimpson));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(janeDoe));

    cache.purgeCache(); // purge the cache

    assertEquals(4, cache.getSize());
    assertTrue(cache.containsCacheable(jonDoe));
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(jakeSimpson));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(sandyHandy));

    // Invoke read and write operations...
    readCacheable(cache, jackHandy, 100);
    cache.writeCacheable(sandyHandy);
    readCacheable(cache, sandyHandy, 50);
    readCacheable(cache, jakeSimpson, 10);
    readCacheable(cache, jonDoe, 5);
    readCacheable(cache, danRather, 2);
    cache.writeCacheable(janeDoe);

    assertEquals(6, cache.getSize());
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(sandyHandy));
    assertTrue(cache.containsCacheable(jakeSimpson));
    assertTrue(cache.containsCacheable(jonDoe));
    assertTrue(cache.containsCacheable(danRather));
    assertTrue(cache.containsCacheable(janeDoe));
    assertFalse(cache.containsCacheable(tonyPark));

    cache.purgeCache(); // purge the cache

    assertEquals(5, cache.getSize());
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(sandyHandy));
    assertTrue(cache.containsCacheable(jakeSimpson));
    assertTrue(cache.containsCacheable(jonDoe));
    assertTrue(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(janeDoe));
    assertFalse(cache.containsCacheable(tonyPark));

    cache.setMaxSize(3); // shrink the cache

    assertEquals(3, cache.getMaxSize());
    assertEquals(5, cache.getSize());

    // Invoke read and write operations...
    readCacheable(cache, jackHandy, 50);
    cache.writeCacheable(tonyPark);
    readCacheable(cache, tonyPark, 121);
    cache.writeCacheable(janeDoe);
    readCacheable(cache, janeDoe, 88);
    readCacheable(cache, sandyHandy, 25);

    cache.purgeCache(); // purge the cache

    assertEquals(3, cache.getSize());
    assertTrue(cache.containsCacheable(jackHandy));
    assertTrue(cache.containsCacheable(tonyPark));
    assertTrue(cache.containsCacheable(janeDoe));
    assertFalse(cache.containsCacheable(jonDoe));
    assertFalse(cache.containsCacheable(sandyHandy));
    assertFalse(cache.containsCacheable(danRather));
    assertFalse(cache.containsCacheable(jakeSimpson));

    cache.setMaxSize(4); // grow the cache

    assertEquals(4, cache.getMaxSize());
    assertEquals(3, cache.getSize());

    // Invoke read and write operations...
    cache.writeCacheable(jonDoe);
    readCacheable(cache, jonDoe, 200);
    cache.writeCacheable(janeDoe);
    readCacheable(cache, janeDoe, 200);
    cache.writeCacheable(danRather);
    readCacheable(cache, danRather, 161);
    readCacheable(cache, tonyPark, 39);
    readCacheable(cache, jackHandy, 5);
    cache.writeCacheable(sandyHandy);
    readCacheable(cache, sandyHandy, 125);

    assertEquals(6, cache.getSize());

    cache.purgeCache(); // purge the cache

    assertEquals(4, cache.getSize());
    assertTrue(cache.containsCacheable(jonDoe));
    assertTrue(cache.containsCacheable(janeDoe));
    assertTrue(cache.containsCacheable(danRather));
    assertTrue(cache.containsCacheable(tonyPark));
    assertFalse(cache.containsCacheable(jackHandy));
    assertFalse(cache.containsCacheable(sandyHandy));
    assertFalse(cache.containsCacheable(jakeSimpson));
  }

}
