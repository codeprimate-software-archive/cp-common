/*
 * ObjectImmutableVetoableChangeListenerTest.java (c) 11 July 2005
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.ObjectImmutableVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.support.MutableVisitor;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ObjectImmutableVetoableChangeListenerTest extends CommonEventTestCase {

  public ObjectImmutableVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ObjectImmutableVetoableChangeListenerTest.class);
    //suite.addTest(new ObjectImmutableVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testHandleOnImmutableBean() throws Exception {
    final Person person = getPerson("Jon", "Doe");

    assertNotNull(person);
    assertTrue(person.isMutable());
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    person.accept(new MutableVisitor(Mutable.IMMUTABLE));

    assertFalse(person.isMutable());

    try {
      person.setPersonId(1);
      fail("Setting the personId property on an immutable Person object to a non-null value should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The object (" + person.getClass().getName() + ") is immutable!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the personId property on an immutable Person object to a non-null value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(person.isMutable());
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setFirstName("John");
      fail("Setting the firstName property on the immutable Person object to 'John' should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The object (" + person.getClass().getName() + ") is immutable!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the firstName property on the immutable Person object to 'John' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(person.isMutable());
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
  }

  public void testHandleOnImmutableBeanNoPropertyChange() throws Exception {
    final Person person = getPerson(0, "Jon", "Doe", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");

    assertNotNull(person);
    assertTrue(person.isMutable());
    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    person.setMutable(false);

    assertFalse(person.isMutable());

    try {
      person.setPersonId(0);
    }
    catch (Exception e) {
      fail("Setting the personId property on an immutable Person object to null threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(person.isMutable());
    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
  }

  public void testHandleOnMutableBean() throws Exception {
    final Person person = getPerson("Jon", "Doe");

    assertNotNull(person);
    assertTrue(person.isMutable());
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setPersonId(0);
      person.setFirstName("John");
      person.setLastName("Dough");
    }
    catch (Exception e) {
      fail("Changing the personId, firstName, and lastName properties on the Person object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertTrue(person.isMutable());
    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("John", person.getFirstName());
    assertEquals("Dough", person.getLastName());
  }

}
