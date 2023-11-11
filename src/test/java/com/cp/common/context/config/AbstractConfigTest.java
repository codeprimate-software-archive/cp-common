/*
 * AbstractConfigTest.java (c) 27 February 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.18
 * @see com.cp.common.context.config.AbstractConfig
 */

package com.cp.common.context.config;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.DateUtil;
import com.cp.common.util.SystemException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.MissingResourceException;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractConfigTest extends TestCase {

  public AbstractConfigTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractConfigTest.class);
    return suite;
  }

  protected Config getConfig() {
    return MockConfig.getInstance();
  }

  public void testGetBigDecimalPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new BigDecimal(42), mockConfig.getBigDecimalPropertyValue("cp-common.decimal.property.value"));
    assertEquals(new BigDecimal("3.14159"), mockConfig.getBigDecimalPropertyValue("cp-common.floatingpoint.property.value"));

    try {
      mockConfig.getBigDecimalPropertyValue("cp-common.calendar.property.value");
      fail("Getting the value of a Calendar property as a BigDecimal should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getBigDecimalPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getBigDecimalPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new BigDecimal(0.0), mockConfig.getBigDecimalPropertyValue("cp-common.nonexistent.property.value", new BigDecimal(0.0)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 0.0 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetBigIntegerPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new BigInteger("42"), mockConfig.getBigIntegerPropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getBigIntegerPropertyValue("cp-common.floatingpoint.property.value");
      fail("Getting the value of a floating-point property as a BigInteger should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getBigIntegerPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getBigIntegerPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new BigInteger("0"), mockConfig.getBigIntegerPropertyValue("cp-common.nonexistent.property.value", new BigInteger("0")));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 0 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetBooleanPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(Boolean.TRUE, mockConfig.getBooleanPropertyValue("cp-common.boolean.property.value"));
    assertEquals(Boolean.FALSE, mockConfig.getBooleanPropertyValue("cp-common.string.property.value"));

    try {
      mockConfig.getBooleanPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value for a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getBooleanPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property and failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(Boolean.TRUE, mockConfig.getBooleanPropertyValue("cp-common.nonexistent.property.value", Boolean.TRUE));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having a default value of TRUE should not have thrown a MissingResourceException!");
    }
  }

  public void testGetBytePropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Byte((byte) 42), mockConfig.getBytePropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getBytePropertyValue("cp-common.floatingpoint.property.value");
      fail("Getting the value of a floating-point property as a Byte should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getBytePropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getBytePropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property with failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Byte((byte) 1), mockConfig.getBytePropertyValue("cp-common.nonexistent.property.value", new Byte((byte) 1)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 1 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetCalendarPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(DateUtil.getCalendar(2007, Calendar.OCTOBER, 3, 22, 2, 30, 0),
      mockConfig.getCalendarPropertyValue("cp-common.calendar.property.value"));
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27),
      mockConfig.getCalendarPropertyValue("cp-common.date.property.value", "MM/dd/yyyy"));

    try {
      mockConfig.getCalendarPropertyValue("cp-common.date.property.value");
      fail("Getting the value for a Date property as a Calendar in the format (" + DateUtil.DEFAULT_DATE_FORMAT_PATTERN
        + ") should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    try {
      mockConfig.getCalendarPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value for a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getCalendarPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), mockConfig.getCalendarPropertyValue("cp-common.nonexistent.property.value", DateUtil.getCalendar(2000, Calendar.JANUARY, 1)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having a default value of January 1, 2000 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetCharacterPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Character('X'), mockConfig.getCharacterPropertyValue("cp-common.character.property.value"));
    assertEquals(new Character('T'), mockConfig.getCharacterPropertyValue("cp-common.string.property.value"));
    assertEquals(new Character('3'), mockConfig.getCharacterPropertyValue("cp-common.floatingpoint.property.value"));
    assertEquals(new Character('0'), mockConfig.getCharacterPropertyValue("cp-common.date.property.value"));
    assertEquals(new Character('t'), mockConfig.getCharacterPropertyValue("cp-common.boolean.property.value"));

    try {
      mockConfig.getCharacterPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value for a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getCharacterPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Character('c'), mockConfig.getCharacterPropertyValue("cp-common.nonexistent.property.value", new Character('c')));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having default value of 'c' should not have thrown a MissingResourceException!");
    }
  }

  public void testGetDatePropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(DateUtil.getCalendar(2007, Calendar.OCTOBER, 3, 22, 2, 30, 0).getTime(),
      mockConfig.getDatePropertyValue("cp-common.calendar.property.value"));
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27).getTime(),
      mockConfig.getDatePropertyValue("cp-common.date.property.value", "MM/dd/yyyy"));

    try {
      mockConfig.getDatePropertyValue("cp-common.date.property.value");
      fail("Getting the value for a Date property as a Date in the format (" + DateUtil.DEFAULT_DATE_FORMAT_PATTERN
        + ") should have thrown an IllegalArgumentExcetion!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    try {
      mockConfig.getDatePropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value for a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getDatePropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(DateUtil.getCalendar(2000, Calendar.JANUARY, 1).getTime(), mockConfig.getDatePropertyValue("cp-common.nonexistent.property.value", DateUtil.getCalendar(2000, Calendar.JANUARY, 1).getTime()));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having a default value of January 1, 2000 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetDoublePropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Double(3.14159), mockConfig.getDoublePropertyValue("cp-common.floatingpoint.property.value"));
    assertEquals(new Double(42), mockConfig.getDoublePropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getDoublePropertyValue("cp-common.string.property.value");
      fail("Getting the value of a String property as a Double should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getDoublePropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getDoublePropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Double(0.99), mockConfig.getDoublePropertyValue("cp-common.nonexistent.property.value", new Double(0.99)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 0.99 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetFloatPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Float(3.14159f), mockConfig.getFloatPropertyValue("cp-common.floatingpoint.property.value"));
    assertEquals(new Float(42f), mockConfig.getFloatPropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getFloatPropertyValue("cp-common.string.property.value");
      fail("Getting the value of a String property as a Float should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getFloatPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getFloatPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Float(0.123f), mockConfig.getFloatPropertyValue("cp-common.nonexistent.property.value", new Float(0.123f)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 0.123 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetIntegerPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Integer(42), mockConfig.getIntegerPropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getIntegerPropertyValue("cp-common.floatingpoint.property.value");
      fail("Getting the value of a floating-point property as an Integer should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getIntegerPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getIntegerPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Integer(33), mockConfig.getIntegerPropertyValue("cp-common.nonexistent.property.value", new Integer(33)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 33 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetLongPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Long(42), mockConfig.getLongPropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getLongPropertyValue("cp-common.floatingpoint.property.value");
      fail("Getting the value of a floating-point property as a Long should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getLongPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getLongPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Long(1234567890), mockConfig.getLongPropertyValue("cp-common.nonexistent.property.value", new Long(1234567890)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having a default value of 1234567890 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetShortPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals(new Short((short) 42), mockConfig.getShortPropertyValue("cp-common.decimal.property.value"));

    try {
      mockConfig.getShortPropertyValue("cp-common.floatingpoint.property.value");
      fail("Getting the value of a floating-point property as a Short should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      mockConfig.getShortPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value of a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getShortPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals(new Short((short) 69), mockConfig.getShortPropertyValue("cp-common.nonexistent.property.value", new Short((short) 69)));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property having default value of 69 should not have thrown a MissingResourceException!");
    }
  }

  public void testGetStringPropertyValue() throws Exception {
    final Config mockConfig = getConfig();

    assertNotNull(mockConfig);
    assertEquals("TEST", mockConfig.getStringPropertyValue("cp-common.string.property.value"));
    assertEquals("true", mockConfig.getStringPropertyValue("cp-common.boolean.property.value"));
    assertEquals("10/03/2007 10:02:30 PM", mockConfig.getStringPropertyValue("cp-common.calendar.property.value"));
    assertEquals("X", mockConfig.getStringPropertyValue("cp-common.character.property.value"));
    assertEquals("05/27/1974", mockConfig.getStringPropertyValue("cp-common.date.property.value"));
    assertEquals("42", mockConfig.getStringPropertyValue("cp-common.decimal.property.value"));
    assertEquals("3.14159", mockConfig.getStringPropertyValue("cp-common.floatingpoint.property.value"));

    try {
      mockConfig.getStringPropertyValue("cp-common.nonexistent.property.value");
      fail("Getting the value for a non-existent property should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }

    try {
      assertNull(mockConfig.getStringPropertyValue("cp-common.nonexistent.property.value", false));
    }
    catch (MissingResourceException e) {
      fail("Getting the value for a non-existent property having failOnMissingProperty set to false should not have thrown a MissingResourceException!");
    }

    try {
      assertEquals("HELLO WORLD!", mockConfig.getStringPropertyValue("cp-common.nonexistent.property.value", "HELLO WORLD!"));
    }
    catch (MissingResourceException e) {
      fail("Getting the value of a non-existent property hvaing a default value 'HELLO WORLD!' should not have thrown a MissingResourceException!");
    }
  }

  private static final class MockConfig extends AbstractConfig {

    private static final String MOCK_CONFIG_PROPERTIES_FILE = "/etc/config/mock-config.properties";

    private static MockConfig INSTANCE;

    private final Properties localProperties;

    private MockConfig() throws IOException {
      localProperties = new Properties();
      localProperties.load(getClass().getResourceAsStream(MOCK_CONFIG_PROPERTIES_FILE));
    }

    public static MockConfig getInstance() throws SystemException {
      if (ObjectUtil.isNull(INSTANCE)) {
        synchronized (MockConfig.class) {
          if (ObjectUtil.isNull(INSTANCE)) {
            try {
              INSTANCE = new MockConfig();
            }
            catch (IOException e) {
              throw new SystemException("Failed to instantiate an instance of the MockConfig class initialized with properties ("
                + MOCK_CONFIG_PROPERTIES_FILE + ")!", e);
            }
          }
        }
      }

      return INSTANCE;
    }

    protected String getPropertyValueImpl(final String propertyName) throws ConfigurationException {
      if (localProperties.containsKey(propertyName)) {
        return localProperties.getProperty(propertyName);
      }
      else {
        throw new MissingResourceException("The property (" + propertyName + ") does not exist!",
          getClass().getName(), propertyName);
      }
    }
  }

  private static final class TestConfig extends AbstractConfig {

    public TestConfig() {
    }

    public TestConfig(final Config config) {
      super(config);
    }

    protected String getPropertyValueImpl(final String propertyName) throws ConfigurationException {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
