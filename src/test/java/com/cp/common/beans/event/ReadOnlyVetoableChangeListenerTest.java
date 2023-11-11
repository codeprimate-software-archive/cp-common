/*
 * ReadOnlyVetoableChangeListenerTest.java (c) 26 December 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.23
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.ReadOnlyVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ReadOnlyVetoableChangeListenerTest extends CommonEventTestCase {

  public ReadOnlyVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ReadOnlyVetoableChangeListenerTest.class);
    //suite.addTest(new ReadOnlyVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testHandleWithAllPropertiesReadOnly() throws Exception {
    final Person person = getPerson("Jon", "Doe");
    person.addVetoableChangeListener(ReadOnlyVetoableChangeListener.INSTANCE);

    assertNotNull(person);
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setPersonId(0);
      fail("Setting the personId property of Person should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (id) of bean (" + person.getClass().getName()
        + ") is read-only and cannot be changed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the personId property of Person threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setFirstName("John");
      fail("Setting the firstName property of Person should have thrown a CosntraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (firstName) of bean (" + person.getClass().getName()
        + ") is read-only and cannot be changed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the firstName property of Person threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
  }

  public void testHandleWithAllPropertiesReadOnlyNoPropertyChange() throws Exception {
    final Person person = getPerson(0, "Jon", "Doe", DateUtil.getCalendar(2000, Calendar.JULY, 4), "333-22-4444");
    person.addVetoableChangeListener(ReadOnlyVetoableChangeListener.INSTANCE);

    assertNotNull(person);
    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setPersonId(0);
    }
    catch (Exception e) {
      fail("Setting the personId property of Person with the same value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
  }

  public void testHandleWithSinglePropertyReadOnlyUsingListener() throws Exception {
    final Person person = getPerson("Jon", "Doe");
    person.addVetoableChangeListener(new ReadOnlyVetoableChangeListener("firstName"));

    assertNotNull(person);
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setPersonId(0);
    }
    catch (Exception e) {
      fail("Setting the personId property of Person threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setFirstName("John");
      fail("Setting the firstName property of Person should have thrown a CosntraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (firstName) of bean (" + person.getClass().getName()
        + ") is read-only and cannot be changed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the firstName property of Person threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
  }

  public void testHandleWithSinglePropertyReadOnlyUsingRegistration() throws Exception {
    final Person person = getPerson("Jon", "Doe");
    person.addVetoableChangeListener("lastName", ReadOnlyVetoableChangeListener.INSTANCE);

    assertNotNull(person);
    assertNull(person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setPersonId(0);
    }
    catch (Exception e) {
      fail("Setting the personId property of Person threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());

    try {
      person.setLastName("Dough");
      fail("Setting the lastName property of Person should have thrown a CosntraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (lastName) of bean (" + person.getClass().getName()
        + ") is read-only and cannot be changed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the lastName property of Person threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(new Integer(0), person.getPersonId());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
  }

}
