/*
 * MutableUtilTest.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.7
 * @see com.cp.common.lang.MutableUtil
 * @see junit.framework.TestCase
 * @see org.jmock.Mockery
 */

package com.cp.common.lang;

import java.awt.Point;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class MutableUtilTest extends TestCase {

  private final Mockery mock = new Mockery();

  public MutableUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MutableUtilTest.class);
    //suite.addTest(new MutableUtilTest("testName"));
    return suite;
  }

  public void testIsInstanceOfJavaImmutableClass() throws Exception {
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass(null));
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass(Boolean.TRUE));
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass(Boolean.FALSE));
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass(2));
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass(Math.PI));
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass('c'));
    assertTrue(MutableUtil.isInstanceOfJavaImmutableClass("test"));
    assertFalse(MutableUtil.isInstanceOfJavaImmutableClass(new Object()));
    assertFalse(MutableUtil.isInstanceOfJavaImmutableClass(Calendar.getInstance()));
    assertFalse(MutableUtil.isInstanceOfJavaImmutableClass(new ValueObjectImpl<String>("test")));
  }

  public void testIsMutable() throws Exception {
    final Mutable mockMutable = mock.mock(Mutable.class);

    mock.checking(new Expectations() {{
      atLeast(1).of(mockMutable).isMutable();
      will(returnValue(Mutable.MUTABLE));
    }});

    assertTrue(MutableUtil.isMutable(new Object()));
    assertTrue(MutableUtil.isMutable(Calendar.getInstance()));
    assertTrue(MutableUtil.isMutable(new ValueObjectImpl<String>("test")));
    assertTrue(MutableUtil.isMutable(mockMutable));

    mock.assertIsSatisfied();
  }

  public void testIsNotMutable() throws Exception {
    final Mutable mockMutable = mock.mock(Mutable.class);

    mock.checking(new Expectations() {{
      atLeast(1).of(mockMutable).isMutable();
      will(returnValue(Mutable.IMMUTABLE));
    }});

    assertFalse(MutableUtil.isMutable(null));
    assertFalse(MutableUtil.isMutable(Boolean.TRUE));
    assertFalse(MutableUtil.isMutable(Boolean.FALSE));
    assertFalse(MutableUtil.isMutable((byte) 2));
    assertFalse(MutableUtil.isMutable((short) 2));
    assertFalse(MutableUtil.isMutable(2));
    assertFalse(MutableUtil.isMutable((long) 2));
    assertFalse(MutableUtil.isMutable(3.14f));
    assertFalse(MutableUtil.isMutable(Math.PI));
    assertFalse(MutableUtil.isMutable('c'));
    assertFalse(MutableUtil.isMutable("test"));
    assertFalse(MutableUtil.isMutable(new Point(5, 5)));
    assertFalse(MutableUtil.isMutable(mockMutable));

    mock.assertIsSatisfied();
  }

  public void testImplementMutable() throws Exception {
    final ValueObject<String> valueObject = new ValueObjectImpl<String>("test");

    assertNotNull(valueObject);
    assertTrue(valueObject.isMutable());
    assertEquals("test", valueObject.getValue());

    valueObject.setMutable(Mutable.IMMUTABLE);
    valueObject.setValue("mock");

    assertFalse(valueObject.isMutable());
    assertEquals("mock", valueObject.getValue());

    final ValueObject<String> valueObjectProxy = MutableUtil.implementMutable(valueObject);

    assertNotNull(valueObjectProxy);
    assertTrue(valueObjectProxy.isMutable());
    assertEquals("mock", valueObjectProxy.getValue());
    assertFalse(valueObject.isMutable());
    assertEquals("mock", valueObject.getValue());

    valueObjectProxy.setValue("null");

    assertTrue(valueObjectProxy.isMutable());
    assertEquals("null", valueObjectProxy.getValue());
    assertFalse(valueObject.isMutable());
    assertEquals("null", valueObject.getValue());

    valueObjectProxy.setMutable(Mutable.IMMUTABLE);
    valueObject.setMutable(Mutable.MUTABLE);

    assertFalse(valueObjectProxy.isMutable());
    assertTrue(valueObject.isMutable());

    try {
      valueObjectProxy.setValue("abc123");
      fail("Calling setValue on the ValueObject proxy object set to immutable should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      assertEquals("Object of class (" + valueObject.getClass().getName() + ") is immutable!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setValue on the ValueObject proxy object set to immutable threw an unexpected Throwable ("
        + t + ")!");
    }

    assertFalse(valueObjectProxy.isMutable());
    assertEquals("null", valueObjectProxy.getValue());
    assertTrue(valueObject.isMutable());
    assertEquals("null", valueObject.getValue());

    valueObjectProxy.setMutable(Mutable.MUTABLE);

    assertTrue(valueObjectProxy.isMutable());
    assertTrue(valueObject.isMutable());
  }

  public void testImplementMutableWithJavaImmutableClass() throws Exception {
    try {
      MutableUtil.implementMutable("test");
      fail("Calling implementMutable with a String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The object is either an instance of class (java.lang.String) which is a Java language immutable class or null!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling implementMutable with a String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testImplementMutableWithNull() throws Exception {
    try {
      MutableUtil.implementMutable(null);
      fail("Calling implementMutable with a null value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The object is either an instance of class (null) which is a Java language immutable class or null!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling implementMutable with a null value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testVerifyMutableWithMutableObjects() throws Exception {
    final Mutable mockMutable = mock.mock(Mutable.class);

    mock.checking(new Expectations() {{
      atLeast(1).of(mockMutable).isMutable();
      will(returnValue(Mutable.MUTABLE));
    }});

    try {
      MutableUtil.verifyMutable(new Object());
      MutableUtil.verifyMutable(Calendar.getInstance());
      MutableUtil.verifyMutable(new ValueObjectImpl<String>("test"));
      MutableUtil.verifyMutable(mockMutable);
    }
    catch (Throwable t) {
      fail("Calling verifyMutable on mutable objects threw an unexpected Throwable (" + t + ")!");
    }

    mock.assertIsSatisfied();
  }

  public void testVerifyMutableWithImmutableJavaClass() throws Exception {
    try {
      MutableUtil.verifyMutable("test");
      fail("Calling verifyMutable on a String value should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      assertEquals("The object of type (java.lang.String) is immutable!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling verifyMutable on a String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testVerifyMutableWithImmutableMutable() throws Exception {
    final Mutable mockMutable = mock.mock(Mutable.class);

    mock.checking(new Expectations() {{
      atLeast(1).of(mockMutable).isMutable();
      will(returnValue(Mutable.IMMUTABLE));
    }});

    try {
      MutableUtil.verifyMutable(mockMutable);
      fail("Calling verifyMutable on a Mutable object that is immutable should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      assertEquals("The object of type (" + mockMutable.getClass().getName() + ") is immutable!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling verifyMutable on a Mutable object that is immutable threw an unexpected Throwable (" + t + ")!");
    }

    mock.assertIsSatisfied();
  }

  public static interface ValueObject<T> extends Mutable {

    public T getValue();

    public void setValue(T value);

  }

  public static class ValueObjectImpl<T> implements ValueObject<T> {

    private boolean mutable = Mutable.MUTABLE;
    private T value;

    public ValueObjectImpl() {
    }

    public ValueObjectImpl(final T value) {
      this.value = value;
    }

    public boolean isMutable() {
      return mutable;
    }

    public void setMutable(final boolean mutable) {
      this.mutable = mutable;
    }

    public T getValue() {
      return value;
    }

    public void setValue(final T value) {
      this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof ValueObject)) {
        return false;
      }

      final ValueObject that = (ValueObject) obj;

      return ObjectUtil.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getValue());
      return hashValue;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{mutable = ");
      buffer.append(isMutable());
      buffer.append(", value = ").append(getValue());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
