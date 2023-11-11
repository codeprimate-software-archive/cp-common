/*
 * StringConverterTest.java (c) 17 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.util.converters.StringConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.enums.Gender;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StringConverterTest extends TestCase {

  public StringConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(StringConverterTest.class);
    //suite.addTest(new StringConverterTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ConvertUtil.unregister(String.class);
  }

  public void testConvert() throws Exception {
    final AbstractConverter stringConverter = new StringConverter();

    assertNotNull(stringConverter);
    assertNull(stringConverter.getDefaultValue());
    assertFalse(stringConverter.isUsingDefaultValue());
    assertNull(stringConverter.convert(String.class, null));
    assertNull(stringConverter.convert(String.class, "null"));
    assertNull(stringConverter.convert(String.class, "NULL"));
    assertNull(stringConverter.convert(String.class, " Null "));

    final String expectedValue = "test";
    final String actualValue = (String) stringConverter.convert(String.class, expectedValue);

    assertEquals(expectedValue, actualValue);
    assertSame(expectedValue, actualValue);
    assertEquals("05/31/2007 11:44 PM", stringConverter.convert(String.class,
      DateUtil.getCalendar(2007, Calendar.MAY, 31, 23, 44, 30, 500)));
    assertEquals("10/10/2008 06:30 PM", stringConverter.convert(String.class,
      DateUtil.getCalendar(2008, Calendar.OCTOBER, 10, 18, 30, 15, 500).getTime()));
    assertEquals(Gender.FEMALE.getCode(), stringConverter.convert(String.class, Gender.FEMALE));

    final MockBean bean = new MockBean(2);

    assertNotNull(bean);
    assertEquals(new Integer(2), bean.getId());
    assertEquals(bean.toString(), stringConverter.convert(String.class, bean));
    assertEquals("true", stringConverter.convert(String.class, true));
    assertEquals("A", stringConverter.convert(String.class, 'A'));
    assertEquals(String.valueOf(Integer.MAX_VALUE), stringConverter.convert(String.class, Integer.MAX_VALUE));
    assertEquals(String.valueOf(Math.PI), stringConverter.convert(String.class, Math.PI));
  }

  public void testConvertHavingDefaultValue() throws Exception {
    final AbstractConverter stringConverter = new StringConverter("nill");
    Object convertedValue = null;

    assertNotNull(stringConverter);
    assertEquals("nill", stringConverter.getDefaultValue());
    assertTrue(stringConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = stringConverter.convert(String.class, null);
    }
    catch (Exception e) {
      fail("Calling convert with a null Object value on the StringConverter class with (nill) as the default value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertEquals("nill", convertedValue);
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter stringConverter = new StringConverter(true);
    Object convertedValue = null;

    assertNotNull(stringConverter);
    assertNull(stringConverter.getDefaultValue());
    assertTrue(stringConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = stringConverter.convert(String.class, null);
    }
    catch (Exception e) {
      fail("Calling convert with a null Object value on the StringConverter class using a default value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertEquals("", convertedValue);
  }

  public void testDateFormatPatternProperty() throws Exception {
    final StringConverter converter = new StringConverter();

    assertNotNull(converter);
    assertEquals(StringConverter.DEFAULT_DATE_FORMAT_PATTERN, converter.getDateFormatPattern());
    assertEquals("10/10/2008 06:46 PM", converter.convert(String.class,
      DateUtil.getCalendar(2008, Calendar.OCTOBER, 10, 18, 46, 30, 500)));

    converter.setDateFormatPattern("MMMMM, yyyy");

    assertEquals("MMMMM, yyyy", converter.getDateFormatPattern());
    assertEquals("October, 2008", converter.convert(String.class,
      DateUtil.getCalendar(2008, Calendar.OCTOBER, 10, 18, 48, 30, 500)));

    converter.setDateFormatPattern(null);

    assertEquals(StringConverter.DEFAULT_DATE_FORMAT_PATTERN, converter.getDateFormatPattern());
    assertEquals("10/10/2008 06:50 PM", converter.convert(String.class,
      DateUtil.getCalendar(2008, Calendar.OCTOBER, 10, 18, 50, 30, 500)));
  }

  public void testSetEnumConversionStrategy() throws Exception {
    final StringConverter converter = new StringConverter();

    assertNotNull(converter);
    assertTrue(converter.getEnumConversionStrategy() instanceof StringConverter.EnumToCodeConversionStrategy);
    assertEquals(Gender.FEMALE.getCode(), converter.convert(String.class, Gender.FEMALE));

    converter.setEnumConversionStrategy(StringConverter.ENUM_TO_EXTERNAL_CODE);

    assertTrue(converter.getEnumConversionStrategy() instanceof StringConverter.EnumToExternalCodeConversionStrategy);
    assertEquals(Gender.FEMALE.getExternalCode(), converter.convert(String.class, Gender.FEMALE));

    converter.setEnumConversionStrategy(null);

    assertTrue(converter.getEnumConversionStrategy() instanceof StringConverter.EnumToCodeConversionStrategy);
    assertEquals(Gender.FEMALE.getCode(), converter.convert(String.class, Gender.FEMALE));
  }

  public void testConvertIdentifiable() throws Exception {
    final StringConverter converter = new StringConverter();
    final MockBean bean = new MockBean(0);

    assertEquals(bean.toString(), converter.convert(bean));
  }

  public void testConvertIdentifiableWithNullIdentifiableObject() throws Exception {
    final StringConverter converter = new StringConverter();
    String convertedValue = null;

    assertNull(convertedValue);

    try {
      convertedValue = converter.convert((Identifiable) null);
      fail("Calling convert with a null Identifiable object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Identifiable object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with a null Identifiable object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testSetBeanProperty() throws Exception {
    final MockBean bean = new MockBean(1);

    assertNotNull(bean);
    assertNull(bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", "null");

    assertNull(bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", "test");

    assertEquals("test", bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", DateUtil.getCalendar(2008, Calendar.OCTOBER, 10, 19, 7, 30, 500));

    assertEquals("10/10/2008 07:07 PM", bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", Gender.FEMALE);

    assertEquals(Gender.FEMALE.getCode(), bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", bean);

    assertEquals(bean.toString(), bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", null);

    assertNull(bean.getValue());
  }

  public void testSetBeanPropertyHavingDefaultValue() throws Exception {
    ConvertUtil.register(String.class, new StringConverter("nill"));
    final MockBean bean = new MockBean(1);

    assertNotNull(bean);
    assertNull(bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", true);

    assertEquals("true", bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", null);

    assertEquals("nill", bean.getValue());
  }

  public void testSetBeanPropertyUsingDefaultValue() throws Exception {
    ConvertUtil.register(String.class, new StringConverter(true));
    final MockBean bean = new MockBean(1);

    assertNotNull(bean);
    assertNull(bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", Math.PI);

    assertEquals(String.valueOf(Math.PI), bean.getValue());

    BeanUtil.setPropertyValue(bean, "value", null);

    assertEquals("", bean.getValue());
  }

  public static final class MockBean extends AbstractBean<Integer> {

    private String value;

    public MockBean() {
    }

    public MockBean(final Integer id) {
      super(id);
    }

    public String getValue() {
      return value;
    }

    public void setValue(final String value) {
      this.value = value;
    }

    public String toString() {
      return getClass().getName() + "(" + ObjectUtil.toString(getId()) + ")";
    }
  }

}
