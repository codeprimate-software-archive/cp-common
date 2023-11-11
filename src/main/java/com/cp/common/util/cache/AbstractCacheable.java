/*
 * AbstractCacheable.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.7.9
 */

package com.cp.common.util.cache;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import org.apache.log4j.Logger;

public abstract class AbstractCacheable implements Cacheable {

  private static final Logger logger = Logger.getLogger(AbstractCacheable.class);

  private Calendar expirationDate;

  private final Object data;

  private final String key;

  /**
   * Creates an instance of the AbstractCacheable class to map a key lookup value
   * to a data item and store the object in a cache.  This Cacheable object is set
   * not to expire.
   * @param key the String lookup value for the Cacheable item.
   * @param data the data to be stored under the key in the cache.
   */
  public AbstractCacheable(final String key, final Object data) {
    this(key, data, null);
  }

  /**
   * Creates an instance of the AbstractCacheable class to map a key lookup value
   * to a data item and store the object in a cache.  This Cacheable object is set
   * to expire on the given date.
   * @param key the String lookup value for the Cacheable item.
   * @param data the data to be stored under the key in the cache.
   * @param expirationDate the date at which this Cacheable object will expire and
   * no longer be valid.
   */
  public AbstractCacheable(final String key, final Object data, final Calendar expirationDate) {
    if (StringUtil.isEmpty(key)) {
      logger.warn("(" + key + ") is not a valid cache key!");
      throw new IllegalArgumentException("(" + key + ") is not a valid cache key!");
    }

    if (ObjectUtil.isNull(data)) {
      logger.warn("The data to cache cannot be null!");
      throw new NullPointerException("The data to cache cannot be null!");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("key (" + key + ")");
      logger.debug("data (" + data + ")");
      logger.debug("expirationDate (" + expirationDate + ")");
    }

    this.key = key;
    this.data = data;
    this.expirationDate = expirationDate;
  }

  /**
   * Copy constructor for Cacheable objects.  This constructor will reset
   * the hitCount property to zero and the lastHit property to null.
   * @param cacheable the Cacheable object to clone.
   */
  protected AbstractCacheable(final Cacheable cacheable) {
    if (ObjectUtil.isNull(cacheable)) {
      logger.warn("The Cacheable object to copy cannot be null!");
      throw new NullPointerException("The Cacheable object to copy cannot be null!");
    }

    this.data = cacheable.getData();
    this.expirationDate = getCalendar(cacheable.getExpirationDate());
    this.key = cacheable.getKey();
  }

  /**
   * Utility method to make a defensive copy of a Calendar object.
   * @param calendar the Calendar object to clone.
   * @return a Calendar object copy of the specified Calendar parameter.
   */
  protected final Calendar getCalendar(final Calendar calendar) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendar (" + calendar + ")");
    }

    if (ObjectUtil.isNull(calendar)) {
      logger.warn("The Calendar object was null; returning null!");
      return null;
    }

    return DateUtil.copy(calendar);
  }

  /**
   * Determines whether this Cacheable object is equal to some other
   * object.
   * @param obj the Object used in comparison with this Cacheable object.
   * @return a boolean value indicating whether this Cacheable object is equal
   * to the object parameter.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Cacheable)) {
      return false;
    }

    final Cacheable cacheable = (Cacheable) obj;

    return ObjectUtil.equals(getData(), cacheable.getData())
      && ObjectUtil.equals(getExpirationDate(), cacheable.getExpirationDate())
      && ObjectUtil.equals(getKey(), cacheable.getKey());
  }

  /**
   * Returns the data associated with this Cacheable instance.  Calling this
   * method is equivalent to a read operation on the data contained by this
   * Cacheable object.  Optionally, this method could be implemented such
   * that the hitCount and lastHit properties get modified accordingly.
   * @return the data that was intended to be cached.
   */
  public Object getData() {
    return data;
  }

  /**
   * Returns the expiration date of the data represented by this Cacheable
   * object.
   * @return a Calendar object signifying the expiration date on the data.
   */
  public Calendar getExpirationDate() {
    return getCalendar(expirationDate);
  }

  /**
   * Returns the key used to map this Cacheable object in the cache for
   * lookup.
   * @return a String value representing the key to map this Cacheable
   * object with in the cache.
   */
  public String getKey() {
    return key;
  }

  /**
   * Returns a boolean value indicating whether this Cacheable object has expired
   * which is determined by whether the expirationDate property value has exceeded
   * the exact moment in time when this method was called.
   * @return a boolean value indicating whether this Cacheable object has expired.
   */
  public boolean hasExpired() {
    final Calendar expirationDate = getExpirationDate();

    if (ObjectUtil.isNotNull(expirationDate)) {
      final Calendar now = Calendar.getInstance();
      now.setTimeZone(expirationDate.getTimeZone());

      if (logger.isDebugEnabled()) {
        logger.debug("Comparing now (" + now + ") with expiration date (" + expirationDate + ")");
      }

      return now.after(expirationDate);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("The expirationDate is null; the Cacheable object will never expire!");
    }

    return false;
  }

  /**
   * Computes the hash value of this Cacheable object.
   * @return a integer value representing the hash value of this Cacheable object.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getData());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getExpirationDate());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getKey());
    return hashValue;
  }

  /**
   * Determines whether this Cacheable object is still valid.
   * @return a boolean value indicating if this Cacheable object is still
   * valid.
   */
  public boolean isValid() {
    return !hasExpired();
  }

  /**
   * Sets the expiration date on the data contained by this Cacheable object.
   * That date signifies when the data will expire no longer be valid.
   * @param expirationDate is a Calenar object signifying the expiration date
   * of the data represented by this Cacheable.
   */
  public void setExpirationDate(final Calendar expirationDate) {
    if (logger.isDebugEnabled()) {
      logger.debug("expirationDate (" + expirationDate + ")");
    }
    this.expirationDate = getCalendar(expirationDate);
  }

  /**
   * Returns a String representation of this Cacheable object.
   * @return a String representation of this Cacheable object.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{data = ");
    buffer.append(getData());
    buffer.append(", expirationDate = ").append(getExpirationDate());
    buffer.append(", key = ").append(getKey());
    buffer.append(", hasExpired = ").append(hasExpired());
    buffer.append(", isValid = ").append(isValid());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
