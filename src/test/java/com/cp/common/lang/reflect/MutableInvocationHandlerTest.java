/*
 * MutableInvocationHandlerTest.java (c) 10 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.06.10
 * @see com.cp.common.lang.reflect.MutableInvocationHandler
 * @see junit.framework.TestCase
 * @see org.jmock.Mockery
 */

package com.cp.common.lang.reflect;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.lang.ObjectUtil;
import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class MutableInvocationHandlerTest extends TestCase {

  private final Mockery mock = new Mockery();

  public MutableInvocationHandlerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MutableInvocationHandlerTest.class);
    //suite.addTest(new MutableInvocationHandlerTest("testName"));
    return suite;
  }

  public void testGetAndSetMethodMatcher() throws Exception {
    final MethodMatcher mockMethodMatcher = mock.mock(MethodMatcher.class);
    final MutableInvocationHandler invocationHandler = new MutableInvocationHandler(new Object());

    assertNotNull(invocationHandler.getMethodMatcher());
    assertSame(MutatorMethodMatcher.INSTANCE, invocationHandler.getMethodMatcher());

    invocationHandler.setMethodMatcher(mockMethodMatcher);

    assertNotNull(invocationHandler.getMethodMatcher());
    assertNotSame(MutatorMethodMatcher.INSTANCE, invocationHandler.getMethodMatcher());
    assertSame(mockMethodMatcher, invocationHandler.getMethodMatcher());

    invocationHandler.setMethodMatcher(null);

    assertNotNull(invocationHandler.getMethodMatcher());
    assertSame(MutatorMethodMatcher.INSTANCE, invocationHandler.getMethodMatcher());
  }

  public void testGetAndSetMutable() throws Exception {
    final MutableInvocationHandler invocationHandler = new MutableInvocationHandler(new Object());

    assertTrue(invocationHandler.isMutable());

    invocationHandler.setMutable(Mutable.IMMUTABLE);

    assertFalse(invocationHandler.isMutable());

    invocationHandler.setMutable(Mutable.MUTABLE);

    assertTrue(invocationHandler.isMutable());
  }

  public void testInvokeOnMutableProperty() throws Exception {
    try {
      final Mutable mockMutable = mock.mock(Mutable.class);

      mock.checking(new Expectations() {{
        never(mockMutable).isMutable();
        never(mockMutable).setMutable(Mutable.MUTABLE);
        never(mockMutable).setMutable(Mutable.IMMUTABLE);
      }});

      final MutableInvocationHandler invocationHandler = new MutableInvocationHandler(mockMutable);

      assertNotNull(invocationHandler);
      assertSame(mockMutable, invocationHandler.getTarget());
      assertTrue(invocationHandler.isMutable());

      final Method isMutableMethod = Mutable.class.getMethod("isMutable", (Class[]) null);
      final Method setMutableMethod = Mutable.class.getMethod("setMutable", Boolean.TYPE);

      invocationHandler.invoke(mockMutable, setMutableMethod, new Object[] { Mutable.IMMUTABLE });

      final Boolean actualValue = (Boolean) invocationHandler.invoke(null, isMutableMethod, null);

      assertNotNull(actualValue);
      assertFalse(actualValue);

      mock.assertIsSatisfied();
    }
    catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public void testInvokeOnImmutableObject() throws Exception {
    try {
      final ValueObject<String> valueObject = new ValueObjectImpl<String>("test");

      assertNotNull(valueObject);
      assertEquals("test", valueObject.getValue());

      final MutableInvocationHandler invocationHandler = new MutableInvocationHandler(valueObject);
      invocationHandler.setMutable(Mutable.IMMUTABLE);

      assertNotNull(invocationHandler);
      assertSame(valueObject, invocationHandler.getTarget());
      assertFalse(invocationHandler.isMutable());

      final Method getValueMethod = ValueObject.class.getMethod("getValue", (Class[]) null);
      final Method setValueMethod = ValueObject.class.getMethod("setValue", Object.class);

      Object actualValue = invocationHandler.invoke(null, getValueMethod, null);

      assertEquals("test", actualValue);

      try {
        invocationHandler.invoke(null, setValueMethod, new Object[] { "mock" });
        fail("Calling setValue on an immutable ValueObject instance should have thrown an ObjectImmutableException!");
      }
      catch (ObjectImmutableException e) {
        assertEquals("Object of class (" + valueObject.getClass().getName() + ") is immutable!", e.getMessage());
      }

      assertEquals("test", valueObject.getValue());
    }
    catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public void testInvokeOnMutableObject() throws Exception {
    try {
      final ValueObject<String> valueObject = new ValueObjectImpl<String>("test");

      assertNotNull(valueObject);
      assertEquals("test", valueObject.getValue());

      final MutableInvocationHandler invocationHandler = new MutableInvocationHandler(valueObject);
      invocationHandler.setMutable(Mutable.MUTABLE);

      assertNotNull(invocationHandler);
      assertSame(valueObject, invocationHandler.getTarget());
      assertTrue(invocationHandler.isMutable());

      final Method setValueMethod = ValueObject.class.getMethod("setValue", Object.class);

      invocationHandler.invoke(null, setValueMethod, new Object[] { "mock" });

      assertEquals("mock", valueObject.getValue());
    }
    catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static interface ValueObject<T> {

    public T getValue();

    public void setValue(T value);

  }

  public static class ValueObjectImpl<T> implements ValueObject<T> {

    private T value;

    public ValueObjectImpl() {
    }

    public ValueObjectImpl(final T value) {
      this.value = value;
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
      final StringBuffer buffer = new StringBuffer("{value = ");
      buffer.append(getValue());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
