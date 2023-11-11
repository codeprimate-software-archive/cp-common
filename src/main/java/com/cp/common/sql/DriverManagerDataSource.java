/*
 * DriverManagerDataSource.java (c) 6 September 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.9.6
 * @see com.cp.common.sql.AbstractDataSource
 * @see java.sql.DriverManager
 */

package com.cp.common.sql;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverManagerDataSource extends AbstractDataSource {

  private boolean jdbcDriverLoaded = false;

  private Properties defaultConnectionProperties = new Properties();

  private String jdbcDriver;
  private String jdbcUrl;
  private String username;
  private String password;

  /**
   * Default constructor to create an unitialized instance of the DriverManagerDataSource class.
   */
  public DriverManagerDataSource() {
  }

  /**
   * Constructs an instance of the DriverManagerDataSource class initilized with the specified JDBC driver,
   * JDBC URL (connection string), username and password connection criteria.
   * @param jdbcDriver a String value specifying the JDBC driver used to manage the connection with the underlying
   * data source.
   * @param jdbcUrl a String value specifyging the JDBC URL (connection string) to the underlying data source.
   * @param username a String value specifying the user to connect to the underlying data source as.
   * @param password a String value specifying the password of the user who is connecting to the underlying data source.
   */
  public DriverManagerDataSource(final String jdbcDriver,
                                 final String jdbcUrl,
                                 final String username,
                                 final String password)
  {
    setJdbcDriver(jdbcDriver);
    setJdbcUrl(jdbcUrl);
    setUsername(username);
    setPassword(password);
  }

  /**
   * Attempts to establish a connection with the data source that this DataSource object represents.
   * @return a connection to the data source.
   * @throws SQLException if a database access error occurs.
   */
  public Connection getConnection() throws SQLException {
    return getConnection(null, null);
  }

  /**
   * Attempts to establish a connection with the data source that this DataSource object represents.
   * @param username a String value specifying the database user on whose behalf the connection is being made.
   * @param password a String value specifying the user's password.
   * @return a connection to the data source.
   * @throws SQLException if a database access error occurs.
   */
  public Connection getConnection(String username, String password) throws SQLException {
    if (!jdbcDriverLoaded) {
      JdbcUtil.loadJdbcDriver(getJdbcDriver());
      jdbcDriverLoaded = true;
    }

    final Properties localConnectionProperties = new Properties();
    localConnectionProperties.putAll(defaultConnectionProperties);

    username = ObjectUtil.getDefaultValue(username, getConnectionProperty("user"), getUsername());
    password = ObjectUtil.getDefaultValue(password, getConnectionProperty("password"), getPassword());

    if (StringUtil.isNotEmpty(username)) {
      localConnectionProperties.setProperty("user", username);
    }

    if (StringUtil.isNotEmpty(password)) {
      localConnectionProperties.put("password", password);
    }

    return getConnectionUsingDriverManager(getJdbcUrl(), localConnectionProperties);
  }

  /**
   * Gets a JDBC Connection object from the DriverManager.
   * @param jdbcUrl the JDBC url used identify and connect to the data source.
   * @param connectionProperties the Properties object containing configuration information such as user credentials.
   * @return a JDBC Connection object to the data source idenfitied by the specified URL.
   * @throws SQLException if a JDBC Connection to the data source identified by the specified URL cannot be established.
   */
  Connection getConnectionUsingDriverManager(final String jdbcUrl, final Properties connectionProperties)
    throws SQLException
  {
    return DriverManager.getConnection(jdbcUrl, connectionProperties);
  }

  /**
   * Gets the value of the specified connection property, identified by name, as a String value.
   * @param property a String value specifying the name of the connection property to return the value for.
   * @return a String specifying the value of the named connection property, or null if the named connection property
   * does not exist.
   */
  public String getConnectionProperty(final String property) {
    return defaultConnectionProperties.getProperty(property);
  }

  /**
   * Allows the caller to specifying additional properties and configuration information upon connecting
   * to the underlying data source.
   * @param property a String value specifying the name of the connection property recognized by the
   * underlying data source.
   * @param value a String specifying the value of the named connection property.
   * @return a String value indicating the previous setting for this specified name connectio property.
   * @throws IllegalArgumentException if the property is null or empty.
   */
  public String setConnectionProperty(final String property, final String value) {
    Assert.notEmpty(property, "The connection property cannot be null or empty!");
    return (String) defaultConnectionProperties.setProperty(property, value);
  }

  /**
   * Gets the collection of connection properties for this DataSource as a Map.
   * @return a Map containing the connnection properties of this DataSource object.
   */
  public Map<Object, Object> getConnectionProperties() {
    return Collections.unmodifiableMap(defaultConnectionProperties);
  }

  /**
   * Sets a collection of connectio properties on this DataSource object.
   * @param connectionProperties a Properties object containing a collection of connection properties recognized
   * by the underlying data source.
   */
  public void setConnectionProperties(final Properties connectionProperties) {
    Assert.notNull(connectionProperties, "The connection properties cannot be null!");
    defaultConnectionProperties.putAll(connectionProperties);
  }

  /**
   * Gets the JDBC driver used to manage connections to the underlying data source.
   * @return a String value specifying the JDBC driver by name for managing connections to the underlying data source.
   * @throws IllegalStateException if the JDBC driver was not specified.
   */
  public String getJdbcDriver() {
    Assert.state(ObjectUtil.isNotNull(jdbcDriver), "The JDBC driver was not properly specified!");
    return jdbcDriver;
  }

  /**
   * Sets the JDBC driver used to manage connections to the underlying data source.
   * @param jdbcDriver a String value specifying the JDBC driver by name for managing connections to the
   * underlying data source.
   * @throws IllegalArgumentException if the JDBC driver specified was null or empty.
   */
  public void setJdbcDriver(final String jdbcDriver) {
    Assert.notEmpty(jdbcDriver, "The JDBC driver must be specified!");
    this.jdbcDriver = jdbcDriver;
  }

  /**
   * Gets the JDBC URL (connection string) used to identify and connect to the underlying data source.
   * @return a String value specifying the JDBC URL (connection string) for identifying and connecting to
   * the underlying data source.
   * @throws IllegalStateException if the JDBC URL was not specified.
   */
  public String getJdbcUrl() {
    Assert.state(ObjectUtil.isNotNull(jdbcUrl), "The JDBC URL was not properly specified!");
    return jdbcUrl;
  }

  /**
   * Sets the JDBC URL (connection string) used to identify and connect to the underlying data source.
   * @param jdbcUrl a String value specifying the JDBC URL (connection string) for identifying and connecting to
   * the underlying data source.
   * @throws IllegalStateException if the JDBC URL specified was null or empty.
   */
  public void setJdbcUrl(final String jdbcUrl) {
    Assert.notEmpty(jdbcUrl, "The JDBC URL must be specified!");
    this.jdbcUrl = jdbcUrl;
  }

  /**
   * Gets the user to authenticate as when connecting to the underlying data source.
   * @return a String value specifying the user to connect as when connecting to the underlying data source.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the user to authenticate as when connecting to the underlying data source.
   * @param username a String value specifying the name of the user to connect as when connecting to the
   * underlying data source.
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Gets the password of the user specified by the username property used to authenticate when connecting to the
   * underlying data source.
   * @return a String value specifying the password of the user identified by the username property.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of the user specified by the username property used to authenticate when connecting to the
   * underlying data source.
   * @param password a String value specifying the password of the user identified by the username property.
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Gets String representation of this DataSource.
   * @return a String value exernalizing the internal state of this DataSource object.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{JDBC driver = ");
    buffer.append(jdbcDriver);
    buffer.append(", JDBC URL = ").append(jdbcUrl);
    buffer.append(", username = ").append(getUsername());
    buffer.append(", password = ").append(getPassword());
    buffer.append(", connection properties (").append(getConnectionProperties()).append(")");
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  @Override public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }
}
