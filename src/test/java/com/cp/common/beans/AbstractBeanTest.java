/*
 * AbstractBeanTest.java (c) 16 October 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.CommonBeanTestCase
 * @see com.cp.common.test.util.TestUtil
 * @see com.cp.common.log4j.LoggingConfigurer
 */

package com.cp.common.beans;

import com.cp.common.beans.support.RequiredVisitor;
import com.cp.common.lang.Auditable;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import com.cp.common.lang.Visitable;
import com.cp.common.lang.support.AuditableVisitor;
import com.cp.common.lang.support.MutableVisitor;
import com.cp.common.lang.support.ResetVisitor;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Visitor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AbstractBeanTest extends CommonBeanTestCase {

  public AbstractBeanTest(final String testName) {
    super(testName);
    // Note, setting the current Threads context class loader is required since the BeanUtils uses a different class
    // loader to load the commons logging classes and the AbstractBean class uses the BeanUtils method to implement
    // the rollback, and loads it's own logging as well using the Log4J logging system.
    //Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBeanTest.class);
    //suite.addTest(new AbstractBeanTest("testCopy"));
    //suite.addTest(new AbstractBeanTest("testToString"));
    return suite;
  }

  private void assertCreatedByModifiedBy(final User expectedChangedBy, final Auditable auditable) {
    assertCreatedByModifiedBy(expectedChangedBy, expectedChangedBy, auditable);
  }

  private void assertCreatedByModifiedBy(final User expectedCreatedBy, final User expectedModifiedBy, final Auditable auditable) {
    TestUtil.assertNullEquals(expectedCreatedBy, auditable.getCreatedBy());
    TestUtil.assertNullEquals(expectedModifiedBy, auditable.getModifiedBy());
  }

  public void testAccept() throws Exception {
    final MockBean bean = new MockBeanImpl();
    final TestVisitor visitor = new TestVisitor();

    assertFalse(visitor.hasVisited());

    bean.accept(visitor);

    assertTrue(visitor.hasVisited());
  }

  public void testAcceptWithAuditableVisitor() throws Exception {
    final Person person = new PersonImpl("Jack", "Handy", DateUtil.getCalendar(1991, Calendar.APRIL, 1), "123-45-6789");

    assertCreatedByModifiedBy(null, person);
    assertTrue(person.isNew());
    assertTrue(person.isModified());

    final User expectedChangedBy = new DefaultUser("root");
    person.accept(AuditableVisitor.getInstance(expectedChangedBy));
    person.commit();

    assertCreatedByModifiedBy(expectedChangedBy, person);
    assertTrue(person.isNew());
    assertFalse(person.isModified());

    final User expectedCreatedBy = expectedChangedBy;
    final User expectedModifiedBy = new DefaultUser("blumj");
    person.setPersonId(1);
    person.accept(AuditableVisitor.getInstance(expectedModifiedBy));
    person.commit();

    assertCreatedByModifiedBy(expectedCreatedBy, expectedModifiedBy, person);
    assertFalse(person.isNew());
    assertFalse(person.isModified());

    person.accept(AuditableVisitor.getInstance(new DefaultUser("hacker")));

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertCreatedByModifiedBy(expectedCreatedBy, expectedModifiedBy, person);
  }

  public void testAcceptWithMutableVisitor() throws Exception {
    final Person person = new PersonImpl();
    MutableVisitor mutableVisitor = new MutableVisitor(Mutable.IMMUTABLE);

    assertTrue(person.isMutable());

    person.accept(mutableVisitor);

    assertFalse(person.isMutable());

    mutableVisitor = new MutableVisitor(Mutable.MUTABLE);
    person.accept(mutableVisitor);

    assertTrue(person.isMutable());
  }

  public void testAcceptWithRequiredVisitor() throws Exception {
    // note, null date of birth
    final Person person = new PersonImpl();
    person.setFirstName("Jon");
    person.setLastName("Doe");
    person.setSsn("333-22-4444");
    person.accept(AuditableVisitor.getInstance(new DefaultUser("blumj")));

    final RequiredVisitor requiredVisitor = new RequiredVisitor();

    assertNull(person.getDateOfBirth());

    try {
      person.accept(requiredVisitor);
      fail("Calling accept with the RequiredVisitor should have thrown an IllegalStateException for a null date of birth on person!");
    }
    catch (IllegalStateException e) {
      final StringBuffer expectedMessage = new StringBuffer("Property (dateOfBirth) of bean (");
      expectedMessage.append(person.getClass().getName());
      expectedMessage.append(") is required!");

      assertEquals(expectedMessage.toString(), e.getMessage());
    }

    final Calendar dateOfBirth = DateUtil.getCalendar(2000, Calendar.APRIL, 30);
    person.setDateOfBirth(dateOfBirth);

    assertEquals(dateOfBirth, person.getDateOfBirth());

    try {
      person.accept(requiredVisitor);
    }
    catch (IllegalStateException e) {
      fail("Calling accept with a RequiredVisitor should not have thrown an IllegalStateException!");
    }
  }

  public void testAcceptWithResetVisitor() throws Exception {
    final Person person = new PersonImpl(1);
    final ResetVisitor resetVisitor = new ResetVisitor();

    assertEquals(new Integer(1), person.getPersonId());

    person.accept(resetVisitor);

    assertNull(person.getPersonId());
  }

  public void testAddRemoveChangeListener() throws Exception {
    final TestChangeListener listener = new TestChangeListener();

    final Person person = new PersonImpl();
    person.addChangeListener(listener);

    assertFalse(listener.isChanged());
    assertNull(listener.getSource());

    person.setFirstName("Jon");

    assertTrue(listener.isChanged());
    assertEquals(person, listener.getSource());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getSource());

    person.removeChangeListener(listener);
    person.setLastName("Doe");

    assertFalse(listener.isChanged());
    assertNull(listener.getSource());
  }

  public void testAddRemovePropertyListener() throws Exception {
    final TestPropertyChangeListener listener = new TestPropertyChangeListener();

    final Person person = new PersonImpl(1);
    person.addPropertyChangeListener(listener);

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setFirstName("Jon");

    assertTrue(listener.isChanged());
    assertEquals("firstName", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setFirstName("John");

    assertTrue(listener.isChanged());
    assertEquals("firstName", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setLastName("Bloom");

    assertTrue(listener.isChanged());
    assertEquals("lastName", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setFirstName("John");

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.removePropertyChangeListener(listener);
    person.setFirstName("Jon");
    person.setLastName("Doe");

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());
  }

  public void testAddRemovePropertyListenerForProperty() throws Exception {
    final TestPropertyChangeListener listener = new TestPropertyChangeListener();

    final Person person = new PersonImpl(1);
    person.addPropertyChangeListener("id", listener);

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setPersonId(2);

    assertTrue(listener.isChanged());
    assertEquals("id", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setFirstName("Jack");

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.removePropertyChangeListener(listener);
    person.setPersonId(3);

    assertTrue(listener.isChanged());
    assertEquals("id", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.removePropertyChangeListener("id", listener);
    person.setPersonId(4);

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());
  }

  public void testAddRemoveVetoableListener() throws Exception {
    final TestVetoableChangeListener listener = new TestVetoableChangeListener();

    final Person person = new PersonImpl();
    person.addVetoableChangeListener(listener);

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setPersonId(1);

    assertTrue(listener.isChanged());
    assertEquals("id", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setSsn("333-22-4444");

    assertTrue(listener.isChanged());
    assertEquals("ssn", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setSsn("333224444");

    assertTrue(listener.isChanged());
    assertEquals("ssn", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setPersonId(1);

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.removeVetoableChangeListener(listener);
    person.setSsn("123-45-6789");

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());
  }

  public void testAddRemoveVetoableListenerForProperty() throws Exception {
    final TestVetoableChangeListener listener = new TestVetoableChangeListener();

    final Person person = new PersonImpl(1);
    person.addVetoableChangeListener("id", listener);

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setPersonId(2);

    assertTrue(listener.isChanged());
    assertEquals("id", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.setSsn("333-22-4444");

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.removeVetoableChangeListener(listener);
    person.setPersonId(3);

    assertTrue(listener.isChanged());
    assertEquals("id", listener.getPropertyName());

    listener.reset();

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());

    person.removeVetoableChangeListener("id", listener);
    person.setPersonId(4);
    person.setSsn("123456789");

    assertFalse(listener.isChanged());
    assertNull(listener.getPropertyName());
  }

  public void testCommit() throws Exception {
    final Person person = new PersonImpl(1);

    assertTrue(person.isModified());

    person.commit();

    assertFalse(person.isModified());

    person.setFirstName("Jack");
    person.setLastName("Handy");
    person.setSsn("123456789");

    assertTrue(person.isModified());

    person.commit();

    assertFalse(person.isModified());
  }

  public void testConstraintViolation() throws Exception {
    final VetoableChangeListener listener = new DateOfBirthVetoableChangeListener();

    final Person person = new PersonImpl(1);
    person.addVetoableChangeListener("dateOfBirth", listener);

    assertNull(person.getDateOfBirth());

    final Calendar july4th1955 = DateUtil.getCalendar(1955, Calendar.JULY, 4);
    try {
      person.setDateOfBirth(july4th1955);
    }
    catch (ConstraintViolationException e) {
      fail("Setting the date of birth to July 4th 1955 should not have thrown a ConstraintViolationException!");
    }

    assertEquals(july4th1955, person.getDateOfBirth());

    try {
      person.setDateOfBirth(DateUtil.getCalendar(1969, Calendar.NOVEMBER, 11));
      fail("Setting the date of birth to November 11th 1969 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      // expected behavior!
    }

    assertEquals(july4th1955, person.getDateOfBirth());

    person.removeVetoableChangeListener("dateOfBirth", listener);

    final Calendar february5th1988 = DateUtil.getCalendar(1988, Calendar.FEBRUARY, 5);
    try {
      person.setDateOfBirth(february5th1988);
    }
    catch (ConstraintViolationException e) {
      fail("Setting the date of birth to February 5th 1988 after removing the DateOfBirthVetoableChangeListener should not have thrown a ConstraintViolationException!");
    }

    assertEquals(february5th1988, person.getDateOfBirth());
  }

  public void testConstructionWithId() throws Exception {
    Person person = new PersonImpl((Integer) null);

    assertNotNull(person);
    assertNull(person.getId());

    person = new PersonImpl(9);

    assertNotNull(person);
    assertEquals(new Integer(9), person.getPersonId());
  }

  public void testCopy() throws Exception {
    final Person person = new PersonImpl(1);
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setDateOfBirth(DateUtil.getCalendar(1921, Calendar.JANUARY, 1));
    person.setSsn("333-22-4444");
    person.accept(AuditableVisitor.getInstance(new DefaultUser("blumj")));
    person.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());

    final Person personCopy = (Person) person.copy();

    assertNotNull(personCopy);
    assertNotSame(person, personCopy);
    assertTrue(personCopy.isNew());
    assertTrue(personCopy.isModified());
    assertNull(personCopy.getPersonId());
    assertNull(personCopy.getCreatedBy());
    assertNull(personCopy.getCreatedDateTime());
    assertNull(personCopy.getCreatingProcess());
    assertNull(personCopy.getModifiedBy());
    assertNull(personCopy.getModifiedDateTime());
    assertNull(personCopy.getModifyingProcess());
    assertEquals(person.getFirstName(), personCopy.getFirstName());
    assertEquals(person.getLastName(), personCopy.getLastName());
    assertEquals(person.getDateOfBirth(), personCopy.getDateOfBirth());
    assertEquals(person.getSsn(), personCopy.getSsn());
  }

  public void testEventDispatch() throws Exception {
    final TestChangeListener changeListener = new TestChangeListener();
    final TestPropertyChangeListener propertyListener = new TestPropertyChangeListener();
    final TestVetoableChangeListener vetoListener = new TestVetoableChangeListener();

    final Person person = new PersonImpl(1);
    person.addChangeListener(changeListener);
    person.addPropertyChangeListener(propertyListener);
    person.addVetoableChangeListener(vetoListener);

    assertTrue(((AbstractBean) person).isEventDispatchEnabled());
    assertTrue(person.isModified());
    assertFalse(changeListener.isChanged());
    assertFalse(propertyListener.isChanged());
    assertFalse(vetoListener.isChanged());

    person.setFirstName("Jon");

    assertTrue(person.isModified());
    assertTrue(changeListener.isChanged());
    assertTrue(propertyListener.isChanged());
    assertTrue(vetoListener.isChanged());

    person.commit();
    changeListener.reset();
    propertyListener.reset();
    vetoListener.reset();

    assertFalse(person.isModified());
    assertFalse(changeListener.isChanged());
    assertFalse(propertyListener.isChanged());
    assertFalse(vetoListener.isChanged());

    person.setPersonId(1);

    assertFalse(person.isModified());
    assertFalse(changeListener.isChanged());
    assertFalse(propertyListener.isChanged());
    assertFalse(vetoListener.isChanged());

    ((AbstractBean) person).setEventDispatchEnabled(false);
    person.setPersonId(2);

    assertFalse(person.isModified());
    assertFalse(changeListener.isChanged());
    assertFalse(propertyListener.isChanged());
    assertFalse(vetoListener.isChanged());

    ((AbstractBean) person).setEventDispatchEnabled(true);
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setSsn("123-45-6789");

    assertTrue(person.isModified());
    assertTrue(changeListener.isChanged());
    assertTrue(propertyListener.isChanged());
    assertTrue(vetoListener.isChanged());
  }

  public void testIsModified() throws Exception {
    Person person = new PersonImpl();

    assertNotNull(person);
    assertFalse(person.isModified());

    person = new PersonImpl(1);

    assertNotNull(person);
    assertTrue(person.isModified());

    person.commit();

    assertFalse(person.isModified());

    person.setFirstName("Jon");
    person.setLastName("Bloom");

    assertTrue(person.isModified());

    person.commit();

    assertFalse(person.isModified());

    person.setPersonId(1);
    person.setFirstName("Jon");

    assertFalse(person.isModified());

    person.setFirstName("John");

    assertTrue(person.isModified());
  }

  public void testIsModifiedProperty() throws Exception {
    final Person person = new PersonImpl(2);

    assertNotNull(person);
    assertTrue(person.isModified());
    assertTrue(person.isModified("id"));
    assertFalse(person.isModified("firstName"));
    assertFalse(person.isModified("lastName"));
    assertFalse(person.isModified("dateOfBirth"));
    assertFalse(person.isModified("ssn"));

    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setSsn(null);

    assertTrue(person.isModified());
    assertTrue(person.isModified("id"));
    assertTrue(person.isModified("firstName"));
    assertTrue(person.isModified("lastName"));
    assertFalse(person.isModified("dateOfBirth"));
    assertFalse(person.isModified("ssn"));

    person.commit();

    assertFalse(person.isModified());
    assertFalse(person.isModified("id"));
    assertFalse(person.isModified("firstName"));
    assertFalse(person.isModified("lastName"));
    assertFalse(person.isModified("dateOfBirth"));
    assertFalse(person.isModified("ssn"));

    person.setPersonId(2);
    person.setFirstName("Jon");
    person.setLastName("Doe");
    person.setDateOfBirth(DateUtil.getCalendar(1977, Calendar.JANUARY, 22));

    assertTrue(person.isModified());
    assertFalse(person.isModified("id"));
    assertFalse(person.isModified("firstName"));
    assertTrue(person.isModified("lastName"));
    assertTrue(person.isModified("dateOfBirth"));
    assertFalse(person.isModified("ssn"));
  }

  public void testIsNew() throws Exception {
    Person person = new PersonImpl();

    assertNotNull(person);
    assertNull(person.getPersonId());
    assertTrue(person.isNew());

    person.setPersonId(1);

    assertEquals(new Integer(1), person.getPersonId());
    assertFalse(person.isNew());

    person = new PersonImpl(2);

    assertNotNull(person);
    assertEquals(new Integer(2), person.getPersonId());
    assertFalse(person.isNew());

    person.setPersonId(-1);

    assertEquals(new Integer(-1), person.getPersonId());
    assertFalse(person.isNew());

    person.setPersonId(null);

    assertNull(person.getPersonId());
    assertTrue(person.isNew());
  }

  public void testGetModifiedProperties() throws Exception {
    final Person person = new PersonImpl(1);

    assertNotNull(person);

    final Set<String> expectedModifiedPropertySet = new HashSet<String>(4);
    expectedModifiedPropertySet.add("id");

    assertTrue(person.isModified());
    assertEquals(expectedModifiedPropertySet, person.getModifiedProperties());

    person.commit();
    expectedModifiedPropertySet.clear();

    assertFalse(person.isModified());
    assertTrue(person.getModifiedProperties().isEmpty());

    person.setPersonId(1);
    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setSsn("333-22-4444");

    expectedModifiedPropertySet.add("firstName");
    expectedModifiedPropertySet.add("lastName");
    expectedModifiedPropertySet.add("ssn");

    assertTrue(person.isModified());
    assertEquals(expectedModifiedPropertySet, person.getModifiedProperties());

    person.commit();
    expectedModifiedPropertySet.clear();

    assertFalse(person.isModified());
    assertTrue(person.getModifiedProperties().isEmpty());

    person.setPersonId(2);
    person.setFirstName("John");
    person.setLastName("Bloom");
    person.setDateOfBirth(DateUtil.getCalendar(2000, Calendar.JULY, 1));
    person.setSsn("333224444");

    expectedModifiedPropertySet.add("id");
    expectedModifiedPropertySet.add("firstName");
    expectedModifiedPropertySet.add("dateOfBirth");
    expectedModifiedPropertySet.add("ssn");

    assertTrue(person.isModified());
    assertEquals(expectedModifiedPropertySet, person.getModifiedProperties());

    person.commit();
    expectedModifiedPropertySet.clear();

    assertFalse(person.isModified());
    assertTrue(person.getModifiedProperties().isEmpty());

    person.setSsn(null);
    expectedModifiedPropertySet.add("ssn");

    assertTrue(person.isModified());
    assertEquals(expectedModifiedPropertySet, person.getModifiedProperties());

    person.commit();
    expectedModifiedPropertySet.clear();

    assertFalse(person.isModified());
    assertTrue(person.getModifiedProperties().isEmpty());

    person.setSsn(null);

    assertFalse(person.isModified());
    assertTrue(person.getModifiedProperties().isEmpty());
  }

  public void testMutable() throws Exception {
    final Person person = new PersonImpl(1);

    assertNotNull(person);
    assertTrue(person.isMutable());

    person.setMutable(Mutable.IMMUTABLE);

    assertFalse(person.isMutable());
    assertNull(person.getSsn());

    try {
      person.setSsn("333224444");
      fail("Setting the SSN property on an immutable Person bean object should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The object (" + person.getClass().getName() + ") is immutable!", e.getMessage());
    }

    assertNull(person.getSsn());
    assertFalse(person.isMutable());

    person.setMutable(Mutable.MUTABLE);

    assertTrue(person.isMutable());
    assertEquals(new Integer(1), person.getPersonId());

    try {
      person.setPersonId(null);
    }
    catch (ConstraintViolationException e) {
      fail("Setting the personId property to null on the Person bean object should not have thrown a ConstraintViolationException!");
    }

    assertNull(person.getPersonId());
    assertTrue(person.isMutable());
  }

  public void testReset() throws Exception {
    final Person person = new PersonImpl(1);

    assertNotNull(person);
    assertTrue(person.isModified());
    assertFalse(person.isRollbackCalled());

    person.rollback();

    assertFalse(person.isModified());
    assertTrue(person.isRollbackCalled());

    person.reset();

    assertFalse(person.isRollbackCalled());
  }

  public void testRollback() throws Exception {
    final Person person = new PersonImpl();

    assertNotNull(person);
    assertFalse(person.isModified());
    assertFalse(person.isRollbackCalled());

    person.setFirstName("Jon");
    person.setLastName("Bloom");
    person.setSsn("333224444");

    assertTrue(person.isModified());
    assertFalse(person.isRollbackCalled());

    person.rollback();

    assertFalse(person.isModified());
    assertTrue(person.isRollbackCalled());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Bloom", person.getLastName());
    assertEquals("333224444", person.getSsn());
    assertFalse(person.isThrowExceptionOnRollback());

    try {
      person.setLastName("Doe");
    }
    catch (ConstraintViolationException e) {
      fail("Setting the lasName property to 'Doe' after rollback has been called on Person with throwExceptionOnRollback set to false should not have thrown a ConstraintViolationException!");
    }

    assertEquals("Doe", person.getLastName());
    assertTrue(person.isModified());
    assertTrue(person.isRollbackCalled());
    assertFalse(person.isThrowExceptionOnRollback());

    ((AbstractBean) person).setThrowExceptionOnRollback(true);
    person.commit();

    assertFalse(person.isModified());
    assertTrue(person.isRollbackCalled());
    assertTrue(person.isThrowExceptionOnRollback());

    try {
      person.setFirstName("Joe");
      fail("Setting the firstName property to 'Joe' after rollback has been called on Person with throwExceptionOnRollback set to true should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      // expected behavior!
    }

    assertEquals("Jon", person.getFirstName());
    assertFalse(person.isModified());
    assertTrue(person.isRollbackCalled());
    assertTrue(person.isThrowExceptionOnRollback());

    person.reset();

    assertFalse(person.isModified());
    assertFalse(person.isRollbackCalled());
    assertTrue(person.isThrowExceptionOnRollback());

    try {
      person.setFirstName("Joe");
    }
    catch (ConstraintViolationException e) {
      fail("Setting the firstName property to 'Joe' should not have thrown a ConstraintViolationException!");
    }

    assertEquals("Joe", person.getFirstName());
    assertTrue(person.isModified());
    assertFalse(person.isRollbackCalled());
    assertTrue(person.isThrowExceptionOnRollback());
  }

  public void testSerializable() throws Exception {
    final Calendar dateOfBirth = DateUtil.getCalendar(1974, Calendar.JULY, 4);

    final Person personOut = new PersonImpl("Jon", "Doe", dateOfBirth, "333-22-4444");
    personOut.setPersonId(101);

    assertEquals(new Integer(101), personOut.getPersonId());
    assertEquals("Jon", personOut.getFirstName());
    assertEquals("Doe", personOut.getLastName());
    assertEquals(dateOfBirth, personOut.getDateOfBirth());

    final ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
    final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(arrayOut));

    out.writeObject(personOut);
    out.flush();
    out.close();

    final ByteArrayInputStream arrayIn = new ByteArrayInputStream(arrayOut.toByteArray());
    final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(arrayIn));
    final Person personIn = (Person) in.readObject();

    in.close();

    assertEquals(new Integer(101), personIn.getPersonId());
    assertEquals("Jon", personIn.getFirstName());
    assertEquals("Doe", personIn.getLastName());
    assertEquals(dateOfBirth, personIn.getDateOfBirth());
    assertEquals(personOut, personIn);
  }

  public void testToString() throws Exception {
    final Consumer consumer = new ConsumerImpl();
    consumer.setPersonId(1);
    consumer.setFirstName("Jon");
    consumer.setLastName("Bloom");
    consumer.setDateOfBirth(DateUtil.getCalendar(2000, Calendar.JULY, 15, 6, 30, 15, 0));
    consumer.setSsn("333224444");
    consumer.setYearBorn(2000);
    consumer.accept(AuditableVisitor.getInstance(new DefaultUser("blumj"), DateUtil.getCalendar(2007, Calendar.MAY, 15, 19, 12, 30, 0)));
    consumer.commit();

    final StringBuffer expectedString = new StringBuffer("{");
    expectedString.append("beanHistory = null, createdBy = blumj, createdDate = 05/15/2007 07:12:30.00 PM, creatingProcess = null");
    expectedString.append(", dateOfBirth = 07/15/2000 06:30:15.00 AM, firstName = Jon");
    expectedString.append(", id = 1, lastModifiedBy = blumj, lastModifiedDate = 05/15/2007 07:12:30.00 PM");
    expectedString.append(", lastName = Bloom");
    expectedString.append(", modified = false, modifiedBy = blumj, modifiedDate = 05/15/2007 07:12:30.00 PM, modifiedProperties = {}:java.util.Collections$UnmodifiableSet, modifyingProcess = null, mutable = true, new = false");
    expectedString.append(", personId = 1");
    expectedString.append(", rollbackCalled = false");
    expectedString.append(", ssn = 333224444");
    expectedString.append(", throwExceptionOnRollback = false");
    expectedString.append(", yearBorn = 2000");
    expectedString.append("}:").append(consumer.getClass().getName());

    final String actualString = consumer.toString();

    assertNotNull(actualString);
    assertEquals(expectedString.toString(), actualString);
  }

  private static final class DateOfBirthVetoableChangeListener implements VetoableChangeListener {

    public void vetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
      if ("dateOfBirth".equals(event.getPropertyName())) {
        final Calendar newDateOfBirth = (Calendar) event.getNewValue();
        if (ObjectUtil.isNotNull(newDateOfBirth)) {
          final int year = newDateOfBirth.get(Calendar.YEAR);
          if (year < 1930 || year > 1959) {
            throw new PropertyVetoException("The dateOfBirth property must be during the 1930s, 1940s, or 1950s!", event);
          }
        }
      }
    }
  }

  private static interface MockBean extends Bean<Integer> {
  }

  private static final class MockBeanImpl extends AbstractBean<Integer> implements MockBean {
  }

  private static final class TestChangeListener implements ChangeListener, Resettable {

    private boolean changed = false;
    private Object source = null;

    public boolean isChanged() {
      return changed;
    }

    public Object getSource() {
      return source;
    }

    public void reset() {
      changed = false;
      source = null;
    }

    public void stateChanged(final ChangeEvent event) {
      changed = true;
      source = event.getSource();
    }
  }

  private static final class TestPropertyChangeListener implements PropertyChangeListener, Resettable {

    private boolean changed = false;
    private String propertyName = null;

    public boolean isChanged() {
      return changed;
    }

    public String getPropertyName() {
      return propertyName;
    }

    public void propertyChange(final PropertyChangeEvent event) {
      changed = true;
      propertyName = event.getPropertyName();
    }

    public void reset() {
      changed = false;
      propertyName = null;
    }
  }

  private static final class TestVetoableChangeListener implements Resettable, VetoableChangeListener {

    private boolean changed = false;
    private String propertyName = null;

    public boolean isChanged() {
      return changed;
    }

    public String getPropertyName() {
      return propertyName;
    }

    public void vetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
      changed = true;
      propertyName = event.getPropertyName();
    }

    public void reset() {
      changed = false;
      propertyName = null;
    }
  }

  private static final class TestVisitor implements Resettable, Visitor {

    private boolean visited = false;

    public boolean hasVisited() {
      return visited;
    }

    public void reset() {
      visited = false;
    }

    public void visit(final Visitable obj) {
      visited = true;
    }
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }

}
