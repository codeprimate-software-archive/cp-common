/*
 * ExtendedAuditableVisitor.java (c) 29 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.12
 * @see com.cp.common.beans.Process
 * @see com.cp.common.beans.User
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.lang.support.AuditableVisitor
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.beans.Process;
import com.cp.common.beans.User;
import com.cp.common.lang.Assert;
import com.cp.common.lang.Auditable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import java.util.Calendar;

public class ExtendedAuditableVisitor extends AuditableVisitor {

  private Process creatingProcess;
  private Process modifyingProcess;

  /**
   * Creates an instance of the ExtendedAuditableVisitor class initialized with audit information.
   * @param createdBy the person/user who created the Auditable object.
   * @param creatingProcess the process that created the Auditable object.
   * @param createdDate the date the Auditable object was created.
   * @param modifiedBy the person/user who modified the Auditable object.
   * @param modifyingProcess the process that modified the Auditable object.
   * @param modifiedDate the date the Auditable object was modified.
   */
  protected ExtendedAuditableVisitor(final User createdBy,
                                     final Process creatingProcess,
                                     final Calendar createdDate,
                                     final User modifiedBy,
                                     final Process modifyingProcess,
                                     final Calendar modifiedDate) {
    super(createdBy, createdDate, modifiedBy, modifiedDate);
    setCreatingProcess(creatingProcess);
    setModifyingProcess(modifyingProcess);
  }

  /**
   * Creates an instance of the ExtendedAuditableVisitor class initialized with audit information.
   * @param createdBy the person/user who created the Auditable object.
   * @param creatingProcess the process that created the Auditable object.
   * @param createdDate the date the Auditable object was created.
   * @param modifiedBy the person/user who modified the Auditable object.
   * @param modifyingProcess the process that modified the Auditable object.
   * @param modifiedDate the date the Auditable object was modified.
   * @return an instance of the ExtendedAuditableVisitor.
   */
  public static ExtendedAuditableVisitor getInstance(final User createdBy,
                                                     final Process creatingProcess,
                                                     final Calendar createdDate,
                                                     final User modifiedBy,
                                                     final Process modifyingProcess,
                                                     final Calendar modifiedDate) {
    return new ExtendedAuditableVisitor(createdBy, creatingProcess, createdDate, modifiedBy, modifyingProcess, modifiedDate);
  }

  /**
   * Creates an instance of the ExtendedAuditableVisitor class initialized with audit information.
   * @param changedBy the person/user who changed (created/modified) the Auditable object.
   * @param changingProcess the process that changed (created/modified ) the Auditable object.
   * @param changedDate the date the Auditable object was changed (created/modified).
   * @return an instance of the ExtendedAuditableVisitor.
   */
  public static ExtendedAuditableVisitor getInstance(final User changedBy,
                                                     final Process changingProcess,
                                                     final Calendar changedDate) {
    return new ExtendedAuditableVisitor(changedBy, changingProcess, changedDate, changedBy, changingProcess, changedDate);
  }

  /**
   * Creates an instance of the ExtendedAuditableVisitor class initialized with audit information.  The changed date
   * (created/modified date) is set to now.
   * @param changedBy the person/user who changed (created/modified) the Auditable object.
   * @param changingProcess the process that changed (created/modified ) the Auditable object.
   * @return an instance of the ExtendedAuditableVisitor.
   */
  public static ExtendedAuditableVisitor getInstance(final User changedBy, final Process changingProcess) {
    final Calendar now = Calendar.getInstance();
    return new ExtendedAuditableVisitor(changedBy, changingProcess, now, changedBy, changingProcess, now);
  }

  /**
   * Gets the process that created the Auditable object.
   * @return a Process instance that represents the creating process of the Auditable object.
   */
  public Process getCreatingProcess() {
    return creatingProcess;
  }

  /**
   * Sets the process that created the Auditable object.
   * @param creatingProcess the Process instance that represents the creating process of the Auditable object.
   */
  private void setCreatingProcess(final Process creatingProcess) {
    Assert.notNull(creatingProcess, "The creating process cannot be null!");
    this.creatingProcess = creatingProcess;
  }

  /**
   * Gets the process that modified the Auditable object.
   * @return a Process instance that represents the modifying process of the Auditable object.
   */
  public Process getModifyingProcess() {
    return modifyingProcess;
  }

  /**
   * Sets the process that modified the Auditable object.
   * @param modifyingProcess the Process instance that represents the modifying process of the Auditable object.
   */
  private void setModifyingProcess(Process modifyingProcess) {
    Assert.notNull(modifyingProcess, "The modifying process cannot be null!");
    this.modifyingProcess = modifyingProcess;
  }

  /**
   * Determines whether the creatingProcess audit information on the Auditable object is null.
   * @param auditable the Auditable object in question.
   * @return a boolean value indicating whether the creatingProcess audit information on the Auditable object is null.
   */
  protected boolean isCreatingProcessNull(final Auditable auditable) {
    return ObjectUtil.isNull(auditable.getCreatingProcess());
  }

  /**
   * Determines whether the modifyingProcess audit information on the Auditable object is null.
   * @param auditable the Auditable object in question.
   * @return a boolean value indicating whether the modifyingProcess audit information on the Auditable object is null.
   */
  protected boolean isModifyingProcessNull(final Auditable auditable) {
    return ObjectUtil.isNull(auditable.getModifyingProcess());
  }

  /**
   * Visits the specified Visitable object performing an audit operation.
   * @param visitableObject the Visitable object visited by this visitor.
   */
  public void visit(final Visitable visitableObject) {
    super.visit(visitableObject);

    if (visitableObject instanceof Auditable) {
      final Auditable auditableObject = (Auditable) visitableObject;

      if (auditableObject.isNew() || isCreatingProcessNull(auditableObject)) {
        if (logger.isDebugEnabled()) {
          logger.debug("populating the Auditable object's creatingProcess property to (" + getCreatingProcess() + ")...");
        }
        auditableObject.setCreatingProcess(getCreatingProcess());
      }

      if (auditableObject.isModified() || isModifyingProcessNull(auditableObject)) {
        logger.debug("populating the Auditable object's modifyingProcess property to (" + getModifyingProcess() + ")...");
        auditableObject.setModifyingProcess(getModifyingProcess());
      }
    }
  }

}
