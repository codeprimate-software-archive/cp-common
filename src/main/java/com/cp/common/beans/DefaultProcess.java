/*
 * DefaultProcess.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.Process
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.ObjectUtil
 */

package com.cp.common.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public class DefaultProcess implements Process {

  private final String processName;

  /**
   * Creates an instance of DefaultProcess initialized with the process name.
   * @param processName the name of the process represented by this object.
   */
  public DefaultProcess(final String processName) {
    Assert.notNull(processName, "The process name cannot be null!");
    this.processName = processName;
  }

  /**
   * Creates an instance of the DefaultProcess class intialized to the Process object.
   * @param process the Process object used to initialize this instance.
   */
  public DefaultProcess(final Process process) {
    Assert.notNull(process, "The process cannot be null!");
    this.processName = process.getProcessName();
  }

  /**
   * Return the name of the process represented by this object.
   * @return a String value indicating the name of the process.
   */
  public String getProcessName() {
    return processName;
  }

  /**
   * Determines whether the specified object is equal to this process.
   * @param obj the Object value being compared for equality with this process.
   * @return a boolean value indicating whether the specified object is equal to this process.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Process)) {
      return false;
    }

    final Process that = (Process) obj;

    return ObjectUtil.equals(getProcessName(), that.getProcessName());
  }

  /**
   * Computes the hash value of this process.
   * @return a integer value of the computed hash code of this process.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getProcessName());
    return hashValue;
  }

  /**
   * Returns a String representation of this process.
   * @return the process name.
   */
  @Override
  public String toString() {
    return getProcessName();
  }

}
