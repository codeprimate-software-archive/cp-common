/*
 * CacheEvent.java (c) 20 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.2.23
 */

package com.cp.common.util.cache;

import com.cp.common.util.cache.*;
import com.cp.common.util.cache.Cache;
import com.cp.common.util.cache.Cacheable;
import java.util.EventObject;
import org.apache.log4j.Logger;

public class CacheEvent extends EventObject {

  private static final Logger logger = Logger.getLogger(CacheEvent.class);

  private final Cacheable cacheObject;

  /**
   * Creates an instance of the CacheEvent class associating a Cache object
   * with a Cacheable object to be used by the data source to load the
   * Cacheable object and store it in the Cache.
   * @param source the reference to the Cache.
   * @param cacheObject the specified Cacheable item to load and store in the
   * Cache.
   */
  public CacheEvent(final Cache source,
                    final Cacheable cacheObject) {
    super(source);
    this.cacheObject = cacheObject;
  }

  /**
   * Returns the reference to the Cache.
   * @return a reference to the Cache.
   */
  public Cache getCache() {
    return (com.cp.common.util.cache.Cache) getSource();
  }

  /**
   * Returns a reference to the Cacheable object that will loaded by the
   * data source.
   * @return a reference to the Cacheable object.
   */
  public Cacheable getCacheable() {
    return cacheObject;
  }

  /**
   * Return a String representation of this CacheEvent.
   * @return a String representation of this CacheEvent.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{cache = ");
    buffer.append(getCache());
    buffer.append(", cacheable = ").append(getCacheable());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
