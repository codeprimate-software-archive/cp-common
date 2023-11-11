/*
 * LFUCache.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.codeprimate.util.cache;

import com.cp.common.lang.NumberUtil;
import com.cp.common.util.cache.CacheException;
import com.cp.common.util.cache.Cacheable;
import com.codeprimate.util.cache.*;
import com.codeprimate.util.cache.CacheableAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class LFUCache extends com.codeprimate.util.cache.AbstractCache {

  private static final Logger logger = Logger.getLogger(LFUCache.class);

  private static final String DESCRIPTION = "Least Frequently Used (LFU) Cache";

  // The cache implementation is a Map implementation.
  private final Map cache;

  /**
   * Creates an instance of the LFUCache object to cache objects using the
   * Least Freguently Used algorithm.
   */
  public LFUCache() {
    cache = new HashMap(getMaxSize(), 100.0f);
  }

  /**
   * Returns the Cacheable object stored in this cache referenced by the specified
   * key.
   * @param key a String value referring to the Cacheable object in this cache.
   * @return the Cacheable object stored in this cache and referrenced by the
   * specified key (lookup value).
   */
  final CPAbstractCacheable getCacheable(final String key) {
    return (CPAbstractCacheable) cache.get(key);
  }

  /**
   * Creates a concrete instance of the Cacheable interface initialized with the
   * specified key, data value and expiration date.
   * @param key the key used as a lookup value to the Cacheable object in this cache.
   * @param data the data to be stored in the cache wrapped with the Cacheable object.
   * @param expirationDate a Calendar object specifying the date at which the data
   * in the Cacheable object is no longer valid.
   * @return a Cacheable object with the specified key, containing the data.
   */
  protected final com.cp.common.util.cache.Cacheable getCacheableInstance(final String key, final Object data, final Calendar expirationDate) {
    return new LFUCacheable(key, data, expirationDate);
  }

  /**
   * Returns a String description of this Cache.
   * @return a String description of this Cache.
   */
  public final String getDescription() {
    return DESCRIPTION;
  }

  /**
   * Returns the current number of Cacheable objects in this cache.
   * @return an integer value of the number of Cacheable objects stored in this
   * cache.
   */
  public int getSize() {
    return cache.size();
  }

  /**
   * Returns an Iterator to traverse the Cacheable objects in this cache.
   * @return a java.util.Iterator object to iterate the Cacheable objects in this
   * cache.
   */
  public Iterator iterateCacheables() {
    final Iterator it = cache.values().iterator();
    return new Iterator() {
      public boolean hasNext() {
        return it.hasNext();
      }

      public Object next() {
        return new com.codeprimate.util.cache.CacheableAdapter((Cacheable) it.next());
      }

      public void remove() {
        throw new UnsupportedOperationException("Not Implemented!");
      }
    };
  }

  /**
   * Cleans the cache using Least Frequently Used algorithm to remove Cacheable
   * objects based on their expiration date/time and access frequency.
   */
  protected final void purgeCache() {
    for (Iterator it = iterateCacheables(); it.hasNext(); ) {
      final Cacheable cacheObject = (com.cp.common.util.cache.Cacheable) it.next();
      if (cacheObject.hasExpired()) {
        if (logger.isDebugEnabled()) {
          logger.debug("The Cacheable object (" + cacheObject + ") has expired!");
        }
        cache.remove(cacheObject.getKey());
      }
    }

    if (getSize() > getMaxSize()) {
      final List cacheList = new ArrayList(cache.values());

      // Least Frequently Used (LFU) Cacheable objects will be at the front of the List
      // after the LFUComparator sorts the List.
      Collections.sort(cacheList, LFUComparator.INSTANCE);

      for (int index = 0, len = (getSize() - getMaxSize()); index < len; index++) {
        cache.remove(((com.cp.common.util.cache.Cacheable) cacheList.get(index)).getKey());
      }
    }
  }

  /**
   * Reads the Cacheable object from this cache based on the rules of the LFU
   * algorithm.
   * @param cacheable the Cacheable item to read from this cache.
   * @return the Cacheable object read from this cache.
   */
  protected final com.cp.common.util.cache.Cacheable _readCacheable(final Cacheable cacheable) {
    final LFUCacheable cacheObject = (LFUCacheable) cacheable;

    // Increment the hit count for the cacheObject.  This line of code is important
    // since it is the foundation to the LFU algorithm.
    cacheObject.setFrequency(cacheObject.getFrequency() + 1);

    // Make defensive copy and return Cacheable object.
    return new com.codeprimate.util.cache.CacheableAdapter(cacheObject);
  }

  /**
   * Returns a String representation of this cache.
   * @return a String representation of this cache.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{description = ");
    buffer.append(getDescription());
    buffer.append(", maxSize = ").append(getMaxSize());
    buffer.append(", size = ").append(getSize());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * Writes the Cacheable object into this cache using the rules of the LFU
   * algorithm.
   * @param cacheObject the Cacheable object being written to this cache.
   * @throws com.cp.common.util.cache.CacheException if the Cacheable object could not be written to
   * this cache, or validation of the Cacheable item failed.
   */
  protected final void _writeCacheable(final Cacheable cacheObject) throws com.cp.common.util.cache.CacheException {
    // Make defensive copy of Cacheable object (for security reasons).
    final LFUCacheable newCacheObject = new LFUCacheable(cacheObject);

    // Lookup cacheObject in cache and determine if it already exists.
    if (containsKey(cacheObject.getKey())) {
      if (logger.isDebugEnabled()) {
        logger.debug("The key (" + cacheObject.getKey() + ") exists in this cache.");
      }
      final LFUCacheable existingCacheObject = (LFUCacheable) cache.get(cacheObject.getKey());
      newCacheObject.setFrequency(existingCacheObject.getFrequency());
    }

    // Write to cache...
    cache.put(newCacheObject.getKey(), newCacheObject);
  }

  private final class LFUCacheable extends CPAbstractCacheable {

    private int frequency = 0;

    private LFUCacheable(final String key, final Object data) {
      super(key, data);
    }

    private LFUCacheable(final String key, final Object data, final Calendar expirationDate) {
      super(key, data, expirationDate);
    }

    private LFUCacheable(final Cacheable cacheObject) {
      super(cacheObject);
    }

    /**
     * Returns the number of hits (reads) on the data contained by this Cacheable
     * object.
     * @return an integer value of the number of hits (reads) on this data.
     */
    public int getFrequency() {
      return frequency;
    }

    /**
     * Sets the number of hits on the data contained by this Cacheable object.
     * @param frequency is the number of hits on the data contained by this
     * Cache.
     */
    public void setFrequency(final int frequency) {
      if (logger.isDebugEnabled()) {
        logger.debug("frequency (" + frequency + ")");
      }
      this.frequency = frequency;
    }
  }

  private static final class LFUComparator implements Comparator {

    static final LFUComparator INSTANCE = new LFUComparator();

    public int compare(final Object o1,
                       final Object o2 ) {
      final LFUCacheable co1 = (LFUCacheable) o1;
      final LFUCacheable co2 = (LFUCacheable) o2;

      // Base order on frequency.
      final int frequencyDiff = (co1.getFrequency() - co2.getFrequency());
      if (logger.isDebugEnabled()) {
        logger.debug("frequencyDiff (" + frequencyDiff + ")");
      }

      if (!NumberUtil.isZero(frequencyDiff)) {
        return frequencyDiff;
      }

      // If the frequencies of two Cacheable objects are equal, base the order on the
      // expiration date.
      return compareCalendar(co1.getExpirationDate(), co2.getExpirationDate());
    }
  }

}
