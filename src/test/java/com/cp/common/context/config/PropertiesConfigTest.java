/*
 * PropertiesConfigTest.java (c) 5 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.4
 * @see com.cp.common.context.config.PropertiesConfig
 */

package com.cp.common.context.config;

import com.cp.common.util.ConfigurationException;
import com.cp.common.util.DateUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.MissingResourceException;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class PropertiesConfigTest extends TestCase {

  private static final String MOCK_CONFIG_PROPERTIES_FILE = "/etc/config/mock-config.properties";

  public PropertiesConfigTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PropertiesConfigTest.class);
    //suite.addTest(new PropertiesConfigTest("testName"));
    return suite;
  }

  public void testInstantiateWithPropertyFile() throws Exception {
    Config propertiesConfig = null;

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, new File(
        PropertiesConfigTest.class.getResource(MOCK_CONFIG_PROPERTIES_FILE).toURI()));
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a non-null, existing properties file threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertiesConfig);
    assertTrue(propertiesConfig.contains("cp-common.string.property.value"));
  }

  public void testInstantiateWithNonExistingPropertyFile() throws Exception {
    Config propertiesConfig = null;
    final File nonExistingPropertiesFile = new File("/path/to/non-existing-config.properties");

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, nonExistingPropertiesFile);
      fail("Instantiating an instance of the PropertiesConfig class with a non-existing properties file (/path/to/non-existing.properties) should have thrown a FileNotFoundException!");
    }
    catch (FileNotFoundException e) {
      assertEquals("The property file (" + nonExistingPropertiesFile.getAbsolutePath() + ") does not exist!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a non-existing properties file (/path/to/non-existing.properties) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertiesConfig);
  }

  public void testInstantiateWithNullPropertyFile() throws Exception {
    Config propertiesConfig = null;

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, (File) null);
      fail("Instantiating an instance of the PropertiesConfig class with a null properties file should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The property file cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a null properties file threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertiesConfig);
  }

  public void testInstantiateWithPropertiesObject() throws Exception {
    final Properties mockProperties = new Properties();
    mockProperties.put("myProperty", "myValue");

    Config propertiesConfig = null;

    try {
      propertiesConfig = new PropertiesConfig(null, mockProperties);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a non-null properties object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertiesConfig);
    assertTrue(propertiesConfig.contains("myProperty"));
    assertEquals("myValue", propertiesConfig.getStringPropertyValue("myProperty"));
  }

  public void testInstantiateWithNullPropertiesObject() throws Exception {
    Config propertiesConfig = null;

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, (Properties) null);
      fail("Instantiating an instance of the PropertiesConfig class with a null Properties object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Properties object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a null Properties object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertiesConfig);
  }

  public void testInstantiateWithResourceObject() throws Exception {
    Config propertiesConfig = null;

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, new ClassPathResource(MOCK_CONFIG_PROPERTIES_FILE));
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a non-null, existing CLASSPATH Resource threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertiesConfig);
    assertTrue(propertiesConfig.contains("cp-common.string.property.value"));
  }

  public void testInstantiateWithNonExistingResource() throws Exception {
    Config propertiesConfig = null;
    final Resource nonExistingResource = new FileSystemResource("/path/to/non-existing-config.properties");

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, nonExistingResource);
      fail("Instantiating an instance of the PropertiesConfig class with a non-existing Resource should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("Failed to obtain an input stream to the specified resource (" + nonExistingResource.getDescription()
        + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a non-existing Resource threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertiesConfig);
  }

  public void testInstantiateWithNullResource() throws Exception {
    Config propertiesConfig = null;

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, (Resource) null);
      fail("Instantiating an instance of the PropertiesConfig class with a null Resource object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Resource object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a null Resource object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertiesConfig);
  }

  public void testInstantiateWithResourceName() throws Exception {
    Config propertiesConfig = null;

    assertNull(propertiesConfig);

    try {
      propertiesConfig = new PropertiesConfig(null, MOCK_CONFIG_PROPERTIES_FILE);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the PropertiesConfig class with a non-null, existing resource name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertiesConfig);
    assertTrue(propertiesConfig.contains("cp-common.string.property.value"));
  }

  public void testContains() throws Exception {
    final Properties parentProps = new Properties();
    parentProps.put("characterProperty", 'X');
    parentProps.put("stringProperty", "X");

    final Config parentConfig = new PropertiesConfig(parentProps);

    final Properties props = new Properties();
    props.put("anotherCharacterProperty", 'A');
    props.put("anotherStringProperty", "A");

    final Config propertiesConfig = new PropertiesConfig(parentConfig, props);

    assertTrue(propertiesConfig.contains("anotherCharacterProperty"));
    assertTrue(propertiesConfig.contains("anotherStringProperty"));
    assertTrue(propertiesConfig.contains("characterProperty"));
    assertTrue(propertiesConfig.contains("stringProperty"));
    assertFalse(propertiesConfig.contains("anotherDoubleProperty"));
    assertFalse(propertiesConfig.contains("integerProperty"));
  }

  public void testGetPropertyValueImpl() throws Exception {
    final Config propertiesConfig = new PropertiesConfig(MOCK_CONFIG_PROPERTIES_FILE);

    assertEquals(Boolean.TRUE, propertiesConfig.getBooleanPropertyValue("cp-common.boolean.property.value"));
    assertEquals(DateUtil.getCalendar(2007, Calendar.OCTOBER, 3, 22, 2, 30, 0), propertiesConfig.getCalendarPropertyValue("cp-common.calendar.property.value"));
    assertEquals(new Character('X'), propertiesConfig.getCharacterPropertyValue("cp-common.character.property.value"));
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27).getTime(), propertiesConfig.getDatePropertyValue("cp-common.date.property.value", "MM/dd/yyyy"));
    assertEquals(new Integer(42), propertiesConfig.getIntegerPropertyValue("cp-common.decimal.property.value"));
    assertEquals(new Double(3.14159d), propertiesConfig.getDoublePropertyValue("cp-common.floatingpoint.property.value"));
    assertEquals("TEST", propertiesConfig.getStringPropertyValue("cp-common.string.property.value"));
    assertEquals("null", propertiesConfig.getStringPropertyValue("cp-common.nonexistent.property.value", "null"));
    assertNull(propertiesConfig.getStringPropertyValue("cp-common.nonexistent.property.value", false));
  }

  public void testGetPropertyValueImplForNonExistingProperty() throws Exception {
    final PropertiesConfig propertiesConfig = new PropertiesConfig(MOCK_CONFIG_PROPERTIES_FILE);
    String propertyValue = null;

    assertNull(propertyValue);

    try {
      propertyValue = propertiesConfig.getPropertyValueImpl("non-existing.property");
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
