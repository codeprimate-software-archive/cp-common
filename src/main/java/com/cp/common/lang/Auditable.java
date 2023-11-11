/*
 * Auditable.java (c) 5 August 2003
 *
 * The Auditable interface represents a final state of accounts on Object instances of Classes implementing this
 * interface, from who and/or what created the instance to who and/or what modified it and when.  This interface
 * may also provide relevant information into the current state of the instance such as whether the Object is
 * newly created, or has just been modified up to and including specifically what properties have been modified.
 * This interface also serves to maintain the Object instances last modification account.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.6
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.Process
 * @see com.cp.common.beans.User
 * @see java.util.Calendar
 */

package com.cp.common.lang;

import com.cp.common.beans.Process;
import com.cp.common.beans.User;
import java.util.Calendar;

public interface Auditable {

  /**
   * Returns the user responsible for the creation of this Object.
   * @return a User object referring to the user that created this Object.
   */
  public User getCreatedBy();

  /**
   * Set the user responsible for creating this Object.
   * @param createdBy a User object referring to the user that created this Object.
   */
  public void setCreatedBy(User createdBy);

  /**
   * Returns the date/time when this Object was created.
   * @return a Calendar object specifying when this Object was created.
   */
  public Calendar getCreatedDateTime();

  /**
   * Set the date/time when this Object was created.
   * @param createdDateTime a Calendar object specifying the date/time when this Object was created.
   */
  public void setCreatedDateTime(Calendar createdDateTime);

  /**
   * Returns the process responsible for creating this Object.
   * @return a Process object specifying the process that created this Object.
   */
  public Process getCreatingProcess();

  /**
   * Set the creatingProcess responsible for creating this Object.
   * @param creatingProcess a Process object specifying the creatingProcess that craeted this Object.
   */
  public void setCreatingProcess(Process creatingProcess);

  /**
   * Returns the last user reponsible for modifying the state of this Object.
   * @return a User object referring to the last user that modified the state of this Object.
   * @see Auditable#getModifiedBy()
   */
  public User getLastModifiedBy();

  /**
   * Returns the last date/time when this Object's state was modified.
   * @return a Calendar object specifying the last date/time this Object's state was modified.
   */
  public Calendar getLastModifiedDateTime();

  /**
   * Determines whether this Object's state has changed, or been modified.
   * @return a boolean value indicating if this Object's state has changed, or been modified.
   * @see Auditable#isModified(String)
   * @see Auditable#isNew()
   */
  public boolean isModified();

  /**
   * Determines whether the specified propertyName of the Object implementing this interface has been modified.
   * @param propertyName the String name of the propertyName.
   * @return a boolean value indicating if the specified propertyName of the Object implementing this interface
   * has been modified.
   * @see Auditable#isModified()
   */
  public boolean isModified(String propertyName);

  /**
   * Returns the user responsible for modifying this Object's state (properties).
   * @return a User object referring to the user responsible for modifying this Object's state (properties).
   */
  public User getModifiedBy();

  /**
   * Set the user responsible for modifying this Object's state (properties).
   * @param modifiedBy a User object referring to the user responsible for modifying this Object's state (properties).
   */
  public void setModifiedBy(User modifiedBy);

  /**
   * Returns the date/time when this Object's state was modified.
   * @return a Calendar object specifying the date/time when this Object's state was modified.
   */
  public Calendar getModifiedDateTime();

  /**
   * Set the date/time when this Object's state was modified.
   * @param modifiedDateTime a Calendar object specifying the date/time when this Object's state was modified.
   */
  public void setModifiedDateTime(Calendar modifiedDateTime);

  /**
   * Returns the process responsible for modifying this Object's state (properties).
   * @return a Process object specifying the process state modified this Object's state (properties).
   */
  public Process getModifyingProcess();

  /**
   * Sets the process responsible for modifying this Object's state (properties).
   * @param process a Process object specifying the process that modified this Object's state (properties).
   */
  public void setModifyingProcess(Process process);

  /**
   * Determines whether this Object is new, has not been persisted to a data store.
   * @return a boolean value indicating whether this Object is new.
   * @see Auditable#isModified()
   */
  public boolean isNew();

}
