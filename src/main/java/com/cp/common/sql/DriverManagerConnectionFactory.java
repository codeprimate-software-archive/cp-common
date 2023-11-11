/*
 * DriverManagerConnectionFactory.java (c) 4 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.7
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.sql.AbstractConnectionFactory
 * @see java.sql.DriverManager
 */

package com.cp.common.sql;

import com.cp.common.context.config.Config;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverManagerConnectionFactory extends AbstractConnectionFactory {

  public static final String JDBC_DRIVER = "cp-common.jdbc.driver";
  public static final String JDBC_PASSWORD = "cp-common.jdbc.password";
  public static final String JDBC_URL = "cp-common.jdbc.url";
  public static final String JDBC_USERNAME = "cp-common.jdbc.username";

  /**
   * Creates an instance of the DriverManagerConnectionFactory class initialized with the specified Config object.
   * @param config the Config object used to obtain configuration information.
   */
  public DriverManagerConnectionFactory(final Config config) {
    super(config);
  }

  /**
   * Opens a JDBC Connection to the underlying data source.
   * @return a JDBC Connection object to the data source.
   * @throws SystemException if the JDBC Connection cannot be opened!
   * @see ConnectionFactory#closeConnection(java.sql.Connection)
   */
  public Connection openConnection() throws SystemException {
    final String driver = getConfig().getStringPropertyValue(JDBC_DRIVER);
    final String url = getConfig().getStringPropertyValue(JDBC_URL);
    final String username = getConfig().getStringPropertyValue(JDBC_USERNAME, false);
    final String password = getConfig().getStringPropertyValue(JDBC_PASSWORD, false);

    if (logger.isDebugEnabled()) {
      logger.debug("JDBC driver (" + driver + ")");
      logger.debug("JDBC url (" + url + ")");
      logger.debug("JDBC username (" + username + ")");
      logger.debug("JDBC password (" + password + ")");
    }

    JdbcUtil.loadJdbcDriver(driver);

    final Properties props = new Properties();

    if (StringUtil.isNotEmpty(username)) {
      props.put("user", username);
    }

    if (StringUtil.isNotEmpty(password)) {
      props.put("password", password);
    }

    try {
      return getConnectionUsingDriverManager(url, props);
    }
    catch (SQLException e) {
      logger.error("Failed to get a JDBC Connection to the following data source (" + url + ")!");
      throw new SystemException("Failed to get a JDBC Connection to the following data source (" + url + ")!", e);
    }
  }

  /**
   * Gets a JDBC Connection object from the DriverManager.
   * @param url the JDBC url used identify and connect to the data store.
   * @param props the Properties object containing configuration information such as user credentials.
   * @return a JDBC Connection object to the data store idenfitied by the specified URL.
   * @throws SQLException if a JDBC Connection to the data store identified by the specified URL cannot be established.
   */
  Connection getConnectionUsingDriverManager(final String url, final Properties props) throws SQLException {
    return DriverManager.getConnection(url, props);
  }

}
