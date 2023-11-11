/*
 * CacheDataSourceListener.java (c) 20 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.11.7
 */

package com.cp.common.util.cache;

import java.util.EventListener;

public interface CacheDataSourceListener extends EventListener {

  /**
   * The cache implementation calls upon this interface to notify the data source
   * via CacheEvents to load Cacheable items to be stored in the cache.
   * @param evt the CacheEvent object storing information about the cache and
   * the Cacheable item to load.
   * @throws com.cp.common.util.cache.CacheException if the data source is unable load the Cacheable object
   * encapsulated by the CacheEvent.
   */
  public void loadObject(CacheEvent evt) throws CacheException;

}
