/*
 * IdentifiableConverterTest.java (c) 19 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.9
 * @see com.cp.common.beans.CommonBeanTestCase
 * @see com.cp.common.beans.util.converters.IdentifiableConverter
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.CommonBeanTestCase;
import com.cp.common.beans.annotation.Default;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.NoSuchConstructorException;
import com.cp.common.util.ConversionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.Converter;

public class IdentifiableConverterTest extends CommonBeanTestCase {

  public IdentifiableConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IdentifiableConverterTest.class);
    //suite.addTest(new IdentifiableConverterTest("testName"));
    return suite;
  }

  public void testConvert() throws Exception {
    final AbstractConverter identifiableConverter = new IdentifiableConverter();

    assertNotNull(identifiableConverter);
    assertNull(identifiableConverter.getDefaultValue());
    assertFalse(identifiableConverter.isUsingDefaultValue());

    final Person expectedPerson = new PersonImpl(1);
    Person actualPerson = (Person) identifiableConverter.convert(Person.class, expectedPerson);

    assertNotNull(actualPerson);
    assertEquals(expectedPerson, actualPerson);
    assertSame(expectedPerson, actualPerson);

    actualPerson = (Person) identifiableConverter.convert(Person.class, expectedPerson.getId());

    assertNotNull(actualPerson);
    assertEquals(expectedPerson, actualPerson);
    assertNotSame(expectedPerson, actualPerson);

    actualPerson = (Person) identifiableConverter.convert(Person.class, null);

    assertNull(actualPerson);
  }

  public void testConvertWithNumberId() throws Exception {
    final AbstractConverter identifiableConverter = new IdentifiableConverter();

    assertNotNull(identifiableConverter);
    assertNull(identifiableConverter.getDefaultValue());
    assertFalse(identifiableConverter.isUsingDefaultValue());

    final Object actualPerson = identifiableConverter.convert(Person.class, 123456789l);

    assertNotNull(actualPerson);
    assertTrue(actualPerson instanceof Person);
    assertEquals(new Integer(123456789), ((Person) actualPerson).getPersonId());
  }

  public void testConvertWithStringId() throws Exception {
    final AbstractConverter identifiableConverter = new IdentifiableConverter();

    assertNotNull(identifiableConverter);
    assertNull(identifiableConverter.getDefaultValue());
    assertFalse(identifiableConverter.isUsingDefaultValue());

    final Object actualPerson = identifiableConverter.convert(Person.class, " 1248 ");

    assertNotNull(actualPerson);
    assertTrue(actualPerson instanceof Person);
    assertEquals(new Integer(1248), ((Person) actualPerson).getPersonId());
  }

  public void testConvertWithIncompatibleType() throws Exception {
    final Converter identifiableConverter = new IdentifiableConverter();
    Object convertedValue = null;

    assertNull(convertedValue);

    try {
      convertedValue = identifiableConverter.convert(Enum.class, 1);
      fail("Calling convert with an incompatiable type (JDK Enum) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(" + Enum.class.getName() + ") must extend or implement the Identifiable interface!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an incompatiable type (JDK Enum) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testConvertWithInvalidValue() throws Exception {
    final Converter identifiableConverter = new IdentifiableConverter();
    Object convertedValue = null;

    assertNull(convertedValue);

    try {
      convertedValue = identifiableConverter.convert(Person.class, "oneTwoThree");
      fail("Calling convert with an invalid Object value (oneTwoThree) should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Value (oneTwoThree) cannot be converted into an Identifiable object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an invalid Object value (oneTwoThree) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testConvertWithNullType() throws Exception {
    final Converter identifiableConverter = new IdentifiableConverter();
    Object convertedValue = null;

    assertNull(convertedValue);

    try {
      convertedValue = identifiableConverter.convert(null, 1);
      fail("Calling convert with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(convertedValue);
  }

  public void testGetArgument() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();

    assertEquals(new Byte((byte) 127), converter.getArgument(Byte.class, 127l));
    assertEquals(new Short((short) 1024), converter.getArgument(Short.class, 1024));
    assertEquals(new Integer(2), converter.getArgument(Integer.class, (short) 2));
    assertEquals(new Long(0), converter.getArgument(Long.class, (byte) 0));
  }

  public void testGetArgumentWithInvalidParameterType() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Object argument = null;

    assertNull(argument);

    try {
      argument = converter.getArgument(Double.class, Math.PI);
      fail("Calling getArgument with a parameter type of class Double and a double number value (Math.PI) should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Expected a whole number as the unique identifier parameter type; but was ("
        + Double.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getArgument with a parameter type of class Double and a double number value (Math.PI) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(argument);
  }

  public void testGetConstructorWithIdParameter() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    final Constructor constructor = converter.getConstructorWithIdParameter(PersonImpl.class);

    assertNotNull(constructor);
    assertEquals("com.cp.common.beans.CommonBeanTestCase$PersonImpl", constructor.getName());
    assertEquals(Integer.class, constructor.getParameterTypes()[0]);
  }

  public void testGetConstructorWithIdParameterUsingBeanHavingNoIdConstructor() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Constructor constructor = null;

    assertNull(constructor);

    try {
      constructor = converter.getConstructorWithIdParameter(MockBean.class);
      fail("Calling getConstructorWithIdParameter on MockBean which has no ID constructor should have thrown a NoSuchConstructorException!");
    }
    catch (NoSuchConstructorException e) {
      assertEquals("Constructor with an ID parameter does not exist for class (" + MockBean.class.getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getConstructorWithIdParameter on MockBean which has no ID constructor threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constructor);
  }

  public void testGetImplementingClass() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Class personImplClass = null;

    try {
      personImplClass = converter.getImplementingClass(Person.class);
    }
    catch (Exception e) {
      fail("Calling getImplementingClass for the Person interface threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(personImplClass);
    assertEquals(PersonImpl.class, personImplClass);
  }

  public void testGetImplementingClassNoClassFound() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Class mockInterfaceImplClass = null;

    assertNull(mockInterfaceImplClass);

    try {
      mockInterfaceImplClass = converter.getImplementingClass(MockInterface.class);
      fail("Calling getImplementingClass for MockInterface interface having no implementing class should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Unable to determine implementing class for interface (" + MockInterface.class.getName() + ")!",
        e.getMessage());
      assertTrue(e.getCause() instanceof ClassNotFoundException);
    }
    catch (Exception e) {
      fail("Calling getImplementingClass for MockInterface interface having no implementing class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(mockInterfaceImplClass);
  }

  public void testGetImplementingClassForNonInstantiableType() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Class implementingClass = null;

    assertNull(implementingClass);

    try {
      implementingClass = converter.getImplementingClass(Default.class);
      fail("Calling getImplementingClass for the Default Annotation should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Type (" + Default.class.getName() + ") is non-instantiable!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getImplementingClass for the Default Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(implementingClass);

    try {
      implementingClass = converter.getImplementingClass(MockEnum.class);
      fail("Calling getImplementingClass for the MockEnum Enum should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Type (" + MockEnum.class.getName() + ") is non-instantiable!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getImplementingClass for the MockEnum Enum threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(implementingClass);

    try {
      implementingClass = converter.getImplementingClass(Integer.TYPE);
      fail("Calling getImplementingClass for the int primitive type should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Type (" + Integer.TYPE.getName() + ") is non-instantiable!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getImplementingClass for the int primitive type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(implementingClass);
  }

  public void testGetImplementingClassForClassType() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Class implementingClass = null;

    assertNull(implementingClass);

    try {
      implementingClass = converter.getImplementingClass(PersonImpl.class);
    }
    catch (Exception e) {
      fail("Calling getImplementingClass with the PersonImpl class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(implementingClass);
    assertEquals(PersonImpl.class, implementingClass);
  }

  public void testGetTypeInstance() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Object instance = null;

    assertNull(instance);

    try {
      instance = converter.getTypeInstance(Person.class, 21);
    }
    catch (Exception e) {
      fail("Calling getTypeInstance using the Person class and Object value of 21 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(instance);
    assertTrue(instance instanceof Person);
    assertEquals(new Integer(21), ((Person) instance).getPersonId());
  }

  public void testGetTypeInstanceThrowsConversionException() throws Exception {
    final IdentifiableConverter converter = new IdentifiableConverter();
    Object instance = null;

    assertNull(instance);

    try {
      instance = converter.getTypeInstance(TestBean.class, 0);
      fail("Calling getTypeInstance using the TestBean class and Object value of 0 should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Failed to instantiate an instance of class (" + TestBean.class.getName() + ") with ID (0)!",
        e.getMessage());
      assertTrue(e.getCause() instanceof InvocationTargetException);
      assertTrue(e.getCause().getCause() instanceof UnsupportedOperationException);
      assertEquals("Not Implemented!", e.getCause().getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling getTypeInstance using the TestBean class and Object value of 0 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(instance);
  }

  public void testSetBeanProperty() throws Exception {
    final MockBean bean = new MockBean();

    assertNotNull(bean);
    assertNull(bean.getPerson());

    BeanUtil.setPropertyValue(bean, "person", 1);

    assertNotNull(bean.getPerson());
    assertEquals(new Integer(1), bean.getPerson().getPersonId());

    BeanUtil.setPropertyValue(bean, "person", null);

    assertNull(bean.getPerson());
  }

  public static final class MockBean {

    private Person person;

    public Person getPerson() {
      return person;
    }

    public void setPerson(final Person person) {
      this.person = person;
    }
  }

  public static enum MockEnum {
    ZERO,
    ONE,
    TWO
  }

  public static interface MockInterface extends Bean<Integer> {
  }

  public static final class TestBean extends AbstractBean<Integer> {

    public TestBean() {
    }

    public TestBean(final Integer id) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
