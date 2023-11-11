/*
 * PropertySearchFilter.java (c) 16 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.util.search.BinarySearchFilter
 */

package com.cp.common.util.search;

import com.cp.common.lang.ObjectUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class PropertySearchFilter implements BinarySearchFilter {

  private static final Logger logger = Logger.getLogger(PropertySearchFilter.class);

  private final Map propertyMap;

  private final String propertyKey;

  /**
   * Creates an instance of the PropertySearchFilter class to filter
   * objects in a Searchable collection based on collection of Bean
   * property values specified by the propertyMap.
   *
   * @param propertyMap the Map collection containing keys referring to
   * the properties of a Bean object, whose value must match the key's
   * value in the Map.
   * @param propertyKey the property whose value serves as the unique
   * identifier for Bean objects in the Searchable collection.  This
   * could also be the property that determines order amongst the
   * elements in the Searchable collection.
   */
  public PropertySearchFilter(final Map propertyMap, final String propertyKey) {
    this.propertyMap = validatePropertyMap(propertyMap);
    this.propertyKey = validatePropertyKey(propertyKey);
  }

  /**
   * Compares whether the object of the Searchable Collection comes before or after
   * other elements in the Searchable Collection based a presorted collection of
   * Searchable elements and the value of the propertyKey.
   * @param obj the Object being compared to other elements of the Searchable Collection
   * in relationship to the search filter criteria.
   * @return an negative integer value  if the object comes before other elements of the
   * Searchable Collection, 0 if the object matched the search fitler criteria,
   * or a positive value if the object comes after other elements of the Searchable
   * Collection.
   * @throws com.cp.common.util.search.SearchException if the search operation failed.
   */
  public int compare(Object obj) throws com.cp.common.util.search.SearchException {
    try {
      final Object objValue = PropertyUtils.getProperty(obj, propertyKey);
      if (logger.isDebugEnabled()) {
        logger.debug("Object Value: " + objValue);
      }
      return ((Comparable) propertyMap.get(propertyKey)).compareTo(objValue);
    }
    catch (Exception e) {
      logger.error("Failed to determine order of the key property value!", e);
      throw new SearchException("Failed to determine order of the key property value!", e);
    }
  }

  /**
   * Determines whether the specified element of the Searchable Collection
   * satisfies the search filter criteria of this SearchFilter.
   * @param obj the Object in question of matching the search filter criteria.
   * @return a boolean value indicating whether the obj of the Searchable
   * Collection matched the search filter criteria.
   * @throws com.cp.common.util.search.SearchException if the search operation could not complete
   * successfully.
   */
  public boolean matches(final Object obj) throws SearchException {
    try {
      for (Iterator it = propertyMap.keySet().iterator(); it.hasNext(); ) {
        final String property =  it.next().toString();
        logger.debug("property = " + property);

        final Object objValue = PropertyUtils.getProperty(obj, property);
        if (logger.isDebugEnabled()) {
          logger.debug("Object Value = " + objValue);
        }

        // Determine equality!
        // TODO: use the DefaultEquator class once ported to the com.cp.common.util package!
        // This code could potentially throw a NullPointerException!
        if (!objValue.equals(propertyMap.get(property))) {
          return false;
        }
      }
    }
    catch (Exception e) {
      logger.error("Failed to determine if this SearchFilter accepts the Object " + obj, e);
      throw new SearchException("Failed to determine if this SearchFilter accepts the Object " + obj, e);
    }
    return true;
  }

  /**
   * Validates the property key is not null, is a key of the propertyMap and the
   * value of the propertyKey implements the ComparableInterface.
   * @param propertyKey the property defined as the unique identifier amongst elements
   * in the Searchable Collection.
   * @return the propertyKey if valid, or throw Exceptions if the propertyKey is not
   * valid.
   * @throws java.lang.NullPointerException if the propertyKey is null!
   * @throws java.lang.IllegalArgumentException if the proepertyKey is not a key of
   * the propertyMap or the value of the propertyKey does not implement the Comparable
   * interface.
   */
  private String validatePropertyKey(final String propertyKey) {
    logger.debug("propertyKey = " + propertyKey);
    if (ObjectUtil.isNull(propertyKey)) {
      logger.warn("propertyKey is null!");
      throw new NullPointerException("The propertyKey cannot be null!");
    }
    if (!propertyMap.containsKey(propertyKey)) {
      logger.warn("propertyKey is not a key of the propertyMap!");
      throw new IllegalArgumentException("The propertyKey must be a key of the propertyMap!");
    }
    if (!(propertyMap.get(propertyKey) instanceof Comparable)) {
      logger.warn("propertyKey value (" + propertyMap.get(propertyKey) + ") does not implement Comparable!");
      throw new IllegalArgumentException("The propertyKey value must implement the Comparable interface!");
    }
    return propertyKey;
  }

  /**
   * Validates the property map ensuring the integrity of the PropertySearchFilter during
   * operation.
   * @param propertyMap the Map of properties used to filter objects of a Searchable
   * Collection.
   * @return the propertyMap if valid or throw Exceptions if the propertyMap is not valid.
   * @throws java.lang.NullPointerException if the property map is null!
   * @throws java.lang.IllegalArgumentException if the property map contains a null key!
   */
  private Map validatePropertyMap(final Map propertyMap) {
    if (logger.isDebugEnabled()) {
      logger.debug("propertyMap: " + propertyMap);
    }
    if (ObjectUtil.isNull(propertyMap)) {
      logger.warn("propertyMap is null!");
      throw new NullPointerException("The propertyMap cannot be null!");
    }
    if (propertyMap.containsKey(null)) {
      logger.warn("propertyMap contains a null key!");
      throw new IllegalArgumentException("The propertyMap cannot contain null properties (null keys)!");
    }
    return new HashMap(propertyMap);
  }

}
