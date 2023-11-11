/*
 * CPCommonProperties.java (c) 17 February 2004
 *
 * This class is used to load information from the cp-common.properties file
 * used in the cp-common API and framework to load provider classes via the
 * Service Provider Interface (SPI).
 *
 * Two examples of service provider contracts that serivce providers must
 * implement are the:
 * <code>
 *     com.cp.common.util.cache.AbstractCacheFactory
 *     com.cp.common.util.record.AbstractRecordFactory
 * </code>
 *
 * This class is final and package private to prevent access to this class
 * outside of this package.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.24
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import org.apache.log4j.Logger;

public final class CPCommonProperties extends PropertyResourceBundle {

  private static CPCommonProperties INSTANCE;
  private static final Logger logger = Logger.getLogger(CPCommonProperties.class);
  private static final String CP_COMMON_PROPERTIES_PATHNAME = "/cp-common.properties";

  /**
   * Private constructor to enforce the non-instantiability property of this
   * class, thus making it a Singleton.
   * @throws IOException if the properties file cannot be found, accessed
   * or loaded.
   */
  private CPCommonProperties() throws IOException {
    super(new BufferedInputStream(CPCommonProperties.class.getResourceAsStream(CP_COMMON_PROPERTIES_PATHNAME)));
  }

  /**
   * Returns the Singleton instance of this class.
   * @return a instance of the CPCommonProperties class.
   * @throws IOException if the properties file cannot be found, accessed or
   * loaded.
   */
  public static CPCommonProperties getInstance() throws IOException {
    if (ObjectUtil.isNull(INSTANCE)) {
      synchronized (CPCommonProperties.class) {
        if (ObjectUtil.isNull(INSTANCE)) {
          if (logger.isDebugEnabled()) {
            logger.debug("Reading properties file (" + CP_COMMON_PROPERTIES_PATHNAME + ")");
          }
          INSTANCE = new CPCommonProperties();
        }
      }
    }
    return INSTANCE;
  }

}
