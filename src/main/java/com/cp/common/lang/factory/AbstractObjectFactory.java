/*
 * AbstractObjectFactory.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.util.SystemException
 */

package com.cp.common.lang.factory;

import com.cp.common.context.config.Config;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.util.SystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractObjectFactory {

  private final Config config;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the AbstractObjectFactory class initialized with the specified Config object.
   * @param config the Config object used to retrieve configuration information.
   */
  public AbstractObjectFactory(final Config config) {
    Assert.notNull(config, "The configuration object cannot be null!");
    this.config = config;
  }

  /**
   * Gets the Config object used to retrieve configuration information.
   * @return a Config object used to retrieve configuration information.
   */
  protected Config getConfig() {
    return config;
  }

  protected <T> T getInstance(final String objectKey) throws SystemException {
    String objectClassName = null;

    try {
      objectClassName = getConfig().getStringPropertyValue(objectKey);

      if (logger.isDebugEnabled()) {
        logger.debug("object class name (" + objectClassName + ")");
      }

      return ClassUtil.<T>getInstance(objectClassName);
    }
    catch (ClassNotFoundException e) {
      logger.error("Failed to find class (" + objectClassName + ") with object key (" + objectKey
        + ") in CLASSPATH!", e);
      throw new SystemException("Failed to find class (" + objectClassName + ") with object key (" + objectKey
        + ") in CLASSPATH!", e);
    }
    catch (InstantiationException e) {
      logger.error("Failed to create an instance of class (" + objectClassName + ")!", e);
      throw new SystemException("Failed to create an instance of class (" + objectClassName + ")!", e);
    }
  }

  protected <T> T getInstance(final String objectKey, final Object[] args) throws SystemException {
    String objectClassName = null;

    try {
      objectClassName = getConfig().getStringPropertyValue(objectKey);

      if (logger.isDebugEnabled()) {
        logger.debug("object class name (" + objectClassName + ")");
      }

      return ClassUtil.<T>getInstance(objectClassName, args);
    }
    catch (ClassNotFoundException e) {
      logger.error("Failed to find class (" + objectClassName + ") with object key (" + objectKey
        + ") in CLASSPATH!", e);
      throw new SystemException("Failed to find class (" + objectClassName + ") with object key (" + objectKey
        + ") in CLASSPATH!", e);
    }
    catch (InstantiationException e) {
      logger.error("Failed to create an instance of class (" + objectClassName + ")!", e);
      throw new SystemException("Failed to create an instance of class (" + objectClassName + ")!", e);
    }
  }

  protected <T> T getInstance(final String objectKey, final Class[] argTypes, final Object[] args)
      throws SystemException {
    String objectClassName = null;

    try {
      objectClassName = getConfig().getStringPropertyValue(objectKey);

      if (logger.isDebugEnabled()) {
        logger.debug("object class name (" + objectClassName + ")");
      }

      return ClassUtil.<T>getInstance(objectClassName, argTypes, args);
    }
    catch (ClassNotFoundException e) {
      logger.error("Failed to find class (" + objectClassName + ") with object key (" + objectKey
        + ") in CLASSPATH!", e);
      throw new SystemException("Failed to find class (" + objectClassName + ") with object key (" + objectKey
        + ") in CLASSPATH!", e);
    }
    catch (InstantiationException e) {
      logger.error("Failed to create an instance of class (" + objectClassName + ")!", e);
      throw new SystemException("Failed to create an instance of class (" + objectClassName + ")!", e);
    }
  }

}
