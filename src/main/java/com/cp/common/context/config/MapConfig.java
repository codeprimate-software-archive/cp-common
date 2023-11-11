/*
 * MapConfig.java (c) 5 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.18
 * @see com.cp.common.context.config.AbstractConfig
 * @see com.cp.common.util.ConfigurationException
 * @see java.util.Map
 */

package com.cp.common.context.config;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import java.util.Collections;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TreeMap;

public class MapConfig extends AbstractConfig {

  private final Map<String, Object> propertyValueMap;

  /**
   * Creates an instance of the MapConfig class initialized and backed by the specified Map object.
   * @param propertyValueMap the Map object containing a mapping of property names to values.
   */
  public MapConfig(final Map<String, Object> propertyValueMap) {
    this(null, propertyValueMap);
  }

  /**
   * Creates an instance of the MapConfig class initialized and backed by the specified Map object.
   * @param parentConfig the parent configuration object used to obtain configuration information.
   * @param propertyValueMap the Map object containing a mapping of property names to values.
   */
  public MapConfig(final Config parentConfig, final Map<String, Object> propertyValueMap) {
    super(parentConfig);
    Assert.notNull(propertyValueMap, "The Map containing property configuration information cannot be null!");
    this.propertyValueMap = Collections.unmodifiableMap(new TreeMap<String, Object>(propertyValueMap));
  }

  /**
   * Determines whether the specified configuration information contains a property with the given name.
   * @param propertyName a String name of the property in the configuration information.
   * @return a boolean value indicating whether the specified configuration information contains a  property
   * with the given name.
   */
  @Override
  public boolean contains(final String propertyName) {
    return (propertyValueMap.containsKey(propertyName) || super.contains(propertyName));
  }

  /**
   * Gets the value for the specified property of the given name.
   * @param propertyName the String name of the property.
   * @return a String value for the specified property by name.
   * @throws ConfigurationException if the value for the property given by name cannot be obtained.
   * @throws MissingResourceException if the property specified by name does not exist!
   */
  @Override
  protected String getPropertyValueImpl(final String propertyName) throws ConfigurationException {
    if (propertyValueMap.containsKey(propertyName)) {
      return ObjectUtil.toString(propertyValueMap.get(propertyName));
    }
    else {
      logger.warn("The property (" + propertyName + ") does not exist!");
      throw new MissingResourceException("The property (" + propertyName + ") does not exist!",
        getClass().getName(), propertyName);
    }
  }

}
