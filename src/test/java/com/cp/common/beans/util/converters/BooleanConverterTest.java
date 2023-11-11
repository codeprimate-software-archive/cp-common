/*
 * BooleanConverterTest.java (c) 17 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.7
 * @see com.cp.common.beans.util.converters.BooleanConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.util.ConversionException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BooleanConverterTest extends TestCase {

  public BooleanConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BooleanConverterTest.class);
    //suite.addTest(new BooleanConverterTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ConvertUtil.unregister(Boolean.class);
  }

  public void testConvert() throws Exception {
    final AbstractConverter booleanConverter = new BooleanConverter();

    assertNotNull(booleanConverter);
    assertNull(booleanConverter.getDefaultValue());
    assertFalse(booleanConverter.isUsingDefaultValue());
    assertNull(booleanConverter.convert(Boolean.class, null));
    assertNull(booleanConverter.convert(Boolean.class, "null"));
    assertNull(booleanConverter.convert(Boolean.class, " null "));
    assertNull(booleanConverter.convert(Boolean.class, "Null"));
    assertNull(booleanConverter.convert(Boolean.class, " NULL "));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, Boolean.TRUE));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, true));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, Boolean.FALSE));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, false));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "true"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "TRUE"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "True"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "TrUe"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "1"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "y"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "Y"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "yes"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "YES"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "Yes"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "YeS"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "false"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "FALSE"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "False"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "FaLsE"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "0"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "n"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "N"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "no"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "NO"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "No"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "nO"));
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter booleanConverter = new BooleanConverter(true);

    assertNotNull(booleanConverter);
    assertNull(booleanConverter.getDefaultValue());
    assertTrue(booleanConverter.isUsingDefaultValue());
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, null));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "null"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, " null "));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "Null"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, " NULL "));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, true));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, Boolean.TRUE));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "true"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "1"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "y"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "yes"));
  }

  public void testConvertWithDefaultValue() throws Exception {
    final AbstractConverter booleanConverter = new BooleanConverter(Boolean.FALSE);

    assertNotNull(booleanConverter);
    assertEquals(Boolean.FALSE, booleanConverter.getDefaultValue());
    assertTrue(booleanConverter.isUsingDefaultValue());
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, null));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "null"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, " null "));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, "Null"));
    assertEquals(Boolean.FALSE, booleanConverter.convert(Boolean.class, " NULL "));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, true));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, Boolean.TRUE));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "true"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "1"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "y"));
    assertEquals(Boolean.TRUE, booleanConverter.convert(Boolean.class, "yes"));
  }

  public void testConvertWithInvalidValue() throws Exception {
    final AbstractConverter booleanConverter = new BooleanConverter();
    Boolean condition = null;

    assertNotNull(booleanConverter);
    assertNull(booleanConverter.getDefaultValue());
    assertNull(condition);

    try {
      condition = (Boolean) booleanConverter.convert(Boolean.class, "tru");
      fail("Calling convert on the invalid value of 'tru' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(tru) is not a valid boolean condition!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert on the invalid value of 'tru' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(condition);

    try {
      condition = (Boolean) booleanConverter.convert(Boolean.class, "one");
      fail("Calling convert on the invalid value of 'one' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(one) is not a valid boolean condition!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert on the invalid value of 'one' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(condition);

    try {
      condition = (Boolean) booleanConverter.convert(Boolean.class, "not true");
      fail("Calling convert on the invalid value of 'not true' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(not true) is not a valid boolean condition!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert on the invalid value of 'not true' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(condition);

    try {
      condition = (Boolean) booleanConverter.convert(Boolean.class, "zero");
      fail("Calling convert on the invalid value of 'zero' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(zero) is not a valid boolean condition!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert on the invalid value of 'zero' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(condition);
  }

  public void testGetBoolean() throws Exception {
    final BooleanConverter converter = new BooleanConverter();

    assertNotNull(converter);
    assertNull(converter.getDefaultValue());
    assertEquals(Boolean.TRUE, converter.getBoolean("true"));
    assertEquals(Boolean.TRUE, converter.getBoolean(" TRUE "));
    assertEquals(Boolean.TRUE, converter.getBoolean("True"));
    assertEquals(Boolean.TRUE, converter.getBoolean(" TrUe "));
    assertEquals(Boolean.TRUE, converter.getBoolean("1"));
    assertEquals(Boolean.TRUE, converter.getBoolean("y"));
    assertEquals(Boolean.TRUE, converter.getBoolean(" Y "));
    assertEquals(Boolean.TRUE, converter.getBoolean("yes"));
    assertEquals(Boolean.TRUE, converter.getBoolean(" YES "));
    assertEquals(Boolean.TRUE, converter.getBoolean("Yes"));
    assertEquals(Boolean.TRUE, converter.getBoolean(" YeS "));
    assertEquals(Boolean.FALSE, converter.getBoolean("false"));
    assertEquals(Boolean.FALSE, converter.getBoolean(" FALSE "));
    assertEquals(Boolean.FALSE, converter.getBoolean("False"));
    assertEquals(Boolean.FALSE, converter.getBoolean(" FaLsE "));
  }

  public void testGetBooleanWithInvalidValue() throws Exception {
    final BooleanConverter converter = new BooleanConverter();
    Boolean value = null;

    assertNotNull(converter);
    assertNull(converter.getDefaultValue());
    assertNull(value);

    try {
      value = converter.getBoolean("tru");
      fail("Calling getBoolean with invalid value 'tru' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(tru) is not a valid boolean condition!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBoolean with invalid value 'tru' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetBooleanWithNullCondition() throws Exception {
    final BooleanConverter converter = new BooleanConverter();
    Boolean value = null;

    assertNotNull(converter);
    assertNull(converter.getDefaultValue());
    assertNull(value);

    try {
      value = converter.getBoolean(null);
      fail("Calling getBoolean with a null condition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The condition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBoolean with a null condition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testSetBeanProperty() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", "y");

    assertEquals(Boolean.TRUE, bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", "n");

    assertEquals(Boolean.FALSE, bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", "1");

    assertEquals(Boolean.TRUE, bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", "0");

    assertEquals(Boolean.FALSE, bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", null);

    assertNull(bean.getCondition());
  }

  public void testSetBeanPropertyUsingDefaultValue() throws Exception {
    ConvertUtil.register(Boolean.class, new BooleanConverter(true));
    final MockBean bean = new MockBean();

    assertNull(bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", "true");

    assertEquals(Boolean.TRUE, bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", null);

    assertEquals(Boolean.FALSE, bean.getCondition());
  }

  public void testSetBeanPropertyWithDefaultValue() throws Exception {
    ConvertUtil.register(Boolean.class, new BooleanConverter(Boolean.FALSE));
    final MockBean bean = new MockBean();

    assertNull(bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", "true");

    assertEquals(Boolean.TRUE, bean.getCondition());

    BeanUtil.setPropertyValue(bean, "condition", null);

    assertEquals(Boolean.FALSE, bean.getCondition());
  }

  public static final class MockBean {

    private Boolean condition;

    public Boolean getCondition() {
      return condition;
    }

    public void setCondition(final Boolean condition) {
      this.condition = condition;
    }
  }

}
