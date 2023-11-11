/*
 * IsNewVisitorTest.java (c) 14 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.lang.support.CommonSupportTestCase
 * @see com.cp.common.lang.support.IsNewVisitor
 */

package com.cp.common.lang.support;

import com.cp.common.enums.State;
import junit.framework.Test;
import junit.framework.TestSuite;

public class IsNewVisitorTest extends CommonSupportTestCase {

  public IsNewVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IsNewVisitorTest.class);
    //suite.addTest(new IsNewVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final Person person = getPerson(2, "Jack", "Handy");

    Address address = getAddress(22, "1000 Palmer", null, "Missoula", State.MONTANA, "59801");
    person.setAddress(address);

    PhoneNumber phoneNumber = getPhoneNumber(222, "319", "555", "1234", "4444");
    person.setPhoneNumber(phoneNumber);

    person.commit();
    address.commit();
    phoneNumber.commit();

    IsNewVisitor visitor = new IsNewVisitor();
    person.accept(visitor);

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertPerson(2, "Jack", "Handy", person);
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertAddress(22, "1000 Palmer", null, "Missoula", State.MONTANA, "59801", address);
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertPhoneNumber(222, "319", "555", "1234", "4444", phoneNumber);
    assertFalse(visitor.isNew());

    // create new address, change phone number
    address = getAddress(null, "2200 Pine St.", "Suite 88", "Dubuque", State.IOWA, "52003");
    person.setAddress(address);
    phoneNumber.setAreaCode("800");
    phoneNumber.setPrefix("555");
    phoneNumber.setSuffix("4321");
    person.accept(visitor);

    assertAddress(null, "2200 Pine St.", "Suite 88", "Dubuque", State.IOWA, "52003", person.getAddress());
    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertTrue(address.isNew());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertTrue(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();
    visitor = new IsNewVisitor();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertTrue(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // change person, set address ID & change phone number ID; nothing new
    person.setPersonId(2);
    address.setAddressId(1);
    phoneNumber.setPhoneNumberId(0);
    person.accept(visitor);

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertFalse(address.isNew());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // set person's phone number to null, set phone number's ID to null
    person.setPhoneNumber(null);
    phoneNumber.setPhoneNumberId(null);
    person.accept(visitor);

    assertNull(person.getPhoneNumber());
    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertNull(person.getPhoneNumber());
    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // create new phone number, change person ID
    phoneNumber = getEmptyPhoneNumber();
    person.setPersonId(-1);
    person.accept(visitor);

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // set phone number ID, add phone number to person
    phoneNumber.setPhoneNumberId(1);
    person.setPhoneNumber(phoneNumber);
    person.accept(visitor);

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // set person ID & address ID to null
    person.setPersonId(null);
    address.setAddressId(null);
    person.accept(visitor);

    assertNull(person.getPersonId());
    assertTrue(person.isNew());
    assertTrue(person.isModified());
    assertNull(address.getAddressId());
    assertTrue(address.isNew());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertTrue(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();
    visitor = new IsNewVisitor();

    assertNull(person.getPersonId());
    assertTrue(person.isNew());
    assertFalse(person.isModified());
    assertNull(address.getAddressId());
    assertTrue(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // set person ID
    person.setPersonId(Integer.MAX_VALUE);
    person.accept(visitor);

    assertEquals(new Integer(Integer.MAX_VALUE), person.getPersonId());
    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertTrue(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertTrue(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();
    visitor = new IsNewVisitor();

    assertEquals(new Integer(Integer.MAX_VALUE), person.getPersonId());
    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertTrue(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // set address ID
    address.setAddressId(Integer.MIN_VALUE);
    person.accept(visitor);

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertEquals(new Integer(Integer.MIN_VALUE), address.getAddressId());
    assertFalse(address.isNew());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertEquals(new Integer(Integer.MIN_VALUE), address.getAddressId());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertFalse(visitor.isNew());

    // set phone number ID to null
    phoneNumber.setPhoneNumberId(null);
    person.accept(visitor);

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertTrue(visitor.isNew());
  }

}
