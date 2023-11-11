/*
 * AbstractObjectFactoryTest.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.16
 * @see com.cp.common.lang.support.AbstractObjectFactory
 * @see junit.framework.TestCase
 */

package com.cp.common.lang.factory;

import com.cp.common.context.config.AbstractConfig;
import com.cp.common.context.config.Config;
import com.cp.common.test.mock.MockPrimitiveObject;
import com.cp.common.test.mock.MockPrimitiveObjectImpl;
import com.cp.common.util.ConfigurationException;
import java.util.HashMap;
import java.util.Map;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractObjectFactoryTest extends TestCase {

  private static AbstractObjectFactory objectFactory;

  public AbstractObjectFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractObjectFactoryTest.class);
    //suite.addTest(new AbstractObjectFactoryTest("testName"));
    return new InitializeObjectFactoryTestSetup(suite);
  }

  protected static AbstractObjectFactory getObjectFactory() {
    return objectFactory;
  }

  public void testGetInstance() throws Exception {
    assertNotNull(getObjectFactory());

    final MockPrimitiveObject expectedMockObject = new MockPrimitiveObjectImpl();
    final MockPrimitiveObject actualMockObject = getObjectFactory().getInstance("cp-common.mock.object");

    assertNotNull(actualMockObject);
    assertEquals(expectedMockObject, actualMockObject);
  }

  public void testGetInstanceWithArguments() throws Exception {
    assertNotNull(getObjectFactory());

    final MockPrimitiveObject expectedMockObject = new MockPrimitiveObjectImpl(1L);
    final MockPrimitiveObject actualMockObject = getObjectFactory().getInstance("cp-common.mock.object", new Object[] { 1L });

    assertNotNull(actualMockObject);
    assertEquals(expectedMockObject, actualMockObject);
  }

  public void testGetInstanceWithArgumentTypesAndArguments() throws Exception {
    assertNotNull(getObjectFactory());

    final MockPrimitiveObject expectedMockObject = new MockPrimitiveObjectImpl(0L);
    expectedMockObject.setBooleanProperty(Boolean.TRUE);
    expectedMockObject.setCharacterProperty('X');
    expectedMockObject.setDoubleProperty(3.14159);
    expectedMockObject.setIntegerProperty(2);
    expectedMockObject.setStringProperty("test");

    final MockPrimitiveObject actualMockObject = getObjectFactory().getInstance("cp-common.mock.object",
      new Class[] { MockPrimitiveObject.class }, new Object[] { expectedMockObject });

    assertNotNull(actualMockObject);
    assertNull(actualMockObject.getId());
    assertEquals(expectedMockObject.getBooleanProperty(), actualMockObject.getBooleanProperty());
    assertEquals(expectedMockObject.getCharacterProperty(), actualMockObject.getCharacterProperty());
    assertEquals(expectedMockObject.getDoubleProperty(), actualMockObject.getDoubleProperty());
    assertEquals(expectedMockObject.getIntegerProperty(), actualMockObject.getIntegerProperty());
    assertEquals(expectedMockObject.getStringProperty(), actualMockObject.getStringProperty());
  }

  protected static final class InitializeObjectFactoryTestSetup extends TestSetup {

    public InitializeObjectFactoryTestSetup(final Test test) {
      super(test);
    }

    @Override
    protected void setUp() throws Exception {
      super.setUp();
      objectFactory = new MockObjectFactory(new MockConfig());
    }

    @Override
    protected void tearDown() throws Exception {
      super.tearDown();
      objectFactory = null;
    }
  }

  protected static final class MockConfig extends AbstractConfig {

    private static final Map<String, String> CONFIG_MAP = new HashMap<String, String>();

    static {
      CONFIG_MAP.put("cp-common.mock.object", "com.cp.common.test.mock.MockPrimitiveObjectImpl");
    }

    protected String getPropertyValueImpl(final String propertyName) throws ConfigurationException {
      return CONFIG_MAP.get(propertyName);
    }
  }

  protected static final class MockObjectFactory extends AbstractObjectFactory {

    public MockObjectFactory(final Config config) {
      super(config);
    }
  }

}
