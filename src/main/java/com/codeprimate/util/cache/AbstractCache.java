/*
 * AbstractCache.java (c) 17 February 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.17
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.util.cache.AbstractCache
 * @see com.cp.common.util.cache.Cacheable
 */

package com.codeprimate.util.cache;

import com.cp.common.lang.Auditable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.cache.CacheException;
import com.cp.common.util.cache.Cacheable;
import java.util.Calendar;
import java.util.Iterator;

public abstract class AbstractCache extends com.cp.common.util.cache.AbstractCache {

  /**
   * Computes the chronological order of two Calendar objects.
   * @param c1 the first Calendar object used to determine where it falls
   * chronologically with respect to the other Calendar object.
   * @param c2 the second Calendar object used to determine where the first
   * Calendar object falls chronologically.
   * @return a numerical value indicating the chronological order of the first
   * Calendar object.
   */
  protected static int compareCalendar(final Calendar c1, final Calendar c2) {
    // NOTE if one of the Calendar parameters is null, then it represents infinite time (ongoing).
    int compareValue = (ObjectUtil.isNull(c1) ? 1 : (ObjectUtil.isNull(c2) ? -1 : 0));

    if (compareValue == 0) {
      compareValue = (c1.before(c2) ? -1 : (c1.after(c2) ? 1 : 0));
    }

    return compareValue;
  }

  /**
   * Returns the Cacheable object stored in this cache referenced by the specified
   * key.  NOTE: this method should not be exposed as part of the public API since
   * it does not affect properties of the Cacheable objects based on algorithm
   * implementation.
   * @param key a String value referring to the Cacheable object in this cache.
   * @return the Cacheable object stored in this cache and referrenced by the
   * specified key (lookup value).
   */
  abstract CPAbstractCacheable getCacheable(String key);

  /**
   * Returns a String description of this Cache.
   * @return a String description of this Cache.
   */
  public abstract String getDescription();

  /**
   * Forces an refresh on the Cacheable objects in this cache on the next read
   * operations.
   */
  public synchronized final void invalidateCache() {
    log.debug("Invalidating Cache");
    for (Iterator it = iterateCacheables(); it.hasNext(); ) {
      ((CPAbstractCacheable) it.next()).setValid(false);
    }
  }

  /**
   * Reads the Cacheable object mapped to the specified key in this cache.
   * @param key the key used as a lookup value for the Cacheable object stored in
   * this cache.
   * @return a Cacheable object associated with the specified key from this cache
   * or null if the key is not mapped to any Cacheable object in this cache.
   */
  public final Cacheable readCacheable(final String key) throws com.cp.common.util.cache.CacheException {
    if (log.isDebugEnabled()) {
      log.debug("key (" + key + ")");
    }

    CPAbstractCacheable cacheObject = getCacheable(key);

    if (ObjectUtil.isNotNull(cacheObject)) {
      if (log.isDebugEnabled()) {
        log.debug("Cacheable object (" + cacheObject + ") is NOT null!");
      }

      // NOTE: the check for hasExpired on the cached object is called here to place
      // responsibility on the caller in deciding the correct action or behavior for
      // expired objects.
      // For example, it would be incorrect for the cache to notify a data source
      // listener that a User object needs to be reloaded because the account of the
      // user has expired.
      // The problem would further be propagated by the fact that the application-specific
      // Exceptions would be masked by a CacheException and thrown from the readCacheable
      // method potentially causing incorrect behavior by the caller.  This could also
      // lead to security related problems.
      if (cacheObject.hasExpired()) {
        log.warn("The Cacheable object (" + cacheObject + ") has expired!");
        return null;
      }

      if (!cacheObject.isValid()) {
        log.warn("The Cacheable object is NOT valid!");
        fireCacheEvent(cacheObject);
        cacheObject = getCacheable(key);
      }
    }
    else {
      return null;
    }

    return _readCacheable(cacheObject);
  }

  protected abstract Cacheable _readCacheable(Cacheable cacheable);

  /**
   * Checks for concurrent modification of the cached object.
   * @param cacheObject the cached object for which concurrent modification is checked.
   * @throws CacheException if the cached object has been concurrently modified.
   */
  private void validate(final Cacheable cacheObject) throws CacheException {
    if (log.isDebugEnabled()) {
      log.debug("cacheObject: " + cacheObject);
    }
    if (cacheObject instanceof Auditable) {
      final Cacheable exisitingCacheObject = getCacheable(cacheObject.getKey());
      if (log.isDebugEnabled()) {
        log.debug("existingCacheObject: " + exisitingCacheObject);
      }

      if (ObjectUtil.isNotNull(exisitingCacheObject) && (exisitingCacheObject instanceof Auditable)
          && cacheObject.getClass().equals(exisitingCacheObject.getClass())) {
        final Auditable aNew = (Auditable) cacheObject;
        final Auditable aExisting = (Auditable) exisitingCacheObject;

        if (!aNew.getLastModifiedBy().equals(aExisting.getModifiedBy())
            || !aNew.getLastModifiedDateTime().equals(aExisting.getModifiedDateTime())) {
          log.warn("Concurrent modification has occurred on cache object (" + cacheObject + ")");
          throw new CacheException("Concurrent modification has occurred on cache object (" + cacheObject + ")");
        }
      }
    }
  }

  /**
   * Stores the specified Cacheable object in this cache mapped to the key property
   * value of the Cacheable object.
   * @param cacheObject the Cacheable object to store in this cache.
   * @throws java.lang.NullPointerException if the cacheObject is null.
   * @throws com.cp.common.util.cache.CacheException if the cacheObject does not contain
   * a proper value for it's key property.
   */
  public final void writeCacheable(final Cacheable cacheObject) throws com.cp.common.util.cache.CacheException {
    if (ObjectUtil.isNull(cacheObject)) {
      log.warn("Cannot store a null Cacheable object in the cache!");
      throw new com.cp.common.util.cache.CacheException("Cannot store a null Cacheable object in the cache!");
    }

    if (!cacheObject.isValid()) {
      log.warn("Cannot store an invalid Cacheable object in the cache!");
      throw new CacheException("Cannot store an invalid Cacheable object in the cache!");
    }

    validate(cacheObject);

    if (log.isDebugEnabled()) {
      log.debug("writing cacheObject (" + cacheObject + ")");
    }

    _writeCacheable(cacheObject);
  }

  protected abstract void _writeCacheable(Cacheable cacheObject) throws CacheException;

  protected abstract class CPAbstractCacheable extends com.cp.common.util.cache.AbstractCacheable {

    // Records whether this Cacheable object is valid or not.
    private boolean valid = true;

    public CPAbstractCacheable(final String key, final Object data) {
      super(key, data);
    }

    public CPAbstractCacheable(final String key, final Object data, final Calendar expirationDate) {
      super(key, data, expirationDate);
    }

    protected CPAbstractCacheable(final Cacheable cacheObject) {
      super(cacheObject);
    }

    /**
     * Determines whether this Cacheable object is still valid.
     * @return a boolean value indicating if this Cacheable object is still
     * valid.
     */
    public boolean isValid() {
      return (valid && super.isValid());
    }

    /**
     * Sets whether this Cacheable object is valid or not.
     * @param valid a boolean value indicating whether this Cacheable object is
     * valid.
     */
    public void setValid(final boolean valid) {
      if (log.isDebugEnabled()) {
        log.debug("valid (" + valid + ")");
      }
      this.valid = valid;
    }
  }

}
