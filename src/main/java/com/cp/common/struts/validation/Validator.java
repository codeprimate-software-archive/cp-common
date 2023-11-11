/*
 * Validator.java (c) 27 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 * @see com.cp.common.struts.CPActionForm
 */

package com.cp.common.struts.validation;

import com.cp.common.struts.CPActionForm;

public interface Validator {

  /**
   * Validates the data types of the properties on the form.  These properties correspond to fields in the HTML form
   * of the user interface.
   * @param form the ActionForm instance to validate.
   * @throws ValidationException if validation of property value data types fail.
   */
  public void validateDataTypes(CPActionForm form) throws ValidationException;

  /**
   * Validates the all required fields in the HTML form on the client were set by the user.
   * @param form the ActionForm instance to validate.
   * @throws ValidationException if a required field in the HTML form was not entered by a user.
   */
  public void validateRequiredFields(CPActionForm form) throws ValidationException;

}
