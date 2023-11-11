/*
 * AbstractConnectionFactoryTest.java (c) 6 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.6
 * @see com.cp.common.sql.AbstractConnectionFactory
 */

package com.cp.common.sql;

import com.cp.common.context.config.Config;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AbstractConnectionFactoryTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractConnectionFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractConnectionFactoryTest.class);
    return suite;
  }

  protected AbstractConnectionFactory getConnectionFactory(final Config config) {
    return new AbstractConnectionFactory(config) {
      public Connection openConnection() throws SystemException {
        throw new UnsupportedOperationException("Not Implemented!");
      }
    };
  }

  protected AbstractConnectionFactory getConnectionFactory(final Config config, final Connection connection) {
    return new AbstractConnectionFactory(config) {
      public Connection openConnection() throws SystemException {
        return connection;
      }
    };
  }

  public void testGetConfig() throws Exception {
    final Config mockConfig = context.mock(Config.class);
    final AbstractConnectionFactory connectionFactory = getConnectionFactory(mockConfig);
    Config actualConfig = null;

    assertNull(actualConfig);

    try {
      actualConfig = connectionFactory.getConfig();
    }
    catch (Exception e) {
      fail("Calling the getConfig method on ConnectionFactory with a non-null Config object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertSame(mockConfig, actualConfig);
  }

  public void testGetConfigWithNullConfigObject() throws Exception {
    final AbstractConnectionFactory connectionFactory = getConnectionFactory(null);
    Config actualConfig = null;

    assertNull(actualConfig);

    try {
      actualConfig = connectionFactory.getConfig();
      fail("Calling the getConfig method on ConnectionFactory with a null Config object should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The ConnectionFactory was not properly initialized with a Config object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the getConfig method on ConnectionFactory with a null Config object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualConfig);
  }

  public void testGetObject() throws Exception {
    final Connection mockConnection = context.mock(Connection.class);
    assertSame(mockConnection, getConnectionFactory(null, mockConnection).getObject());
  }

  public void testGetObjectType() throws Exception {
    assertEquals(Connection.class, getConnectionFactory(null).getObjectType());
  }

  public void testIsSingleton() throws Exception {
    assertFalse(getConnectionFactory(null).isSingleton());
  }

  public void testCloseConnection() throws Exception {
    final Connection mockConnection = context.mock(Connection.class);

    context.checking(new Expectations() {{
      oneOf(mockConnection).close();
    }});

    getConnectionFactory(null).closeConnection(mockConnection);

    context.assertIsSatisfied();
  }

}
