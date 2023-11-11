/*
 * Cacheable.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.2.20
 */

package com.cp.common.util.cache;

import java.util.Calendar;

public interface Cacheable {

  /**
   * Returns the data associated with this Cacheable instance.  Calling this
   * method is equivalent to a read operation on the data contained by this
   * Cacheable object.  Optionally, this method could be implemented such
   * that the hitCount and lastHit properties get modified accordingly.
   * @return the data that was intended to be cached.
   */
  public Object getData();

  /**
   * Returns the expiration date of the data represented by this Cacheable
   * object.
   * @return a Calendar object signifying the expiration date on the data.
   */
  public Calendar getExpirationDate();

  /**
   * Returns the key used to map this Cacheable object in the cache for
   * lookup.
   * @return a String value representing the key to map this Cacheable
   * object with in the cache.
   */
  public String getKey();

  /**
   * Determines whether the data contained by this Cacheable object has
   * expired.
   * @return a boolean value indicating if the data in this Cacheable object
   * has expired.
   */
  public boolean hasExpired();

  /**
   * Determines whether this Cacheable object is still valid.
   * @return a boolean value indicating if this Cacheable object is still
   * valid.
   */
  public boolean isValid();

  /**
   * Sets the expiration date on the data contained by this Cacheable object.
   * That date signifies when the data will expire no longer be valid.
   * @param expirationDate is a Calenar object signifying the expiration date
   * of the data represented by this Cacheable.
   */
  public void setExpirationDate(Calendar expirationDate);

}
