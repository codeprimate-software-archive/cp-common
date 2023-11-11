/*
 * MockAuditableObject.java (c) 25 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Auditable
 */

package com.cp.common.test.mock;

import com.cp.common.beans.Process;
import com.cp.common.beans.User;
import com.cp.common.lang.Auditable;
import java.util.Calendar;

public abstract class MockAuditableObject implements Auditable {

  public User getCreatedBy() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setCreatedBy(final User createdBy) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Calendar getCreatedDate() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setCreatedDate(final Calendar createdDate) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Process getCreatingProcess() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setCreatingProcess(final Process creatingProcess) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public User getLastModifiedBy() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Calendar getLastModifiedDate() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public boolean isModified() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public boolean isModified(final String propertyName) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public User getModifiedBy() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setModifiedBy(final User modifiedBy) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Calendar getModifiedDate() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setModifiedDate(final Calendar modifiedDate) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Process getModifyingProcess() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setModifyingProcess(final Process process) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public boolean isNew() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
