/*
 * IsModifiedVisitorTest.java (c) 14 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.lang.support.CommonSupportTestCase
 * @see com.cp.common.lang.support.IsModifiedVisitor
 */

package com.cp.common.lang.support;

import com.cp.common.enums.State;
import junit.framework.Test;
import junit.framework.TestSuite;

public class IsModifiedVisitorTest extends CommonSupportTestCase {

  public IsModifiedVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IsModifiedVisitorTest.class);
    //suite.addTest(new IsModifiedVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final Person person = getPerson(1, "Jon", "Doe");

    final Address address = getAddress(10, "100 Main St.", null, "Portland", State.OREGON, "97205");
    person.setAddress(address);

    final PhoneNumber phoneNumber = getPhoneNumber(100, "503", "555", "1234", null);
    person.setPhoneNumber(phoneNumber);

    person.commit();
    address.commit();
    phoneNumber.commit();

    IsModifiedVisitor visitor = new IsModifiedVisitor();
    person.accept(visitor);

    assertFalse(person.isModified());
    assertPerson(1, "Jon", "Doe", person);
    assertFalse(address.isModified());
    assertAddress(10, "100 Main St.", null, "Portland", State.OREGON, "97205", address);
    assertFalse(phoneNumber.isModified());
    assertPhoneNumber(100, "503", "555", "1234", null, phoneNumber);
    assertFalse(visitor.isModified());

    // change address
    address.setStreet1("445 Waupelani Dr.");
    address.setStreet2("Apt. L8");
    address.setCity("State College");
    address.setState(State.PENNSYLVANIA);
    address.setZipCode("16801");
    person.accept(visitor);

    assertFalse(person.isModified());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertTrue(visitor.isModified());

    address.commit();
    visitor = new IsModifiedVisitor();

    assertFalse(person.isModified());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isModified());

    // change phone number
    phoneNumber.setAreaCode("814");
    phoneNumber.setPrefix("238");
    phoneNumber.setSuffix("2338");
    person.accept(visitor);

    assertFalse(person.isModified());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isModified());
    assertTrue(visitor.isModified());

    phoneNumber.commit();
    visitor = new IsModifiedVisitor();

    assertFalse(person.isModified());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isModified());

    // call person, address and phone number setters; make no changes
    person.setPersonId(1);
    person.setLastName("Doe");
    address.setCity("State College");
    address.setState(State.PENNSYLVANIA);
    address.setZipCode("16801");
    phoneNumber.setAreaCode("814");
    phoneNumber.setExtension(null);
    person.accept(visitor);

    assertFalse(person.isModified());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isModified());

    // change person & phone number
    person.setFirstName("John");
    person.setLastName("Dough");
    phoneNumber.setExtension("4200");
    person.accept(visitor);

    assertTrue(person.isModified());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isModified());
    assertTrue(visitor.isModified());

    person.commit();
    phoneNumber.commit();
    visitor = new IsModifiedVisitor();

    assertFalse(person.isModified());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isModified());

    // change person; set address to null
    person.setAddress(null);
    person.accept(visitor);

    assertNull(person.getAddress());
    assertTrue(person.isModified());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertTrue(visitor.isModified());

    person.commit();
    visitor = new IsModifiedVisitor();

    assertNull(person.getAddress());
    assertFalse(person.isModified());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isModified());

    // change address, verify it does not affect person or phone number
    address.setAddressId(null);
    address.setStreet1("700 Turner St.");
    address.setStreet2("Apt. A");
    address.setCity("Missoula");
    address.setState(State.MONTANA);
    address.setZipCode("59812");
    person.accept(visitor);

    assertFalse(person.isModified());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isModified());
  }

}
