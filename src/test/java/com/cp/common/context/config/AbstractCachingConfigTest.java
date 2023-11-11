/*
 * AbstractCachingConfigTest.java (c) 10 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.10
 * @see com.cp.common.context.config.AbstractCachingConfig
 */

package com.cp.common.context.config;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import com.cp.common.util.ConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractCachingConfigTest extends TestCase {

  public AbstractCachingConfigTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractCachingConfigTest.class);
    //suite.addTest(new AbstractCachingConfigTest("testName"));
    return suite;
  }

  public void testCachingConfig() throws Exception {
    final MockConfig config = new MockConfig();

    assertFalse(config.isAccessed());
    assertEquals(Boolean.TRUE, config.getBooleanPropertyValue("booleanProperty"));
    assertTrue(config.isAccessed());

    config.reset();

    assertFalse(config.isAccessed());
    assertEquals(Boolean.TRUE, config.getBooleanPropertyValue("booleanProperty"));
    assertFalse(config.isAccessed());
    assertEquals(new Integer(2), config.getIntegerPropertyValue("integerProperty"));
    assertTrue(config.isAccessed());

    config.reset();

    assertFalse(config.isAccessed());
    assertNull(config.getStringPropertyValue("nonExistentProperty", false));
    assertTrue(config.isAccessed());

    config.reset();

    assertFalse(config.isAccessed());
    assertEquals("null", config.getStringPropertyValue("nonExistentProperty", "null"));
    assertFalse(config.isAccessed());
    assertEquals("test", config.getStringPropertyValue("stringProperty"));
    assertTrue(config.isAccessed());

    config.reset();

    assertFalse(config.isAccessed());
    assertEquals("test", config.getStringPropertyValue("stringProperty"));
    assertFalse(config.isAccessed());
  }

  protected static final class MockConfig extends AbstractCachingConfig implements Resettable {

    private static final Map<String, Object> config = new HashMap<String, Object>(5);

    private boolean accessed = false;

    static {
      config.put("booleanProperty", true);
      config.put("characterProperty", '*');
      config.put("integerProperty", 2);
      config.put("doubleProperty", 3.14159);
      config.put("stringProperty", "test");
    }

    public boolean isAccessed() {
      return accessed;
    }

    protected String getPropertyValueImpl(final String propertyName) throws ConfigurationException {
      accessed = true;

      final String propertyValue = ObjectUtil.toString(config.get(propertyName));

      if (ObjectUtil.isNotNull(propertyValue)) {
        return propertyValue;
      }
      else {
        logger.warn("The property (" + propertyName + ") does not exist in this configuration!");
        throw new MissingResourceException("The property (" + propertyName + ") does not exist in this configuration!",
          getClass().getName(), propertyName);
      }
    }

    public void reset() {
      accessed = false;
    }
  }

}
