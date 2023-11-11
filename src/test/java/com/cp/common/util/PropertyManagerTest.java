/*
 * PropertyManagerTest.java (c) 4 February 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.24
 */

package com.cp.common.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PropertyManagerTest extends TestCase {

  private static final String MOCK_CP_COMMON_PROPERTIES_PATHNAME = "/etc/config/mock-cp-common.properties";

  public PropertyManagerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PropertyManagerTest.class);
    //suite.addTest(new PropertyManagerTest("testGetBooleanPropertyValue"));
    return suite;
  }

  protected void setUp() throws Exception {
    PropertyManager.INSTANCE = null;
  }

  protected void tearDown() throws Exception {
    PropertyManager.INSTANCE = null;
  }

  public void testGetBooleanPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals(Boolean.TRUE, propertyManager.getBooleanPropertyValue("cp-common.boolean.property.value"));
    assertEquals(Boolean.FALSE, propertyManager.getBooleanPropertyValue("cp-common.string.property.value", false));
    assertEquals(Boolean.FALSE, propertyManager.getBooleanPropertyValue("cp-common.yesboolean.property.value", false));
    assertEquals(Boolean.FALSE, propertyManager.getBooleanPropertyValue("cp-common.cache.factory", false));
    assertEquals(Boolean.FALSE, propertyManager.getBooleanPropertyValue("cp-common.cache.factory", Boolean.TRUE, false));
    assertNull(propertyManager.getBooleanPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals(Boolean.TRUE, propertyManager.getBooleanPropertyValue("cp-webapp.nonexistent.property.value", Boolean.TRUE, false));
  }

  public void testGetBooleanPropertyValueWithDefaultValueAndFailForMissingProperty() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals(Boolean.FALSE, propertyManager.getBooleanPropertyValue("cp-webapp.nonexistent.property.value", Boolean.FALSE, true));
    }
    catch (MissingResourceException e) {
      fail("Calling getBooleanPropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getBooleanPropertyValue("cp-webapp.nonexistent.property.value", null, true);
      fail("Calling getBooleanPropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetBytePropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals(new Byte((byte) 127), propertyManager.getBytePropertyValue("cp-common.byte.property.value", false));
    assertNull(propertyManager.getBytePropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals(new Byte((byte) 1), propertyManager.getBytePropertyValue("cp-webapp.nonexistent.property.value",
      new Byte((byte) 1), false));

    try {
      propertyManager.getBytePropertyValue("cp-common.character.property.value", false);
      fail("Returning a Byte value for property 'cp-common.character.property.value' should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
  }

  public void testGetBytePropertyValueWithDefaultValueAndFailForMissingProperty() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals(new Byte((byte) 99), propertyManager.getBytePropertyValue("cp-webapp.nonexistent.property.value", new Byte((byte) 99), true));
    }
    catch (MissingResourceException e) {
      fail("Calling getBytePropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getBytePropertyValue("cp-webapp.nonexistent.property.value", null, true);
      fail("Calling getBytePropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetCalendarPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals("Expected May 27, 1974!", DateUtil.getCalendar(1974, Calendar.MAY, 27), propertyManager.getCalendarPropertyValue("cp-common.date.property.value", false));
    assertEquals("Expected May 27, 1974 & not January 22, 1975!", DateUtil.getCalendar(1974, Calendar.MAY, 27),
      propertyManager.getCalendarPropertyValue("cp-common.date.property.value", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), false));
    assertNull("Expected null!", propertyManager.getCalendarPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals("Expected default Calendar value (January 22, 1975)!", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), propertyManager.getCalendarPropertyValue("cp-webapp.nonexistent.property.value",
      DateUtil.getCalendar(1975, Calendar.JANUARY, 22), false));
    assertEquals("Expected July 4, 1776 & not December 1, 2000!", DateUtil.getCalendar(1776, Calendar.JULY, 4, 18, 30, 0, 0), propertyManager.getCalendarPropertyValue("cp-common.customdate.property.value",
      "MM/dd/yyyy KK:mm a", DateUtil.getCalendar(2000, Calendar.DECEMBER, 1), false));

    try {
      propertyManager.getCalendarPropertyValue("cp-common.date.property.value", "MM/dd/yyyy KK:mm a", DateUtil.getCalendar(2000, Calendar.JANUARY, 1), false);
      fail("Parsing the date value for property 'cp-common.date.property.value' using the date format (MM/dd/yyyy KK:mm a) should have thrown a ParseException!");
    }
    catch (ParseException e) {
      // expected behavior!
    }

    try {
      propertyManager.getCalendarPropertyValue("cp-common.invaliddate.property.value", "MMM dd, yyyy", false);
      fail("Parsing the date value for property 'cp-common.invaliddate.property.value' should have thrown a ParseException!");
    }
    catch (ParseException e) {
      // expected behavior!
    }

    try {
      propertyManager.getCalendarPropertyValue("cp-common.floatingpoint.property.value", DateUtil.getCalendar(2000, Calendar.JULY, 1), false);
      fail("Returning a Date value for property 'cp-common.floatingpoint.property.value' should have thrown a ParseException!");
    }
    catch (ParseException e) {
      // expected behavior!
    }
  }

  public void testGetCalendarPropertyValueWithDefaultValueAndFailForMissingProperty() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);
    final Calendar february_21_2006 = DateUtil.getCalendar(2006, Calendar.FEBRUARY, 21);

    assertNotNull(propertyManager);

    try {
      assertEquals(february_21_2006, propertyManager.getCalendarPropertyValue("cp-webapp.nonexistent.property.value", february_21_2006, true));
    }
    catch (MissingResourceException e) {
      fail("Calling getCalendarPropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getCalendarPropertyValue("cp-webapp.nonexistent.property.value", (Calendar) null, true);
      fail("Calling getCalendarPropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetCharacterPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals(new Character('A'), propertyManager.getCharacterPropertyValue("cp-common.character.property.value", false));
    assertEquals(new Character('H'), propertyManager.getCharacterPropertyValue("cp-common.string.property.value", false));
    assertNull(propertyManager.getCharacterPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals(new Character('x'), propertyManager.getCharacterPropertyValue("cp-webapp.nonexistent.property.value", new Character('x'), false));
    assertEquals(new Character('y'), propertyManager.getCharacterPropertyValue("cp-common.yesboolean.property.value", new Character('X'), false));
    assertEquals(new Character('3'), propertyManager.getCharacterPropertyValue("cp-common.floatingpoint.property.value", false));
    assertEquals(new Character('0'), propertyManager.getCharacterPropertyValue("cp-common.customdate.property.value", new Character('O'), false));
  }

  public void testGetCharacterPropertyValueWithDefaultValueAndFailForMissingProperty() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals(new Character('J'), propertyManager.getCharacterPropertyValue("cp-webapp.nonexistent.property.value", new Character('J'), true));
    }
    catch (MissingResourceException e) {
      fail("Calling getCharacterPropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getCharacterPropertyValue("cp-webapp.nonexistent.property.value", null, true);
      fail("Calling getCharacterPropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetDatePropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals("05/27/1974", propertyManager.getStringPropertyValue("cp-common.date.property.value"));

    propertyManager.setPropertyValue("cp-common.date.property.value", "01/22/1975");

    assertEquals("01/22/1975", propertyManager.getStringPropertyValue("cp-common.date.property.value"));
    assertEquals("Expected January 22nd, 1975", DateUtil.getCalendar(1975, Calendar.JANUARY, 22).getTime(),
      propertyManager.getDatePropertyValue("cp-common.date.property.value"));
    assertEquals("Expected July 4th, 1776 @ 6:30 pm", DateUtil.getCalendar(1776, Calendar.JULY, 4, 18, 30, 0, 0).getTime(),
      propertyManager.getDatePropertyValue("cp-common.customdate.property.value", "MM/dd/yyyy KK:mm a", DateUtil.getCalendar(1974, Calendar.MAY, 27).getTime()));

    try {
      propertyManager.getDatePropertyValue("cp-common.invaliddate.property.value");
      fail("Parsing the date value for proeprty 'cp-common.invaliddate.property.value' should have thrown a ParseException!");
    }
    catch (ParseException e) {
      // expected behavior!
    }

    try {
      propertyManager.getDatePropertyValue("cp-common.date.property.value", "MM/dd/yyyy KK:mm a");
      fail("Parsing the date value for property 'cp-common.date.property.value' using the date pattern 'MM/dd/yyyy KK:mm a' should have thrown a ParseException!");
    }
    catch (ParseException e) {
      // expected behavior!
    }

    try {
      propertyManager.getDatePropertyValue("cp-common.boolean.property.value");
      fail("Parsing a non-date value as a date for property 'cp-common.boolean.property.value' should have thrown a ParseException!");
    }
    catch (ParseException e) {
      // expected behavior!
    }
  }

  public void testGetDatePropertyValueWithDefaultValueAndFailForMissingProperty() throws Exception {
    final Date defaultDate = DateUtil.getCalendar(2006, Calendar.FEBRUARY, 22).getTime();
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals(defaultDate, propertyManager.getDatePropertyValue("cp-webapp.nonexistent.property.value", defaultDate, true));
    }
    catch (MissingResourceException e) {
      fail("Calling getDatePropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getDatePropertyValue("cp-webapp.nonexistent.property.value", (Date) null, true);
      fail("Calling getDatePropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetDecimalPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals(new Integer(21202123), propertyManager.getIntegerPropertyValue("cp-common.decimal.property.value", new Integer(2), false));
    assertEquals(new Short((short) 127), propertyManager.getShortPropertyValue("cp-common.byte.property.value", false));
    assertNull(propertyManager.getLongPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals(new Long(123456789), propertyManager.getLongPropertyValue("cp-webapp.nonexistent.property.value", new Long(123456789), false));

    try {
      propertyManager.getIntegerPropertyValue("cp-common.floatingpoint.property.value", false);
      fail("Return a Decimal value for property 'cp-common.floatingpoint.property.value' should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }

    try {
      propertyManager.getLongPropertyValue("cp-common.boolean.property.value", new Long(9), false);
      fail("Returning a Long value for property 'cp-common.boolean.property.value' should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
  }

  public void testGetDecimalPropertyValueWithDefaultValueAndFailForMissingProperty() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals(new Integer(0), propertyManager.getIntegerPropertyValue("cp-webapp.nonexistent.property.value", new Integer(0), true));
    }
    catch (MissingResourceException e) {
      fail("Calling getIntegerPropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getIntegerPropertyValue("cp-webapp.nonexistent.property.value", null, true);
      fail("Calling getIntegerPropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetFloatingPointPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals(new Float(3.14159f), propertyManager.getFloatPropertyValue("cp-common.floatingpoint.property.value", false));
    assertEquals(new Double(3.14159), propertyManager.getDoublePropertyValue("cp-common.floatingpoint.property.value", new Double(1.001), false));
    assertEquals(new Double(21202123), propertyManager.getDoublePropertyValue("cp-common.decimal.property.value", false));
    assertEquals(new Float(127f), propertyManager.getFloatPropertyValue("cp-common.byte.property.value", false));
    assertNull(propertyManager.getFloatPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals(new Double(123456789.0), propertyManager.getDoublePropertyValue("cp-webapp.nonexistent.property.value", new Double(123456789.0), false));

    try {
      propertyManager.getFloatPropertyValue("cp-common.yesboolean.property.value", new Float(4.0), false);
      fail("Return a Float value for property 'cp-common.yesboolean.property.value' should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
  }

  public void testGetFloatingPropertyValueWithDefaultValueAndFailForMissingPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals(new Double(3.14159), propertyManager.getDoublePropertyValue("cp-webapp.nonexistent.property.value", new Double(3.14159), true));
    }
    catch (MissingResourceException e) {
      fail("Calling getDoublePropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getDoublePropertyValue("cp-webapp.nonexistent.property.value", null, true);
      fail("Calling getDoublePropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetStringPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals("HELLO WORLD", propertyManager.getStringPropertyValue("cp-common.string.property.value", false));
    assertEquals("A", propertyManager.getStringPropertyValue("cp-common.character.property.value", "Z", false));
    assertEquals("TRUE", propertyManager.getStringPropertyValue("cp-common.boolean.property.value", false));
    assertEquals("07/04/1776 06:30 pm", propertyManager.getStringPropertyValue("cp-common.customdate.property.value", "TEST", false));
    assertEquals("3.14159", propertyManager.getStringPropertyValue("cp-common.floatingpoint.property.value", false));
    assertEquals("com.codeprimate.util.MockCacheFactory", propertyManager.getStringPropertyValue("cp-common.cache.factory", "NULL", false));
    assertNull(propertyManager.getStringPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals("NULL", propertyManager.getStringPropertyValue("cp-webapp.nonexistent.property.value", "NULL", false));
  }

  public void testGetStringPropertyValueWithDefaultValueAndFailForMissingProperties() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);

    try {
      assertEquals("TEST", propertyManager.getStringPropertyValue("cp-webapp.nonexistent.property.value", "TEST", true));
    }
    catch (MissingResourceException e) {
      fail("Calling getStringPropertyValue with a non-existent property, a default value and true for fail for missing properties should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getStringPropertyValue("cp-webapp.nonexistent.property.value", null, true);
      fail("Calling getStringPropertyValue with a non-existent property, a null default value and true for fail for missing properties should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

  public void testGetPropertyValue() throws Exception {
    PropertyManager propertyManager = PropertyManager.getInstance();

    assertNotNull(propertyManager);
    assertEquals("com.codeprimate.util.cache.CPCacheFactory", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.codeprimate.util.record.CPRecordFactory", propertyManager.getPropertyValue("cp-common.record.factory", false));
    assertEquals("com.cp.common.enums.Gender$DefaultGenderFactory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Gender.factory", false));
    assertEquals("com.cp.common.enums.Race$DefaultRaceFactory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Race.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));

    System.setProperty("cp-common.cache.factory", "null");
    System.setProperty("cp-common.com.cp.common.enums.Gender.factory", "my.gender.factory");

    assertEquals("null", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.codeprimate.util.record.CPRecordFactory", propertyManager.getPropertyValue("cp-common.record.factory", false));
    assertEquals("my.gender.factory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Gender.factory", false));
    assertEquals("com.cp.common.enums.Race$DefaultRaceFactory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Race.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));

    PropertyManager.INSTANCE = null;
    propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals("null", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.codeprimate.util.MockRecordFactory", propertyManager.getPropertyValue("cp-common.record.factory", false));
    assertEquals("my.gender.factory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Gender.factory", false));
    assertEquals("com.codeprimate.util.enumx.MockRaceFactory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Race.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));

    System.clearProperty("cp-common.cache.factory");
    System.clearProperty("cp-common.com.cp.common.enums.Gender.factory");

    assertEquals("com.codeprimate.util.MockCacheFactory", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.codeprimate.util.MockRecordFactory", propertyManager.getPropertyValue("cp-common.record.factory", false));
    assertEquals("com.codeprimate.util.enumx.MockGenderFactory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Gender.factory", false));
    assertEquals("com.codeprimate.util.enumx.MockRaceFactory", propertyManager.getPropertyValue("cp-common.com.cp.common.enums.Race.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));
  }

  public void testSetProperties() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance();

    assertNotNull(propertyManager);
    assertEquals("com.codeprimate.util.search.CPSearchFactory", propertyManager.getPropertyValue("cp-common.search.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", "null", false));
    assertNull(propertyManager.getPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertEquals("nill", propertyManager.getPropertyValue("cp-common.nill.property.value", "nill", false));

    final Properties myProperties = new Properties();
    myProperties.setProperty("cp-common.search.factory", "com.companyx.search.SearchFactory");
    myProperties.setProperty("cp-webapp.nonexistent.property.value", "42");

    propertyManager.setProperties(myProperties);

    assertEquals("com.companyx.search.SearchFactory", propertyManager.getPropertyValue("cp-common.search.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", "null", false));
    assertEquals("42", propertyManager.getPropertyValue("cp-webapp.nonexistent.property.value", false));
    assertNull(propertyManager.getPropertyValue("cp-common.nill.property.value", false));
  }

  public void testSetPropertyValue() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals("com.codeprimate.util.MockCacheFactory", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));
    assertNull(propertyManager.getPropertyValue("cp-webapp.nonexistent.property.value", false));

    propertyManager.setPropertyValue("cp-common.cache.factory", "com.companyx.util.MyCacheFactory");
    propertyManager.setPropertyValue("cp-common.sort.factory", null);
    propertyManager.setPropertyValue("cp-webapp.nonexistent.property.value", "null");

    assertEquals("com.companyx.util.MyCacheFactory", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.codeprimate.util.sort.CPSortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));
    assertEquals("null", propertyManager.getPropertyValue("cp-webapp.nonexistent.property.value", "TEST", false));

    propertyManager.setPropertyValue("cp-common.cache.factory", null);
    propertyManager.setPropertyValue("cp-common.sort.factory", "com.companyx.util.MySortFactory");
    propertyManager.setPropertyValue("cp-webapp.nonexistent.property.value", null);
    System.setProperty("cp-common.sort.factory", "VOID");
    System.setProperty("cp-webapp.nonexistent.property.value", "TEST");

    assertEquals("com.codeprimate.util.cache.CPCacheFactory", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("VOID", propertyManager.getPropertyValue("cp-common.sort.factory", false));
    assertEquals("TEST", propertyManager.getPropertyValue("cp-webapp.nonexistent.property.value", "null", false));

    System.clearProperty("cp-common.sort.factory");
    System.clearProperty("cp-webapp.nonexistent.property.value");

    assertEquals("com.codeprimate.util.cache.CPCacheFactory", propertyManager.getPropertyValue("cp-common.cache.factory", false));
    assertEquals("com.companyx.util.MySortFactory", propertyManager.getPropertyValue("cp-common.sort.factory", false));
    assertEquals("NILL", propertyManager.getPropertyValue("cp-webapp.nonexistent.property.value", "NILL", false));
  }

  public void testThrowMissingResourceException() throws Exception {
    final PropertyManager propertyManager = PropertyManager.getInstance(MOCK_CP_COMMON_PROPERTIES_PATHNAME);

    assertNotNull(propertyManager);
    assertEquals("HELLO WORLD", propertyManager.getStringPropertyValue("cp-common.string.property.value"));

    try {
      assertEquals("TEST", propertyManager.getStringPropertyValue("cp-webapp.nonexistent.property.value", "TEST"));
    }
    catch (MissingResourceException e) {
      fail("Returning a String value for a non-existent property (cp-webapp.nonexistent.property.value) with a default value (TEST) should not have thrown a MissingResourceException!");
    }

    try {
      propertyManager.getStringPropertyValue("cp-webapp.nonexistent.property.value");
      fail("Returning a String value for a non-existent property (cp-webapp.nonexistent.property.value) should have thrown a MissingResourceException!");
    }
    catch (MissingResourceException e) {
      // expected behavior!
    }
  }

}
