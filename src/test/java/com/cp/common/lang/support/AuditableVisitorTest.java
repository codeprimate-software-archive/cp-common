/*
 * AuditableVisitorTest (c) 15 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.beans.User
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.lang.support.AuditableVisitor
 * @see com.cp.common.lang.support.CommonSupportTestCase
 * @see com.cp.common.test.util.TestUtil
 * @see com.cp.common.test.mock.MockBean
 * @see com.cp.common.test.mock.MockBeanImpl
 * @see com.cp.common.util.DateUtil
 */

package com.cp.common.lang.support;

import com.cp.common.beans.User;
import com.cp.common.lang.Auditable;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AuditableVisitorTest extends CommonSupportTestCase {

  public AuditableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AuditableVisitorTest.class);
    //suite.addTest(new AuditableVisitorTest("testName"));
    return suite;
  }

  private void assertCreatedByModifiedBy(final User user, final Auditable auditable) {
    assertCreatedByModifiedBy(user, user, auditable);
  }

  private void assertCreatedByModifiedBy(final String message, final User user, final Auditable auditable) {
    assertCreatedByModifiedBy(message, user, user, auditable);
  }

  private void assertCreatedByModifiedBy(final User createdBy, final User modifiedBy, final Auditable auditable) {
    TestUtil.assertNullEquals(createdBy, auditable.getCreatedBy());
    TestUtil.assertNullEquals(modifiedBy, auditable.getModifiedBy());
  }

  private void assertCreatedByModifiedBy(final String message, final User createdBy, final User modifiedBy, final Auditable auditable) {
    TestUtil.assertNullEquals(message, createdBy, auditable.getCreatedBy());
    TestUtil.assertNullEquals(message, modifiedBy, auditable.getModifiedBy());
  }

  public void testVisit() throws Exception {
    final Household house = getHousehold(null, "HouseOfTheRisingSun", 101);

    assertNotNull(house);

    house.addMember(getPerson(null, "Jack", "Handy"));
    house.addMember(getPerson(2, "Sandy", "Handy"));
    house.addMember(getPerson(null, "Jordan", "Handy"));

    assertEquals("The household's size should be 3 members!", 3, house.size());
    assertTrue("The house should be new!", house.isNew());
    assertTrue("The house should be modified!", house.isModified());
    assertTrue("Jack Handy should be new!", house.getMember("Jack", "Handy").isNew());
    assertTrue("Jack Handy should be modified!", house.getMember("Jack", "Handy").isModified());
    assertFalse("Sandy Handy should NOT be new!", house.getMember("Sandy", "Handy").isNew());
    assertTrue("Sandy Handy should be modified!", house.getMember("Sandy", "Handy").isModified());
    assertTrue("Jordan Handy should be new!", house.getMember("Jordan", "Handy").isNew());
    assertTrue("Jordan Handy should be modified!", house.getMember("Jordan", "Handy").isModified());
    assertCreatedByModifiedBy("The House's auditable fields should be null!", null, house);
    assertCreatedByModifiedBy("The Jack Handy's auditable fields should be null!", null, house.getMember("Jack", "Handy"));
    assertCreatedByModifiedBy("The Sandy Handy's auditable fields should be null!", null, house.getMember("Sandy", "Handy"));
    assertCreatedByModifiedBy("The Jordan Handy's auditable fields should be null!", null, house.getMember("Jordan", "Handy"));

    final User jblum = getUser("jblum");
    house.accept(AuditableVisitor.getInstance(jblum));
    house.commit();

    assertEquals("The household's size should be 3 members!", 3, house.size());
    assertTrue("The household should be new!", house.isNew());
    assertFalse("The household should NOT be modified!", house.isModified());
    assertTrue("Jack Handy should be new!", house.getMember("Jack", "Handy").isNew());
    assertFalse("Jack Handy should NOT be modified!", house.getMember("Jack", "Handy").isModified());
    assertFalse("Sandy Handy should be new!", house.getMember(2).isNew());
    assertFalse("Sandy Handy should NOT be modified!", house.getMember(2).isModified());
    assertTrue("Jordan Handy should be new!", house.getMember("Jordan", "Handy").isNew());
    assertFalse("Jordan Handy should NOT be modified!", house.getMember("Jordan", "Handy").isModified());
    assertCreatedByModifiedBy("The household's createdBy/modifiedBy properties should be set to 'jblum'!", jblum, house);
    assertCreatedByModifiedBy("Jack Handy's createdBy/modifiedBy properties should be set to 'jblum'!", jblum, house.getMember("Jack", "Handy"));
    assertCreatedByModifiedBy("Sandy Handy's createdBy/modifiedBy properties should be set to 'jblum'!", jblum, house.getMember(2));
    assertCreatedByModifiedBy("Jordan Handy's createdBy/modifiedBy properties should be set to 'jblum'!", jblum, house.getMember("Jordan", "Handy"));

    final User administrator = getUser("administrator");
    house.setHouseholdId(1);
    house.setHouseholdNumber(202);
    house.getMember("Jack", "Handy").setPersonId(1);
    house.getMember(1).setLastName("Black");
    house.getMember("Jordan", "Handy").setPersonId(3);
    house.getMember(new Integer(3)).setLastName("Micheals");
    house.accept(AuditableVisitor.getInstance(administrator));

    assertEquals("The household's size should be 3 members!", 3, house.size());
    assertFalse("The household should NOT be new!", house.isNew());
    assertTrue("The household should be modified!", house.isModified());
    assertFalse(house.getMember(1).isNew());
    assertTrue(house.getMember(1).isModified());
    assertFalse(house.getMember(2).isNew());
    assertFalse(house.getMember(2).isModified());
    assertFalse(house.getMember(3).isNew());
    assertTrue(house.getMember(3).isModified());

    house.commit();

    assertCreatedByModifiedBy(jblum, administrator, house);
    assertCreatedByModifiedBy(jblum, administrator, house.getMember(1));
    assertCreatedByModifiedBy(jblum, house.getMember(2));
    assertCreatedByModifiedBy(jblum, administrator, house.getMember(3));

    final User root = getUser("root");
    house.setHouseholdName("HouseOfTheRisingSun");
    house.addMember(getPerson(4, "Jon", "Doe"));
    house.getMember(1).setLastName("Off");
    house.getMember(2).setName("Sandy", "Handy");
    house.getMember(3).setLastName("Micheal");
    house.accept(AuditableVisitor.getInstance(root));

    assertEquals(4, house.size());
    assertFalse(house.isNew());
    assertFalse(house.isModified());
    assertFalse(house.getMember(1).isNew());
    assertTrue(house.getMember(1).isModified());
    assertFalse(house.getMember(2).isNew());
    assertFalse(house.getMember(2).isModified());
    assertFalse(house.getMember(3).isNew());
    assertTrue(house.getMember(3).isModified());
    assertFalse(house.getMember(4).isNew());
    assertTrue(house.getMember(4).isModified());

    house.commit();

    assertCreatedByModifiedBy(jblum, administrator, house);
    assertCreatedByModifiedBy(jblum, root, house.getMember(1));
    assertCreatedByModifiedBy(jblum, house.getMember(2));
    assertCreatedByModifiedBy(jblum, root, house.getMember(3));
    assertCreatedByModifiedBy(root, house.getMember(4));
  }

  public void testSetAuditableProperties() throws Exception {
    MockBean bean = new MockBeanImpl();

    assertNull(bean.getId());
    assertTrue(bean.isNew());
    assertFalse(bean.isModified());
    assertCreatedAndModifiedProperties(null, null, bean);

    final User blumj = getUser("blumj");
    final Calendar yesterday = DateUtil.getCalendar(2007, Calendar.JUNE, 24);

    bean.setCreatedBy(blumj);
    bean.setCreatedDateTime(yesterday);
    bean.setModifiedBy(blumj);
    bean.setModifiedDateTime(yesterday);

    assertNull(bean.getId());
    assertTrue(bean.isNew());
    assertTrue(bean.isModified());
    assertCreatedAndModifiedProperties(blumj, yesterday, bean);

    final User jblum = getUser("jblum");
    final Calendar tomorrow = DateUtil.getCalendar(2007, Calendar.JUNE, 26);

    bean.accept(AuditableVisitor.getInstance(jblum, tomorrow));

    assertNull(bean.getId());
    assertTrue(bean.isNew());
    assertTrue(bean.isModified());
    assertCreatedAndModifiedProperties(jblum, tomorrow, bean);

    bean = new MockBeanImpl();
    bean.setId(1);

    assertEquals(new Integer(1), bean.getId());
    assertFalse(bean.isNew());
    assertTrue(bean.isModified());
    assertCreatedAndModifiedProperties(null, null, bean);

    bean.accept(AuditableVisitor.getInstance(jblum, tomorrow));

    assertEquals(new Integer(1), bean.getId());
    assertFalse(bean.isNew());
    assertTrue(bean.isModified());
    assertCreatedAndModifiedProperties(jblum, tomorrow, bean);

    bean.commit();

    assertFalse(bean.isNew());
    assertFalse(bean.isModified());
    assertCreatedAndModifiedProperties(jblum, tomorrow, bean);

    bean.accept(AuditableVisitor.getInstance(blumj, yesterday));

    assertFalse(bean.isNew());
    assertFalse(bean.isModified());
    assertCreatedAndModifiedProperties(jblum, tomorrow, bean);
  }

  public void testInstantiateAuditableVisitor() throws Exception {
    try {
      AuditableVisitor.getInstance(null, null, null, null);
      fail("Creating an AuditiableVisitor with null auditable information should have thrown a NullPointerException!");
    }
    catch (Exception e) {
      // expected behavior!
    }

    final User createdBy = getUser("blumj");
    final User modifiedBy = getUser("jblum");

    final Calendar createdDate = DateUtil.getCalendar(2007, Calendar.JUNE, 25);
    final Calendar modifiedDate = DateUtil.getCalendar(2007, Calendar.JUNE, 26);

    AuditableVisitor visitor = null;

    try {
      visitor = AuditableVisitor.getInstance(createdBy, createdDate, modifiedBy, modifiedDate);
    }
    catch (Exception e) {
      fail("Creating an AuditableVisitor with non-null auditable information should not have thrown an Exception!");
    }

    assertEquals(createdBy, visitor.getCreatedBy());
    assertEquals(createdDate, visitor.getCreatedDate());
    assertEquals(modifiedBy, visitor.getModifiedBy());
    assertEquals(modifiedDate, visitor.getModifiedDate());

    try {
      visitor = AuditableVisitor.getInstance(createdBy, createdDate);
    }
    catch (Exception e) {
      fail("Creating an AuditableVisitor with changedBy and changedDate auditable information should not have thrown an Exception!");
    }

    assertEquals(createdBy, visitor.getCreatedBy());
    assertEquals(createdDate, visitor.getCreatedDate());
    assertEquals(createdBy, visitor.getModifiedBy());
    assertEquals(createdDate, visitor.getModifiedDate());

    final Calendar today = DateUtil.getCalendarNoTimestamp();

    try {
      visitor = AuditableVisitor.getInstance(modifiedBy);
    }
    catch (Exception e) {
      fail("Creating an AuditableVisitor with changeBy and default date/time auditable information should not have thrown an Exception!");
    }

    assertEquals(modifiedBy, visitor.getCreatedBy());
    assertEquals(today, DateUtil.truncate(visitor.getCreatedDate()));
    assertEquals(modifiedBy, visitor.getModifiedBy());
    assertEquals(today, DateUtil.truncate(visitor.getModifiedDate()));
  }

}
