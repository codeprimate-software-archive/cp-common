/*
 * ConfigUtilTest.java (c) 11 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.11
 * @see com.cp.common.context.config.ConfigUtil
 */

package com.cp.common.context.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConfigUtilTest extends TestCase {

  public ConfigUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ConfigUtilTest.class);
    //suite.addTest(new ConfigUtilTest("testName"));
    return suite;
  }

  public void testGetProperties() throws Exception {
    final Map<String, Object> propertyValueMap = new HashMap<String, Object>(5);
    propertyValueMap.put("booleanProperty", true);
    propertyValueMap.put("characterProperty", 'X');
    propertyValueMap.put("integerProperty", 2);
    propertyValueMap.put("doubleProperty", 3.14159);
    propertyValueMap.put("stringProperty", "test");

    final Properties expectedProperties = new Properties();
    expectedProperties.put("booleanProperty", "true");
    expectedProperties.put("characterProperty", "X");
    expectedProperties.put("integerProperty", "2");
    expectedProperties.put("doubleProperty", "3.14159");
    expectedProperties.put("stringProperty", "test");

    final Properties actualProperties = ConfigUtil.getProperties(propertyValueMap);

    assertNotNull(actualProperties);
    assertEquals(expectedProperties, actualProperties);
  }

}
