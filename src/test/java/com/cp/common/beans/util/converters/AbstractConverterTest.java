/*
 * AbstractConverterTest.java (c) 7 March 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.7
 * @see com.cp.common.beans.util.converters.AbstractConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractConverterTest extends TestCase {

  public AbstractConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractConverterTest.class);
    //suite.addTest(new AbstractConverterTest("testName"));
    return suite;
  }

  public void testConvert() throws Exception {
    final AbstractConverter testConverter = new TestConverter();

    assertNotNull(testConverter);
    assertNull(testConverter.getDefaultValue());
    assertFalse(testConverter.isUsingDefaultValue());
    assertNull(testConverter.convert(Object.class, null));
    assertNull(testConverter.convert(Object.class, "null"));
    assertNull(testConverter.convert(Object.class, " null "));
    assertNull(testConverter.convert(Object.class, "Null"));
    assertNull(testConverter.convert(Object.class, " NULL "));
    assertEquals("nill", testConverter.convert(Object.class, "nill"));
    assertEquals("test", testConverter.convert(Object.class, "test"));
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter testConverter = new TestConverter(true);

    assertNotNull(testConverter);
    assertNull(testConverter.getDefaultValue());
    assertTrue(testConverter.isUsingDefaultValue());
    assertEquals(Boolean.FALSE, testConverter.convert(Boolean.class, null));
    assertEquals(new Character('?'), testConverter.convert(Character.class, null));
    assertEquals(new Double(0.0), testConverter.convert(Double.class, null));
    assertEquals(new Integer(0), testConverter.convert(Integer.class, null));
    assertEquals("", testConverter.convert(String.class, null));
  }

  public void testConvertWithDefaultValue() throws Exception {
    final AbstractConverter testConverter = new TestConverter("testing");

    assertNotNull(testConverter);
    assertEquals("testing", testConverter.getDefaultValue());
    assertTrue(testConverter.isUsingDefaultValue());
    assertEquals("testing", testConverter.convert(Object.class, null));
    assertEquals("testing", testConverter.convert(Object.class, "null"));
    assertEquals("testing", testConverter.convert(Object.class, " null "));
    assertEquals("testing", testConverter.convert(Object.class, "Null"));
    assertEquals("testing", testConverter.convert(Object.class, " NULL "));
    assertEquals("nill", testConverter.convert(Object.class, "nill"));
    assertEquals("test", testConverter.convert(Object.class, "test"));
  }

  protected static final class TestConverter extends AbstractConverter<Object> {

    public TestConverter() {
    }

    public TestConverter(final Object defaultValue) {
      super(defaultValue);
    }

    public TestConverter(final boolean useDefaultValue) {
      super(useDefaultValue);
    }

    protected Object convertImpl(final Class type, final Object value) {
      return value;
    }
  }

}
