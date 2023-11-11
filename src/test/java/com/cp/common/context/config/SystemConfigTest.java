/*
 * SystemConfigTest.java (c) 3 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.4
 * @see com.cp.common.context.config.SystemConfig
 */

package com.cp.common.context.config;

import java.util.MissingResourceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class SystemConfigTest extends TestCase {

  private final Mockery context = new Mockery();

  public SystemConfigTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SystemConfigTest.class);
    return suite;
  }

  public void testContains() throws Exception {
    final Config mockParentConfig = context.mock(Config.class);

    context.checking(new Expectations() {{
      atLeast(1).of(mockParentConfig).contains("myProperty");
      will(returnValue(true));
      allowing(mockParentConfig).contains(with(any(String.class)));
      will(returnValue(false));
    }});

    final Config systemConfig = new SystemConfig(mockParentConfig);

    assertTrue(systemConfig.contains("java.specification.version"));
    assertTrue(systemConfig.contains("java.version"));
    assertTrue(systemConfig.contains("java.vm.version"));
    assertTrue(systemConfig.contains("java.home"));
    assertTrue(systemConfig.contains("java.io.tmpdir"));
    assertTrue(systemConfig.contains("os.name"));
    assertTrue(systemConfig.contains("user.name"));
    assertTrue(systemConfig.contains("user.home"));
    assertTrue(systemConfig.contains("myProperty"));
    assertFalse(systemConfig.contains("yourProperty"));
    assertFalse(systemConfig.contains("testProperty"));

    context.assertIsSatisfied();
  }

  public void testGetPropertyValueImpl() throws Exception {
    final Config systemConfig = new SystemConfig();

    assertEquals("Sun Microsystems Inc.", systemConfig.getStringPropertyValue("java.vendor"));
  }

  public void testGetPropertyValueImplForNonExistingProperty() throws Exception {
    final SystemConfig systemConfig = new SystemConfig();
    String propertyValue = null;

    assertNull(propertyValue);

    try {
      propertyValue = systemConfig.getPropertyValueImpl("non-existing.property");
      fail("Calling getPropertyValueImpl for a non-existing property (non-existing.property) should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      assertEquals("The property (non-existing.property) does not exist!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValueImpl for a non-existing property (non-existing.property) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyValue);
  }

}
