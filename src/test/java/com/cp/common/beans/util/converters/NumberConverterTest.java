/*
 * NumberConverterTest.java (c) 17 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.util.converters.NumberConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.util.ConversionException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NumberConverterTest extends TestCase {

  public NumberConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NumberConverterTest.class);
    //suite.addTest(new NumberConverterTest("testName"));
    return suite;
  }

  public void testConvert() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter();

    assertNotNull(numberConverter);
    assertNull(numberConverter.getDefaultValue());
    assertFalse(numberConverter.isUsingDefaultValue());
    assertNull(numberConverter.convert(Integer.class, null));
    assertEquals(new Byte((byte) 0), numberConverter.convert(Byte.class, "0"));
    assertEquals(new Short((short) 1), numberConverter.convert(Short.class, "1"));
    assertEquals(new Integer(2), numberConverter.convert(Integer.class, "2"));
    assertEquals(new Long(4l), numberConverter.convert(Long.class, "4"));
    assertEquals(new Float(3.14159f), numberConverter.convert(Float.class, "3.14159"));
    assertEquals(new Double(Math.PI), numberConverter.convert(Double.class, String.valueOf(Math.PI)));

    final Integer expectedEight = 8;
    final Integer actualEight = (Integer) numberConverter.convert(Integer.class, expectedEight);

    assertEquals(expectedEight, actualEight);
    assertSame(expectedEight, actualEight);
  }

  public void testConvertHavingDefaultValue() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter(2);
    Object convertedValue = null;

    assertNotNull(numberConverter);
    assertEquals(new Integer(2), numberConverter.getDefaultValue());
    assertTrue(numberConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = numberConverter.convert(Integer.class, null);
    }
    catch (Exception e) {
      fail("Calling convert with a null Object value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertTrue(convertedValue instanceof Integer);
    assertEquals(new Integer(2), convertedValue);
    assertSame(numberConverter.getDefaultValue(), convertedValue);
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter(true);
    Object convertedValue = null;

    assertNotNull(numberConverter);
    assertNull(numberConverter.getDefaultValue());
    assertTrue(numberConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = numberConverter.convert(Integer.class, null);
    }
    catch (Exception e) {
      fail("Calling convert with a null Object value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertTrue("Expected the Integer type; but was (" + convertedValue.getClass().getName() + ")",
      convertedValue instanceof Integer);
    assertEquals(new Integer(0), convertedValue);
  }

  public void testConvertWithIncompatibleType() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter();
    Object convertedValue = null;

    assertNotNull(numberConverter);
    assertNull(numberConverter.getDefaultValue());
    assertFalse(numberConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = numberConverter.convert(Boolean.class, 1);
      fail("Calling convert with an incompatiable type of Boolean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Class type to convert to must be a type of Number!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an incompatiable type of Boolean threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testConvertWithInvalidType() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter();
    Object convertedValue = null;

    assertNotNull(numberConverter);
    assertNull(numberConverter.getDefaultValue());
    assertFalse(numberConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = numberConverter.convert(Number.class, 1);
      fail("Calling convert with an invalid type of Number should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Class type cannot be the Number class explicitly but rather a sublclass of Number!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an invalid type of Number threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testConvertWithInvalidValue() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter();
    Object convertedValue = null;

    assertNotNull(numberConverter);
    assertNull(numberConverter.getDefaultValue());
    assertFalse(numberConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = numberConverter.convert(Integer.class, " ");
      fail("Calling convert with an invalid Integer value of ( ) should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("( ) is not valid numeric value!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an invalid Integer value of ( ) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(convertedValue);

    try {
      numberConverter.convert(Integer.class, Math.PI);
      fail("Calling convert with an invalid Integer value of Math.PI should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(" + Math.PI + ") is not valid (" + Integer.class.getName() + ") value!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an invalid Integer value of Math.PI threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testConvertWithNullType() throws Exception {
    final AbstractConverter numberConverter = new NumberConverter();
    Object convertedValue = null;

    assertNotNull(numberConverter);
    assertNull(numberConverter.getDefaultValue());
    assertFalse(numberConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = numberConverter.convert(null, 1);
      fail("Calling convert with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type to convert to cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testGetNumber() throws Exception {
    final NumberConverter converter = new NumberConverter();

    assertEquals(new Integer(2), converter.getNumber(Integer.class, "2"));
    assertEquals(new Double(Math.PI), converter.getNumber(Double.class, Math.PI));
  }

  public void testGetNumberWithInvalidType() throws Exception {
    final NumberConverter converter = new NumberConverter();
    Number number = null;

    assertNull(number);

    try {
      number = converter.getNumber(Number.class, Math.PI);
      fail("Calling getNumber with an invalid Class type of Number should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Class type cannot be the Number class explicitly but rather a subclass of Number!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getNumber with double value Math.PI and incompatible Number type of Integer threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(number);
  }

  public void testGetNumberWithInvalidValue() throws Exception {
    final NumberConverter converter = new NumberConverter();
    Number number = null;

    assertNull(number);

    try {
      number = converter.getNumber(Integer.class, Math.PI);
      fail("Calling getNumber with double value Math.PI and incompatible Number type of Integer should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(" + Math.PI + ") is not valid (" + Integer.class.getName() + ") value!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getNumber with double value Math.PI and incompatible Number type of Integer threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(number);
  }

  public void testGetNumberWithNullType() throws Exception {
    final NumberConverter converter = new NumberConverter();
    Number number = null;

    assertNull(number);

    try {
      number = converter.getNumber(null, Math.PI);
      fail("Calling getNumber with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getNumber with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(number);
  }

  public void testSetBeanProperty() throws Exception {
    final MockBean bean = new MockBean();

    assertNotNull(bean);
    assertNull(bean.getDoubleValue());
    assertNull(bean.getIntegerValue());

    BeanUtil.setPropertyValue(bean, "doubleValue", "3.14159");
    BeanUtil.setPropertyValue(bean, "integerValue", "2");

    assertEquals(new Double(3.14159d), bean.getDoubleValue());
    assertEquals(new Integer(2), bean.getIntegerValue());

    BeanUtil.setPropertyValue(bean, "doubleValue", null);
    BeanUtil.setPropertyValue(bean, "integerValue", null);

    assertNull(bean.getDoubleValue());
    assertNull(bean.getIntegerValue());
  }

  public static final class MockBean {

    private Double doubleValue;
    private Integer integerValue;

    public Double getDoubleValue() {
      return doubleValue;
    }

    public void setDoubleValue(final Double doubleValue) {
      this.doubleValue = doubleValue;
    }

    public Integer getIntegerValue() {
      return integerValue;
    }

    public void setIntegerValue(final Integer integerValue) {
      this.integerValue = integerValue;
    }
  }

}
