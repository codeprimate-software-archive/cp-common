/*
 * ClassUtilTest.java (c) 01 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.6
 * @see com.cp.common.test.util.TestUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import com.cp.common.test.util.TestUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.SystemException;
import java.io.Serializable;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ClassUtilTest extends TestCase {

  public ClassUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ClassUtilTest.class);
    //suite.addTest(new ClassUtilTest("testName"));
    return suite;
  }

  public void testGetAllInterfaces() throws Exception {
    final Class[] expected = { D.class, F.class };
    final Class[] actual = ClassUtil.getAllInterfaces(C.class);

    TestUtil.assertEquals(expected, actual);
  }

  public void testGetAllInterfacesWithInterface() throws Exception {
    TestUtil.assertEquals(new Class[] { D.class }, ClassUtil.getAllInterfaces(D.class));
    TestUtil.assertEquals(new Class[] { E.class }, ClassUtil.getAllInterfaces(E.class));
    TestUtil.assertEquals(new Class[] { F.class }, ClassUtil.getAllInterfaces(F.class));
  }

  public void testGetAllInterfacesWithNullClass() throws Exception {
    try {
      ClassUtil.getAllInterfaces(null);
      fail("Calling getAllInterfaces with a null Class object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getAllInterfaces with a null Class object threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testGetClass() throws Exception {
    assertNull(ClassUtil.getClass(null));
    assertEquals(Boolean.class, ClassUtil.getClass(Boolean.TRUE));
    assertEquals(Byte.class, ClassUtil.getClass((byte) 1));
    assertEquals(Short.class, ClassUtil.getClass((short) 1));
    assertEquals(Integer.class, ClassUtil.getClass(1));
    assertEquals(Long.class, ClassUtil.getClass(1L));
    assertEquals(Float.class, ClassUtil.getClass(3.14159f));
    assertEquals(Double.class, ClassUtil.getClass(Math.PI));
    assertEquals(Character.class, ClassUtil.getClass('c'));
    assertEquals(String.class, ClassUtil.getClass("test"));
    assertEquals(String.class, ClassUtil.getClass("mock"));
    assertEquals(String.class, ClassUtil.getClass("null"));
    assertEquals(String.class, ClassUtil.getClass("false"));
    assertEquals(String.class, ClassUtil.getClass("0"));
    assertEquals(String.class, ClassUtil.getClass("0.00"));
    assertEquals(String.class, ClassUtil.getClass("C"));
    assertEquals(String.class, ClassUtil.getClass("java.lang.Character"));
  }

  public void testGetClassNameWithObject() throws Exception {
    assertNull(ClassUtil.getClassName((Object) null));
    assertEquals("java.lang.Boolean", ClassUtil.getClassName(Boolean.TRUE));
    assertEquals("java.lang.Byte", ClassUtil.getClassName((byte) 1));
    assertEquals("java.lang.Short", ClassUtil.getClassName((short) 1));
    assertEquals("java.lang.Integer", ClassUtil.getClassName(1));
    assertEquals("java.lang.Long", ClassUtil.getClassName(1L));
    assertEquals("java.lang.Float", ClassUtil.getClassName(3.14159f));
    assertEquals("java.lang.Double", ClassUtil.getClassName(Math.PI));
    assertEquals("java.lang.Character", ClassUtil.getClassName('c'));
    assertEquals("java.lang.String", ClassUtil.getClassName("test"));
    assertEquals("java.lang.String", ClassUtil.getClassName("mock"));
    assertEquals("java.lang.String", ClassUtil.getClassName("null"));
    assertEquals("java.lang.String", ClassUtil.getClassName("false"));
    assertEquals("java.lang.String", ClassUtil.getClassName("0"));
    assertEquals("java.lang.String", ClassUtil.getClassName("0.00"));
    assertEquals("java.lang.String", ClassUtil.getClassName("C"));
    assertEquals("java.lang.String", ClassUtil.getClassName("java.lang.Character"));
  }

  public void testGetClassNameWithClass() throws Exception {
    assertNull(ClassUtil.getClassName((Class) null));
    assertEquals("java.lang.Class", ClassUtil.getClassName(Class.class));
    assertEquals("java.lang.Boolean", ClassUtil.getClassName(Boolean.class));
    assertEquals("java.lang.Byte", ClassUtil.getClassName(Byte.class));
    assertEquals("java.lang.Short", ClassUtil.getClassName(Short.class));
    assertEquals("java.lang.Integer", ClassUtil.getClassName(Integer.class));
    assertEquals("java.lang.Long", ClassUtil.getClassName(Long.class));
    assertEquals("java.lang.Float", ClassUtil.getClassName(Float.class));
    assertEquals("java.lang.Double", ClassUtil.getClassName(Double.class));
    assertEquals("java.lang.Character", ClassUtil.getClassName(Character.class));
    assertEquals("java.lang.String", ClassUtil.getClassName(String.class));
  }

  public void testGetClassTypes() throws Exception {
    final Object[] arguments = { null, Boolean.TRUE, null, (byte) 1, (short) 2, 3, 4L, null, 3.14159f, Math.PI, null,
      'C', null, "test", "mock", "null" };

    final Class[] expectedArgumentTypes = { null, Boolean.class, null, Byte.class, Short.class, Integer.class,
      Long.class, null, Float.class, Double.class, null, Character.class, null, String.class, String.class, String.class };

    final Class[] actualArgumentTypes = ClassUtil.getClassTypes(arguments);

    TestUtil.assertEquals(expectedArgumentTypes, actualArgumentTypes);
  }

  public void testGetClassTypesWithEmptyObjectArray() throws Exception {
    final Class[] classTypes = ClassUtil.getClassTypes(new Object[0]);

    assertNotNull(classTypes);
    assertEquals(0, classTypes.length);
  }

  public void testGetClassTypesWithNullObjectArray() throws Exception {
    try {
      ClassUtil.getClassTypes((Object[]) null);
      fail("Calling getClassTypes with a null Object array should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The array of objects cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getClassTypes with a null Object array threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testGetInstance() throws Exception {
    final ValueObject valueObject = ClassUtil.getInstance("com.cp.common.lang.ClassUtilTest$ValueObjectImpl");

    assertNotNull(valueObject);
    assertNull(valueObject.getValue());

    final ValueObject anotherValueObject = ClassUtil.getInstance(ValueObjectImpl.class);

    assertNotNull(anotherValueObject);
    assertNull(anotherValueObject.getValue());
    assertFalse(valueObject.equals(anotherValueObject));
    assertNotSame(valueObject, anotherValueObject);
  }

  public void testGetInstanceAcceptingArguments() throws Exception {
    final Boolean booleanValue = ClassUtil.getInstance("java.lang.Boolean", new Object[] { "true" });

    assertNotNull(booleanValue);
    assertTrue(booleanValue);
    assertNotSame(Boolean.TRUE, booleanValue);

    final Double doubleValue = ClassUtil.getInstance("java.lang.Double", new Object[] { String.valueOf(Math.PI) });

    assertNotNull(doubleValue);
    assertEquals(Math.PI, doubleValue);

    final Integer integerValue = ClassUtil.getInstance("java.lang.Integer", new Object[] { "7" });

    assertNotNull(integerValue);
    assertEquals(7, integerValue.intValue());

    final String stringValue = ClassUtil.getInstance(String.class, new Object[] { "test" });

    assertNotNull(stringValue);
    assertEquals("test", stringValue);
    assertNotSame("test", stringValue);
  }

  public void testGetInstanceAcceptingArgumentTypesAndArguments() throws Exception {
    final Character characterValue = ClassUtil.getInstance(Character.class, new Class[] { Character.TYPE },
      new Object[] { 'c' });

    assertNotNull(characterValue);
    assertEquals('c', characterValue.charValue());

    final ValueObject<String> valueObject = ClassUtil.getInstance(ValueObjectImpl.class, new Class[] { Object.class },
      new Object[] { "test" });

    assertNotNull(valueObject);
    assertEquals("test", valueObject.getValue());
  }

  public void testGetInstanceClassNotFoundException() throws Exception {
    try {
      ClassUtil.getInstance("no.such.Class");
      fail("Calling getInstance with class name 'no.such.Class' should have thrown a ClassNotFoundException!");
    }
    catch (ClassNotFoundException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling getInstance with class name 'no.such.Class' threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testGetInstanceNoSuchConstructor() throws Exception {
    try {
      ClassUtil.getInstance(ValueObjectImpl.class, new Class[] { String.class }, new Object[] { "test" });
      fail("Calling getInstance on the ValueObjectImpl class expecting a constructor with a String argument should have thrown a NoSuchMethodException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("No constructor in class (" + ValueObjectImpl.class.getName()
        + ") exists having the following parameters ([class java.lang.String])!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling the getInstance method on the ValueObjectImpl class expecting a constructor with a String argument threw an unexpected Throwable ("
        + t + ")!");
    }
  }

  public void testGetInstanceThrowingIllegalAccessException() throws Exception {
    try {
      ClassUtil.getInstance(ValueObjectImpl.class, new Class[] { ValueObject.class }, new Object[] { new ValueObjectImpl<Object>("test") });
      fail("Calling getInstance with an instance of class ValueObject using a private constructor should have thrown an SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to access constructor of class (" + ValueObjectImpl.class.getName() + ") having parameters ("
        + ArrayUtil.toString(new Class[] { ValueObject.class })
        + "); please verify the constructor access modifiers and system permissions!", e.getMessage());
      assertTrue("Expected an IllegalAccessException but was (" + e.getCause().getClass().getName() + ")",
        e.getCause() instanceof IllegalAccessException);
    }
    catch (Throwable t) {
      fail("Calling getInstance with an instance of class ValueObject using a private constructor threw an unexpected Throwable ("
        + t + ")!");
    }
  }

  public void testGetInstanceThrowingInvocationTargetException() throws Exception {
    try {
      ClassUtil.getInstance(ValueObjectImpl.class, new Class[] { Throwable.class },
        new Object[] { new IllegalArgumentException("Illegal Argument!") });
      fail("Calling getInstance with the ValueObjectImpl class using a constructor throwing an Exception should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Invoking constructor with parameters (" + ArrayUtil.toString(new Class[] { Throwable .class })
        + ") of class (" + ValueObjectImpl.class.getName() + ") threw an Exception!", e.getMessage());
      assertTrue("Expected an IllegalArgumentException; but was (" + e.getCause().getClass().getName() + ")",
        e.getCause() instanceof IllegalArgumentException);
      assertEquals("Illegal Argument!", e.getCause().getMessage());
    }
    catch (Throwable t) {
      fail("Calling getInstance with the ValueObjectImpl class using a constructor throwing an Exception threw an unexpected Throwable ("
        + t + ")!");
    }
  }

  public void testGetInstanceThrowingInstantiationException() throws Exception {
    try {
      ClassUtil.getInstance(ValueObject.class);
      fail("Calling getInstance with an interface type should have thrown an InstantiationException!");
    }
    catch (InstantiationException e) {
      assertEquals("Failed to create instance of class (" + ValueObject.class.getName() + ")!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getInstance with an interface type threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testGetInstanceWithNullClass() throws Exception {
    try {
      ClassUtil.getInstance((Class) null, new Class[] { Object.class }, new Object[] { "test" });
      fail("Calling getInstance with a null Class object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type from which the instance will be created cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getInstance with a null Class object threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testGetInstanceWithWrongNumberAndTypesOfArguments() throws Exception {
    try {
      ClassUtil.getInstance(ValueObjectImpl.class, new Class[] { Object.class }, new Object[] { "test", "mock", "null" });
      fail("Calling getInstance with a Class passing arguments of the wrong number and type should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The number of arguments (3) does not match the number of parameters (1) in the constructor of class ("
        + ValueObjectImpl.class.getName() + ")!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getInstance with a Class passing arguments of the wrong number and type threw an unexpected Throwable ("
        + t + ")!");
    }
  }

  public void testLoadClass() throws Exception {
    final Class integerClass = ClassUtil.loadClass("java.lang.Integer");

    assertNotNull(integerClass);
    assertEquals(Integer.class, integerClass);
  }

  public void testLoadNoSuchClass() throws Exception {
    try {
      ClassUtil.loadClass("no.such.Class");
      fail("Calling loadClass with no such class available should have thrown a ClassNotFoundException!");
    }
    catch (ClassNotFoundException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling loadClass with no such class available threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testLoadNoSuchIntegerClass() throws Exception {
    try {
      ClassUtil.loadClass("Integer");
      fail("Calling loadClass with no such Integer class should have thrown a ClassNotFoundException!");
    }
    catch (ClassNotFoundException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling loadClass with no such Integer class threw an unexpected Throwable (" + t + ")!");
    }
  }

  private static interface D {
  }

  private static interface E {
  }

  private static interface F extends E, D {
  }

  private static class A implements F, D {
  }

  private static class B extends A {
  }

  private static class C extends B implements D {
  }

  public static interface ValueObject<T> extends Serializable {

    public T getValue();

    public void setValue(T value);

  }

  public static class ValueObjectImpl<T> implements ValueObject<T> {

    private T value;

    public ValueObjectImpl() {
    }

    public ValueObjectImpl(T value) {
      this.value = value;
    }

    private ValueObjectImpl(final ValueObject<T> valueObj) {
      Assert.notNull(valueObj, "The ValueObject instance cannot be null!");
      this.value = valueObj.getValue();
    }

    public ValueObjectImpl(final Throwable t) throws Throwable {
      throw t;
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
