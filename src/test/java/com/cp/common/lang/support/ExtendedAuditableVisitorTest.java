/*
 * ExtendedAuditableVisitorTest.java (c) 30 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.beans.Process
 * @see com.cp.common.beans.User
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.lang.support.CommonSupportTestCase
 * @see com.cp.common.lang.support.ExtendedAuditableVisitor
 * @see com.cp.common.test.util.TestUtil
 */

package com.cp.common.lang.support;

import com.cp.common.beans.Process;
import com.cp.common.beans.User;
import com.cp.common.lang.Auditable;
import com.cp.common.test.util.TestUtil;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ExtendedAuditableVisitorTest extends CommonSupportTestCase {

  public ExtendedAuditableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ExtendedAuditableVisitorTest.class);
    //suite.addTest(new ExtendedAuditableVisitorTest("testName"));
    return suite;
  }

  private void assertCreatedByModifiedByProcessedBy(final User user, final Process process, final Auditable auditable) {
    assertCreatedByModifiedByProcessedBy(user, process, user, process, auditable);
  }

  private void assertCreatedByModifiedByProcessedBy(final String message, final User user, final Process process, final Auditable auditable) {
    assertCreatedByModifiedByProcessedBy(message, user, process, user, process, auditable);
  }

  private void assertCreatedByModifiedByProcessedBy(final User createdBy,
                                                    final Process creatingProcess,
                                                    final User modifiedBy,
                                                    final Process modifyingProcess,
                                                    final Auditable auditable) {
    TestUtil.assertNullEquals(createdBy, auditable.getCreatedBy());
    TestUtil.assertNullEquals(creatingProcess, auditable.getCreatingProcess());
    TestUtil.assertNullEquals(modifiedBy, auditable.getModifiedBy());
    TestUtil.assertNullEquals(modifyingProcess, auditable.getModifyingProcess());
  }

  private void assertCreatedByModifiedByProcessedBy(final String message,
                                                    final User createdBy,
                                                    final Process creatingProcess,
                                                    final User modifiedBy,
                                                    final Process modifyingProcess,
                                                    final Auditable auditable) {
    TestUtil.assertNullEquals(message, createdBy, auditable.getCreatedBy());
    TestUtil.assertNullEquals(message, creatingProcess, auditable.getCreatingProcess());
    TestUtil.assertNullEquals(message, modifiedBy, auditable.getModifiedBy());
    TestUtil.assertNullEquals(message, modifyingProcess, auditable.getModifyingProcess());
  }

  public void testVisit() throws Exception {
    final Household household = getHousehold(null, "WhiteHouse", 69);

    assertNotNull(household);

    household.addMember(getPerson(null, "Jon", "Bloom"));
    household.addMember(getPerson(null, "Sarah", "Bloom"));

    assertEquals(2, household.size());
    assertTrue(household.isNew());
    assertTrue(household.isModified());
    assertTrue(household.getMember("Jon", "Bloom").isNew());
    assertTrue(household.getMember("Jon", "Bloom").isModified());
    assertTrue(household.getMember("Sarah", "Bloom").isNew());
    assertTrue(household.getMember("Sarah", "Bloom").isModified());

    final User createdBy = getUser("blumj");
    final Process creatingProcess = getProcess("consoleApp");

    household.accept(ExtendedAuditableVisitor.getInstance(createdBy, creatingProcess));

    assertCreatedByModifiedByProcessedBy(createdBy, creatingProcess, household);
    assertCreatedByModifiedByProcessedBy(createdBy, creatingProcess, household.getMember("Jon", "Bloom"));
    assertCreatedByModifiedByProcessedBy(createdBy, creatingProcess, household.getMember("Sarah", "Bloom"));
    assertTrue(household.isNew());
    assertTrue(household.isModified());
    assertTrue(household.getMember("Jon", "Bloom").isNew());
    assertTrue(household.getMember("Jon", "Bloom").isModified());
    assertTrue(household.getMember("Sarah", "Bloom").isNew());
    assertTrue(household.getMember("Sarah", "Bloom").isModified());

    final User newCreatedBy = getUser("jblum");
    final Process newCreatingProcess = getProcess("webApp");

    household.accept(ExtendedAuditableVisitor.getInstance(newCreatedBy, newCreatingProcess));

    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, household);
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, household.getMember("Jon", "Bloom"));
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, household.getMember("Sarah", "Bloom"));
    assertTrue(household.isNew());
    assertTrue(household.isModified());
    assertTrue(household.getMember("Jon", "Bloom").isNew());
    assertTrue(household.getMember("Jon", "Bloom").isModified());
    assertTrue(household.getMember("Sarah", "Bloom").isNew());
    assertTrue(household.getMember("Sarah", "Bloom").isModified());

    household.commit();
    household.getMember("Jon", "Bloom").commit();
    household.getMember("Sarah", "Bloom").commit();

    assertTrue(household.isNew());
    assertFalse(household.isModified());
    assertTrue(household.getMember("Jon", "Bloom").isNew());
    assertFalse(household.getMember("Jon", "Bloom").isModified());
    assertTrue(household.getMember("Sarah", "Bloom").isNew());
    assertFalse(household.getMember("Sarah", "Bloom").isModified());

    household.setHouseholdId(0);
    household.getMember("Jon", "Bloom").setPersonId(1);
    household.getMember("Sarah", "Bloom").setPersonId(2);

    assertFalse(household.isNew());
    assertTrue(household.isModified());
    assertFalse(household.getMember("Jon", "Bloom").isNew());
    assertTrue(household.getMember("Jon", "Bloom").isModified());
    assertFalse(household.getMember("Sarah", "Bloom").isNew());
    assertTrue(household.getMember("Sarah", "Bloom").isModified());

    final User modifiedBy = getUser("root");
    final Process modifyingProcess = getProcess("batchApp");

    household.accept(ExtendedAuditableVisitor.getInstance(modifiedBy, modifyingProcess));

    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, modifiedBy, modifyingProcess, household);
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, modifiedBy, modifyingProcess, household.getMember("Jon", "Bloom"));
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, modifiedBy, modifyingProcess, household.getMember("Sarah", "Bloom"));
    assertFalse(household.isNew());
    assertTrue(household.isModified());
    assertFalse(household.getMember("Jon", "Bloom").isNew());
    assertTrue(household.getMember("Jon", "Bloom").isModified());
    assertFalse(household.getMember("Sarah", "Bloom").isNew());
    assertTrue(household.getMember("Sarah", "Bloom").isModified());

    household.commit();
    household.getMember("Jon", "Bloom").commit();
    household.getMember("Sarah", "Bloom").commit();

    assertFalse(household.isNew());
    assertFalse(household.isModified());
    assertFalse(household.getMember("Jon", "Bloom").isNew());
    assertFalse(household.getMember("Jon", "Bloom").isModified());
    assertFalse(household.getMember("Sarah", "Bloom").isNew());
    assertFalse(household.getMember("Sarah", "Bloom").isModified());

    final User newModifiedBy = getUser("hacker");
    final Process newModifyingProcess = getProcess("trojan");

    household.accept(ExtendedAuditableVisitor.getInstance(newModifiedBy, newModifyingProcess));

    assertFalse(household.isNew());
    assertFalse(household.isModified());
    assertFalse(household.getMember("Jon", "Bloom").isNew());
    assertFalse(household.getMember("Jon", "Bloom").isModified());
    assertFalse(household.getMember("Sarah", "Bloom").isNew());
    assertFalse(household.getMember("Sarah", "Bloom").isModified());
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, modifiedBy, modifyingProcess, household);
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, modifiedBy, modifyingProcess, household.getMember("Jon", "Bloom"));
    assertCreatedByModifiedByProcessedBy(newCreatedBy, newCreatingProcess, modifiedBy, modifyingProcess, household.getMember("Sarah", "Bloom"));
  }

}
