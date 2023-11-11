/*
 * BeanUtilTest.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.17
 * @see com.cp.common.beans.CommonBeanTestCase
 * @see com.cp.common.beans.util.BeanUtil
 */

package com.cp.common.beans.util;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.CommonBeanTestCase;
import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.User;
import com.cp.common.beans.annotation.BoundedDate;
import com.cp.common.beans.annotation.BoundedLength;
import com.cp.common.beans.annotation.BoundedNumber;
import com.cp.common.beans.annotation.Default;
import com.cp.common.beans.annotation.Required;
import com.cp.common.enums.State;
import com.cp.common.lang.support.AuditableVisitor;
import com.cp.common.log4j.BeanRenderer;
import com.cp.common.log4j.CalendarRenderer;
import com.cp.common.log4j.CollectionRenderer;
import com.cp.common.log4j.DefaultRenderer;
import com.cp.common.log4j.EnumRenderer;
import com.cp.common.log4j.MapRenderer;
import com.cp.common.log4j.NullRenderer;
import com.cp.common.log4j.ProcessRenderer;
import com.cp.common.log4j.UserRenderer;
import com.cp.common.test.mock.MockValueObject;
import com.cp.common.test.mock.MockValueObjectImpl;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class BeanUtilTest extends CommonBeanTestCase {

  private static final Set<String> ALL_BEAN_PROPERTIES = new HashSet<String>(17);
  private static final Set<String> READABLE_BEAN_PROPERTIES = new HashSet<String>(17);
  private static final Set<String> WRITABLE_BEAN_PROPERTIES = new HashSet<String>(17);

  static {
    ALL_BEAN_PROPERTIES.add("class");
    ALL_BEAN_PROPERTIES.add("beanHistory");
    ALL_BEAN_PROPERTIES.add("createdBy");
    ALL_BEAN_PROPERTIES.add("createdDate");
    ALL_BEAN_PROPERTIES.add("creatingProcess");
    ALL_BEAN_PROPERTIES.add("id");
    ALL_BEAN_PROPERTIES.add("lastModifiedBy");
    ALL_BEAN_PROPERTIES.add("lastModifiedDate");
    ALL_BEAN_PROPERTIES.add("modified");
    ALL_BEAN_PROPERTIES.add("modifiedBy");
    ALL_BEAN_PROPERTIES.add("modifiedDate");
    ALL_BEAN_PROPERTIES.add("modifiedProperties");
    ALL_BEAN_PROPERTIES.add("modifyingProcess");
    ALL_BEAN_PROPERTIES.add("mutable");
    ALL_BEAN_PROPERTIES.add("new");
    ALL_BEAN_PROPERTIES.add("rollbackCalled");
    ALL_BEAN_PROPERTIES.add("throwExceptionOnRollback");
    READABLE_BEAN_PROPERTIES.addAll(ALL_BEAN_PROPERTIES);
    WRITABLE_BEAN_PROPERTIES.addAll(ALL_BEAN_PROPERTIES);
    WRITABLE_BEAN_PROPERTIES.remove("class");
    WRITABLE_BEAN_PROPERTIES.remove("lastModifiedBy");
    WRITABLE_BEAN_PROPERTIES.remove("lastModifiedDate");
    WRITABLE_BEAN_PROPERTIES.remove("modified");
    WRITABLE_BEAN_PROPERTIES.remove("modifiedProperties");
    WRITABLE_BEAN_PROPERTIES.remove("new");
    WRITABLE_BEAN_PROPERTIES.remove("rollbackCalled");
    WRITABLE_BEAN_PROPERTIES.remove("throwExceptionOnRollback");
  }

  public BeanUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BeanUtilTest.class);
    //suite.addTest(new BeanUtilTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    BeanUtil.unregisterRenderer(Object.class);
    BeanUtil.unregisterRenderer(Bean.class);
  }

  protected void assertEquals(final PropertyChangeEvent expectedEvent, final PropertyChangeEvent actualEvent) {
    TestUtil.assertNullEquals(expectedEvent.getSource(), actualEvent.getSource());
    TestUtil.assertNullEquals(expectedEvent.getPropertyName(), actualEvent.getPropertyName());
    TestUtil.assertNullEquals(expectedEvent.getOldValue(), actualEvent.getOldValue());
    TestUtil.assertNullEquals(expectedEvent.getNewValue(), actualEvent.getNewValue());
  }

  public void testGetAnnotatedProperties() throws Exception {
    final Consumer consumer = new ConsumerImpl();

    final Set<String> expectedAnnotatedProperties = new TreeSet<String>();
    expectedAnnotatedProperties.add("dateOfBirth");
    expectedAnnotatedProperties.add("firstName");
    expectedAnnotatedProperties.add("lastName");
    expectedAnnotatedProperties.add("createdBy");
    expectedAnnotatedProperties.add("createdDate");
    expectedAnnotatedProperties.add("modifiedBy");
    expectedAnnotatedProperties.add("modifiedDate");

    Set<String> actualAnnotatedProperties = BeanUtil.getAnnotatedProperties(consumer, Required.class);

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    expectedAnnotatedProperties.clear();
    expectedAnnotatedProperties.add("yearBorn");

    actualAnnotatedProperties = BeanUtil.getAnnotatedProperties(consumer, BoundedNumber.class);

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    final Person person = new PersonImpl();

    expectedAnnotatedProperties.clear();
    expectedAnnotatedProperties.add("dateOfBirth");
    expectedAnnotatedProperties.add("firstName");
    expectedAnnotatedProperties.add("lastName");
    expectedAnnotatedProperties.add("createdBy");
    expectedAnnotatedProperties.add("createdDate");
    expectedAnnotatedProperties.add("modifiedBy");
    expectedAnnotatedProperties.add("modifiedDate");

    actualAnnotatedProperties = BeanUtil.getAnnotatedProperties(person, Required.class);

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    expectedAnnotatedProperties.clear();
    actualAnnotatedProperties = BeanUtil.getAnnotatedProperties(person, BoundedNumber.class);

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());

    final Object obj = new Object();

    actualAnnotatedProperties = BeanUtil.getAnnotatedProperties(obj, Required.class);

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());
  }

  public void testGetAnnotatedPropertiesWithNullAnnotation() throws Exception {
    try {
      BeanUtil.getAnnotatedProperties(new Object(), null);
      fail("Calling getAnnotatedProperties with a null Annotation should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Annotation cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getAnnotatedProperties with a null Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetAnnotatedPropertiesWithNullBean() throws Exception {
    try {
      BeanUtil.getAnnotatedProperties(null, Required.class);
      fail("Calling getAnnotatedProperties with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The annotated bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getAnnotatedProperties with a null bean Object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testIsAnnotationPresentWithNonNullMethod() throws Exception {
    final Method setDateOfBirth = PersonImpl.class.getMethod("setDateOfBirth", Calendar.class);

    assertNotNull(setDateOfBirth);

    try {
      assertTrue(BeanUtil.isAnnotationPresent(setDateOfBirth, Required.class));
    }
    catch (Exception e) {
      fail("Calling isAnnotationPresent with setDateOfBirth Method and the Required Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testIsAnnotationPresentWithNonNullMethodHavingNoAnnotations() throws Exception {
    final Method getDateOfBirth = PersonImpl.class.getMethod("getDateOfBirth", (Class[]) null);

    assertNotNull(getDateOfBirth);

    try {
      assertFalse(BeanUtil.isAnnotationPresent(getDateOfBirth, Required.class));
    }
    catch (Exception e) {
      fail("Calling isAnnotationPresent with getDateOfBirth Method and the Required Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testIsAnnotationPresentWithNullMethod() throws Exception {
    try {
      assertFalse(BeanUtil.isAnnotationPresent(null, Required.class));
    }
    catch (Exception e) {
      fail("Calling isAnnotationPresent with a null Method object and the Required Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetBeanInfo() throws Exception {
    final Person person = getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");

    assertNotNull(person);

    final BeanInfo personBeanInfo = BeanUtil.getBeanInfo(person);

    assertNotNull(personBeanInfo);

    final BeanDescriptor personBeanDescriptor = personBeanInfo.getBeanDescriptor();

    assertNotNull(personBeanDescriptor);
    assertEquals(PersonImpl.class, personBeanDescriptor.getBeanClass());
  }

  public void testGetBeanInfoWithNullBean() throws Exception {
    try {
      BeanUtil.getBeanInfo(null);
      fail("Calling getBeanInfo with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBeanInfo with a null bean Object threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetBoundedDateProperties() throws Exception {
    final Set<String> expectedAnnotatedProperties = new TreeSet<String>();
    expectedAnnotatedProperties.add("propertyOne");
    expectedAnnotatedProperties.add("propertyThree");

    Set<String> actualAnnotatedProperties = BeanUtil.getBoundedDateProperties(new TestAnnotatedBean());

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    actualAnnotatedProperties = BeanUtil.getBoundedDateProperties(new Object());

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());
  }

  public void testGetBoundedLengthProperties() throws Exception {
    final Set<String> expectedAnnotatedProperties = new TreeSet<String>();
    expectedAnnotatedProperties.add("propertyTwo");
    expectedAnnotatedProperties.add("propertyThree");

    Set<String> actualAnnotatedProperties = BeanUtil.getBoundedLengthProperties(new TestAnnotatedBean());

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    actualAnnotatedProperties = BeanUtil.getBoundedLengthProperties(new Object());

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());
  }

  public void testGetBoundedNumberProperties() throws Exception {
    final Set<String> expectedAnnotatedProperties = new TreeSet<String>();
    expectedAnnotatedProperties.add("propertyOne");
    expectedAnnotatedProperties.add("propertyTwo");

    Set<String> actualAnnotatedProperties = BeanUtil.getBoundedNumberProperties(new TestAnnotatedBean());

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    actualAnnotatedProperties = BeanUtil.getBoundedNumberProperties(new Object());

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());
  }

  public void testGetDefaultedProperties() throws Exception {
    final Set<String> expectedAnnotatedProperties = new TreeSet<String>();
    expectedAnnotatedProperties.add("propertyTwo");

    Set<String> actualAnnotatedProperties = BeanUtil.getDefaultedProperties(new TestAnnotatedBean());

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    actualAnnotatedProperties = BeanUtil.getDefaultedProperties(new Object());

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());
  }

  public void testGetIndex() throws Exception {
    assertEquals(-1, BeanUtil.getIndex(null));
    assertEquals(-1, BeanUtil.getIndex(""));
    assertEquals(-1, BeanUtil.getIndex(" "));
    assertEquals(-1, BeanUtil.getIndex("propertyName"));
    assertEquals(-1, BeanUtil.getIndex("propertyName(0)"));
    assertEquals(-1, BeanUtil.getIndex("propertyName{1}"));
    assertEquals(2, BeanUtil.getIndex("propertyName[2]"));
    assertEquals(1, BeanUtil.getIndex("propertyName[-1]"));
  }

  public void testGetIndexThrowsNumberFormatException() throws Exception {
    int index = 0;

    assertEquals(0, index);

    try {
      index = BeanUtil.getIndex("propertyName[oneTwoThree]");
      fail("Calling getIndex with indexed property (propertyName[oneTwoThree]) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getIndex with indexed property (propertyName[oneTwoThree]) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(0, index);
  }

  public void testIsIndexedProperty() throws Exception {
    assertTrue(BeanUtil.isIndexedProperty("members[0]"));
    assertTrue(BeanUtil.isIndexedProperty("members[000]"));
    assertTrue(BeanUtil.isIndexedProperty("values[9]"));
    assertFalse(BeanUtil.isIndexedProperty("values[O]"));
    assertFalse(BeanUtil.isIndexedProperty("values[]"));
    assertFalse(BeanUtil.isIndexedProperty("values[one]"));
    assertFalse(BeanUtil.isIndexedProperty("values[-2]"));
    assertFalse(BeanUtil.isIndexedProperty("values[2"));
    assertFalse(BeanUtil.isIndexedProperty("values2]"));
    assertFalse(BeanUtil.isIndexedProperty("values(2)"));
    assertFalse(BeanUtil.isIndexedProperty("values{2}"));
    assertFalse(BeanUtil.isIndexedProperty("values2"));
    assertFalse(BeanUtil.isIndexedProperty("[2]"));
    assertFalse(BeanUtil.isIndexedProperty("value@[2]"));
    assertFalse(BeanUtil.isIndexedProperty(" "));
    assertFalse(BeanUtil.isIndexedProperty(""));
    assertFalse(BeanUtil.isIndexedProperty(null));
  }

  public void testIsNestedProperty() throws Exception {
    assertTrue(BeanUtil.isNestedProperty("abc.def.ghi"));
    assertTrue(BeanUtil.isNestedProperty("ABC.DEF.GHI"));
    assertTrue(BeanUtil.isNestedProperty("ABC.Def.ghi"));
    assertTrue(BeanUtil.isNestedProperty("a9c.$ef._hi"));
    assertTrue(BeanUtil.isNestedProperty("one.two"));
    assertTrue(BeanUtil.isNestedProperty("$._"));
    assertFalse(BeanUtil.isNestedProperty("*.*"));
    assertFalse(BeanUtil.isNestedProperty("1.two"));
    assertFalse(BeanUtil.isNestedProperty("one."));
    assertFalse(BeanUtil.isNestedProperty(".two"));
    assertFalse(BeanUtil.isNestedProperty("zero. .two"));
    assertFalse(BeanUtil.isNestedProperty("value"));
    assertFalse(BeanUtil.isNestedProperty("@9c.93f.g41"));
    assertFalse(BeanUtil.isNestedProperty(" "));
    assertFalse(BeanUtil.isNestedProperty(""));
    assertFalse(BeanUtil.isNestedProperty(null));
    assertTrue(BeanUtil.isNestedProperty("list[0].value"));
    assertTrue(BeanUtil.isNestedProperty("list[0].array[1]"));
    assertTrue(BeanUtil.isNestedProperty("value.list[1]"));
  }

  public void testGetAllPropertiesForPerson() throws Exception {
    final Set<String> expectedProperties = new HashSet<String>(5);
    expectedProperties.addAll(ALL_BEAN_PROPERTIES);
    expectedProperties.add("personId");
    expectedProperties.add("firstName");
    expectedProperties.add("lastName");
    expectedProperties.add("dateOfBirth");
    expectedProperties.add("ssn");

    final Person person = new PersonImpl();
    final Set<String> actualProperties = BeanUtil.getAllProperties(person);

    assertNotNull(actualProperties);
    assertEquals(expectedProperties.size(), actualProperties.size());
    assertTrue(actualProperties.containsAll(expectedProperties));
  }

  public void testGetAllPropertiesForTestBean() throws Exception {
    final Set<String> expectedProperties = new HashSet<String>(11);
    expectedProperties.addAll(ALL_BEAN_PROPERTIES);
    expectedProperties.add("errorableProperty");
    expectedProperties.add("readPrivateProperty");
    expectedProperties.add("readProtectedProperty");
    expectedProperties.add("readableWritableProperty");
    expectedProperties.add("unreadableProperty");
    expectedProperties.add("unwritableProperty");
    expectedProperties.add("writePrivateProperty");
    expectedProperties.add("writeProtectedProperty");

    final Bean<Integer> testBean = new TestBean("test");
    final Set<String> actualProperties = BeanUtil.getAllProperties(testBean);

    assertNotNull(actualProperties);
    assertEquals(expectedProperties.size(), actualProperties.size());
    assertTrue(actualProperties.containsAll(expectedProperties));
  }

  public void testGetAllPropertiesWithNullBean() throws Exception {
    try {
      BeanUtil.getAllProperties(null);
      fail("Calling getAllProperties with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getAllProperties with a null bean Object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetReadableProperties() throws Exception {
    final Set<String> expectedProperties = new HashSet<String>(7);
    expectedProperties.addAll(READABLE_BEAN_PROPERTIES);
    expectedProperties.add("class");
    expectedProperties.add("errorableProperty");
    expectedProperties.add("readableWritableProperty");
    expectedProperties.add("unwritableProperty");
    expectedProperties.add("writePrivateProperty");
    expectedProperties.add("writeProtectedProperty");

    final Bean<Integer> testBean = new TestBean("test");
    final Set<String> actualProperties = BeanUtil.getReadableProperties(testBean);

    assertNotNull(actualProperties);
    assertEquals(expectedProperties.size(), actualProperties.size());
    assertTrue(actualProperties.containsAll(expectedProperties));
  }

  public void testGetReadablePropertiesWithNullBean() throws Exception {
    try {
      BeanUtil.getReadableProperties(null);
      fail("Calling getReadableProperties with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getReadableProperties with a null bean Object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testGetWritableProperties() throws Exception {
    final Set<String> expectedProperties = new HashSet<String>(5);
    expectedProperties.addAll(WRITABLE_BEAN_PROPERTIES);
    expectedProperties.add("errorableProperty");
    expectedProperties.add("readPrivateProperty");
    expectedProperties.add("readProtectedProperty");
    expectedProperties.add("readableWritableProperty");
    expectedProperties.add("unreadableProperty");

    final Bean<Integer> testBean = new TestBean("test");
    final Set<String> actualProperties = BeanUtil.getWritableProperties(testBean);

    assertNotNull(actualProperties);
    assertEquals(expectedProperties.size(), actualProperties.size());
    assertTrue(actualProperties.containsAll(expectedProperties));
  }

  public void testGetWritablePropertiesWithNullBean() throws Exception {
    try {
      BeanUtil.getWritableProperties(null);
      fail("Calling getWritableProperties with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getWritableProperties with a null bean Object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testGetPropertyChangeEvent() throws Exception {
    final Object source = new Object();

    final PropertyChangeEvent expectedEvent = new PropertyChangeEvent(source, "someProperty", "old", "new");
    final PropertyChangeEvent actualEvent = BeanUtil.getPropertyChangeEvent(source, "someProperty", "old", "new");

    assertNotNull(actualEvent);
    assertEquals(expectedEvent, actualEvent);
  }

  public void testGetPropertyDescriptor() throws Exception {
    final Person person = getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");

    assertNotNull(person);

    final PropertyDescriptor idPropertyDescriptor = BeanUtil.getPropertyDescriptor(person, "id");

    assertNotNull(idPropertyDescriptor);
    assertEquals("id", idPropertyDescriptor.getName());
    assertEquals(Comparable.class, idPropertyDescriptor.getPropertyType());
    assertEquals("getId", idPropertyDescriptor.getReadMethod().getName());
    assertEquals("setId", idPropertyDescriptor.getWriteMethod().getName());

    final PropertyDescriptor lastNamePropertyDescriptor = BeanUtil.getPropertyDescriptor(person, "lastName");

    assertNotNull(lastNamePropertyDescriptor);
    assertEquals("lastName", lastNamePropertyDescriptor.getName());
    assertEquals(String.class, lastNamePropertyDescriptor.getPropertyType());
    assertEquals("getLastName", lastNamePropertyDescriptor.getReadMethod().getName());
    assertEquals("setLastName", lastNamePropertyDescriptor.getWriteMethod().getName());

    final PropertyDescriptor dateOfBirthPropertyDescriptor = BeanUtil.getPropertyDescriptor(person, "dateOfBirth");

    assertNotNull(dateOfBirthPropertyDescriptor);
    assertEquals("dateOfBirth", dateOfBirthPropertyDescriptor.getName());
    assertEquals(Calendar.class, dateOfBirthPropertyDescriptor.getPropertyType());
    assertEquals("getDateOfBirth", dateOfBirthPropertyDescriptor.getReadMethod().getName());
    assertEquals("setDateOfBirth", dateOfBirthPropertyDescriptor.getWriteMethod().getName());

    final PropertyDescriptor personIdPropertyDescriptor = BeanUtil.getPropertyDescriptor(person, "personId");

    assertNotNull(personIdPropertyDescriptor);
    assertEquals("personId", personIdPropertyDescriptor.getName());
    assertEquals(Integer.class, personIdPropertyDescriptor.getPropertyType());
    assertEquals("getPersonId", personIdPropertyDescriptor.getReadMethod().getName());
    assertEquals("setPersonId", personIdPropertyDescriptor.getWriteMethod().getName());
  }

  public void testGetPropertyDescriptorWithNullBean() throws Exception {
    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(null, "value");
      fail("Calling getPropertyDescriptor with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor with a null bean Object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDescriptor);
  }

  public void testGetPropertyDescriptorWithEmptyProperty() throws Exception {
    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(new Object(), " ");
      fail("Calling getPropertyDescriptor with an empty property should have thrown a IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor with an empty property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDescriptor);
  }

  public void testGetPropertyDescriptorWithNullProperty() throws Exception {
    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(new Object(), null);
      fail("Calling getPropertyDescriptor with a null property should have thrown a IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor with a null property threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(propertyDescriptor);
  }

  public void testGetPropertyDescriptorWithNonExistingProperty() throws Exception {
    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(new Object(), "value");
      fail("Calling getPropertyDescriptor on a bean for a non-existing property (value) should have thrown a NoSuchPropertyException!");
    }
    catch (NoSuchPropertyException e) {
      assertEquals("(value) is not a property of bean (java.lang.Object)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor on a bean for a non-existing property (value) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDescriptor);
  }

  public void testGetPropertyDescriptorWithIndexedProperty() throws Exception {
    final MockValueObject valueObjectOne = new TestValueObjectImpl<Integer>(1);
    final MockValueObject valueObjectTwo = new TestValueObjectImpl<Integer>(2);
    final MockValueObject testValueObject = new TestValueObjectImpl<String>("test", valueObjectOne, valueObjectTwo);

    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(testValueObject, "valueObjectList[1]");
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor on an indexed property (valueObjectList) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyDescriptor);
    assertEquals("valueObjectList", propertyDescriptor.getName());
    assertEquals(List.class, propertyDescriptor.getPropertyType());
    assertEquals("getValueObjectList", propertyDescriptor.getReadMethod().getName());
    assertNull(propertyDescriptor.getWriteMethod());
  }

  public void testGetPropertyDescriptorWithNestedProperty() throws Exception {
    final MockValueObject valueObjectOne = new TestValueObjectImpl<Boolean>(true);
    final MockValueObject valueObjectTwo = new TestValueObjectImpl<Integer>(0, valueObjectOne);

    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(valueObjectTwo, "valueObject.value");
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor on bean with nested property value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyDescriptor);
    assertEquals("value", propertyDescriptor.getName());
    assertEquals(Object.class, propertyDescriptor.getPropertyType());
    assertEquals("getValue", propertyDescriptor.getReadMethod().getName());
    assertEquals("setValue", propertyDescriptor.getWriteMethod().getName());
  }

  public void testGetPropertyDescriptorWithNestedPropertyHavingNullReference() throws Exception {
    final MockValueObject valueObjectZero = new TestValueObjectImpl<String>("zero");
    final MockValueObject valueObjectOne = new TestValueObjectImpl<String>("one", valueObjectZero);
    final MockValueObject valueObjectTwo = new TestValueObjectImpl<String>("two", valueObjectOne);

    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = BeanUtil.getPropertyDescriptor(valueObjectTwo, "valueObject.valueObject.valueObject.value");
      fail("Calling getPropertyDescriptor on a series of composed beans leading to null should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The value of property (valueObject) on bean (" + valueObjectZero.getClass().getName()
        + ") cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyDescriptor on a series of composed beans leading to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDescriptor);
  }

  public void testGetPropertyValue() throws Exception {
    final Calendar dateOfBirth = DateUtil.getCalendar(1991, Calendar.NOVEMBER, 11, 10, 30, 15, 0);
    final Calendar changedDate = DateUtil.getCalendar(2008, Calendar.NOVEMBER, 1, 23, 36, 30, 0);

    final User blumj = getUser("blumj");

    final Person person = new PersonImpl();
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setDateOfBirth(dateOfBirth);
    person.setSsn("333-22-4444");
    person.accept(AuditableVisitor.getInstance(blumj, changedDate));

    assertNull(BeanUtil.getPropertyValue(person, "personId"));
    assertEquals("Jon", BeanUtil.getPropertyValue(person, "firstName"));
    assertEquals("Bloom", BeanUtil.getPropertyValue(person, "lastName"));
    assertEquals(dateOfBirth, BeanUtil.getPropertyValue(person, "dateOfBirth"));
    assertEquals("333-22-4444", BeanUtil.getPropertyValue(person, "ssn"));
    assertEquals(blumj, BeanUtil.getPropertyValue(person, "createdBy"));
    assertEquals(changedDate, BeanUtil.getPropertyValue(person, "createdDate"));
    assertNull(BeanUtil.getPropertyValue(person, "creatingProcess"));
    assertNull(BeanUtil.getPropertyValue(person, "lastModifiedBy"));
    assertNull(BeanUtil.getPropertyValue(person, "lastModifiedDate"));
    assertEquals(Boolean.TRUE, BeanUtil.getPropertyValue(person, "modified"));
    assertEquals(blumj, BeanUtil.getPropertyValue(person, "modifiedBy"));
    assertEquals(changedDate, BeanUtil.getPropertyValue(person, "modifiedDate"));
    assertNull(BeanUtil.getPropertyValue(person, "modifyingProcess"));
    assertEquals(Boolean.TRUE, BeanUtil.getPropertyValue(person, "mutable"));
    assertEquals(Boolean.TRUE, BeanUtil.getPropertyValue(person, "new"));
    assertEquals(Boolean.FALSE, BeanUtil.getPropertyValue(person, "rollbackCalled"));
    assertEquals(person.getClass(), BeanUtil.getPropertyValue(person, "class"));

    person.setPersonId(1);

    assertEquals(new Integer(1), BeanUtil.getPropertyValue(person, "personId"));
    assertEquals(Boolean.TRUE, BeanUtil.getPropertyValue(person, "modified"));
    assertEquals(Boolean.FALSE, BeanUtil.getPropertyValue(person, "new"));

    person.commit();

    assertEquals(blumj, BeanUtil.getPropertyValue(person, "lastModifiedBy"));
    assertEquals(changedDate, BeanUtil.getPropertyValue(person, "lastModifiedDate"));
    assertEquals(Boolean.FALSE, BeanUtil.getPropertyValue(person, "modified"));
    assertEquals(Boolean.FALSE, BeanUtil.getPropertyValue(person, "new"));
  }

  public void testGetPropertyValueWithNullBean() throws Exception {
    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(null, "value");
      fail("Calling getPropertyValue with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with a null bean Object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValueWithEmptyProperty() throws Exception {
    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(new Object(), " ");
      fail("Calling getPropertyValue with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with an empty property threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValueWithNullProperty() throws Exception {
    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(new Object(), null);
      fail("Calling getPropertyValue with a null property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with a null property threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValueWithIndexedProperty() throws Exception {
    final MockValueObject valueObjectZero = new TestValueObjectImpl<Integer>(0);
    final MockValueObject valueObjectOne = new TestValueObjectImpl<Integer>(1);
    final MockValueObject valueObjectTwo = new TestValueObjectImpl<Integer>(2);
    final MockValueObject valueObject = new TestValueObjectImpl<String>("test", valueObjectZero, valueObjectOne, valueObjectTwo);

    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(valueObject, "valueObjectList[1]");
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with an indexed property (valueObejctList[1]) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertSame(valueObjectOne, value);
  }

  public void testGetPropertyValueWithNestedProperty() throws Exception {
    final MockValueObject valueObjectZero = new TestValueObjectImpl<Integer>(0);
    final MockValueObject valueObjectOne = new TestValueObjectImpl<Integer>(1, valueObjectZero);
    final MockValueObject valueObjectTwo = new TestValueObjectImpl<Integer>(2, valueObjectOne);

    Integer value = null;

    try {
      value = BeanUtil.getPropertyValue(valueObjectTwo, "valueObject.valueObject.value");
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with a nested property (valueObject.valueObject.value) threw an unexpected Excetpion ("
        + e.getMessage() + ")!");
    }

    assertNotNull(value);
    assertEquals(new Integer(0), value);
  }

  public void testGetPropertyValueForNestedIndexedProperty() throws Exception {
    final MockValueObject valueObjectZero = new TestValueObjectImpl<Integer>(0);
    final MockValueObject valueObjectOne = new TestValueObjectImpl<Integer>(1);
    final MockValueObject valueObjectTwo = new TestValueObjectImpl<Integer>(2, valueObjectZero);
    final MockValueObject valueObjectThree = new TestValueObjectImpl<Integer>(3, valueObjectOne, valueObjectTwo);
    final MockValueObject valueObjectFour = new TestValueObjectImpl<Integer>(4);
    final MockValueObject valueObjectFive = new TestValueObjectImpl<Integer>(5);
    final MockValueObject valueObjectSix = new TestValueObjectImpl<Integer>(6, valueObjectFour, valueObjectFive);
    final MockValueObject valueObjectSeven = new TestValueObjectImpl<Integer>(7, valueObjectSix);
    final MockValueObject valueObjectEight = new TestValueObjectImpl<Integer>(8, valueObjectThree, valueObjectSeven);
    final MockValueObject valueObjectNine = new TestValueObjectImpl<Integer>(9, valueObjectEight);

    Integer value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(valueObjectNine, "valueObject.valueObjectList[0].valueObjectList[1].valueObject.value");
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with a nested, indexed property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(value);
    assertEquals(new Integer(0), value);

    try {
      value = BeanUtil.getPropertyValue(valueObjectNine, "valueObject.valueObjectList[1].valueObject.valueObjectList[0].value");
    }
    catch (Exception e) {
      fail("Calling getPropertyValue with a nested, indexed property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(value);
    assertEquals(new Integer(4), value);
  }

  public void testGetPropertyValueForNonNestedPropertyWithNestedProperty() throws Exception {
    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValueForNonNestedProperty(new Object(), "nested.property");
      fail("Calling getPropertyValueForNonNestedProperty with a nested property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The property (nested.property) should not be nested!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValueForNonNestedProperty with a nested property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValueForNonIndexedPropertyUsingIndex() throws Exception {
    final MockValueObject valueObject = new TestValueObjectImpl<String>("test");

    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(valueObject, "value[0]");
      fail("Calling getPropertyValue on non-indexed property (value) using index should have thrown a FailedToReadPropertyException!");
    }
    catch (FailedToReadPropertyException e) {
      assertEquals("Failed to read property (value[0]) of bean (" + valueObject.getClass().getName() + ")!",
        e.getMessage());
      assertTrue(e.getCause() instanceof IllegalArgumentException);
      assertEquals("The value of property (value[0]) on bean (" + valueObject.getClass().getName()
        + ") is not an array or Collection type!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValue on non-indexed property (value) using index threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValueFailsToReadProperty() throws Exception {
    final Bean bean = new TestBean("test");
    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(bean, "errorableProperty");
      fail("Calling getPropertyValue on TestBean for the errorableProperty should have thrown a FailedToReadPropertyException!");
    }
    catch (FailedToReadPropertyException e) {
      assertEquals("Failed to read property (errorableProperty) of bean (" + bean.getClass().getName() + ")!",
        e.getMessage());
      assertTrue(e.getCause() instanceof IllegalStateException);
      assertEquals("errorableProperty not set!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValue on TestBean for the errorableProperty threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValueForUnreadableProperty() throws Exception {
    final Bean bean = new TestBean("test");
    Object value = null;

    assertNull(value);

    try {
      value = BeanUtil.getPropertyValue(bean, "unreadableProperty");
      fail("Calling getPropertyValue for an unreadable property should have thrown a UnreadablePropertyException!");
    }
    catch (UnreadablePropertyException e) {
      assertEquals("Property (unreadableProperty) of bean (" + bean.getClass().getName() + ") cannot be read!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValue for an unreadable property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetPropertyValues() throws Exception {
    final Calendar createdDateTime = DateUtil.getCalendar(2006, Calendar.MAY, 14, 8, 7, 30, 512);
    final Calendar modifiedDateTime = DateUtil.getCalendar(2008, Calendar.NOVEMBER, 2, 1, 21, 15, 500);

    final User createdBy = getUser("root");
    final User modifiedBy = getUser("blumj");

    final Person person = new PersonImpl();
    person.setDateOfBirth(DateUtil.getCalendar(1974, Calendar.MAY, 27, 1, 2, 30, 0));
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setSsn("333-22-4444");
    person.accept(AuditableVisitor.getInstance(createdBy, createdDateTime));
    person.commit();
    person.setPersonId(1);
    person.accept(AuditableVisitor.getInstance(modifiedBy, modifiedDateTime));
    person.commit();

    final Map<String, Object> expectedPropertyValueMap = new TreeMap<String, Object>();
    expectedPropertyValueMap.put("personId", person.getPersonId());
    expectedPropertyValueMap.put("dateOfBirth", person.getDateOfBirth());
    expectedPropertyValueMap.put("firstName", person.getFirstName());
    expectedPropertyValueMap.put("lastName", person.getLastName());
    expectedPropertyValueMap.put("ssn", person.getSsn());
    expectedPropertyValueMap.put("beanHistory", person.getBeanHistory());
    expectedPropertyValueMap.put("createdBy", person.getCreatedBy());
    expectedPropertyValueMap.put("createdDateTime", person.getCreatedDateTime());
    expectedPropertyValueMap.put("creatingProcess", person.getCreatingProcess());
    expectedPropertyValueMap.put("id", person.getId());
    expectedPropertyValueMap.put("lastModifiedBy", person.getLastModifiedBy());
    expectedPropertyValueMap.put("lastModifiedDate", person.getLastModifiedDateTime());
    expectedPropertyValueMap.put("modified", person.isModified());
    expectedPropertyValueMap.put("modifiedBy", person.getModifiedBy());
    expectedPropertyValueMap.put("modifiedDateTime", person.getModifiedDateTime());
    expectedPropertyValueMap.put("modifiedProperties", person.getModifiedProperties());
    expectedPropertyValueMap.put("modifyingProcess", person.getModifyingProcess());
    expectedPropertyValueMap.put("mutable", person.isMutable());
    expectedPropertyValueMap.put("new", person.isNew());
    expectedPropertyValueMap.put("rollbackCalled", person.isRollbackCalled());
    expectedPropertyValueMap.put("throwExceptionOnRollback", person.isThrowExceptionOnRollback());
    expectedPropertyValueMap.put("class", person.getClass());

    final Map<String, Object> actualPropertyValueMap = BeanUtil.getPropertyValues(person);

    assertNotNull(actualPropertyValueMap);
    assertEquals(expectedPropertyValueMap, actualPropertyValueMap);
  }

  public void testGetPropertyValuesWithNullBean() throws Exception {
    Map<String, Object> propertyValueMap = null;

    assertNull(propertyValueMap);

    try {
      propertyValueMap = BeanUtil.getPropertyValues(null);
      fail("Calling getPropertyValues with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValues with a null bean Object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(propertyValueMap);
  }

  public void testGetPropertyValuesFailsToReadProperty() throws Exception {
    final Bean bean = new TestBean("test");
    Map<String, Object> propertyValueMap = null;

    assertNull(propertyValueMap);

    try {
      propertyValueMap = BeanUtil.getPropertyValues(bean);
      fail("Calling getPropertyValues on TestBean with an unreadable property should have thrown a FailedToReadPropertyException!");
    }
    catch (FailedToReadPropertyException e) {
      assertEquals("Failed to read property (errorableProperty) of bean (" + bean.getClass().getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getPropertyValues on TestBean with an unreadable property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyValueMap);
  }

  public void testGetRequiredProperties() throws Exception {
    final Set<String> expectedAnnotatedProperties = new TreeSet<String>();
    expectedAnnotatedProperties.add("propertyOne");
    expectedAnnotatedProperties.add("propertyTwo");
    expectedAnnotatedProperties.add("propertyThree");

    Set<String> actualAnnotatedProperties = BeanUtil.getRequiredProperties(new TestAnnotatedBean());

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);

    actualAnnotatedProperties = BeanUtil.getRequiredProperties(new Object());

    assertNotNull(actualAnnotatedProperties);
    assertTrue(actualAnnotatedProperties.isEmpty());

    expectedAnnotatedProperties.clear();
    expectedAnnotatedProperties.add("dateOfBirth");
    expectedAnnotatedProperties.add("firstName");
    expectedAnnotatedProperties.add("lastName");
    expectedAnnotatedProperties.add("createdBy");
    expectedAnnotatedProperties.add("createdDate");
    expectedAnnotatedProperties.add("modifiedBy");
    expectedAnnotatedProperties.add("modifiedDate");

    actualAnnotatedProperties = BeanUtil.getRequiredProperties(new PersonImpl());

    assertNotNull(actualAnnotatedProperties);
    assertEquals(expectedAnnotatedProperties, actualAnnotatedProperties);
  }

  public void testSetPropertyValue() throws Exception {
    final Person person = new PersonImpl();

    assertNull(person.getPersonId());
    assertNull(person.getFirstName());
    assertNull(person.getLastName());
    assertNull(person.getDateOfBirth());
    assertNull(person.getSsn());

    final Calendar dateOfBirth = DateUtil.getCalendar(1991, Calendar.APRIL, 1, 5, 15, 30, 555);

    BeanUtil.setPropertyValue(person, "personId", 1);
    BeanUtil.setPropertyValue(person, "firstName", "Jon");
    BeanUtil.setPropertyValue(person, "lastName", "Bloom");
    BeanUtil.setPropertyValue(person, "dateOfBirth", dateOfBirth);
    BeanUtil.setPropertyValue(person, "ssn", "333-22-4444");

    assertEquals(new Integer(1), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Bloom", person.getLastName());
    assertEquals(dateOfBirth, person.getDateOfBirth());
    assertEquals("333-22-4444", person.getSsn());

    final Calendar newDateOfBirth = DateUtil.getCalendar(1988, Calendar.OCTOBER, 31, 13, 31, 13, 111);

    BeanUtil.setPropertyValue(person, "personId", 2);
    BeanUtil.setPropertyValue(person, "dateOfBirth", newDateOfBirth);
    BeanUtil.setPropertyValue(person, "ssn", null);

    assertEquals(new Integer(2), person.getPersonId());
    assertEquals(newDateOfBirth, person.getDateOfBirth());
    assertNull(person.getSsn());
  }

  public void testSetPropertyValueWithNullBean() throws Exception {
    try {
      BeanUtil.setPropertyValue(null, "value", "test");
      fail("Calling setPropertyValue with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue  with a null bean Object threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testSetPropertyValueWithEmptyProperty() throws Exception {
    try {
      BeanUtil.setPropertyValue(new Object(), " ", "test");
      fail("Calling setPropertyValue with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue with an empty property threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testSetPropertyValueWithNullProperty() throws Exception {
    try {
      BeanUtil.setPropertyValue(new Object(), null, "test");
      fail("Calling setPropertyValue with a null property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue with a null property threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testSetPropertyValueFailsToWriteProperty() throws Exception {
    final Bean bean = new TestBean("test");

    try {
      BeanUtil.setPropertyValue(bean, "errorableProperty", "test");
      fail("Calling setPropertyValue on TestBean for property (errorableProperty) should have thrown a FailedToWritePropertyException!");
    }
    catch (FailedToWritePropertyException e) {
      assertEquals("Failed to write property (errorableProperty) of type (java.lang.Object) on bean ("
        + bean.getClass().getName() + ") with value (test)!", e.getMessage());
      assertTrue(e.getCause() instanceof UnsupportedOperationException);
      assertEquals("setting errorableProperty not implemented!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue on TestBean for property (errorableProperty) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSetPropertyValueForUnwritableProperty() throws Exception {
    final TestBean bean = new TestBean("test");

    assertEquals("test", bean.getUnwritableProperty());

    try {
      BeanUtil.setPropertyValue(bean, "unwritableProperty", null);
      fail("Calling setPropertyValue on TestBean for an unwritable property should have thrown an UnwritablePropertyException!");
    }
    catch (UnwritablePropertyException e) {
      assertEquals("Cannot set value (null) of property (unwritableProperty) on bean (" + bean.getClass().getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue on TestBean for an unwritable property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("test", bean.getUnwritableProperty());
  }

  public void testSetPropertyValueForConstrainedProperty() throws Exception {
    final Calendar dateOfBirth = DateUtil.getCalendar(2008, Calendar.NOVEMBER, 8, 0, 34, 30, 500);
    final Person person = getPerson(1, "Jon", "Bloom", dateOfBirth, "333-22-4444");

    assertEquals(dateOfBirth, person.getDateOfBirth());

    try {
      BeanUtil.setPropertyValue(person, "dateOfBirth", null);
      fail("Calling setPropertyValue with a null value for a required bean property should have thrown an FailedToWritePropertyException!");
    }
    catch (FailedToWritePropertyException e) {
      assertEquals("Failed to write property (dateOfBirth) of type (java.util.Calendar) on bean ("
        + person.getClass().getName() + ") with value (null)!", e.getMessage());
      assertTrue(e.getCause() instanceof ConstraintViolationException);
      assertEquals("The property (dateOfBirth) of bean (" + person.getClass().getName() + ") is required!",
        e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue with a null value for a required bean property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(dateOfBirth, person.getDateOfBirth());
  }

  public void testSetPropertyValueWithConversion() throws Exception {
    final Person person = new PersonImpl();

    assertNull(person.getPersonId());
    assertNull(person.getFirstName());
    assertNull(person.getLastName());
    assertNull(person.getDateOfBirth());
    assertNull(person.getSsn());

    try {
      BeanUtil.setPropertyValue(person, "personId", "1");
      BeanUtil.setPropertyValue(person, "firstName", "Jon");
      BeanUtil.setPropertyValue(person, "lastName", "Bloom");
      BeanUtil.setPropertyValue(person, "dateOfBirth", "05/27/1974");
      BeanUtil.setPropertyValue(person, "ssn", 333224444);
    }
    catch (Exception e) {
      fail("Calling setPropertyValue for various properties of the Person class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(new Integer(1), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Bloom", person.getLastName());
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27), person.getDateOfBirth());
    assertEquals("333224444", person.getSsn());
  }

  public void testSetPropertyValueForNonNestedNonIndexedPropertyWithANestedIndexedProperty() throws Exception {
    try {
      BeanUtil.setPropertyValueForNonNestedNonIndexedProperty(new Object(), "array[0].value", "test");
      fail("Calling setPropertyValueForNonNestedNonIndexedProperty with an indexed, nested property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Cannot set the value of an indexed or nested property (array[0].value)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValueForNonNestedNonIndexedProperty with an indexed, nested property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSetPropertyValueForNestedProperty() throws Exception {
    final TestValueObjectImpl<Boolean> nestedValueObject = new TestValueObjectImpl<Boolean>();
    final TestValueObjectImpl<String> valueObject = new TestValueObjectImpl<String>("test", nestedValueObject);

    assertEquals("test", valueObject.getValue());
    assertSame(nestedValueObject, valueObject.getValueObject());
    assertNull(nestedValueObject.getValue());

    try {
      BeanUtil.setPropertyValue(valueObject, "valueObject.value", Boolean.TRUE);
    }
    catch (Exception e) {
      fail("Calling setPropertyValue on nested property (valueObject.value) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("test", valueObject.getValue());
    assertSame(nestedValueObject, valueObject.getValueObject());
    assertTrue(nestedValueObject.getValue());
  }

  public void testSetPropertyValueForNestedPropertyHavingNullReference() throws Exception {
    final TestValueObjectImpl<String> valueObject = new TestValueObjectImpl<String>("test");

    assertEquals("test", valueObject.getValue());
    assertNull(valueObject.getValueObject());

    try {
      BeanUtil.setPropertyValue(valueObject, "valueObject.value", Boolean.TRUE);
      fail("Calling setPropertyValue on a nested property of a bean Object having a null reference should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The value of property (valueObject) on bean (" + valueObject.getClass().getName()
        + ") cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValue on a nested property of a bean Object having a null reference threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("test", valueObject.getValue());
    assertNull(valueObject.getValueObject());
  }

  public void testSetPropertyValueForNestedIndexedProperty() throws Exception {
    final TestValueObjectImpl<Boolean> booleanValueObject = new TestValueObjectImpl<Boolean>(false);
    final TestValueObjectImpl<Integer> integerValueObject = new TestValueObjectImpl<Integer>(0);
    final TestValueObjectImpl<String> stringValueObject = new TestValueObjectImpl<String>("test");
    final TestValueObjectImpl<Object> rootValueObject = new TestValueObjectImpl<Object>(null,
      booleanValueObject, integerValueObject, stringValueObject);

    assertFalse(booleanValueObject.getValue());
    assertEquals(new Integer(0), integerValueObject.getValue());
    assertEquals("test", stringValueObject.getValue());
    assertNull(rootValueObject.getValue());
    assertEquals(3, rootValueObject.size());

    try {
      BeanUtil.setPropertyValue(rootValueObject, "valueObjectList[1].value", 1);
    }
    catch (Exception e) {
      fail("Calling setPropertyValue on the nested, indexed property (valueObjectList[1].value) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(booleanValueObject.getValue());
    assertEquals(new Integer(1), integerValueObject.getValue());
    assertEquals("test", stringValueObject.getValue());
    assertNull(rootValueObject.getValue());
    assertEquals(3, rootValueObject.size());
  }

  public void testSetPropertyValues() throws Exception {
    final Person person = new PersonImpl();

    final Map<String, Object> expectedPropertyValueMap = new HashMap<String, Object>(5);
    expectedPropertyValueMap.put("personId", 1);
    expectedPropertyValueMap.put("dateOfBirth", DateUtil.getCalendar(2002, Calendar.JULY, 4, 15, 30, 15, 800));
    expectedPropertyValueMap.put("firstName", "Jon");
    expectedPropertyValueMap.put("lastName", "Bloom");
    expectedPropertyValueMap.put("ssn", "333-22-4444");

    BeanUtil.setPropertyValues(person, expectedPropertyValueMap);

    assertEquals(expectedPropertyValueMap.get("personId"), person.getPersonId());
    assertEquals(expectedPropertyValueMap.get("dateOfBirth"), person.getDateOfBirth());
    assertEquals(expectedPropertyValueMap.get("firstName"), person.getFirstName());
    assertEquals(expectedPropertyValueMap.get("lastName"), person.getLastName());
    assertEquals(expectedPropertyValueMap.get("ssn"), person.getSsn());
  }

  public void testSetPropertyValuesHavingNestedIndexedProperties() throws Exception {
    final TestValueObjectImpl<Boolean> indexedValueObject = new TestValueObjectImpl<Boolean>(false);
    final TestValueObjectImpl<Integer> nestedValueObject = new TestValueObjectImpl<Integer>(0);
    final TestValueObjectImpl<String> rootValueObject = new TestValueObjectImpl<String>("null", nestedValueObject);
    rootValueObject.add(indexedValueObject);

    assertEquals("null", rootValueObject.getValue());
    assertSame(nestedValueObject, rootValueObject.getValueObject());
    assertEquals(1, rootValueObject.size());
    assertSame(indexedValueObject, rootValueObject.getValueObjectList().get(0));
    assertFalse(indexedValueObject.getValue());
    assertEquals(new Integer(0), nestedValueObject.getValue());

    final Map<String, Object> expectedPropertyValueMap = new HashMap<String, Object>(3);
    expectedPropertyValueMap.put("value", "test");
    expectedPropertyValueMap.put("valueObject.value", 1);
    expectedPropertyValueMap.put("valueObjectList[0].value", true);

    try {
      BeanUtil.setPropertyValues(rootValueObject, expectedPropertyValueMap);
    }
    catch (Exception e) {
      fail("Calling setPropertyValues with the property value Map (" + expectedPropertyValueMap
        + ") threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("test", rootValueObject.getValue());
    assertSame(nestedValueObject, rootValueObject.getValueObject());
    assertEquals(1, rootValueObject.size());
    assertSame(indexedValueObject, rootValueObject.getValueObjectList().get(0));
    assertTrue(indexedValueObject.getValue());
    assertEquals(new Integer(1), nestedValueObject.getValue());
  }

  public void testSetPropertyValuesWithNullBean() throws Exception {
    final Map<String, Object> mockPropertyValueMap = new HashMap<String, Object>();
    mockPropertyValueMap.put("firstName", "Jon");
    mockPropertyValueMap.put("lastName", "Bloom");

    try {
      BeanUtil.setPropertyValues(null, mockPropertyValueMap);
      fail("Calling setPropertyValues with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValues with a null bean Object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSetPropertyValuesWithNullPropertyValueMap() throws Exception {
    final Person person = new PersonImpl();

    assertNull(person.getPersonId());
    assertNull(person.getFirstName());
    assertNull(person.getLastName());
    assertNull(person.getDateOfBirth());
    assertNull(person.getSsn());

    try {
      BeanUtil.setPropertyValues(person, null);
      fail("Calling setPropertyValues with a null property to value Map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The property to value Map cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setPropertyValues with a null property to value Map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(person.getPersonId());
    assertNull(person.getFirstName());
    assertNull(person.getLastName());
    assertNull(person.getDateOfBirth());
    assertNull(person.getSsn());
  }

  public void testSetPropertyValuesWithEmptyPropertyValueMap() throws Exception {
    final Map<String, Object> mockPropertyValueMap = Collections.emptyMap();
    final Person person = new PersonImpl();

    assertNull(person.getPersonId());
    assertNull(person.getFirstName());
    assertNull(person.getLastName());
    assertNull(person.getDateOfBirth());
    assertNull(person.getSsn());

    try {
      BeanUtil.setPropertyValues(person, mockPropertyValueMap);
    }
    catch (Exception e) {
      fail("Calling setPropertyValues with an empty property to value Map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(person.getPersonId());
    assertNull(person.getFirstName());
    assertNull(person.getLastName());
    assertNull(person.getDateOfBirth());
    assertNull(person.getSsn());
  }

  public void testGetRenderer() throws Exception {
    assertNull(BeanUtil.getRenderer(null, BeanUtil.DEFAULT_RENDERER_MAP));
    assertNull(BeanUtil.getRenderer("null", BeanUtil.DEFAULT_RENDERER_MAP));
    assertNull(BeanUtil.getRenderer(DateUtil.getCalendar(2008, Calendar.NOVEMBER, 10).getTime(), BeanUtil.DEFAULT_RENDERER_MAP));
    assertNull(BeanUtil.getRenderer(TestEnum.ZERO, BeanUtil.DEFAULT_RENDERER_MAP));
    assertTrue(BeanUtil.getRenderer(getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      BeanUtil.DEFAULT_RENDERER_MAP) instanceof BeanRenderer);
    assertTrue(BeanUtil.getRenderer(DateUtil.getCalendar(2008, Calendar.NOVEMBER, 10), BeanUtil.DEFAULT_RENDERER_MAP) instanceof CalendarRenderer);
    assertTrue(BeanUtil.getRenderer(Collections.emptyList(), BeanUtil.DEFAULT_RENDERER_MAP) instanceof CollectionRenderer);
    assertTrue(BeanUtil.getRenderer(Collections.emptySet(), BeanUtil.DEFAULT_RENDERER_MAP) instanceof CollectionRenderer);
    assertTrue(BeanUtil.getRenderer(State.OREGON, BeanUtil.DEFAULT_RENDERER_MAP) instanceof EnumRenderer);
    assertTrue(BeanUtil.getRenderer(Collections.emptyMap(), BeanUtil.DEFAULT_RENDERER_MAP) instanceof MapRenderer);
    assertTrue(BeanUtil.getRenderer(getProcess("system"), BeanUtil.DEFAULT_RENDERER_MAP) instanceof ProcessRenderer);
    assertTrue(BeanUtil.getRenderer(getUser("root"), BeanUtil.DEFAULT_RENDERER_MAP) instanceof UserRenderer);
  }

  public void testGetRendererWithNullRendererMap() throws Exception {
    ObjectRenderer renderer = null;

    assertNull(renderer);

    try {
      renderer = BeanUtil.getRenderer(new Object(), null);
      fail("Calling getRenderer with a null ObjectRenderer Map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The ObjectRenderer Map cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getRenderer with a null ObjectRenderer Map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(renderer);
  }

  public void testRegisterRenderer() throws Exception {
    assertTrue(BeanUtil.registerRenderer(Object.class, DefaultRenderer.INSTANCE));
    assertTrue(BeanUtil.registerRenderer(Bean.class, DefaultRenderer.INSTANCE));
    assertTrue(BeanUtil.registerRenderer(Bean.class, NullRenderer.INSTANCE));
  }

  public void testRegisterRendererWithNullClassType() throws Exception {
    boolean success = false;

    assertFalse(success);

    try {
      BeanUtil.registerRenderer(null, DefaultRenderer.INSTANCE);
      fail("Calling registerRenderer with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class to register the ObjectRenderer for cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling registerRenderer with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(success);
  }

  public void testRegisterRendererWithNullObjectRenderer() throws Exception {
    boolean success = false;

    assertFalse(success);

    try {
      BeanUtil.registerRenderer(Object.class, null);
      fail("Calling registerRenderer with a null ObjectRenderer should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The ObjectRenderer cannot be null", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling registerRenderer with a null ObjectRenderer threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(success);
  }

  public void testUnregisterRenderer() throws Exception {
    assertFalse(BeanUtil.unregisterRenderer(null));
    assertFalse(BeanUtil.unregisterRenderer(Bean.class));
    assertFalse(BeanUtil.unregisterRenderer(com.cp.common.enums.Enum.class));
    assertFalse(BeanUtil.unregisterRenderer(Object.class));
    assertTrue(BeanUtil.registerRenderer(Object.class, DefaultRenderer.INSTANCE));
    assertTrue(BeanUtil.unregisterRenderer(Object.class));
    assertFalse(BeanUtil.unregisterRenderer(Object.class));
  }

  public void testToString() throws Exception {
    final Person person = new PersonImpl(1);
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setDateOfBirth(DateUtil.getCalendar(1974, Calendar.MAY, 27, 1, 2, 30, 0));
    person.setSsn("333-22-4444");
    person.accept(AuditableVisitor.getInstance(getUser("blumj"), DateUtil.getCalendar(2007, Calendar.MAY, 13, 21, 15, 30, 0)));
    person.commit();

    final StringBuffer expectedString = new StringBuffer("{beanHistory = null, class = ");
    expectedString.append(person.getClass());
    expectedString.append(", createdBy = blumj, createdDate = 05/13/2007 09:15:30 PM, creatingProcess = null");
    expectedString.append(", dateOfBirth = 05/27/1974 01:02:30 AM, firstName = Jon");
    expectedString.append(", id = 1, lastModifiedBy = blumj, lastModifiedDate = 05/13/2007 09:15:30 PM");
    expectedString.append(", lastName = Bloom");
    expectedString.append(", modified = false, modifiedBy = blumj, modifiedDate = 05/13/2007 09:15:30 PM, modifiedProperties = {}:java.util.Collections$UnmodifiableSet, modifyingProcess = null, mutable = true, new = false");
    expectedString.append(", personId = 1");
    expectedString.append(", rollbackCalled = false");
    expectedString.append(", ssn = 333-22-4444");
    expectedString.append(", throwExceptionOnRollback = false");
    expectedString.append("}");

    final String actualString = BeanUtil.toString(person);

    assertNotNull(actualString);
    assertEquals(expectedString.toString(), actualString);
  }

  public void testToStringWithCustomRenderer() throws Exception {
    final Person person = new PersonImpl(1);
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setDateOfBirth(DateUtil.getCalendar(1974, Calendar.MAY, 27, 1, 2, 30, 0));
    person.setSsn("333-22-4444");
    person.accept(AuditableVisitor.getInstance(getUser("blumj"), DateUtil.getCalendar(2008, Calendar.NOVEMBER, 10, 1, 22, 30, 500)));
    person.commit();

    assertTrue(BeanUtil.registerRenderer(Bean.class, NullRenderer.INSTANCE));

    final String expectedString = "null";
    final String actualString = BeanUtil.toString(person);

    assertNotNull(actualString);
    assertEquals(expectedString, actualString);
  }

  private static final class TestAnnotatedBean {

    public Object getPropertyOne() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    @BoundedDate(min="01/01/2008", max="12/31/2008", pattern="MM/dd/yyyy")
    @BoundedNumber(min=-9, max=9)
    @Required
    public void setPropertyOne(final Object propertyOneValue) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public Object getPropertyTwo() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    @BoundedLength(min=1, max=9)
    @BoundedNumber(min=0, max=1)
    @Default(value="false")
    @Required
    public void setPropertyTwo(final Object propertyTwoValue) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public Object getPropertyThree() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    @BoundedDate(min="06/01/2008", max="08/31/2008", pattern="MM/dd/yyyy")
    @BoundedLength(min=0, max=Integer.MAX_VALUE)
    @Required
    public void setPropertyThree(final Object propertyThreeValue) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

  private static final class TestBean extends AbstractBean<Integer> {

    private Object inaccessibleProperty;
    private Object readPrivateProperty;
    private Object readProtectedProperty;
    private Object readableWritableProperty;
    private Object unreadableProperty;
    private Object unwritableProperty;
    private Object writePrivateProperty;
    private Object writeProtectedProperty;

    public TestBean(final Object value) {
      setUnreadableProperty(value);
      this.unwritableProperty = value;
    }

    public Object getErrorableProperty() {
      throw new IllegalStateException("errorableProperty not set!");
    }

    public void setErrorableProperty(final Object errorableProperty) {
      throw new UnsupportedOperationException("setting errorableProperty not implemented!");
    }

    private Object getInaccessibleProperty() {
      return inaccessibleProperty;
    }

    private void setInaccessibleProperty(final Object inaccessibleProperty) {
      this.inaccessibleProperty = inaccessibleProperty;
    }

    private Object getReadPrivateProperty() {
      return readPrivateProperty;
    }

    public void setReadPrivateProperty(final Object readPrivateProperty) {
      this.readPrivateProperty = readPrivateProperty;
    }

    protected Object getReadProtectedProperty() {
      return readProtectedProperty;
    }

    public void setReadProtectedProperty(final Object readProtectedProperty) {
      this.readProtectedProperty = readProtectedProperty;
    }

    public Object getReadableWritableProperty() {
      return readableWritableProperty;
    }

    public void setReadableWritableProperty(final Object readableWritableProperty) {
      this.readableWritableProperty = readableWritableProperty;
    }

    public void setUnreadableProperty(final Object unreadableProperty) {
      this.unreadableProperty = unreadableProperty;
    }

    public Object getUnwritableProperty() {
      return unwritableProperty;
    }

    public Object getWritePrivateProperty() {
      return writePrivateProperty;
    }

    private void setWritePrivateProperty(final Object writePrivateProperty) {
      this.writePrivateProperty = writePrivateProperty;
    }

    public Object getWriteProtectedProperty() {
      return writeProtectedProperty;
    }

    protected void setWriteProtectedProperty(final Object writeProtectedProperty) {
      this.writeProtectedProperty = writeProtectedProperty;
    }
  }

  private static enum TestEnum {
    ZERO,
    ONE,
    TWO,
  }

  private static class TestValueObjectImpl<T> extends MockValueObjectImpl<T> {

    private List<MockValueObject> valueObjectList = new LinkedList<MockValueObject>();

    private MockValueObject valueObject;

    public TestValueObjectImpl() {
    }

    public TestValueObjectImpl(final T value) {
      super(value);
    }

    public TestValueObjectImpl(final T value, final MockValueObject valueObject) {
      super(value);
      this.valueObject = valueObject;
    }

    public TestValueObjectImpl(final T value, final MockValueObject... valueObjectArray) {
      super(value);
      valueObjectList.addAll(Arrays.asList(valueObjectArray));
    }

    public MockValueObject getValueObject() {
      return valueObject;
    }

    public List<MockValueObject> getValueObjectList() {
      return Collections.unmodifiableList(valueObjectList);
    }

    public boolean add(final MockValueObject valueObject) {
      return valueObjectList.add(valueObject);
    }

    public boolean remove(final MockValueObject valueObject) {
      return valueObjectList.remove(valueObject);
    }

    public int size() {
      return valueObjectList.size();
    }
  }

}
