/*
 * DriverManagerConnectionFactoryTest.java (c) 6 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.7
 * @see com.cp.common.sql.DriverManagerConnectionFactory
 * @see com.cp.common.test.TestUtil
 */

package com.cp.common.sql;

import com.cp.common.context.config.Config;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class DriverManagerConnectionFactoryTest extends TestCase {

  private final Mockery context = new Mockery();

  public DriverManagerConnectionFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DriverManagerConnectionFactoryTest.class);
    return suite;
  }

  protected ConnectionFactory getConnectionFactory(final Config config,
                                                   final String expectedJdbcUrl,
                                                   final String expectedJdbcUsername,
                                                   final String expectedJdbcPassword,
                                                   final Connection mockConnection,
                                                   final SQLException e)
  {
    return new DriverManagerConnectionFactory(config) {
      @Override
      Connection getConnectionUsingDriverManager(final String url, final Properties props) throws SQLException {
        assertEquals(expectedJdbcUrl, url);
        TestUtil.assertNullEquals(StringUtil.getNullIfEmpty(expectedJdbcUsername), props.get("user"));
        TestUtil.assertNullEquals(StringUtil.getNullIfEmpty(expectedJdbcPassword), props.get("password"));

        if (ObjectUtil.isNotNull(e)) {
          throw e;
        }

        return mockConnection;
      }
    };
  }

  public void testOpenConnectionWithUserCredentials() throws Exception {
    final String expectedJdbcUrl = "mock.ds@myDb:myServer:1521";
    final String expectedJdbcUsername = "dbadmin";
    final String expectedJdbcPassword = "dbpass";

    final Config mockConfig = context.mock(Config.class);
    final Connection mockConnection = context.mock(Connection.class);

    context.checking(new Expectations() {{
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_DRIVER);
      will(returnValue("sun.jdbc.odbc.JdbcOdbcDriver"));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_URL);
      will(returnValue(expectedJdbcUrl));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_USERNAME, false);
      will(returnValue(expectedJdbcUsername));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_PASSWORD, false);
      will(returnValue(expectedJdbcPassword));
    }});

    final ConnectionFactory connectionFactory = getConnectionFactory(mockConfig, expectedJdbcUrl,
      expectedJdbcUsername, expectedJdbcPassword, mockConnection, null);

    final Connection actualConnection = connectionFactory.openConnection();

    assertSame(mockConnection, actualConnection);

    context.assertIsSatisfied();
  }

  public void testOpenConnectionWithoutUserCredentials() throws Exception {
    final String expectedJdbcUrl = "mock.ds@myDb:myServer:1521";
    final String expectedJdbcUsername = " ";
    final String expectedJdbcPassword = null;

    final Config mockConfig = context.mock(Config.class);
    final Connection mockConnection = context.mock(Connection.class);

    context.checking(new Expectations() {{
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_DRIVER);
      will(returnValue("sun.jdbc.odbc.JdbcOdbcDriver"));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_URL);
      will(returnValue(expectedJdbcUrl));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_USERNAME, false);
      will(returnValue(expectedJdbcUsername));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_PASSWORD, false);
      will(returnValue(expectedJdbcPassword));
    }});

    final ConnectionFactory connectionFactory = getConnectionFactory(mockConfig, expectedJdbcUrl,
      expectedJdbcUsername, expectedJdbcPassword, mockConnection, null);

    final Connection actualConnection = connectionFactory.openConnection();

    assertSame(mockConnection, actualConnection);

    context.assertIsSatisfied();
  }

  public void testOpenConnectionThrowsSQLException() throws Exception {
    final String expectedJdbcUrl = "mock.ds@myDb:myServer:1521";
    final String expectedJdbcUsername = "dbadmin";
    final String expectedJdbcPassword = "dbpass";

    final Config mockConfig = context.mock(Config.class);

    context.checking(new Expectations() {{
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_DRIVER);
      will(returnValue("sun.jdbc.odbc.JdbcOdbcDriver"));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_URL);
      will(returnValue(expectedJdbcUrl));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_USERNAME, false);
      will(returnValue(expectedJdbcUsername));
      oneOf(mockConfig).getStringPropertyValue(DriverManagerConnectionFactory.JDBC_PASSWORD, false);
      will(returnValue(expectedJdbcPassword));
    }});

    final ConnectionFactory connectionFactory = getConnectionFactory(mockConfig, expectedJdbcUrl,
      expectedJdbcUsername, expectedJdbcPassword, null, new SQLException("Failed to open JDBC connection!"));
    Connection actualConnection = null;

    assertNull(actualConnection);

    try {
      actualConnection = connectionFactory.openConnection();
      fail("Calling openConnection throwing a SQLException should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to get a JDBC Connection to the following data source (" + expectedJdbcUrl + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling openConnection throwing a SQLException threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualConnection);

    context.assertIsSatisfied();
  }

}
