/*
 * ValidationUtil.java (c) 15 September 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.9.15
 */

package com.cp.common.util;

import com.cp.common.lang.StringUtil;

public class ValidationUtil {

  /**
   * Determines whether the specified SSN is valid or not.
   * @param ssn a String value specifying the SSN being tested for validity.
   * @return a boolean value indicating whether the SSN is valid.
   */
  public static boolean isValidSsn(String ssn) {
    if (StringUtil.isEmpty(ssn) || ssn.length() != 11) {
      return false;
    }

    if (ssn.charAt(3) != '-' || ssn.charAt(6) != '-') {
      return false;
    }

    ssn = StringUtil.replace(ssn, "-", "");

    return (StringUtil.isDigitsOnly(ssn) && (ssn.length() == 9));
  }

}
