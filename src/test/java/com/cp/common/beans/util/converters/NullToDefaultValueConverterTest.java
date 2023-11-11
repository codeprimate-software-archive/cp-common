/*
 * NullToDefaultValueConverterTest.java (c) 6 October 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.util.converters.NullToDefaultValueConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.Bean;
import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.util.DateUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NullToDefaultValueConverterTest extends TestCase {

  public NullToDefaultValueConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NullToDefaultValueConverterTest.class);
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    NullToDefaultValueConverter.INSTANCE.reset();
  }

  protected void assertEquals(final Calendar date1, final Calendar date2) {
    assertEquals(date1.get(Calendar.YEAR), date2.get(Calendar.YEAR));
    assertEquals(date1.get(Calendar.MONTH), date2.get(Calendar.MONTH));
    assertEquals(date1.get(Calendar.DAY_OF_MONTH), date2.get(Calendar.DAY_OF_MONTH));
    assertEquals(date1.get(Calendar.HOUR_OF_DAY), date2.get(Calendar.HOUR_OF_DAY));
    assertEquals(date1.get(Calendar.MINUTE), date2.get(Calendar.MINUTE));
  }

  protected void assertEquals(final Date date1, final Date date2) {
    assertEquals(DateUtil.getCalendar(date1), DateUtil.getCalendar(date2));
  }

  public void testConvert() throws Exception {
    assertNull(NullToDefaultValueConverter.INSTANCE.convert(Bean.class, null));
    assertNull(NullToDefaultValueConverter.INSTANCE.convert(com.cp.common.enums.Enum.class, null));
    assertNull(NullToDefaultValueConverter.INSTANCE.convert(Enum.class, null));
    assertEquals(new BigInteger("0"), NullToDefaultValueConverter.INSTANCE.convert(BigInteger.class, null));
    assertEquals(new BigDecimal("0.0"), NullToDefaultValueConverter.INSTANCE.convert(BigDecimal.class, null));
    assertEquals(Boolean.FALSE, NullToDefaultValueConverter.INSTANCE.convert(Boolean.class, null));
    assertEquals((byte) 0, NullToDefaultValueConverter.INSTANCE.convert(Byte.class, null));
    assertEquals(Calendar.getInstance(), NullToDefaultValueConverter.INSTANCE.convert(Calendar.class, null));
    assertEquals('?', NullToDefaultValueConverter.INSTANCE.convert(Character.class, null));
    assertEquals(Calendar.getInstance().getTime(), NullToDefaultValueConverter.INSTANCE.convert(Date.class, null));
    assertEquals(0.0d, NullToDefaultValueConverter.INSTANCE.convert(Double.class, null));
    assertEquals(0.0f, NullToDefaultValueConverter.INSTANCE.convert(Float.class, null));
    assertEquals(0, NullToDefaultValueConverter.INSTANCE.convert(Integer.class, null));
    assertEquals(0l, NullToDefaultValueConverter.INSTANCE.convert(Long.class, null));
    assertEquals(new DefaultProcess("system"), NullToDefaultValueConverter.INSTANCE.convert(com.cp.common.beans.Process.class, null));
    assertNull(NullToDefaultValueConverter.INSTANCE.convert(Process.class, null));
    assertEquals((short) 0, NullToDefaultValueConverter.INSTANCE.convert(Short.class, null));
    assertEquals("", NullToDefaultValueConverter.INSTANCE.convert(String.class, null));
    assertEquals("test", NullToDefaultValueConverter.INSTANCE.convert(String.class, "test"));
    assertEquals(new DefaultUser("root"), NullToDefaultValueConverter.INSTANCE.convert(User.class, null));
  }

  public void testGetDefaultValue() throws Exception {
    final Map<Class, Object> defaultValueMap = new HashMap<Class, Object>(3);
    defaultValueMap.put(Double.class, Math.PI);
    defaultValueMap.put(Integer.class, -1);

    assertEquals(-1, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Integer.class));
    assertEquals(Math.PI, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Double.class));
    assertNull(NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Float.class));
    assertNull(NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Long.class));
    assertNull(NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Boolean.class));
    assertNull(NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, String.class));

    defaultValueMap.put(Number.class, 0.0d);

    assertEquals(-1, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Integer.class));
    assertEquals(Math.PI, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Double.class));
    assertEquals(0.0d, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Float.class));
    assertEquals(0.0d, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Long.class));
    assertNull(NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Boolean.class));
    assertNull(NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, String.class));
  }

  public void testGetDefaultValueUsingValueFactory() throws Exception {
    final MockValueFactory<String> factory = new MockValueFactory<String>("test");

    final Map<Class, Object> defaultValueMap = new HashMap<Class, Object>(2);
    defaultValueMap.put(Boolean.class, Boolean.FALSE);
    defaultValueMap.put(String.class, factory);

    assertEquals(Boolean.FALSE, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Boolean.class));
    assertEquals("test", NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, String.class));

    factory.setValue("null");

    assertEquals(Boolean.FALSE, NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, Boolean.class));
    assertEquals("null", NullToDefaultValueConverter.INSTANCE.getDefaultValue(defaultValueMap, String.class));
  }

  public void testGetDefaultValueWithNullDefaultValueMap() throws Exception {
    try {
      NullToDefaultValueConverter.INSTANCE.getDefaultValue(null, Object.class);
      fail("Calling the getDefaultValue method with a null default value Map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The default value mapping cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the getDefaultValue method with a null default value Map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetDefaultValueWithNullType() throws Exception {
    try {
      NullToDefaultValueConverter.INSTANCE.getDefaultValue(new HashMap<Class, Object>(0), null);
      fail("Calling the getDefaultValue method with null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type of the default value must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the getDefaultValue method with a null Class type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testRegisterDefaultValue() throws Exception {
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Boolean.class, Boolean.FALSE));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Calendar.class, DateUtil.getCalendar(2008, Calendar.JULY,  30)));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Character.class, '?'));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Date.class, DateUtil.getCalendar(2008, Calendar.JULY, 31).getTime()));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Number.class, 0.0));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(String.class, ""));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(String.class, "null"));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Integer.class, -1));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Double.class, Math.PI));
  }

  public void testRegisterDefaultValueWithNullDefaultValue() throws Exception {
    try {
      NullToDefaultValueConverter.registerDefaultValue(Object.class, (Object) null);
      fail("Calling registerDefaultValue with a null default value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Class type (null) of the default value must be assignable to (" + Object.class.getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling registerDefaultValue with a null default value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testRegisterDefaultValueWithNullType() throws Exception {
    try {
      NullToDefaultValueConverter.registerDefaultValue(null, "test");
      fail("Calling registerDefaultValue with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type of the default value cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling registerDefaultValue with a null Class type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testUnregisterDefaultValue() throws Exception {
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Object.class, "test"));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Boolean.class, Boolean.FALSE));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Calendar.class, Calendar.getInstance()));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(Number.class, 0.0));
    assertTrue(NullToDefaultValueConverter.registerDefaultValue(String.class, "null"));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(BOOL.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(Character.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(Date.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(Double.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(Integer.class));
    assertTrue(NullToDefaultValueConverter.unregisterDefaultValue(Boolean.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(Boolean.class));
    assertTrue(NullToDefaultValueConverter.unregisterDefaultValue(Number.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(Number.class));
    assertTrue(NullToDefaultValueConverter.unregisterDefaultValue(String.class));
    assertFalse(NullToDefaultValueConverter.unregisterDefaultValue(String.class));
  }

  private static final class BOOL {
  }

  private static final class MockValueFactory<T> implements NullToDefaultValueConverter.ValueFactory<T> {

    private T value;

    public MockValueFactory() {
    }

    public MockValueFactory(final T value) {
      this.value = value;
    }

    public T getValue() {
      return value;
    }

    public void setValue(final T value) {
      this.value = value;
    }
  }

}
