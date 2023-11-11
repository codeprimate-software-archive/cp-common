/*
 * IsNewVisitor.java (c) 14 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.23
 */

package com.cp.common.beans.support;

import com.cp.common.beans.AbstractBean;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import org.apache.log4j.Logger;

public class SyncAuditablePropertiesVisitor implements Visitor {

  private static final Logger logger = Logger.getLogger(SyncAuditablePropertiesVisitor.class);

  public static final SyncAuditablePropertiesVisitor INSTANCE = new SyncAuditablePropertiesVisitor();

  /**
   * Private constructor to enforce non-instantiability of the SyncAuditablePropertiesVisitor class.
   */
  private SyncAuditablePropertiesVisitor() {
  }

  /**
   * Visits each bean in the object graph setting the lastModifiedBy and lastModifiedDate properties
   * to the values of the modifiedBy and modifiedDate properties respectively.
   * @param visitableObject the bean object visited by this Visitor.
   */
  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof AbstractBean) {
      final AbstractBean beanObject = (AbstractBean) visitableObject;

      if (logger.isDebugEnabled()) {
        logger.debug("synchronizing the lastModified properties of the bean (" + beanObject.getClass().getName()
          + ") to the modified properties");
      }

      if (beanObject.isModified()) {
        beanObject.setLastModifiedBy(beanObject.getModifiedBy());
        beanObject.setLastModifiedDate(beanObject.getModifiedDate());
      }
    }
  }

}
