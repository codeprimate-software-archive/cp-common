/*
 * SyncAuditablePropertiesVisitorTest.java (c) 14 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.2
 * @see com.cp.common.beans.support.CommonVisitorTest
 */

package com.cp.common.beans.support;

import com.cp.common.enums.State;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SyncAuditablePropertiesVisitorTest extends CommonVisitorTest {

  public SyncAuditablePropertiesVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SyncAuditablePropertiesVisitorTest.class);
    //suite.addTest(new SyncAuditablePropertiesVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final Person person = getPerson(null, "Test", "Person");
    final Address address = getAddress(null, "100 Main St.", null, "Somewhere", State.OREGON, "12345");
    final PhoneNumber phoneNumber = getPhoneNumber(null, "503", "555", "1234", null);

    final Calendar jblumDate = DateUtil.getCalendar(2005, Calendar.AUGUST, 14);
    final TestUser jblum = new TestUser("jblum");

    person.setAddress(address);
    person.setPhoneNumber(phoneNumber);
    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(jblum, jblumDate));

    assertTrue(person.isNew());
    assertTrue(person.isModified());
    assertTrue(address.isNew());
    assertTrue(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertCreatedAndModifiedProperties(jblum, jblumDate, person);
    assertLastModifiedProperties(null, null, person);
    assertCreatedAndModifiedProperties(jblum, jblumDate, address);
    assertLastModifiedProperties(null, null, address);
    assertCreatedAndModifiedProperties(jblum, jblumDate, phoneNumber);
    assertLastModifiedProperties(null, null, phoneNumber);

    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertLastModifiedProperties(jblum, jblumDate, person);
    assertLastModifiedProperties(jblum, jblumDate, address);
    assertLastModifiedProperties(jblum, jblumDate, phoneNumber);

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertTrue(person.isNew());
    assertFalse(person.isModified());
    assertTrue(address.isNew());
    assertFalse(address.isModified());
    assertTrue(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());

    final Calendar rootDate = DateUtil.getCalendar(2005, Calendar.SEPTEMBER, 2);
    final TestUser root = new TestUser("root");

    person.setPersonId(new Integer(1));
    person.setAddress(null);
    address.setStreet2("Apt. 2");
    address.setCity("Corvalis");
    phoneNumber.setPhoneNumberId(new Integer(1));
    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(root, rootDate));

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertTrue(address.isNew());
    assertTrue(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertTrue(phoneNumber.isModified());
    assertCreatedAndModifiedProperties(jblum, jblumDate, root, rootDate, person);
    assertLastModifiedProperties(jblum, jblumDate, person);
    assertCreatedAndModifiedProperties(jblum, jblumDate, address);
    assertLastModifiedProperties(jblum, jblumDate, address);
    assertCreatedAndModifiedProperties(jblum, jblumDate, root, rootDate, phoneNumber);
    assertLastModifiedProperties(jblum, jblumDate, phoneNumber);

    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertLastModifiedProperties(root, rootDate, person);
    assertLastModifiedProperties(jblum, jblumDate, address);
    assertLastModifiedProperties(root, rootDate, phoneNumber);

    person.commit();
    address.commit();
    phoneNumber.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertTrue(address.isNew());
    assertFalse(address.isModified());
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());

    final Calendar adminDate = DateUtil.getCalendar(2007, Calendar.JANUARY, 14);
    final TestUser admin = new TestUser("admin");

    person.setFirstName("Jon");
    person.setLastName("Doe");
    person.setAddress(address);
    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(admin, adminDate));

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertTrue(address.isNew());
    assertTrue(address.isModified()); // The AuditableVisitor changed the address
    assertFalse(phoneNumber.isNew());
    assertFalse(phoneNumber.isModified());
    assertCreatedAndModifiedProperties(jblum, jblumDate, admin, adminDate, person);
    assertLastModifiedProperties(root, rootDate, person);
    assertCreatedAndModifiedProperties(admin, adminDate, address);
    assertLastModifiedProperties(jblum, jblumDate, address);
    assertCreatedAndModifiedProperties(jblum, jblumDate, root, rootDate, phoneNumber);
    assertLastModifiedProperties(root, rootDate, phoneNumber);

    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertLastModifiedProperties(admin, adminDate, person);
    assertLastModifiedProperties(admin, adminDate, address);
    assertLastModifiedProperties(root, rootDate, phoneNumber);
  }

  public void testVisitByACS() throws Exception {
    final Person person = getPerson(new Integer(2), "Jack", "Handy");
    final Address address = getAddress(new Integer(2), "100 Alder St.", null, "Portland", State.OREGON, "97205");

    final Calendar modDate0 = DateUtil.getCalendar(2005, Calendar.AUGUST, 15, 12, 30, 15, 0);
    final TestUser blumj = new TestUser("blumj");

    person.setAddress(address);
    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(blumj, modDate0));
    person.commit();
    address.commit();

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertEquals(new Integer(2), person.getPersonId());
    assertEquals("Jack", person.getFirstName());
    assertEquals("Handy", person.getLastName());
    assertCreatedAndModifiedProperties(blumj, modDate0, person);
    assertLastModifiedProperties(blumj, modDate0, person);
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertEquals(new Integer(2), address.getAddressId());
    assertEquals("100 Alder St.", address.getStreet1());
    assertNull(address.getStreet2());
    assertEquals("Portland", address.getCity());
    assertEquals(State.OREGON, address.getState());
    assertEquals("97205", address.getZipCode());
    assertCreatedAndModifiedProperties(blumj, modDate0, address);
    assertLastModifiedProperties(blumj, modDate0, address);

    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertLastModifiedProperties(blumj, modDate0, person);
    assertLastModifiedProperties(blumj, modDate0, address);

    final Calendar modDate1 = DateUtil.getCalendar(2005, Calendar.AUGUST, 16, 11, 0, 0, 0);
    final TestUser root = new TestUser("root");

    person.setPersonId(new Integer(22));
    person.setLastName("Arse");
    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(root, modDate1));

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertEquals(new Integer(22), person.getPersonId());
    assertEquals("Jack", person.getFirstName()); // no change
    assertEquals("Arse", person.getLastName());
    assertCreatedAndModifiedProperties(blumj, modDate0, root, modDate1, person);
    assertLastModifiedProperties(blumj, modDate0, person);
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, address);
    assertLastModifiedProperties(blumj, modDate0, address);

    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, root, modDate1, person);
    assertLastModifiedProperties(root, modDate1, person);
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, address);
    assertLastModifiedProperties(blumj, modDate0, address);

    person.commit();
    address.commit();

    assertLastModifiedProperties(root, modDate1, person);
    assertLastModifiedProperties(blumj, modDate0, address);

    final Calendar modDate2 = DateUtil.getCalendar(2005, Calendar.SEPTEMBER, 12, 16, 14, 5, 0);
    final TestUser admin = new TestUser("admin");

    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(admin, modDate2));
    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, root, modDate1, person);
    assertLastModifiedProperties(root, modDate1, person);
    assertFalse(address.isNew());
    assertFalse(address.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, address);
    assertLastModifiedProperties(blumj, modDate0, address);

    person.setFirstName("Jack");
    address.setStreet1("120 Main St.");
    address.setState(State.MAINE);
    address.setZipCode("12345");
    person.accept(com.cp.common.beans.support.AuditableVisitor.getInstance(admin, modDate2));

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, root, modDate1, person);
    assertLastModifiedProperties(root, modDate1, person);
    assertFalse(address.isNew());
    assertTrue(address.isModified());
    assertEquals("120 Main St.", address.getStreet1());
    assertEquals(State.MAINE, address.getState());
    assertEquals("12345", address.getZipCode());
    assertCreatedAndModifiedProperties(blumj, modDate0, admin, modDate2, address);
    assertLastModifiedProperties(blumj, modDate0, address);

    person.accept(SyncAuditablePropertiesVisitor.INSTANCE);

    assertFalse(person.isNew());
    assertFalse(person.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, root, modDate1, person);
    assertLastModifiedProperties(root, modDate1, person);
    assertFalse(address.isNew());
    assertTrue(address.isModified());
    assertCreatedAndModifiedProperties(blumj, modDate0, admin, modDate2, address);
    assertLastModifiedProperties(admin, modDate2, address);
  }

}
