/*
 * JdbcUtilTest.java (c) 6 12 2008 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.6
 */

package com.cp.common.sql;

import com.cp.common.util.SystemException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class JdbcUtilTest extends TestCase {

  private final Mockery context = new Mockery();

  public JdbcUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(JdbcUtilTest.class);
    return suite;
  }

  public void testCloseConnection() throws Exception {
    final Connection mockConnection = context.mock(Connection.class);

    context.checking(new Expectations() {{
      oneOf(mockConnection).close();
    }});

    try {
      JdbcUtil.closeConnection(mockConnection);
    }
    catch (Exception e) {
      fail("Calling closeConnection with a non-null Connection object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseConnectionThrowsSQLException() throws Exception {
    final Connection mockConnection = context.mock(Connection.class);

    context.checking(new Expectations() {{
      oneOf(mockConnection).close();
      will(throwException(new SQLException("Failed to close JDBC connection!")));
    }});

    try {
      JdbcUtil.closeConnection(mockConnection);
    }
    catch (Exception e) {
      fail("Calling closeConnection on a Connection object throwing a SQLException threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseNullConnection() throws Exception {
    try {
      JdbcUtil.closeConnection(null);
    }
    catch (Exception e) {
      fail("Calling closeConnection with a null Connection object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testResultSet() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);

    context.checking(new Expectations() {{
      oneOf(mockResultSet).close();
    }});

    try {
      JdbcUtil.closeResultSet(mockResultSet);
    }
    catch (Exception e) {
      fail("Calling closeResultSet on a non-null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseStatement() throws Exception {
    final Statement mockStatement = context.mock(Statement.class);

    context.checking(new Expectations() {{
      oneOf(mockStatement).close();
    }});

    try {
      JdbcUtil.closeStatement(mockStatement);
    }
    catch (Exception e) {
      fail("Calling closeStatement on a non-null Statement object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseStatementThrowsSQLException() throws Exception {
    final Statement mockStatement = context.mock(Statement.class);

    context.checking(new Expectations() {{
      oneOf(mockStatement).close();
      will(throwException(new SQLException("Failed to close JDBC statement!")));
    }});

    try {
      JdbcUtil.closeStatement(mockStatement);
    }
    catch (Exception e) {
      fail("Calling closeStatement on a Statement object throwing a SQLException threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseNullStatement() throws Exception {
    try {
      JdbcUtil.closeStatement(null);
    }
    catch (Exception e) {
      fail("Calling closeStatement on a null Statement object threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testLoadJdbcDriver() throws Exception {
    try {
      JdbcUtil.loadJdbcDriver("sun.jdbc.odbc.JdbcOdbcDriver");
    }
    catch (Exception e) {
      fail("Calling loadJdbcDriver for the Sun ODBC JDBC Driver threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testLoadFakeJdbcDriver() throws Exception {
    try {
      JdbcUtil.loadJdbcDriver("mock.jdbc.driver.MockDriver");
      fail("Calling loadJdbcDriver for a non-existing JDBC Driver (mock.jdbc.driver.MockDriver) should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to load JDBC driver (mock.jdbc.driver.MockDriver)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling loadJdbcDriver for a non-existing JDBC Driver (mock.jdbc.driver.MockDriver) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testLoadJdbcDriverWithEmptyDriverName() throws Exception {
    try {
      JdbcUtil.loadJdbcDriver(" ");
      fail("Calling loadJdbcDriver with an empty Driver name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JDBC driver class name must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling loadJdbcDriver with an empty Driver name threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testLoadJdbcDriverWithNullDriverName() throws Exception {
    try {
      JdbcUtil.loadJdbcDriver(null);
      fail("Calling loadJdbcDriver with a null Driver name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JDBC driver class name must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling loadJdbcDriver with a null Driver name threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

}
