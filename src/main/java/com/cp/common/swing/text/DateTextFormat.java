/*
 * DateTextFormat (c) 2004 October 5
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.9
 * @see com.cp.common.swing.text.TextFormat
 */

package com.cp.common.swing.text;

import com.cp.common.lang.StringUtil;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.log4j.Logger;

public class DateTextFormat implements TextFormat {

  private static final Logger logger = Logger.getLogger(DateTextFormat.class);

  private static final char FORWARD_SLASH_CHAR = '/';

  private static final int FIRST_FORWARD_SLASH_POSITION = 2;
  private static final int SECOND_FORWARD_SLASH_POSITION = 5;

  private static final String DATE_FORMAT = "MM/dd/yyyy";

  /**
   * Verifies that the text that will be inserted into the JTextField component has a valid format and performs
   * any other mutations on the text as determined by the validation and formatting rules specified by instances
   * of this class.
   * @param doc the Document object representing the model used by the JTextField component to format and verify
   * input.
   * @param offset is an integer value specifying the offset into the Document.
   * @param text a String object containing the text to format and validate before inserting into the
   * JTextField component.
   * @throws com.cp.common.swing.text.InvalidTextFormatException if the text format is not valid input to the
   * text field component.
   */
  public final String format(final Document doc, final int offset, String text) throws InvalidTextFormatException {
    if (logger.isDebugEnabled()) {
      logger.debug("doc (" + doc.getClass().getName() + ")");
      logger.debug("offset = " + offset);
      logger.debug("text (" + text + ")");
    }

    try {
      final String currentDate = doc.getText(0, doc.getLength());
      if (logger.isDebugEnabled()) {
        logger.debug("currentDate (" + currentDate + ")");
      }

      text = mutate(offset, text);
      if (logger.isDebugEnabled()) {
        logger.debug("text mutated (" + text + ")");
      }

      final String composedDate = StringUtil.insert(currentDate, text, offset);
      if (logger.isDebugEnabled()) {
        logger.debug("composedDate (" + composedDate + ")");
      }

      if (!isValidDateFormat(composedDate)) {
        logger.warn("(" + composedDate + ") is not a valid date!");
        throw new InvalidTextFormatException("(" + composedDate + ") is not a valid date!");
      }
      return text;
    }
    catch (BadLocationException e) {
      logger.error("Failed to insert text (" + text + ") at offset (" + offset + ")!", e);
      throw new InvalidTextFormatException("Failed to insert text (" + text + ") at offset (" + offset + ")!", e);
    }
  }

  /**
   * Determines whether the specified String value is a valid date format.
   * @param dateValue the String value representing the current date value.
   * @return a boolean value indicating if the current String dateValue is a valid date format.
   */
  protected boolean isValidDateFormat(final String dateValue) {
    if (logger.isDebugEnabled()) {
      logger.debug("dateValue (" + dateValue + ")");
    }

    if (dateValue.length() > DATE_FORMAT.length()) {
      logger.debug("invalid length!");
      return false;
    }

    boolean valid = true; // innocent until proven guilty
    for (int index = 0, len = dateValue.length(); index < len && valid; index++) {
      final char c = dateValue.charAt(index);
      logger.debug("c = '" + c + "'");
      switch (index) {
        case FIRST_FORWARD_SLASH_POSITION:
        case SECOND_FORWARD_SLASH_POSITION:
          valid &= (c == FORWARD_SLASH_CHAR);
          break;
        default:
          valid &= Character.isDigit(c);
      }
    }
    return valid;
  }

  /**
   * Mutates the inserted value to conform to the date format as defined by this TextFormat.
   * @param offset the offset into the document at which the value will be inserted, thus dictating
   * the type of format that is imposed on the value.
   * @param value the value being inserted into the document of the text component.
   * @return the modified value with date formatting imposed.
   */
  private String mutate(final int offset, final String value) {
    if (logger.isDebugEnabled()) {
      logger.debug("offset = " + offset);
      logger.debug("value (" + value + ")");
    }

    // first, get the digits from the value
    final String valueDigits = StringUtil.getDigitsOnly(value);
    if (logger.isDebugEnabled()) {
      logger.debug("valueDigits (" + valueDigits + ")");
    }

    if (!StringUtil.isEmpty(valueDigits)) {
      final StringBuffer buffer = new StringBuffer();

      for (int position = offset, index = 0, len = valueDigits.length(); index < len; position++) {
        switch (position) {
          case FIRST_FORWARD_SLASH_POSITION:
          case SECOND_FORWARD_SLASH_POSITION:
            buffer.append(FORWARD_SLASH_CHAR);
            break;
          default:
            buffer.append(valueDigits.charAt(index++));
        }
      }

      return buffer.toString();
    }

    return value;
  }

  /**
   * Returns a String description of this class.
   * @return a String value describing this class.
   */
  public String toString() {
    return getClass().getName() + "(" + DATE_FORMAT + ")";
  }

}
