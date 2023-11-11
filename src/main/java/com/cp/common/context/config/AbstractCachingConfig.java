/*
 * AbstractCachingConfig.java (c) 10 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.11
 * @see com.cp.common.context.config.AbstractConfig
 * @see com.cp.common.context.config.Config
 */

package com.cp.common.context.config;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractCachingConfig extends AbstractConfig {

  private final Map<String, Object> configCache = new TreeMap<String, Object>();

  /**
   * Creates an instance of the AbstractCachingConfig class with the default initialization.
   */
  public AbstractCachingConfig() {
  }

  /**
   * Creates an instance of the AbstractCachingConfig class initialized with the parent configuration object.
   * @param parentConfig the parent Config object used in property value lookup operations if the property
   * cannot be found in the current property configuration.
   */
  public AbstractCachingConfig(final Config parentConfig) {
    super(parentConfig);
  }

  /**
   * Returns the String value for the property with the specified name.  The method accepts a GetValueStrategy
   * to handle property value conversions to the appropriate wrapper object type and default values.  This method
   * will throw a MissingResourceException if the property is not defined in the property configuration resource,
   * and the failOnMissingProperty boolean value is set to true.
   * @param propertyName the name of the property for which the value of will be retrieved.
   * @param valueStrategy the GetValueStrategy used to get the value as an appropriate Object data type.
   * @param failOnMissingProperty a boolean value indicating whether to throw a MissingResourceException if the
   * property specified by name is not defined in the configuration file and the default String value is null.
   * @return a String value of the property specified by name as defined in the configuration file, or the
   * default String value if the property specified by name is not defined in the configuration file.
   * @throws ConfigurationException if there is a problem reading from the property configuration resource.
   * @throws java.util.MissingResourceException if the  property specified by name is not defined in the property configuration
   * resource and the failOnMissingProperty is set to true.
   * @see AbstractConfig#getPropertyValueImpl(String)
   */
  public <T> T getPropertyValue(final String propertyName, final GetValueStrategy<T> valueStrategy, final boolean failOnMissingProperty)
    throws ConfigurationException {
    if (configCache.containsKey(propertyName)) {
      return (T) ObjectUtil.getDefaultValue(configCache.get(propertyName), valueStrategy.getDefaultValue());
    }
    else {
      final T propertyValue = super.getPropertyValue(propertyName, valueStrategy, failOnMissingProperty);
      configCache.put(propertyName, propertyValue);
      return propertyValue;
    }
  }

}
