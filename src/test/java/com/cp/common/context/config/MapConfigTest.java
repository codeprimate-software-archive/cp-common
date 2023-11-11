/*
 * MapConfigTest.java (c) 5 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.4
 * @see com.cp.common.context.config.MapConfig
 */

package com.cp.common.context.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MapConfigTest extends TestCase {

  public MapConfigTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MapConfigTest.class);
    //suite.addTest(new MapConfigTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    final Map<String, Object> propertyValueMap = new HashMap<String, Object>(1);
    propertyValueMap.put("myProperty", "myValue");

    Config mapConfig = null;

    assertNull(mapConfig);

    try {
      mapConfig = new MapConfig(propertyValueMap);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the MapConfig class with non-null property value mapping threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(mapConfig);
    assertTrue(mapConfig.contains("myProperty"));
    assertEquals("myValue", mapConfig.getStringPropertyValue("myProperty"));
  }

  public void testInstantiateWithNullPropertyValueMap() throws Exception {
    Config mapConfig = null;

    assertNull(mapConfig);

    try {
      mapConfig = new MapConfig(null);
      fail("Instantiating an instance of the MapConfig class with null property value mapping should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Map containing property configuration information cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the MapConfig class with null property value mapping threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(mapConfig);
  }

  public void testContains() throws Exception {
    final Map<String, Object> parentPropertyValueMap = new HashMap<String, Object>(5);
    parentPropertyValueMap.put("booleanProperty", true);
    parentPropertyValueMap.put("characterProperty", 'X');
    parentPropertyValueMap.put("doubleProperty", 3.14159);
    parentPropertyValueMap.put("integerProperty", 2);
    parentPropertyValueMap.put("stringProperty", "test");

    final Config parentConfig = new MapConfig(parentPropertyValueMap);

    final Map<String, Object> propertyValueMap = new HashMap<String, Object>(5);
    propertyValueMap.put("anotherBooleanProperty", false);
    propertyValueMap.put("anotherCharacterProperty", 'A');
    propertyValueMap.put("anotherDoubleProperty", 1.01);
    propertyValueMap.put("anotherIntegerProperty", 0);
    propertyValueMap.put("anotherStringProperty", "null");

    final Config mapConfig = new MapConfig(parentConfig, propertyValueMap);

    assertTrue(mapConfig.contains("anotherBooleanProperty"));
    assertTrue(mapConfig.contains("anotherStringProperty"));
    assertTrue(mapConfig.contains("characterProperty"));
    assertTrue(mapConfig.contains("doubleProperty"));
    assertTrue(mapConfig.contains("integerProperty"));
    assertFalse(mapConfig.contains("yetAnotherCharacterProperty"));
    assertFalse(mapConfig.contains("bigDecimalProperty"));
    assertFalse(mapConfig.contains("bigIntegerProperty"));
  }

  public void testGetPropertyValueImpl() throws Exception {
    final Map<String, Object> propertyValueMap = new HashMap<String, Object>(5);
    propertyValueMap.put("booleanProperty", true);
    propertyValueMap.put("characterProperty", 'X');
    propertyValueMap.put("doubleProperty", Math.PI);
    propertyValueMap.put("integerProperty", 2);
    propertyValueMap.put("stringProperty", "test");

    final Config config = new MapConfig(propertyValueMap);

    assertNotNull(config);
    assertEquals("true", config.getStringPropertyValue("booleanProperty"));
    assertEquals("X", config.getStringPropertyValue("characterProperty"));
    assertEquals("2", config.getStringPropertyValue("integerProperty"));
    assertEquals(String.valueOf(Math.PI), config.getStringPropertyValue("doubleProperty"));
    assertEquals("test", config.getStringPropertyValue("stringProperty"));
    assertEquals(Boolean.TRUE, config.getBooleanPropertyValue("booleanProperty"));
    assertEquals(new Character('X'), config.getCharacterPropertyValue("characterProperty"));
    assertEquals(new Integer(2), config.getIntegerPropertyValue("integerProperty"));
    assertEquals(new Double(Math.PI), config.getDoublePropertyValue("doubleProperty"));
    assertEquals("test", config.getStringPropertyValue("stringProperty"));
  }

  public void testGetPropertyValueImplForNonExistingProperty() throws Exception {
    final MapConfig mapConfig = new MapConfig(Collections.<String, Object>emptyMap());
    String propertyValue = null;

    assertNull(propertyValue);

    try {
      propertyValue = mapConfig.getPropertyValueImpl("non-existing.property");
      fail("Calling getPropertyValueImpl with a non-existing property (non-existing.property) should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      assertEquals("The property (non-existing.property) does not exist!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValueImpl with a non-existing property (non-existing.property) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyValue);
  }

}
