/*
 * Cache.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.2.25
 */

package com.cp.common.util.cache;

import java.util.Calendar;
import java.util.Iterator;

public interface Cache {

  /**
   * Adds the CacheDataSourceListener object to the list of listeners listening for
   * CacheEvents.
   * @param dsl the CacheDataSourceListener which represents the  data store for the
   * data kept in this cache.
   */
  public void addCacheDataSourceListener(CacheDataSourceListener dsl);

  /**
   * Determines whether the specified Cacheable object is stored in this cache.
   * @param cacheObject the Cacheable object in question of being contained
   * by this class.
   * @return a boolean value whether the Cacheable object exists in this
   * cache.
   */
  public boolean containsCacheable(com.cp.common.util.cache.Cacheable cacheObject);

  /**
   * Determines whether a Cacheable object is mapped to the specified key in
   * this cache.
   * @param key the String value used to determine whether it maps to a
   * Cacheable object in the cache.
   * @return a boolean value indicating if the key is mapped to a Cacheable
   * object in the cache.
   */
  public boolean containsKey(String key);

  /**
   * Determines whether the specified Object value is stored in this cache.
   * @param obj the Object value used in determining whether it is stored by
   * this cache.
   * @return a boolean value indicating whether the Object value is stored
   * in this cache (wrapped in a Cacheable object and mapped to a key
   * maintained in this cache).
   */
  public boolean containsObject(Object obj);

  /**
   * Returns the maximum number of Cacheable objects that this cache can contain
   * before the cache algorithm is called upon to determine which Cacheable
   * objects will be replaced by other Objects currently in use.
   * @return an integer value stating the upper limit on the number of Cacheable
   * objects this cache can hold.
   */
  public int getMaxSize();

  /**
   * Returns the current number of Cacheable objects in this cache.
   * @return an integer value of the number of Cacheable objects stored in this
   * cache.
   */
  public int getSize();

  /**
   * Forces an refresh on the Cacheable objects in this cache on the next read
   * operations.
   */
  public void invalidateCache();

  /**
   * Returns an Iterator to traverse the Cacheable objects in this cache.
   * @return a java.util.Iterator object to iterate the Cacheable objects in this
   * cache.
   */
  public Iterator iterateCacheables();

  /**
   * Returns an Iterator to traverse the keys mapped to Cacheable objects in this
   * cache.
   * @return a java.util.Iterater object to iterator the keys mapping to Cacheable
   * objects in this cache.
   */
  public Iterator iterateKeys();

  /**
   * Returns an Iterator to the stored data in this cache.
   * @return a java.util.Iterator object to iterate the data value Objects
   * contained in this cache.
   */
  public Iterator iterateObjects();

  /**
   * Reads the Cacheable object mapped to the specified key in this cache.
   * @param key the key used as a lookup value for the Cacheable object stored in
   * this cache.
   * @return a Cacheable object associated with the specified key from this cache
   * or null if the key is not mapped to any Cacheable object in this cache.
   * @throws com.cp.common.util.cache.CacheException if the Cacheable object for the desired key unable to
   * be read from this cache.
   */
  public com.cp.common.util.cache.Cacheable readCacheable(String key) throws com.cp.common.util.cache.CacheException;

  /**
   * Reads the data Object value mapped to the specified key in this cache.
   * @param key the String value mapping the data Object in the cache.
   * @return the data value as a java.lang.Object for the specified key in
   * this cache, or null if the key is not mapped to an Object value in this
   * cache.
   * @throws CacheException if the data Object for the desired key unable to
   * be read from this cache.
   */
  public Object readObject(String key) throws com.cp.common.util.cache.CacheException;

  /**
   * Removes the CacheDataSourceListener object from the list of listeners listening for
   * CacheEvents.
   * @param dsl the CacheDataSourceListener which represents the data store for the
   * data kept in this cache.
   */
  public void removeCacheDataSourceListener(CacheDataSourceListener dsl);

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
  public void setMaxSize(int maxSize);

  /**
   * Called to release any resources held by this Cache object.
   */
  public void terminate();

  /**
   * Stores the specified Cacheable object in this cache mapped to the key property
   * value of the Cacheable object.
   * @param cacheObject the Cacheable object to store in this cache.
   * @throws java.lang.NullPointerException if the cacheObject is null.
   * @throws com.cp.common.util.cache.CacheException if the cacheObject does not contain
   * a proper value for it's key property.
   */
  public void writeCacheable(Cacheable cacheObject) throws com.cp.common.util.cache.CacheException;

  /**
   * Stores the specified Object in this cache mapped to the specified key.
   * Note, the cacheObject will be wrapped in a Cacheable object implementation.
   * @param key the key used to map the cacheObject in this cache.
   * @param data the Object to wrap with an instance of the Cacheable interface
   * mapped to the specified key and stored in this cache.
   * @throws java.lang.NullPointerException if the cacheObject is null.
   * @throws com.cp.common.util.cache.CacheException if the key value is not valid
   * lookup value to be used in this cache.
   */
  public void writeObject(String key, Object data) throws CacheException;

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
  public void writeObject(String key, Object data, Calendar expirationDate) throws com.cp.common.util.cache.CacheException;

}
