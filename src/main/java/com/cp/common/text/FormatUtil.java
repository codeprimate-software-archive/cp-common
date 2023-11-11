/*
 * FormatUtil.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.17
 */

package com.cp.common.text;

import com.cp.common.lang.Assert;
import com.cp.common.lang.BooleanUtil;
import com.cp.common.lang.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FormatUtil {

  private static final Log log = LogFactory.getLog(FormatUtil.class);

  public static final String DEFAULT_PHONE_NUMBER_PATTERN = "(###) ###-####";

  /**
   * Default private constructor to prevent instantiation of the FormatUtil utility class.
   */
  private FormatUtil() {
  }

  /**
   * Formats the given Boolean object into a corresponding String representation, defaulting to true and false
   * respectively.  If the given Boolean object is null, then the String representation is "false".
   * @param condition the Boolean value to format as a String value.
   * @return a String representation of the Boolean value.
   * @see FormatUtil#format(Boolean, String, String)
   */
  public static String format(final Boolean condition) {
    return format(condition, "true", "false");
  }

  /**
   * Formats the given Boolean object into a corresponding String representation, defaulting to true and false
   * respectively.  If the given Boolean object is null, then the String representation is "false".
   * @param condition the Boolean value to format as a String value.
   * @param trueValue the String value representing a true boolean value.
   * @param falseValue the String value representing a false boolean value.
   * @return a String representation of the Boolean value.
   * @see FormatUtil#format(Boolean)
   */
  public static String format(final Boolean condition, final String trueValue, final String falseValue) {
    return (BooleanUtil.valueOf(condition) ? trueValue : falseValue);
  }

  /**
   * Formats a phone number String using the default pattern (###) ###-####.
   * @param phoneNumber a String value containing a phone number.
   * @return a phone number String formatted using the default pattern.
   * @see FormatUtil#formatPhoneNumber(String, String)
   */
  public static String formatPhoneNumber(final String phoneNumber) {
    return formatPhoneNumber(phoneNumber, DEFAULT_PHONE_NUMBER_PATTERN);
  }

  /**
   * Formats a phone number String using the specified pattern.
   * @param phoneNumber a String value containing a phone number.
   * @param pattern the String pattern used to format the phone number.
   * @return a phone number String formatted using the specified format.
   * @see FormatUtil#formatPhoneNumber(String)
   */
  public static String formatPhoneNumber(String phoneNumber, final String pattern) {
    Assert.notEmpty(pattern, "The pattern cannot be null or empty!");

    final String phoneNumberCopy = phoneNumber;

    if (log.isDebugEnabled()) {
      log.debug("phone number (" + phoneNumber + ")");
      log.debug("pattern (" + pattern + ")");
    }

    if (StringUtil.isEmpty(phoneNumber)) {
      return "";
    }

    phoneNumber = StringUtil.getDigitsOnly(phoneNumber);

    final StringBuffer buffer = new StringBuffer();
    final char[] phoneNumberDigits = phoneNumber.toCharArray();
    int index = 0;

    for (char patternElement : pattern.toCharArray()) {
      if (patternElement == '#') {
        try {
          buffer.append(phoneNumberDigits[index++]);
        }
        catch (ArrayIndexOutOfBoundsException e) {
          log.warn("The phone number (" + phoneNumberCopy + ") cannot be formatted using pattern (" + pattern + ")!", e);
          throw new IllegalFormatException("The phone number (" + phoneNumberCopy + ") cannot be formatted using pattern ("
            + pattern + ")!", e);
        }
      }
      else {
        buffer.append(patternElement);
      }
    }


    return buffer.toString();
  }

  /**
   * Formats a numerical String representing an SSN number into the following
   * format: xxx-xx-xxxx.  If the SSN parameter is null, or the empty String,
   * the method returns an empty String.
   * @param ssn the numerical based String representation of an SSN.
   * @return the SSN formated with dashes.
   */
  public static String formatSsn(String ssn) {
    // if ssn is null or an empty String, return an empty String
    if (StringUtil.isEmpty(ssn)) {
      return "";
    }

    ssn = StringUtil.getDigitsOnly(ssn);

    // if ssn is not null, ensure that the ssn is valid (consists of only numbers and has 9 digits).
    if (ssn.length() != 9) {
      log.warn("Invalid SSN (" + ssn + ")!");
      throw new IllegalArgumentException("The SSN (" + ssn + ") is not valid!  The SSN must consist of 9 digits.");
    }

    final StringBuffer buffer = new StringBuffer();
    buffer.append(ssn.substring(0, 3));
    buffer.append("-");
    buffer.append(ssn.substring(3, 5));
    buffer.append("-");
    buffer.append(ssn.substring(5));
    return buffer.toString();
  }

}
