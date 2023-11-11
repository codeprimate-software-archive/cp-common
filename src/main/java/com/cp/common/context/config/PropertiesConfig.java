/*
 * PropertiesConfig.java (c) 3 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.4
 * @see com.cp.common.context.config.AbstractConfig
 * @see com.cp.common.util.ConfigurationException
 * @see java.util.Properties
 */

package com.cp.common.context.config;

import com.cp.common.lang.Assert;
import com.cp.common.util.ConfigurationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import org.springframework.core.io.Resource;

public class PropertiesConfig extends AbstractConfig {

  private Properties localProperties;

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified property file as the source of
   * configuration information.
   * @param propertyFile the file system object containing the properties for this configuration.
   * @throws FileNotFoundException if the specified property file cannot be found in the file system.
   */
  public PropertiesConfig(final File propertyFile) throws FileNotFoundException {
    this(null, propertyFile);
  }

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified property file as the source of
   * configuration information.
   * @param parentConfig the parent configuration object used to obtain configuration information.
   * @param propertyFile the file system object containing the properties for this configuration.
   * @throws FileNotFoundException if the specified property file cannot be found in the file system.
   */
  public PropertiesConfig(final Config parentConfig, final File propertyFile) throws FileNotFoundException {
    super(parentConfig);

    Assert.notNull(propertyFile, "The property file cannot be null!");

    if (!propertyFile.exists()) {
      logger.warn("The property file (" + propertyFile.getAbsolutePath() + ") does not exist!");
      throw new FileNotFoundException("The property file (" + propertyFile.getAbsolutePath() + ") does not exist!");
    }

    initLocalProperties(new BufferedInputStream(new FileInputStream(propertyFile)));
  }

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified Properties object as the source
   * of configuration information.
   * @param properties the Properties object used as the source for the configuration information.
   */
  public PropertiesConfig(final Properties properties) {
    this(null, properties);
  }

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified Properties object as the source
   * of configuration information.
   * @param parentConfig the parent configuration object used to obtain configuration information.
   * @param properties the Properties object used as the source for the configuration information.
   */
  public PropertiesConfig(final Config parentConfig, final Properties properties) {
    super(parentConfig);
    Assert.notNull(properties, "The Properties object cannot be null!");
    localProperties = properties;
  }
  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified resource as the source of
   * configuration information.
   * @param resource a Spring Resource object referencing the configuration resource source.
   */
  public PropertiesConfig(final Resource resource) {
    this(null, resource);
  }

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified resource as the source of
   * configuration information.
   * @param parentConfig the parent configuration object used to obtain configuration information.
   * @param resource a Spring Resource object referencing the configuration resource source.
   */
  public PropertiesConfig(final Config parentConfig, final Resource resource) {
    super(parentConfig);

    Assert.notNull(resource, "The Resource object cannot be null!");

    try {
      initLocalProperties(resource.getInputStream());
    }
    catch (IOException e) {
      logger.warn("Failed to obtain an input stream to the specified resource (" + resource.getDescription() + ")!", e);
      throw new ConfigurationException("Failed to obtain an input stream to the specified resource ("
        + resource.getDescription() + ")!", e);
    }
  }

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified resource name as the source of
   * configuration information.
   * @param resourceName the name or path of the resource configuration source.
   */
  public PropertiesConfig(final String resourceName) {
    this(null, resourceName);
  }

  /**
   * Creates an instance of the PropertiesConfig class initialized with the specified resource name as the source of
   * configuration information.
   * @param parentConfig the parent configuration object used to obtain configuration information.
   * @param resourceName the name or path of the resource configuration source.
   */
  public PropertiesConfig(final Config parentConfig, final String resourceName) {
    super(parentConfig);
    initLocalProperties(getClass().getResourceAsStream(resourceName));
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
    return (hasProperty(propertyName) || super.contains(propertyName));
  }

  /**
   * Determines whether the local properties defines the specified property.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether the specified property exists in the local properties.
   */
  protected boolean hasProperty(final String propertyName) {
    return localProperties.containsKey(propertyName);
  }

  /**
   * Initializes and configures the local Properties object containing the configuration information.
   * @param inputStream an input stream to read the configuration information from the source.
   */
  protected void initLocalProperties(final InputStream inputStream) {
    localProperties = new Properties();

    try {
      localProperties.load(inputStream);
    }
    catch (IOException e) {
      logger.error("Failed to load properties from the configuration resource!", e);
      throw new ConfigurationException("Failed to load properties from the configuration resource!", e);
    }
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
    if (hasProperty(propertyName)) {
      return localProperties.getProperty(propertyName);
    }
    else {
      logger.warn("The property (" + propertyName + ") does not exist!");
      throw new MissingResourceException("The property (" + propertyName + ") does not exist!",
        getClass().getName(), propertyName);
    }
  }

}
