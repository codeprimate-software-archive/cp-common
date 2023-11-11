/*
 * ObjectUtilTest.java (c) 6 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.01
 * @see com.cp.common.lang.ObjectUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import com.cp.common.test.mock.MockValueObject;
import com.cp.common.test.mock.MockValueObjectImpl;
import com.cp.common.util.SystemException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ObjectUtilTest extends TestCase {

  public ObjectUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ObjectUtilTest.class);
    //suite.addTest(new ObjectUtilTest("testName"));
    return suite;
  }

  public void testEquals() throws Exception {
    Object value = null;

    assertTrue(ObjectUtil.equals(null, null));
    assertTrue(ObjectUtil.equals(value, null));
    assertTrue(ObjectUtil.equals("test", "test"));
    assertFalse(ObjectUtil.equals("null", null));
    assertFalse(ObjectUtil.equals(0, null));
    assertFalse(ObjectUtil.equals("test", "TEST"));

    value = 101;

    assertTrue(ObjectUtil.equals(value, value));
    assertTrue(ObjectUtil.equals(value, 101));
    assertFalse(ObjectUtil.equals(value, 101.0));
    assertFalse(ObjectUtil.equals("test", value));
  }

  public void testGetDefaultValue() throws Exception {
    assertNull(ObjectUtil.getDefaultValue(null, null, null));
    assertEquals("one", ObjectUtil.getDefaultValue("one", "two", "three", "four"));
    assertEquals("four", ObjectUtil.getDefaultValue(null, null, null, "four"));
    assertEquals("two", ObjectUtil.getDefaultValue(null, "two", null, null));
    assertEquals("two", ObjectUtil.getDefaultValue(null, "two", null, "four"));
    assertEquals("null", ObjectUtil.getDefaultValue("null", null, null, null));
    assertEquals("null", ObjectUtil.getDefaultValue("null", "two", null, null));
  }

  public void testGetSingleDefaultValue() throws Exception {
    assertNull(ObjectUtil.getSingleDefaultValue(null, null));
    assertEquals("GoodBye", ObjectUtil.getSingleDefaultValue(null, "GoodBye"));
    assertEquals("Hello", ObjectUtil.getSingleDefaultValue("Hello", null));
    assertEquals("Hello", ObjectUtil.getSingleDefaultValue("Hello", "GoodBye"));
  }

  public void testInvokeInstanceMethod() throws Exception {
    final MockValueObject<String> mockValueObject = new MockValueObjectImpl<String>("test");

    assertEquals("test", mockValueObject.getValue());
    assertEquals("test", ObjectUtil.invokeInstanceMethod(mockValueObject, "getValue"));
    assertNull(ObjectUtil.invokeInstanceMethod(mockValueObject, "setValue", new Class[] { Object.class }, new Object[] { "null" }));
    assertEquals("null", mockValueObject.getValue());
    assertNull(ObjectUtil.invokeInstanceMethod(mockValueObject, "setValue", new Class[] { Object.class }, new Object[] { null }));
    assertNull(mockValueObject.getValue());
  }

  public void testInvokeNoSuchInstanceMethod() throws Exception {
    final MockValueObject<String> mockValueObject = new MockValueObjectImpl<String>();

    try {
      ObjectUtil.invokeInstanceMethod(mockValueObject, "noSuchMethod");
      fail("Invoking 'noSuchMethod' on the MockValueObjectImpl class should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertTrue("Expected NoSuchMethodException but was (" + ClassUtil.getClassName(e.getCause()) + ")",
        e.getCause() instanceof NoSuchMethodException);
      assertEquals("The method (noSuchMethod) does not exist on class (" + mockValueObject.getClass().getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Invoking 'noSuchMethod' on the MockValueObjectImpl class threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testInvokeInstanceMethodHavingProtectedAccess() throws Exception {
    final MockValueObject<String> mockValueObject = new MockValueObjectImpl<String>();

    assertFalse(mockValueObject.isProtectedMethodInvoked());

    try {
      ObjectUtil.invokeInstanceMethod(mockValueObject, "protectedMethod");
      fail("Invoking method 'protectedMethod' on the MockValueObjectImpl class with protected access should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to access method (protectedMethod) on class (" + mockValueObject.getClass().getName()
        + "); please verify the method access modifier and system permissions!", e.getMessage());
      assertTrue("Expected IllegalAccessException but was (" + ClassUtil.getClassName(e.getCause()) + ")",
        e.getCause() instanceof IllegalAccessException);
    }
    catch (Exception e) {
      fail("Invoking method 'protectedMethod' on the MockValueObjectImpl class with protected access threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(mockValueObject.isProtectedMethodInvoked());
  }

  public void testInvokeInstanceMethodReturningVoid() throws Exception {
    final MockValueObject<String> mockValueObject = new MockValueObjectImpl<String>();

    assertFalse(mockValueObject.isVoidMethodInvoked());
    assertNull(ObjectUtil.invokeInstanceMethod(mockValueObject, "voidMethod"));
    assertTrue(mockValueObject.isVoidMethodInvoked());
  }

  public void testInvokeInstanceMethodThrowingException() throws Exception {
    final MockValueObject<String> mockValueObject = new MockValueObjectImpl<String>();

    try {
      ObjectUtil.invokeInstanceMethod(mockValueObject, "exceptionalMethod");
      fail("Calling a method on an Object throwing an Exception should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Invoking method (exceptionalMethod) on class (" + mockValueObject.getClass().getName()
          + ") threw an Exception!", e.getMessage());
      assertTrue("Expected UnsupportedOperationException but was (" + ClassUtil.getClassName(e.getCause().getCause()) + ")",
        e.getCause() instanceof UnsupportedOperationException);
      assertEquals("Not Implemented!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling a method on an Object throwing an Exception threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testInvokeStaticMethod() throws Exception {
    final MockValueObject<Object> mockValueObject = ObjectUtil.invokeClassMethod(MockValueObjectImpl.class, "create");

    assertNotNull(mockValueObject);
    assertTrue(mockValueObject instanceof MockValueObjectImpl);
  }

  public void testInvokeStaticMethodWithArguments() throws Exception {
    final MockValueObject<Object> mockValueObject = ObjectUtil.invokeClassMethod(MockValueObjectImpl.class, "create",
      new Class[] { Object.class }, new Object[] { "test" });

    assertNotNull(mockValueObject);
    assertTrue(mockValueObject instanceof MockValueObjectImpl);
    assertEquals("test", mockValueObject.getValue());
  }

  public void testHashCode() throws Exception {
    assertEquals(0, ObjectUtil.hashCode(null));
    assertEquals(Boolean.TRUE.hashCode(), ObjectUtil.hashCode(Boolean.TRUE));
    assertEquals(new Character('C').hashCode(), ObjectUtil.hashCode('C'));
    assertEquals(new Double(3.14159).hashCode(), ObjectUtil.hashCode(3.14159));
    assertEquals(new Integer(21).hashCode(), ObjectUtil.hashCode(21));
    assertEquals("hashCode".hashCode(), ObjectUtil.hashCode("hashCode"));
  }

  public void testIsAnyNull() throws Exception {
    assertFalse(ObjectUtil.isAnyNull("test", "testing", "tested"));
    assertFalse(ObjectUtil.isAnyNull("test", "not null", "null"));
    assertFalse(ObjectUtil.isAnyNull(0, 1, 2));
    assertFalse(ObjectUtil.isAnyNull(true, true, false));
    assertTrue(ObjectUtil.isAnyNull("test", "testing", null));
    assertTrue(ObjectUtil.isAnyNull(null, 1, 2));
    assertTrue(ObjectUtil.isAnyNull(true, null, false));
    assertTrue(ObjectUtil.isAnyNull(null, "null", null));
    assertTrue(ObjectUtil.isAnyNull(null, null, null));
  }

  public void testIsNotNull() throws Exception {
    assertFalse(ObjectUtil.isNotNull(null));
    assertTrue(ObjectUtil.isNotNull("null"));
    assertTrue(ObjectUtil.isNotNull(""));
    assertTrue(ObjectUtil.isNotNull("nill"));
    assertTrue(ObjectUtil.isNotNull(Boolean.FALSE));
    assertTrue(ObjectUtil.isNotNull('\0'));
    assertTrue(ObjectUtil.isNotNull(0));
    assertTrue(ObjectUtil.isNotNull(3.14159));
    assertTrue(ObjectUtil.isNotNull("test"));
  }

  public void testIsNotSame() throws Exception {
    String str1 = "test";
    String str2 = new String("test");

    assertTrue(ObjectUtil.isNotSame(str1, str2));

    str2 = "test";

    assertFalse(ObjectUtil.isNotSame(str1, str2));
  }

  public void testIsNull() throws Exception {
    assertTrue(ObjectUtil.isNull(null));
    assertFalse(ObjectUtil.isNull("null"));
    assertFalse(ObjectUtil.isNull(""));
    assertFalse(ObjectUtil.isNull("nill"));
    assertFalse(ObjectUtil.isNull(Boolean.FALSE));
    assertFalse(ObjectUtil.isNull('\0'));
    assertFalse(ObjectUtil.isNull(0));
    assertFalse(ObjectUtil.isNull(3.14159));
    assertFalse(ObjectUtil.isNull("test"));
  }

  public void testIsSame() throws Exception {
    String str1 = "test";
    String str2 = str1;

    assertTrue(ObjectUtil.isSame(str1, str2));

    str2 = "test";

    assertTrue(ObjectUtil.isSame(str1, str2));

    str2 = new String("test");

    assertFalse(ObjectUtil.isSame(str1, str2));
  }

  public void testToString() throws Exception
  {
    assertEquals("false", ObjectUtil.toString(Boolean.FALSE));
    assertEquals("J", ObjectUtil.toString('J'));
    assertEquals("0", ObjectUtil.toString(0));
    assertEquals("3.14159", ObjectUtil.toString(3.14159));
    assertEquals("TEST", ObjectUtil.toString("TEST"));
    assertEquals("TeSt", ObjectUtil.toString("TeSt"));
    assertEquals("", ObjectUtil.toString(""));
    assertEquals(null, ObjectUtil.toString(null));
  }

}
