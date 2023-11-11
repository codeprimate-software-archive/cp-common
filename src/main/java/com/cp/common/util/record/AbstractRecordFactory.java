/*
 * AbstractRecordFactory.java (c) 3 March 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 */

package com.cp.common.util.record;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.PropertyManager;
import org.apache.log4j.Logger;

public abstract class AbstractRecordFactory {

  private static AbstractRecordFactory INSTANCE;

  private static final Logger logger = Logger.getLogger(AbstractRecordFactory.class);

  private static final String RECORD_FACTORY_PROPERTY_KEY = "cp-common.record.factory";

  /**
   * Factory method returning a service provider instance of the AbstractRecordFactory class.
   * @return a service provider instance of the AbstractRecordFatory class.
   */
  public synchronized static AbstractRecordFactory getInstance() {
    if (ObjectUtil.isNull(INSTANCE)) {
      String defaultRecordFactoryClassName = null;

      try {
        defaultRecordFactoryClassName = PropertyManager.getInstance().getStringPropertyValue(RECORD_FACTORY_PROPERTY_KEY);

        if (logger.isDebugEnabled()) {
          logger.debug("defaultRecordFatoryClassName = (" + defaultRecordFactoryClassName + ")");
        }

        INSTANCE = (AbstractRecordFactory) ClassUtil.getInstance(defaultRecordFactoryClassName);
      }
      catch (ClassNotFoundException e) {
        logger.error("Unable to find provider class (" + defaultRecordFactoryClassName + ") in CLASSPATH!", e);
        throw new ConfigurationException("Unable to find provider class (" + defaultRecordFactoryClassName + ") in CLASSPATH!", e);
      }
      catch (ConfigurationException e) {
        logger.error("Unable to determine provider class from property (" + RECORD_FACTORY_PROPERTY_KEY + ")!", e);
        throw new ConfigurationException("Unable to determine provider class from property (" + RECORD_FACTORY_PROPERTY_KEY + ")!", e);
      }
      catch (InstantiationException e) {
        logger.error("Unable to create an instance of provider class (" + defaultRecordFactoryClassName + ")!", e);
        throw new ConfigurationException("Unable to create an instance of provider class (" + defaultRecordFactoryClassName + ")!", e);
      }
    }

    return INSTANCE;
  }

  /**
   * Returns a service provider instance of the Record interface.
   * @return a Record interface implementation.
   */
  public abstract com.cp.common.util.record.Record getRecordInstance();

  /**
   * Returns a service provider instance of the Record interface based on an
   * existing Record object.
   * @param record the Record instance on which the newly created Record object
   * is based.
   * @return a service provider implementation of the Record interface.
   */
  public abstract com.cp.common.util.record.Record getRecordInstance(com.cp.common.util.record.Record record);

  /**
   * Returns a service provider instance of the RecordTable interface.
   * @return a service provider implementation of the RecordTable interface.
   */
  public abstract RecordTable getRecordTableInstance();

  /**
   * Returns a service provider instance of the RecordTable interface initialized
   * to the specified structure, columns.
   * @param columns the array of columns constituting the structure of the
   * RecordTable instance.
   * @return a service provider implemenation of the RecordTable interface.
   */
  public abstract RecordTable getRecordTableInstance(Column[] columns);

  /**
   * Returns a service provider instance of the RecordTable interface based on an
   * existing RecordTable object.
   * @param recordTable the RecordTable object on which the newly created RecordTable
   * object is based.
   * @return a service provider implementation of the RecordTable interface.
   */
  public abstract com.cp.common.util.record.RecordTable getRecordTableInstance(RecordTable recordTable);

}
