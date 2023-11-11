/*
 * AbstractSearchFactory.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 */

package com.cp.common.util.search;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.PropertyManager;
import org.apache.log4j.Logger;

public abstract class AbstractSearchFactory {

  private static AbstractSearchFactory INSTANCE;

  private static final Logger logger = Logger.getLogger(AbstractSearchFactory.class);

  private static final String SEARCH_FACTORY_PROPERTY_KEY = "cp-common.search.factory";

  /**
   * Factory method returning a service provider instance of the AbstractSearchFactory class.
   * @return a service provider instance of the AbstractSearchFatory class.
   */
  public synchronized static AbstractSearchFactory getInstance() {
    if (ObjectUtil.isNull(INSTANCE)) {
      String defaultSearchFactoryClassName = null;

      try {
        defaultSearchFactoryClassName = PropertyManager.getInstance().getStringPropertyValue(SEARCH_FACTORY_PROPERTY_KEY);

        if (logger.isDebugEnabled()) {
          logger.debug("defaultSearchFactoryClassName (" + defaultSearchFactoryClassName + ")");
        }

        INSTANCE = (AbstractSearchFactory) ClassUtil.getInstance(defaultSearchFactoryClassName);
      }
      catch (ClassNotFoundException e) {
        logger.error("Unable to find provider class (" + defaultSearchFactoryClassName + ") in CLASSPATH!", e);
        throw new ConfigurationException("Unable to find provider class (" + defaultSearchFactoryClassName + ") in CLASSPATH!", e);
      }
      catch (ConfigurationException e) {
        logger.error("Unable to determine provider class from property (" + SEARCH_FACTORY_PROPERTY_KEY + ")!", e);
        throw new ConfigurationException("Unable to determine provider class from property (" + SEARCH_FACTORY_PROPERTY_KEY + ")!", e);
      }
      catch (InstantiationException e) {
        logger.error("Unable to create an instance of provider class (" + defaultSearchFactoryClassName + ")!", e);
        throw new ConfigurationException("Unable to create an instance of provider class (" + defaultSearchFactoryClassName + ")!", e);
      }
    }

    return INSTANCE;
  }

  /**
   * Gets an instance of the Binary Search algorithm.
   * @param filter the SearchFilter used to identify the item being searched.
   * @return a Searcher that implements the Binary Search algorithm.
   */
  public Searcher getBinarySearch(final BinarySearchFilter filter) {
    return new BinarySearch(filter);
  }

  /**
   * Gets an instance of the Indexed Search algorithm.
   * @param filter the SearchFilter used to identify the item being searched.
   * @return a Searcher that implements the Indexed Search algorithm.
   */
  public abstract com.cp.common.util.search.Searcher getIndexedSearch(com.cp.common.util.search.SearchFilter filter);

  /**
   * Gets an instance of the Linear Search algorithm.
   * @param filter the SearchFilter used to identify the item being searched.
   * @return a Searcher that implements the Linear Search algorithm.
   */
  public com.cp.common.util.search.Searcher getLinearSearch(final SearchFilter filter) {
    return new com.cp.common.util.search.LinearSearch(filter);
  }

  /**
   * Gets a Searcher implementing the search algorithm based on the SearchType enumerated-type.
   * @param searchType the enumerated-type specifying the searching algorithm to get.
   * @param filter the SearchFilter used to identify the item being searched.
   * @return a Searcher implementing the searching algorithm specified by the SearchType enumerated-type.
   */
  public com.cp.common.util.search.Searcher getSearch(final com.cp.common.util.search.SearchType searchType, final SearchFilter filter) {
    if (logger.isDebugEnabled()) {
      logger.debug("searchType (" + searchType + ")");
      logger.debug("filter (" + filter + ")");
    }
    if (SearchType.BINARY_SEARCH.equals(searchType)) {
      return getBinarySearch((BinarySearchFilter) filter);
    }
    else if (com.cp.common.util.search.SearchType.INDEXED_SEARCH.equals(searchType)) {
      return getIndexedSearch(filter);
    }
    else if (SearchType.LINEAR_SEARCH.equals(searchType)) {
      return getLinearSearch(filter);
    }
    else {
      logger.warn("The searchType (" + searchType + ") is not a supported search algorithm!");
      throw new IllegalArgumentException("The searchType (" + searchType + ") is not a supported search algorithm!");
    }
  }

}
