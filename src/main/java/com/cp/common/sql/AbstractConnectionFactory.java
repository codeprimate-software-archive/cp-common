/*
 * AbstractConnectionFactory.java (c) 4 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.6
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.sql.ConnectionFactory
 * @see com.cp.common.sql.JdbcUtil
 * @see java.sql.Connection
 * @see org.springframework.beans.factory.FactoryBean
 */

package com.cp.common.sql;

import com.cp.common.context.config.Config;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

public abstract class AbstractConnectionFactory implements ConnectionFactory, FactoryBean {

  protected final Log logger = LogFactory.getLog(getClass());

  private final Config config;

  /**
   * Default constructor for creating an instance of the AbstractConnectionFactory class.
   */
  public AbstractConnectionFactory() {
    this(null);
  }

  /**
   * Creates an instance of the AbstractConnectionFactory class initialized with the specifie Config object.
   * @param config the configuration object used by this ConnectionFactory to initialize and establish JDBC connections.
   */
  public AbstractConnectionFactory(final Config config) {
    this.config = config;
  }

  /**
   * Gets the configuration object used by this ConnectionFactory to establish JDBC connections.
   * @return a Config object to read configuration information used to establish JDBC connections.
   * @throws IllegalStateException if the Config object was not properly initialized.
   */
  protected Config getConfig() {
    Assert.state(ObjectUtil.isNotNull(config), "The ConnectionFactory was not properly initialized with a Config object!");
    return config;
  }

  /**
   * Gets the Connection object created by this ConnectionFactory.
   * @return a Connection object created by this ConnectionFactory.
   * @throws Exception if the Connection object cannot be successfully created!
   */
  public Object getObject() throws Exception {
    return openConnection();
  }

  /**
   * Returns the Connection class type as the type of Objects created by this ConnectionFactory.
   * @return the Connection class type.
   */
  public Class getObjectType() {
    return Connection.class;
  }

  /**
   * Returns a boolean value indicating whether Connections created by this ConnectionFactory are singletons
   * or prototypes.
   * @return a boolean value indicating whether the Connections created by this ConnectionFactory are singletons
   * or prototypes.
   */
  public boolean isSingleton() {
    return false;
  }

  /**
   * Closes the specified JDBC Connection to the data source.
   * @param connection the JDBC Connection to the data source to close.
   * @throws SystemException if the JDBC Connection cannot be closed!
   * @see ConnectionFactory#openConnection()
   */
  public void closeConnection(final Connection connection) throws SystemException {
    JdbcUtil.closeConnection(connection);
  }

}
