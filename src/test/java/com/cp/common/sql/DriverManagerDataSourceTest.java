/*
 * DriverManagerDataSourceTest.java (c) 6 September 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.9.6
 * @see com.cp.common.sql.DriverManagerDataSource
 * @see junit.framework.TestCase
 */

package com.cp.common.sql;

import com.cp.common.lang.Assert;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Mockery;

public class DriverManagerDataSourceTest extends TestCase {

  private int index = 0;

  private final Mockery context = new Mockery();

  public DriverManagerDataSourceTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DriverManagerDataSourceTest.class);
    //suite.addTest(new DriverManagerDataSourceTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@host:1521:sid", "dbadmin", "test");

    assertNotNull(dataSource);
    assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getJdbcDriver());
    assertEquals("jdbc:oracle:thin:@host:1521:sid", dataSource.getJdbcUrl());
    assertEquals("dbadmin", dataSource.getUsername());
    assertEquals("test", dataSource.getPassword());

    dataSource = new DriverManagerDataSource(
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@host:1521:sid", null, null);

    assertNotNull(dataSource);
    assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getJdbcDriver());
    assertEquals("jdbc:oracle:thin:@host:1521:sid", dataSource.getJdbcUrl());
    assertNull(dataSource.getUsername());
    assertNull(dataSource.getPassword());
  }

  public void testGetConnection() throws Exception {
    Properties expectedConnectionProperties = new Properties();
    expectedConnectionProperties.setProperty("user", "dbadmin");
    expectedConnectionProperties.setProperty("password", "test");
    expectedConnectionProperties.setProperty("key", "value");

    DriverManagerDataSource dataSource = new TestDriverManagerDataSource(expectedConnectionProperties,
      "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:TestDB", "dbadmin", "test");
    dataSource.setConnectionProperty("key", "value");

    Connection connection = dataSource.getConnection();

    assertNotNull(connection);

    expectedConnectionProperties.setProperty("user", "blumj");
    expectedConnectionProperties.setProperty("password", "jblum");
    dataSource = new TestDriverManagerDataSource(expectedConnectionProperties,
      "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:TestDB", "dbadmin", "test");
    dataSource.setConnectionProperty("user", "blumj");
    dataSource.setConnectionProperty("password", "jblum");
    dataSource.setConnectionProperty("key", "value");
    connection = dataSource.getConnection();

    assertNotNull(connection);

    expectedConnectionProperties.remove("user");
    expectedConnectionProperties.remove("password");
    dataSource = new TestDriverManagerDataSource(expectedConnectionProperties,
      "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:TestDB", null, null);
    dataSource.setConnectionProperty("key", "value");
    connection = dataSource.getConnection();

    assertNotNull(connection);
  }

  public void testGetConnectionWithUsernameAndPassword() throws Exception {
    final Properties expectedConnectionProperties = new Properties();
    expectedConnectionProperties.setProperty("user", "sysdba");
    expectedConnectionProperties.setProperty("password", "syst");

    final DriverManagerDataSource dataSource = new TestDriverManagerDataSource(expectedConnectionProperties,
      "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:TestDB", null, null);
    dataSource.setConnectionProperty("user", "dbadmin");
    dataSource.setConnectionProperty("password", "test");

    final Connection connection = dataSource.getConnection("sysdba", "syst");

    assertNotNull(connection);
  }

  public void testSetConnectionProperty() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    assertNull(dataSource.getConnectionProperty("user"));
    assertNull(dataSource.getConnectionProperty("username"));
    assertNull(dataSource.getConnectionProperty("password"));
    assertNull(dataSource.getConnectionProperty("key"));

    dataSource.setConnectionProperty("user", "jblum");
    dataSource.setConnectionProperty("password", "blumj");
    dataSource.setConnectionProperty("key", "");

    assertEquals("jblum", dataSource.getConnectionProperty("user"));
    assertNull(dataSource.getConnectionProperty("username"));
    assertEquals("blumj", dataSource.getConnectionProperty("password"));
    assertEquals("", dataSource.getConnectionProperty("key"));
  }

  public void testConnectionPropertyWithEmptyProperty() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    try {
      dataSource.setConnectionProperty(" ", "value");
      fail("Calling setConnectionProperty with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The connection property cannot be null or empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setConnectionProperty with an empty property threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testConnectionPropertyWithNullProperty() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    try {
      dataSource.setConnectionProperty(null, "value");
      fail("Calling setConnectionProperty with a null property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The connection property cannot be null or empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setConnectionProperty with a null property threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testConnectionPropertyWithNullValue() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    try {
      dataSource.setConnectionProperty("key", null);
      fail("Calling setConnectionProperty with a null value should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling setConnectionProperty with a null value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testSetConnectionProperties() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    final Properties expectedProperties = new Properties();
    expectedProperties.setProperty("user", "dbadmin");
    expectedProperties.setProperty("password", "test");

    Map<Object, Object> actualProperties = dataSource.getConnectionProperties();

    assertNotNull(actualProperties);
    assertTrue(actualProperties.isEmpty());

    dataSource.setConnectionProperties(expectedProperties);
    actualProperties = dataSource.getConnectionProperties();

    assertNotNull(actualProperties);
    assertEquals(expectedProperties.size(), actualProperties.size());
    assertTrue(actualProperties.entrySet().containsAll(expectedProperties.entrySet()));
  }

  public void testGetUnspecifiedJdbcDriver() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    try {
      dataSource.getJdbcDriver();
      fail("Calling getJdbcDriver when the JDBC driver has not been specified should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The JDBC driver was not properly specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getJdbcDriver when the JDBC driver has not been specified threw an expected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetJdbcDriverWithEmptyValue() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource(
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@host:1521:sid", "dbadmin", "test");

    assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getJdbcDriver());

    try {
      dataSource.setJdbcDriver(" ");
      fail("Calling setJdbcDriver with an empty String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JDBC driver must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setJdbcDriver with an empty String value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getJdbcDriver());
  }

  public void testSetJdbcDriverWithNullValue() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource(
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@host:1521:sid", "dbadmin", "test");

    assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getJdbcDriver());

    try {
      dataSource.setJdbcDriver(null);
      fail("Calling setJdbcDriver with a null value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JDBC driver must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setJdbcDriver with a null value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertEquals("oracle.jdbc.driver.OracleDriver", dataSource.getJdbcDriver());
  }

  public void testGetUnspecifiedJdbcUrl() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    try {
      dataSource.getJdbcUrl();
      fail("Calling getJdbcUrl when the JDBC URL (connection string) has not been specified should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The JDBC URL was not properly specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getJdbcUrl when the JDBC URL (connection string) has not been specified threw an expected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetJdbcUrlWithEmptyValue() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource(
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@host:1521:sid", "dbadmin", "test");

    assertEquals("jdbc:oracle:thin:@host:1521:sid", dataSource.getJdbcUrl());

    try {
      dataSource.setJdbcUrl(" ");
      fail("Calling setJdbcUrl with an empty String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JDBC URL must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setJdbcUrl with an empty String value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertEquals("jdbc:oracle:thin:@host:1521:sid", dataSource.getJdbcUrl());
  }

  public void testSetJdbcUrlWithNullValue() throws Exception {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource(
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@host:1521:sid", "dbadmin", "test");

    assertEquals("jdbc:oracle:thin:@host:1521:sid", dataSource.getJdbcUrl());

    try {
      dataSource.setJdbcUrl(null);
      fail("Calling setJdbcUrl with a null value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JDBC URL must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setJdbcUrl with a null value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertEquals("jdbc:oracle:thin:@host:1521:sid", dataSource.getJdbcUrl());
  }

  private final class TestDriverManagerDataSource extends DriverManagerDataSource {

    private final Properties expectedConnectionProperties;

    public TestDriverManagerDataSource(final Properties connectionProperties) {
      Assert.notNull(connectionProperties, "The connection properties cannot be null!");
      this.expectedConnectionProperties = connectionProperties;
    }

    public TestDriverManagerDataSource(final Properties connectionProperties,
                                       final String jdbcDriver,
                                       final String jdbcUrl,
                                       final String username,
                                       final String password)
    {
      super(jdbcDriver, jdbcUrl, username, password);
      Assert.notNull(connectionProperties, "The connection properties cannot be null!");
      this.expectedConnectionProperties = connectionProperties;
    }

    Connection getConnectionUsingDriverManager(final String actualJdbcUrl, final Properties actualConnectionProperties)
      throws SQLException
    {
      assertEquals(getJdbcUrl(), actualJdbcUrl);
      assertNotSame(expectedConnectionProperties, actualConnectionProperties);
      assertEquals(expectedConnectionProperties, actualConnectionProperties);
      return context.mock(Connection.class, "test" + (index++));
    }
  }

}
