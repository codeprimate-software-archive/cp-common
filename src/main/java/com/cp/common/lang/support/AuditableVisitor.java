/*
 * AuditableVisitor.java (c) 11 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.12
 * @see com.cp.common.beans.User
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.DateUtil
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.beans.User;
import com.cp.common.lang.Assert;
import com.cp.common.lang.Auditable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Visitor;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuditableVisitor implements Visitor {

  private static final Format DATE_FORMAT_PATTERN = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

  private Calendar createdDate;
  private Calendar modifiedDate;

  protected final Log logger = LogFactory.getLog(getClass());

  private User createdBy;
  private User modifiedBy;

  /**
   * Creates an instance of the AuditableVisitor class initiliazed with the specified Auditable information.
   * @param createdBy the User who created the visited object.
   * @param createdDate the date which the object visited by this Visitor was created.
   * @param modifiedBy the User who edited the visited object.
   * @param modifiedDate the date which the object visited by this Visitor was edited.
   */
  protected AuditableVisitor(final User createdBy,
                             final Calendar createdDate,
                             final User modifiedBy,
                             final Calendar modifiedDate) {
    setCreatedBy(createdBy);
    setCreatedDate(createdDate);
    setModifiedBy(modifiedBy);
    setModifiedDate(modifiedDate);
  }

  /**
   * A factory method used to instantiate an instance of the AuditableVisitor class.
   * @param createdBy the User who created the visited object.
   * @param createdDate the date which the object visited by this Visitor was created.
   * @param modifiedBy the User who edited the visited object.
   * @param modifiedDate the date which the object visited by this Visitor was edited.
   * @return an instance of the AuditableVisitor class initialized to the specified created
   * and modified property values.
   */
  public static AuditableVisitor getInstance(final User createdBy,
                                             final Calendar createdDate,
                                             final User modifiedBy,
                                             final Calendar modifiedDate) {
    return new AuditableVisitor(createdBy, createdDate, modifiedBy, modifiedDate);
  }

  /**
   * A factory method used to instantiate an instance of the AuditableVisitor class having
   * the same createdBy and modifiedBy and same createdDate and modifiedDate property values.
   * @param changedBy the User who created/modified the visited object.
   * @param changedDate the date/time when the visited object was created/modified.
   * @return an instance of the AuditableVisitor class initialized to the specified changed by
   * and changed date property values.
   */
  public static AuditableVisitor getInstance(final User changedBy, final Calendar changedDate) {
    return new AuditableVisitor(changedBy, changedDate, changedBy, changedDate);
  }

  /**
   * A factory method used to instantiate an instance of the AuditableVisitor class, defaulting
   * the time to current date/time (now) and setting both the createdBy and changedBy properties
   * to the specified User.
   * @param changedBy the User who created and modified this object.
   * @return an instance of the AuditableVisitor class initializing createdBy and changedBy to the
   * specified changedBy User value and setting date/time to now.
   */
  public static AuditableVisitor getInstance(final User changedBy) {
    final Calendar now = Calendar.getInstance();
    return new AuditableVisitor(changedBy, now, changedBy, now);
  }

  /**
   * Returns the User who will be identified as the one who created a new object visited by this Visitor.
   * @return the User responsible for creating new objects visited by this Visitor.
   */
  public User getCreatedBy() {
    return createdBy;
  }

  /**
   * Sets the User that this Visitor will use when setting the createdBy property on Auditable objects.
   * @param createdBy the designated User responsible for creating object visited by this Visitor.
   * @throws NullPointerException if the createdBy value is null.
   */
  private void setCreatedBy(final User createdBy) {
    Assert.notNull(createdBy, "The createdBy property cannot be null!");
    this.createdBy = createdBy;
  }

  /**
   * Returns specified date/time that the Visitor will use for setting the createdDate property on new
   * visited objects.
   * @return a Calendar object signifying the date/time that the visited object was created.
   */
  public Calendar getCreatedDate() {
    return DateUtil.copy(createdDate);
  }

  /**
   * Sets specified date/time that the Visitor will use for setting the createdDate property on new
   * visited objects.
   * @param createdDate the date/time used to populate the createdDate property of the Auditable visited
   * objects.
   * @throws NullPointerException if the createdDate value is null.
   */
  private void setCreatedDate(final Calendar createdDate) {
    Assert.notNull(createdDate, "The createdDate property cannot be null!");
    this.createdDate = DateUtil.copy(createdDate);
  }

  /**
   * Returns the User who will be identified as the one who modified the object visited by this Visitor.
   * @return the User responsible for modifying objects visited by this Visitor.
   */
  public User getModifiedBy() {
    return modifiedBy;
  }

  /**
   * Sets the User that this Visitor will use when setting the modifiedBy property on Auditable objects.
   * @param modifiedBy the designated User responsible for modifying objects visited by this Visitor.
   * @throws NullPointerException if the modifiedBy value is null.
   */
  private void setModifiedBy(final User modifiedBy) {
    Assert.notNull(modifiedBy, "The modifiedBy property cannot be null!");
    this.modifiedBy = modifiedBy;
  }

  /**
   * Returns specified date/time that the Visitor will use for setting the modifiedDate property on
   * visited objects.
   * @return a Calendar object signifying the date/time that the visited object was modified.
   */
  public Calendar getModifiedDate() {
    return DateUtil.copy(modifiedDate);
  }

  /**
   * Sets specified date/time that the Visitor will use for setting the modifiedDate property on
   * visited objects.
   * @param modifiedDate the date/time used to populate the modifiedDate property of the Auditable visited
   * objects.
   * @throws NullPointerException if the modifiedDate value is null.
   */
  private void setModifiedDate(final Calendar modifiedDate) {
    Assert.notNull(modifiedDate, "The modifiedDate property cannot be null!");
    this.modifiedDate = DateUtil.copy(modifiedDate);
  }

  /**
   * Determines whether the specified Auditable object has created information set on the object.
   * @param obj the Auditable object used to determine if created information has been set on the object.
   * @return a boolean value indicating whether the Auditable object has created information.
   */
  protected boolean isCreatedInfoNull(final Auditable obj) {
    return ObjectUtil.isNull(obj.getCreatedBy());
  }

  /**
   * Determines whether the specified Auditable object has modified information set on the object.
   * @param obj the Auditable object used to determine if modified information has been set on the object.
   * @return a boolean value indicating whether the Auditable object has modified information.
   */
  protected boolean isModifiedInfoNull(final Auditable obj) {
    return ObjectUtil.isNull(obj.getModifiedBy());
  }

  /**
   * Visits the specified Visitable object performing an audit operation.
   * @param visitableObject the Visitable object visited by this visitor.
   */
  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof Auditable) {
      final Auditable auditableObject = (Auditable) visitableObject;

      if (logger.isDebugEnabled()) {
        logger.debug("visiting auditable properties of auditable object (" + auditableObject.getClass().getName() + ")");
      }

/*
       NOTE: setting created date if at has already been set when the createdBy has not is a bit misleading.
       When the createdDate is set and the createdBy is not, this is more like assigning ownership for
       creation (existence) of the Auditable object.
*/
      if (auditableObject.isNew() || isCreatedInfoNull(auditableObject)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Populating Bean's createdBy (" + getCreatedBy() + ") and createdDate ("
            + DATE_FORMAT_PATTERN.format(getCreatedDate().getTime()) + ") properties.");
        }

        auditableObject.setCreatedBy(getCreatedBy());
        auditableObject.setCreatedDateTime(getCreatedDate());
      }

      if (auditableObject.isModified() || isModifiedInfoNull(auditableObject)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Populating Bean's modifiedBy (" + getModifiedBy() + ") and modifiedDate ("
            + DATE_FORMAT_PATTERN.format(getModifiedDate().getTime()) + ") properties.");
        }

        auditableObject.setModifiedBy(getModifiedBy());
        auditableObject.setModifiedDateTime(getModifiedDate());
      }
    }
  }

}
