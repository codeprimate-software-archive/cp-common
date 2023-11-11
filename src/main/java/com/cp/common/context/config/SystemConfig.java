/*
 * SystemConfig.java (c) 3 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.4
 * @see com.cp.common.context.config.AbstractConfig
 * @see com.cp.common.util.ConfigurationException
 * @see java.lang.System
 */

package com.cp.common.context.config;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import java.util.MissingResourceException;

public class SystemConfig extends AbstractConfig {

  /**
   * Default constructor for creating a new instance of the SystemConfig class.
   */
  public SystemConfig() {
  }

  /**
   * Creates a new instance of the SystemConfig class initialized with a parent Config object.
   * @param parentConfig the parent Config object used to retrieve configuration information if this Config object
   * does not contain the specified property.
   */
  public SystemConfig(final Config parentConfig) {
    super(parentConfig);
  }

  /**
   * Determines whether the configuration information represented by this Config object contains a property
   * with the given name.
   * @param propertyName a String name of the property in the configuration information.
   * @return a boolean value indicating whether the specified configuration information contains a property
   * with the given name.
   */
  @Override
  public boolean contains(final String propertyName) {
    return (ObjectUtil.isNotNull(System.getProperty(propertyName)) || super.contains(propertyName));
  }

  /**
   * Gets a String value of the specified property by name using the implementation approach as defined
   * by this Config object.
   * @param propertyName the String name of the property in the configuration to retrieve the value for.
   * @return a String value for the specified property by name from the configuration.
   * @throws ConfigurationException if the property value could not be read from the configuration.
   */
  @Override
  protected String getPropertyValueImpl(final String propertyName) throws ConfigurationException {
    if (contains(propertyName)) {
      return System.getProperty(propertyName);
    }
    else {
      logger.warn("The property (" + propertyName + ") does not exist!");
      throw new MissingResourceException("The property (" + propertyName + ") does not exist!",
        getClass().getName(), propertyName);
    }
  }

}
