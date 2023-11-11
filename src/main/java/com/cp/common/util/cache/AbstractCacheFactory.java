/*
 * AbstractCacheFactory.java (c) 17 February 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 */

package com.cp.common.util.cache;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.PropertyManager;
import org.apache.log4j.Logger;

public abstract class AbstractCacheFactory {

  private static AbstractCacheFactory INSTANCE;

  private static final Logger logger = Logger.getLogger(AbstractCacheFactory.class);

  private static final String CACHE_FACTORY_PROPERTY_KEY = "cp-common.cache.factory";

  /**
   * Factory method returning a service provider instance of the AbstractCacheFactory class.
   * @return a service provider instance of the AbstractCacheFatory class.
   */
  public synchronized static AbstractCacheFactory getInstance() {
    if (ObjectUtil.isNull(INSTANCE)) {
      String defaultCacheFactoryClassName = null;

      try {
        defaultCacheFactoryClassName = PropertyManager.getInstance().getStringPropertyValue(CACHE_FACTORY_PROPERTY_KEY);

        if (logger.isDebugEnabled()) {
          logger.debug("defaultCacheFactoryClassName = (" + defaultCacheFactoryClassName + ")");
        }

        INSTANCE = (AbstractCacheFactory) ClassUtil.getInstance(defaultCacheFactoryClassName);
      }
      catch (ClassNotFoundException e) {
        logger.error("Unable to find provider class (" + defaultCacheFactoryClassName + ") in CLASSPATH!", e);
        throw new ConfigurationException("Unable to find provider class (" + defaultCacheFactoryClassName + ") in CLASSPATH!", e);
      }
      catch (ConfigurationException e) {
        logger.error("Unable to determine provider class from property (" + CACHE_FACTORY_PROPERTY_KEY + ")!", e);
        throw new ConfigurationException("Unable to determine provider class from property (" + CACHE_FACTORY_PROPERTY_KEY + ")!", e);
      }
      catch (InstantiationException e) {
        logger.error("Unable to create an instance of provider class (" + defaultCacheFactoryClassName + ")!", e);
        throw new ConfigurationException("Unable to create an instance of provider class (" + defaultCacheFactoryClassName + ")!", e);
      }
    }

    return INSTANCE;
  }

  /**
   * Returns an Cache implemenation implementing a specified cache algorithm based on the
   * CacheType.
   * @param cacheType an Object describing the type of cache implementation or algorithm
   * used.
   * @return a implentation of the Cache interface defined by the Service Provider for
   * the specified cache type.
   */
  public abstract com.cp.common.util.cache.Cache getCacheInstance(CacheType cacheType);

  /**
   * Returns a Cache implementation implementing the Least Frequently Used algorithm.
   * @return a LFU Cache implementation.
   */
  public com.cp.common.util.cache.Cache getLFUCacheInstance() {
    return getCacheInstance(CacheType.LFU);
  }

  /**
   * Returns a Cache implementation implementing the Least Recently Used algorithm.
   * @return a LRU Cache implementation.
   */
  public com.cp.common.util.cache.Cache getLRUCacheInstance() {
    return getCacheInstance(CacheType.LRU);
  }

}
