/*
 * CPActionMapping.java (c) 3 May 2004
 *
 * Copyright (c) 2004, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 */

package com.cp.common.struts;

import com.cp.common.lang.Assert;
import org.apache.struts.action.ActionMapping;

public class CPActionMapping extends ActionMapping {

  private String sessionObjectKey = null;
  private String validatorKey = null;

  /**
   * Gets the key used to store an object in session that will be used by an Action class.
   * @return the a String value indicating the key used to store an object in session
   * as defined by this ActionMapping.
   */
  public String getSessionObjectKey() {
    return sessionObjectKey;
  }

  /**
   * Sets the key used to store an object in session by an Action class that will use the object.
   * @param sessionObjectKey is a String value indicating the key used to store an object in session
   * as defined by this ActionMapping.
   */
  public void setSessionObjectKey(final String sessionObjectKey) {
    Assert.notEmpty(sessionObjectKey, "The session object key must be specified!");
    this.sessionObjectKey = sessionObjectKey;
  }

  /**
   * Gets a key referring to the Validator used by an Action class to validate the contents of an ActionForm.
   * @return a String value indicating the key referring the to the Validator instance.
   */
  public String getValidatorKey() {
    return validatorKey;
  }

  /**
   * Sets a key referring to the Validator used by an Action class to validate the contents of an ActionForm.
   * @param validatorKey a String value indicating the key referring the to the Validator instance.
   */
  public void setValidatorKey(final String validatorKey) {
    Assert.notEmpty(validatorKey, "The validator key must be specified!");
    this.validatorKey = validatorKey;
  }

  /**
   * Returns a String containing the state of this ActionMapping instance.
   * @return a String representation of this ActionMapping instance.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{sessionObjectKey = ");
    buffer.append(getSessionObjectKey());
    buffer.append(", validatorKey = ").append(getValidatorKey());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
