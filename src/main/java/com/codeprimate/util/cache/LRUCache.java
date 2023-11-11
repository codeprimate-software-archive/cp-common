/*
 * LRUCache.java (c) 17 April 2002
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

public class LRUCache extends com.codeprimate.util.cache.AbstractCache {

  private static final Logger logger = Logger.getLogger(LRUCache.class);

  private static final String DESCRIPTION = "Least Recently Used (LRU) Cache";

  private final Map cache;

  /**
   * Creates an instance of the LRUCache to cache objects using the
   * Least Recently Used algorithm.
   */
  public LRUCache() {
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
  protected final Cacheable getCacheableInstance(final String key, final Object data, final Calendar expirationDate) {
    return new LRUCacheable(key, data, expirationDate);
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
   * Cleans the cache using Least Recently Used algorithm to remove Cacheable
   * objects based on their expiration date/time and timeStamp.
   */
  protected final void purgeCache() {
    for (Iterator it = iterateCacheables(); it.hasNext(); ) {
      final Cacheable cacheObject = (Cacheable) it.next();
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
      Collections.sort(cacheList, LRUComparator.INSTANCE);

      for (int index = 0, len = (getSize() - getMaxSize()); index < len; index++) {
        cache.remove(((Cacheable) cacheList.get(index)).getKey());
      }
    }
  }

  /**
   * Reads the Cacheable object from this cache based on the rules of the LRU
   * algorithm.
   * @param cacheable the Cacheable item to read from this cache.
   * @return the Cacheable object read from this cache.
   */
  protected synchronized final Cacheable _readCacheable(final Cacheable cacheable) {
    final LRUCacheable cacheObject = (LRUCacheable) cacheable;

    // Set the timeStamp on the Cacheable object to now.
    cacheObject.setTimeStamp(Calendar.getInstance());

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
   * Writes the Cacheable object into this cache using the rules of the LRU
   * algorithm.
   * @param cacheObject the Cacheable object being written to this cache.
   * @throws CacheException if the Cacheable object could not be written to
   * this cache, or validation of the Cacheable item failed.
   */
  protected final void _writeCacheable(Cacheable cacheObject) throws com.cp.common.util.cache.CacheException {
    // Make defensive copy of Cacheable object (for security reasons).
    cache.put(cacheObject.getKey(), new LRUCacheable(cacheObject));
  }

  private final class LRUCacheable extends CPAbstractCacheable {

    private Calendar timeStamp = Calendar.getInstance();

    private LRUCacheable(final String key, final Object data) {
      super(key, data);
    }

    private LRUCacheable(final String key, final Object data, final Calendar expirationDate) {
      super(key, data, expirationDate);
    }

    private LRUCacheable(final Cacheable cacheObject) {
      super(cacheObject);
    }

    /**
     * Returns the date/time when this Cacheable object was last accessed.
     * @return a Calendar object signifying when this Cacheable object was last
     * read.
     */
    public Calendar getTimeStamp() {
      return timeStamp;
    }

    /**
     * Sets the data/time when this Cacheable object was last accessed.
     * @param timeStamp a Calendar object specifying when this Cacheable object
     * was last accessed.
     */
    public void setTimeStamp(final Calendar timeStamp) {
      if (logger.isDebugEnabled()) {
        logger.debug("timeStamp (" + timeStamp + ")");
      }
      this.timeStamp = timeStamp;
    }
  }

  private static final class LRUComparator implements Comparator {

    static final LRUComparator INSTANCE = new LRUComparator();

    public int compare(final Object o1,
                       final Object o2 ) {
      final LRUCacheable c1 = (LRUCacheable) o1;
      final LRUCacheable c2 = (LRUCacheable) o2;

      // Base order on timeStamp
      final Calendar timeStamp1 = c1.getTimeStamp();
      final Calendar timeStamp2 = c2.getTimeStamp();

      int timeStampDiff = compareCalendar(timeStamp1, timeStamp2);
      if (logger.isDebugEnabled()) {
        logger.debug("timeStampDiff (" + timeStampDiff + ")");
      }

      if (!NumberUtil.isZero(timeStampDiff)) {
        return timeStampDiff;
      }

      // If the timeStamp of two Cacheable objects are equal, base the order on the
      // expiration date.
      return compareCalendar(c1.getExpirationDate(), c2.getExpirationDate());
    }
  }

}
