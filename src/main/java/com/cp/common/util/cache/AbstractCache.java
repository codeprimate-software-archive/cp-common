/*
 * AbstractCache.java (c) 17 April 2002
 *
 * This class is an abstraction of a Cache implementation and it's algorithm
 * for storing and managing Objects in memory.
 *
 * Implementing classes that subclass this AbstractCache class should provide
 * implementations of the following methods
 *
 * <code>
 *    getCacheableInstance
 *    getSize
 *    invalidateCache
 *    iterateCacheables
 *    purgeCache
 *    readCacheable
 *    writeCacheable
 * </code>
 *
 * which are consistent with the cache algorithm.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.17
 * @see com.cp.common.util.Cache
 */

package com.cp.common.util.cache;

import com.cp.common.lang.NumberUtil;
import com.cp.common.lang.ObjectUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import javax.swing.event.EventListenerList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractCache implements Cache {

  private static final int DEFAULT_MAX_SIZE = 256; // 256 Objects
  private static final int DEFAULT_PURGE_INTERVAL = 120000; // 2 minutes

  private int maxSize = DEFAULT_MAX_SIZE;
  private int purgeInterval = DEFAULT_PURGE_INTERVAL;

  private EventListenerList eventListenerList;

  protected final Log log = LogFactory.getLog(getClass());

  private final Thread purgeTx;

  /**
   * Instantiates AbstractCache by starting a Thread to run purge operations on this
   * cache.
   */
  public AbstractCache() {
    eventListenerList = new EventListenerList();
    purgeTx = new Thread(new PurgeCacheTx());
    purgeTx.setDaemon(true);
    purgeTx.start();
  }

  /**
   * Adds the CacheDataSourceListener object to the list of listeners listening for
   * CacheEvents.
   * @param dsl the CacheDataSourceListener which represents the  data store for the
   * data kept in this cache.
   */
  public void addCacheDataSourceListener(final CacheDataSourceListener dsl) {
    if (ObjectUtil.isNotNull(dsl)) {
      log.debug("adding CacheDataSourceListener (" + dsl.getClass().getName() + ")");
      eventListenerList.add(com.cp.common.util.cache.CacheDataSourceListener.class, dsl);
    }
  }

  /**
   * Determines whether the specified Cacheable object is stored in this cache.
   * @param cacheObject the Cacheable object in question of being contained
   * by this class.
   * @return a boolean value whether the Cacheable object exists in this
   * cache.
   */
  public boolean containsCacheable(final Cacheable cacheObject) {
    if (log.isDebugEnabled()) {
      log.debug("cacheObject (" + cacheObject + ")");
    }

    for (Iterator it = iterateCacheables(); it.hasNext(); ) {
      final com.cp.common.util.cache.Cacheable theCacheable = (Cacheable) it.next();
      if (theCacheable.equals(cacheObject)) {
        if (log.isDebugEnabled()) {
          log.debug("contains cacheObject");
        }
        return true;
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("does NOT contain cacheObject");
    }

    return false;
  }

  /**
   * Determines whether a Cacheable object is mapped to the specified key in
   * this cache.
   * @param key the String value used to determine whether it maps to a
   * Cacheable object in the cache.
   * @return a boolean value indicating if the key is mapped to a Cacheable
   * object in the cache.
   */
  public boolean containsKey(final String key) {
    if (log.isDebugEnabled()) {
      log.debug("key (" + key + ")");
    }

    for (Iterator it = iterateKeys(); it.hasNext(); ) {
      final String theKey = it.next().toString();
      if (theKey.equals(key)) {
        if (log.isDebugEnabled()) {
          log.debug("contains key (" + key + ")");
        }
        return true;
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("does NOT contain key (" + key + ")");
    }

    return false;
  }

  /**
   * Determines whether the specified Object value is stored in this cache.
   * @param obj the Object value used in determining whether it is stored by
   * this cache.
   * @return a boolean value indicating whether the Object value is stored
   * in this cache (wrapped in a Cacheable object and mapped to a key
   * maintained in this cache).
   */
  public boolean containsObject(final Object obj) {
    if (log.isDebugEnabled()) {
      log.debug("obj (" + obj + ")");
    }

    for (Iterator it = iterateObjects(); it.hasNext(); ) {
      final Object theData = it.next();
      if (theData.equals(obj)) {
        if (log.isDebugEnabled()) {
          log.debug("contains obj");
        }
        return true;
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("does NOT contain obj");
    }

    return false;
  }

  /**
   * Called by the Garbage Collector (gc) to reclaim any resources held by this
   * Cache object.
   * @throws Throwable if the finalize operation fails.
   */
  protected final void finalize() throws Throwable {
    super.finalize();
    purgeTx.interrupt();
    invalidateCache();
    purgeCache();
  }

  /**
   * Fires a CacheEvent to the specified CacheDataSourceListeners that registered
   * themselves with this cache.
   * @param cacheObject the Cacheable object for which the event was generated.
   * @throws com.cp.common.util.cache.CacheException if one of the listeners throws an Exception while
   * processing the Cacheable object.
   */
  protected void fireCacheEvent(final Cacheable cacheObject) throws com.cp.common.util.cache.CacheException {
    if (ObjectUtil.isNull(cacheObject)) {
      log.warn("The cacheObejct for the CacheEvent cannot be null!");
      throw new NullPointerException("The cacheObejct for the CacheEvent cannot be null!");
    }

    if (eventListenerList.getListenerCount() > 0) {
      final com.cp.common.util.cache.CacheEvent event = new com.cp.common.util.cache.CacheEvent(this, cacheObject);
      if (log.isDebugEnabled()) {
        log.debug("event: " + event);
      }

      for (Iterator it = Arrays.asList(eventListenerList.getListenerList()).iterator(); it.hasNext(); ) {
        final CacheDataSourceListener listener = (CacheDataSourceListener) it.next();
        if (log.isDebugEnabled()) {
          log.debug("Notifying CacheDataSourceListener (" + listener.getClass().getName() + ")");
        }
        listener.loadObject(event);
      }
    }
  }

  /**
   * Creates a concrete instance of the Cacheable interface initialized with the
   * specified key and data Object value.
   * @param key the key used as a lookup value to the Cacheable object in this cache.
   * @param data the data to be stored in the cache wrapped with the Cacheable object.
   * @return a Cacheable object with the specified key, containing the data.
   */
  protected com.cp.common.util.cache.Cacheable getCacheableInstance(final String key, final Object data) {
    return getCacheableInstance(key, data, null);
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
  protected abstract Cacheable getCacheableInstance(String key, Object data, Calendar expirationDate);

  /**
   * Returns the maximum number of Cacheable objects that this cache can contain
   * before the cache algorithm is called upon to determine which Cacheable
   * objects will be replaced by other Objects currently in use.
   * @return an integer value stating the upper limit on the number of Cacheable
   * objects this cache can hold.
   */
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * Returns the number of milliseconds that the purge thread will sleep for before
   * running the purge operation on this cache.
   * @return an integer value for the number of milliseconds that the purge thread
   * will sleep.
   */
  protected int getPurgeInterval() {
    return purgeInterval;
  }

  /**
   * Returns an Iterator to traverse the keys mapped to Cacheable objects in this
   * cache.
   * @return a java.util.Iterater object to iterator the keys mapping to Cacheable
   * objects in this cache.
   */
  public Iterator iterateKeys() {
    final Iterator cacheableIterator = iterateCacheables();
    return new Iterator() {
      public boolean hasNext() {
        return cacheableIterator.hasNext();
      }
      public Object next() {
        return ((com.cp.common.util.cache.Cacheable) cacheableIterator.next()).getKey();
      }
      public void remove() {
        throw new UnsupportedOperationException("Not Implemented!");
      }
    };
  }

  /**
   * Returns an Iterator to the stored data in this cache.
   * @return a java.util.Iterator object to iterate the data value Objects
   * contained in this cache.
   */
  public Iterator iterateObjects() {
    final Iterator cacheableIterator = iterateCacheables();
    return new Iterator() {
      public boolean hasNext() {
        return cacheableIterator.hasNext();
      }
      public Object next() {
        return ((com.cp.common.util.cache.Cacheable) cacheableIterator.next()).getData();
      }
      public void remove() {
        throw new UnsupportedOperationException("Not Implemented!");
      }
    };
  }

  /**
   * Cleans the cache by removing expired and/or invalid cache objects from the
   * cache.  This method is called from within the PurgeCacheTx thread every
   * 30 seconds.
   */
  protected abstract void purgeCache();

  /**
   * Reads the data Object value mapped to the specified key in this cache.
   * @param key the String value mapping the data Object in the cache.
   * @return the data value as a java.lang.Object for the specified key in
   * this cache, or null if the key is not mapped to an Object value in this
   * cache.
   */
  public Object readObject(final String key) throws com.cp.common.util.cache.CacheException {
    final com.cp.common.util.cache.Cacheable cacheObject = readCacheable(key);
    return (ObjectUtil.isNotNull(cacheObject) ? cacheObject.getData() : null);
  }

  /**
   * Removes the CacheDataSourceListener object from the list of listeners listening for
   * CacheEvents.
   * @param dsl the CacheDataSourceListener which represents the data store for the
   * data kept in this cache.
   */
  public void removeCacheDataSourceListener(final CacheDataSourceListener dsl) {
    if (ObjectUtil.isNotNull(dsl)) {
      log.debug("removing CacheDataSourceListener (" + dsl.getClass().getName() + ")");
      eventListenerList.remove(com.cp.common.util.cache.CacheDataSourceListener.class, dsl);
    }
  }

  /**
   * Sets the maximum number of Cacheable objects held by this cache.  Note, that
   * if the cache currently contains more Cacheable objects than the newly
   * specified value for maxSize, then on subsequent writes, Cacheable objects
   * will be replaced, using a deterministic caching algorithm, until the number
   * of Cacheable objects is less than or equal to maxSize.
   * @param maxSize the upper bound on the number of Cacheable objects allowed in
   * this cache.
   * @throws java.lang.IllegalArgumentException if the maxSize value is less than
   * or equal to zero.
   */
  public void setMaxSize(final int maxSize) {
    if (log.isDebugEnabled()) {
      log.debug("maxSize (" + maxSize + ")");
    }

    if (NumberUtil.isNegative(maxSize)) {
      log.warn(maxSize + " is not a valid max size for this cache.  Please specify non-negative whole value!");
      throw new IllegalArgumentException(maxSize + " is not a valid max size for this cache.  Please specify non-negative whole value!");
    }

    this.maxSize = maxSize;
  }

  /**
   * Set the specified time interval in which the purge operation will be run.
   * The interval is set in milliseconds and determines how long the PurgeThreadTx
   * will sleep before calling the purgeCache method again.
   * @param purgeInterval the number of milliseconds between purgeCache operations.
   */
  protected void setPurgeInterval(final int purgeInterval) {
    log.debug("purgeInterval = " + purgeInterval);
    if (purgeInterval < 0) {
      log.warn(purgeInterval + " is not a valid interval.  Please specify a positive whole value in milliseconds.");
      throw new IllegalArgumentException(purgeInterval + " is not a valid interval.  Please specify a positive whole value in milliseconds.");
    }
    this.purgeInterval = purgeInterval;
  }

  /**
   * Called to release any resources held by this Cache object.  If this method is
   * overridden, make sure to include a call to super.terminate at the beginning
   * of the method.
   */
  public void terminate() {
    try {
      finalize();
    }
    catch (Throwable t) {
      log.warn(t);
    }
  }

  /**
   * Stores the specified Object in this cache mapped to the specified key.
   * Note, the cacheObject will be wrapped in a Cacheable object implementation.
   * @param key the key used to map the cacheObject in this cache.
   * @param data the Object to wrap with an instance of the Cacheable
   * interface mapped to the specified key and stored in this cache.
   * @throws java.lang.NullPointerException if the cacheObject is null.
   * @throws com.cp.common.util.cache.CacheException if the key value is not valid
   * lookup value to be used in this cache.
   */
  public void writeObject(final String key, final Object data)
      throws com.cp.common.util.cache.CacheException {
    writeCacheable(getCacheableInstance(key, data));
  }

  /**
   * Stores the specified Object in this cache mapped to the specified key.
   * Note, the cacheObject will be wrapped in a Cacheable object implementation.
   * @param key the key used to map the cacheObject in this cache.
   * @param data the Object to wrap with an instance of the Cacheable interface
   * mapped to the specified key and stored in this cache.
   * @param expirationDate a Calendar object specifying the expiration date of
   * when the data is no longer valid.
   * @throws java.lang.NullPointerException if the cacheObject is null.
   * @throws com.cp.common.util.cache.CacheException if the key value is not valid
   * lookup value to be used in this cache.
   */
  public void writeObject(final String key, final Object data, final Calendar expirationDate)
      throws com.cp.common.util.cache.CacheException {
    writeCacheable(getCacheableInstance(key, data, expirationDate));
  }

  /**
   * PurgeCacheTx is a Runnable implementation to be run in a Thread and clean the
   * cache of it's contents.
   */
  protected final class PurgeCacheTx implements Runnable {
    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        purgeCache();
        try {
          Thread.sleep(getPurgeInterval());
        }
        catch (InterruptedException ignore) {
        }
      }
    }
  }

}
