/**
 * CacheableAdapter.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.19
 */

package com.codeprimate.util.cache;

import com.cp.common.util.cache.Cacheable;
import java.util.Calendar;

final class CacheableAdapter implements com.cp.common.util.cache.Cacheable {

  private final com.cp.common.util.cache.Cacheable cacheable;

  CacheableAdapter(final Cacheable cacheable) {
    this.cacheable = cacheable;
  }

  /**
   * Returns the data associated with this Cacheable instance.  Calling this method
   * is equivalent to a read operation on the data contained by this Cacheable object.
   * Optionally, this method could be implemented such that the hitCount and lastHit
   * properties get modified accordingly.
   * @return the data that was intended to be cached.
   */
  public Object getData() {
    return cacheable.getData();
  }

  /**
   * Returns the expiration date of the data represented by this Cacheable object.
   * @return a Calendar object signifying the expiration date on the data.
   */
  public Calendar getExpirationDate() {
    return cacheable.getExpirationDate();
  }

  /**
   * Sets the expiration date on the data contained by this Cacheable object.
   * That date signifies when the data will expire no longer be valid.
   * @param expirationDate is a Calenar object signifying the expiration date
   * of the data represented by this Cacheable.
   */
  public void setExpirationDate(final Calendar expirationDate) {
    cacheable.setExpirationDate(expirationDate);
  }

  /**
   * Determines whether the data contained by this Cacheable object has expired.
   * @return a boolean value indicating if the data in this Cacheable object has expired.
   */
  public boolean hasExpired() {
    return cacheable.hasExpired();
  }

  /**
   * Returns the key used to map this Cacheable object in the cache for lookup.
   * @return a String value representing the key to map this Cacheable object
   * with in the cache.
   */
  public String getKey() {
    return cacheable.getKey();
  }

  /**
   * Determines whether this Cacheable object is still valid.
   * @return a boolean value indicating if this Cacheable object is still valid.
   */
  public boolean isValid() {
    return cacheable.isValid();
  }

  /**
   * Determines whether this cacheable object is equal to some other object.
   * @param obj the object compared with this cacheable object for equality.
   * @return a boolean value indicating whether the other object is equal to
   * this cacheable object.
   */
  public boolean equals(final Object obj) {
    return cacheable.equals(obj);
  }

  /**
   * Calculates the hash value of this cacheable object.
   * @return a integer value specifying the hash value of this cacheable object.
   */
  public int hashCode() {
    return cacheable.hashCode();
  }

  /**
   * Returns a String representation of this Cacheable object.
   * @return a String representation of this Cacheable object.
   */
  public String toString() {
    return cacheable.toString();
  }

}
