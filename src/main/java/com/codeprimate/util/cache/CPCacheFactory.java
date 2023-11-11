/*
 * CPCacheFactory.java (c) 17 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.13
 */

package com.codeprimate.util.cache;

import com.cp.common.util.cache.AbstractCacheFactory;
import com.cp.common.util.cache.Cache;
import com.cp.common.util.cache.CacheType;

public class CPCacheFactory extends AbstractCacheFactory {

  /**
   * Returns an Cache implemenation implementing a specified cache algorithm based on the
   * CacheType.
   * @param cacheType an Object describing the type of cache implementation or algorithm
   * used.
   * @return a implentation of the Cache interface defined by the Code Primate Service
   * Provider for the specified cache type.
   */
  public Cache getCacheInstance(final com.cp.common.util.cache.CacheType cacheType) {
    return (cacheType.equals(CacheType.LFU) ? getLFUCacheInstance() : getLRUCacheInstance());
  }

  /**
   * Returns a Cache implementation implementing the Least Frequently Used algorithm.
   * @return a LFU Cache implementation.
   */
  public com.cp.common.util.cache.Cache getLFUCacheInstance() {
    return new LFUCache();
  }

  /**
   * Returns a Cache implementation implementing the Least Recently Used algorithm.
   * @return a LRU Cache implementation.
   */
  public com.cp.common.util.cache.Cache getLRUCacheInstance() {
    return new LRUCache();
  }

}
