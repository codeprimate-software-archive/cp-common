/*
 * JndiObjectConnectionFactoryTest.java (c) 6 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.6
 * @see com.cp.common.sql.JndiObjectConnectionFactory
 */

package com.cp.common.sql;

import com.cp.common.context.config.Config;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.jndi.JndiTemplate;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class JndiObjectConnectionFactoryTest extends TestCase {

  private static SimpleNamingContextBuilder namingContext;

  private final Mockery context = new Mockery();

  public JndiObjectConnectionFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(JndiObjectConnectionFactoryTest.class);
    return new JndiObjectConnectionFactoryTestSetup(suite);
  }

  protected <T extends ConnectionFactory> T getConnectionFactory(final Config config) {
    return (T) new JndiObjectConnectionFactory(config);
  }

  protected <T extends ConnectionFactory> T getConnectionFactory(final Config config, final JndiTemplate jndiTemplate) {
    return (T) new JndiObjectConnectionFactory(config, jndiTemplate);
  }

  public void testGetFullyQualifiedJndiName() throws Exception {
    final Config mockConfig = context.mock(Config.class);

    context.checking(new Expectations() {{
      oneOf(mockConfig).getStringPropertyValue(JndiObjectConnectionFactory.JNDI_NAMESPACE_PROPERTY, JndiObjectConnectionFactory.DEFAULT_JNDI_NAMESPACE);
      will(returnValue("tomcat:mock/env/"));
      allowing(mockConfig).getStringPropertyValue(JndiObjectConnectionFactory.JNDI_NAMESPACE_PROPERTY, JndiObjectConnectionFactory.DEFAULT_JNDI_NAMESPACE);
      will(returnValue(JndiObjectConnectionFactory.DEFAULT_JNDI_NAMESPACE));
    }});

    final JndiObjectConnectionFactory connectionFactory = getConnectionFactory(mockConfig);

    assertEquals("tomcat:mock/env/bean/MyService", connectionFactory.getFullyQualifiedJndiName("bean/MyService"));
    assertEquals("java:comp/env/jdbc/MyDataSource", connectionFactory.getFullyQualifiedJndiName("jdbc/MyDataSource"));
    assertEquals("was:cell/nodes/server/env/conf/MyConfig", connectionFactory.getFullyQualifiedJndiName("was:cell/nodes/server/env/conf/MyConfig"));
  }

  public void testGetFullyQualifiedJndiNameWithAnEmptyJndiName() throws Exception {
    final JndiObjectConnectionFactory connectionFactory = getConnectionFactory(null);
    String jndiName = null;

    assertNull(jndiName);

    try {
      jndiName = connectionFactory.getFullyQualifiedJndiName(" ");
      fail("Calling getFullyQualifiedJndiName with an empty JNDI name argument should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JNDI name of the resource must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getFullyQualifiedJndiName with an empty JNDI name argument threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(jndiName);
  }

  public void testGetFullyQualifiedJndiNameWithANullJndiName() throws Exception {
    final JndiObjectConnectionFactory connectionFactory = getConnectionFactory(null);
    String jndiName = null;

    assertNull(jndiName);

    try {
      jndiName = connectionFactory.getFullyQualifiedJndiName(null);
      fail("Calling getFullyQualifiedJndiName with a null JNDI name argument should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The JNDI name of the resource must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getFullyQualifiedJndiName with a null JNDI name argument threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(jndiName);
  }

  public void testGetJndiTemplate() throws Exception {
    final JndiTemplate expectedJndiTemplate = new JndiTemplate();
    assertSame(expectedJndiTemplate, ((JndiObjectConnectionFactory) getConnectionFactory(null, expectedJndiTemplate))
      .getJndiTemplate());
  }

  public void testGetJndiTemplateWithANullJndiTemplateObject() throws Exception {
    final JndiObjectConnectionFactory connectionFactory = getConnectionFactory(null);
    JndiTemplate jndiTemplate = null;

    assertNull(jndiTemplate);

    try {
      jndiTemplate = connectionFactory.getJndiTemplate();
      fail("Calling getJndiTemplate with a null JndiTemplate object should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The JndiTemplate object has not been properly configured for this ConnectionFactory!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getJndiTemplate with a null JndiTemplate object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(jndiTemplate);
  }

  public void testSetJndiTemplate() throws Exception {
    final JndiTemplate expectedJndiTemplate = new JndiTemplate();
    final JndiObjectConnectionFactory connectionFactory = getConnectionFactory(null, expectedJndiTemplate);

    assertSame(expectedJndiTemplate, connectionFactory.getJndiTemplate());

    try {
      connectionFactory.setJndiTemplate(null);
      fail("Calling setJndiTemplate with a null JndiTemplate object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The JndiTemplate object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setJndiTemplate with a null JndiTemplate object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertSame(expectedJndiTemplate, connectionFactory.getJndiTemplate());
  }

  public void testOpenConnection() throws Exception {
    final Config mockConfig = context.mock(Config.class);
    final Connection mockConnection = context.mock(Connection.class);
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockConfig).getStringPropertyValue(JndiObjectConnectionFactory.JNDI_DATASOURCE_NAME_PROPERTY,
        JndiObjectConnectionFactory.DEFAULT_DATASOURCE_NAME);
      will(returnValue("jdbc/MockDataSource"));
      oneOf(mockConfig).getStringPropertyValue(JndiObjectConnectionFactory.JNDI_NAMESPACE_PROPERTY,
        JndiObjectConnectionFactory.DEFAULT_JNDI_NAMESPACE);
      will(returnValue("java:mock/env/"));
      oneOf(mockDataSource).getConnection();
      will(returnValue(mockConnection));
    }});

    namingContext.bind("java:mock/env/jdbc/MockDataSource", mockDataSource);

    final ConnectionFactory connectionFactory = getConnectionFactory(mockConfig, new JndiTemplate());
    Connection actualConnection = null;

    assertNull(actualConnection);

    try {
      actualConnection = connectionFactory.openConnection();
    }
    catch (Exception e) {
      fail("Calling openConnection threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertSame(mockConnection, actualConnection);

    context.assertIsSatisfied();
  }

  public void testOpenConnectionThrowsSQLException() throws Exception {
    final Config mockConfig = context.mock(Config.class);
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockConfig).getStringPropertyValue(JndiObjectConnectionFactory.JNDI_DATASOURCE_NAME_PROPERTY,
        JndiObjectConnectionFactory.DEFAULT_DATASOURCE_NAME);
      will(returnValue("jdbc/TestDataSource"));
      oneOf(mockConfig).getStringPropertyValue(JndiObjectConnectionFactory.JNDI_NAMESPACE_PROPERTY,
        JndiObjectConnectionFactory.DEFAULT_JNDI_NAMESPACE);
      will(returnValue("tomcat:test/env/"));
      oneOf(mockDataSource).getConnection();
      will(throwException(new SQLException("Failed to create JDBC connection!")));
    }});

    namingContext.bind("tomcat:test/env/jdbc/TestDataSource", mockDataSource);

    final ConnectionFactory connectionFactory = getConnectionFactory(mockConfig, new JndiTemplate());
    Connection actualConnection = null;

    assertNull(actualConnection);

    try {
      actualConnection = connectionFactory.openConnection();
      fail("Calling openConnection throwing a SQLException should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to get connection!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling openConnection throwing a SQLException threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualConnection);

    context.assertIsSatisfied();
  }

  public static final class JndiObjectConnectionFactoryTestSetup extends TestSetup {

    public JndiObjectConnectionFactoryTestSetup(final Test test) {
      super(test);
    }

    @Override
    protected void setUp() throws Exception {
      super.setUp();
      namingContext = new SimpleNamingContextBuilder();
      namingContext.activate();
    }

    @Override
    protected void tearDown() throws Exception {
      super.tearDown();
      namingContext.clear();
    }
  }

}
