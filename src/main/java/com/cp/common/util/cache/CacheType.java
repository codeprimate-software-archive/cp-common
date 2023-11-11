/*
 * CacheType.java (c) 17 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.5
 */

package com.cp.common.util.cache;

import com.cp.common.lang.ObjectUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public final class CacheType {

  private static final Logger logger = Logger.getLogger(CacheType.class);

  public static final String LFU_ACRONYM = "LFU";
  public static final String LFU_DESCRIPTION = "Least Frequently Used";
  public static final String LRU_ACRONYM = "LRU";
  public static final String LRU_DESCRIPTION = "Least Recently Used";

  public static final CacheType LFU = new CacheType(LFU_ACRONYM, LFU_DESCRIPTION);
  public static final CacheType LRU = new CacheType(LRU_ACRONYM, LRU_DESCRIPTION);

  private static final List cacheTypeList = new ArrayList();
  static {
    cacheTypeList.add(LFU);
    cacheTypeList.add(LRU);
  }

  private final String acronym;
  private final String description;

  /**
   * Private constructor to enforce the non-instantiability property of a
   * Singleton class type.
   * @param acronym a String acronym of the cache algorithm.
   * @param description a String describing the cache algorithm.
   */
  private CacheType(final String acronym,
                    final String description) {
    this.acronym = acronym;
    this.description = description;
  }

  /**
   * Factory method returning the Singleton instance by acronym, or null if
   * a Singleton instance does not exist with the specified acronym.
   * @param acronym a String acronym value of the cache algorithm.
   * @return the Singleton instance matching the acronym.
   */
  public static CacheType getIntance(final String acronym) {
    logger.debug("acronym = '" + acronym + "'");
    for (Iterator it = cacheTypeList.iterator(); it.hasNext(); ) {
      final CacheType cacheType = (CacheType) it.next();
      if (cacheType.getAcronym().equalsIgnoreCase(acronym)) {
        return cacheType;
      }
    }
    return null;
  }

  /**
   * Returns the acronym of the cache algorithm.
   * @return a String value representing the cache algorithm acronym.
   */
  public String getAcronym() {
    return acronym;
  }

  /**
   * Returns the description of the cache algorithm.
   * @return a String value describing the cache algorithm.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Determines whether this CacheType is equivalent to some Object.
   * @param obj the Object used for the equality check.
   * @return a boolean value if the Object is same as this CacheType.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof CacheType)) {
      return false;
    }

    final CacheType that = (CacheType) obj;

    return ObjectUtil.equals(getAcronym(), that.getAcronym())
      && ObjectUtil.equals(getDescription(), that.getDescription());
  }

  /**
   * Computes the hash value of this CacheType.
   * @return a integer hash value for this CacheType.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getAcronym());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDescription());
    return hashValue;
  }

  /**
   * Returns a String representation of this CacheType.
   * @return a String representation of this CacheType.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{acronym = ");
    buffer.append(getAcronym());
    buffer.append(", description = ");
    buffer.append(getDescription());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
