/*
 * AbstractCacheTest.java (c) 25 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.5
 */

package com.codeprimate.util.cache;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.cache.Cache;
import java.util.Calendar;
import java.util.Iterator;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

public abstract class AbstractCacheTest extends TestCase {

  private static final Logger logger = Logger.getLogger(AbstractCacheTest.class);

  public AbstractCacheTest(final String testName) {
    super(testName);
  }

  protected final void logKeys(final Cache cache) {
    if (logger.isDebugEnabled()) {
      for (Iterator it = cache.iterateKeys(); it.hasNext(); ) {
        logger.debug(it.next());
      }
    }
  }
  protected final void readCacheable(final Cache cache,
                                     final com.cp.common.util.cache.Cacheable cacheable,
                                     final int numberOfReads)
      throws com.cp.common.util.cache.CacheException {
    for (int count = numberOfReads; --count >= 0; ) {
      cache.readCacheable(cacheable.getKey());
    }
  }

  protected final class User implements com.cp.common.util.cache.Cacheable {

    private Calendar expirationDate;

    private final Integer id;

    private final String firstName;
    private final String lastName;

    public User(final Integer id) {
      this(id, null, null);
    }

    public User(final Integer id,
                final String firstName,
                final String lastName) {
      if (ObjectUtil.isNull(id)) {
        logger.warn("The ID cannot be null!");
        throw new NullPointerException("The ID cannot be null!");
      }
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public Object getData() {
      return this;
    }

    public Calendar getExpirationDate() {
      return expirationDate;
    }

    public String getFirstName() {
      return firstName;
    }

    public Integer getId() {
      return id;
    }

    public String getKey() {
      return id.toString();
    }

    public String getLastName() {
      return lastName;
    }

    public boolean hasExpired() {
      final Calendar expirationDate = getExpirationDate();
      if (ObjectUtil.isNotNull(expirationDate)) {
        final Calendar now = Calendar.getInstance();
        now.setTimeZone(expirationDate.getTimeZone());
        return now.after(expirationDate);
      }
      return false;
    }

    public boolean isValid() {
      return !hasExpired();
    }

    public void setExpirationDate(final Calendar expirationDate) {
      // WARNING: no defensive copy is being made.
      this.expirationDate = expirationDate;
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof User)) {
        return false;
      }

      final User user = (User) obj;

      return ObjectUtil.equals(getExpirationDate(), user.getExpirationDate())
        && ObjectUtil.equals(getFirstName(), user.getFirstName())
        && ObjectUtil.equals(getId(), user.getId())
        && ObjectUtil.equals(getKey(), user.getKey())
        && ObjectUtil.equals(getLastName(), user.getLastName());
    }

    // NOTE do not inluce the data property in the hash calculation; getData returns this, thus leading
    // to infinite recursion.
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getExpirationDate());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getFirstName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getId());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getKey());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getLastName());
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{expirationDate = ");
      buffer.append(getExpirationDate());
      buffer.append(", firstName = ").append(getFirstName());
      buffer.append(", id = ").append(getId());
      buffer.append(", key = ").append(getKey());
      buffer.append(", lastName = ").append(getLastName());
      buffer.append(", expired = ").append(hasExpired());
      buffer.append(", valid = ").append(isValid());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
