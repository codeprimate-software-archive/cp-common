/*
 * LoggingPropertyChangeListener.java (c) 14 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.16
 * @see com.cp.common.beans.event.AbstractPropertyChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggingPropertyChangeListener extends AbstractPropertyChangeListener {

  private static final Level DEFAULT_LOG_LEVEL = Level.DEBUG;

  private final Level logLevel;

  private final Logger logger;

  /**
   * Creates an instance of the PropertyChangeListener class, enabling logging for all properties,
   * or properties for which this listener has been registered, on the bean.
   * @param bean the Object for which the LoggingPropertyChangeListener will be registered for.
   */
  public LoggingPropertyChangeListener(final Object bean) {
    this(bean, DEFAULT_LOG_LEVEL);
  }

  /**
   * Creates an instance of the PropertyChangeListener class, enabling logging for all properties,
   * or properties for which this listener has been registered, on the bean, initialized with the
   * specified log level.  The only properties of the bean that will be logged are ones where the
   * log level of this class configured in the log config file is higher than the log level
   * specified in the instance of this class.
   * @param bean the Object for which the LoggingPropertyChangeListener will be registered for.
   * @param logLevel is a Log4j log level.
   */
  public LoggingPropertyChangeListener(final Object bean, final Level logLevel) {
    Assert.notNull(bean, "The bean cannot be null!");
    this.logger = Logger.getLogger(bean.getClass());
    this.logLevel = ObjectUtil.getDefaultValue(logLevel, DEFAULT_LOG_LEVEL);
  }

  /**
   * Gets the logger used by this listener to log property change events.
   * @return the Logger used by this listener to log property change events.
   */
  protected Logger getLogger() {
    return logger;
  }

  /**
   * Returns the log level of the LoggingPropertyChangeListener.
   * @return the Log4j log level.
   */
  public Level getLogLevel() {
    return logLevel;
  }

  /**
   * Logs the specified event source (bean), property, old and new values.
   * @param event the PropertyChangeEvent tracking the property that changed and will be logged.
   */
  public void handle(final PropertyChangeEvent event) {
    //System.out.println("logger level (" + logger.getLevel() + ") bean level (" + getLogLevel() + ")");
    if (getLogger().isEnabledFor(getLogLevel())) {
      getLogger().log(getLogLevel(), "property (" + event.getPropertyName() + ") of bean ("
        + event.getSource().getClass().getName() + ") changed from (" + event.getOldValue() + ") to ("
        + event.getNewValue() + ")");
    }
  }

}
