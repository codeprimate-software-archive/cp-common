/*
 * PercentTextFormat (c) 8 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.12
 */

package com.cp.common.swing.text;

import com.cp.common.lang.StringUtil;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.log4j.Logger;

public class PercentTextFormat implements TextFormat {

  private static final Logger logger = Logger.getLogger(PercentTextFormat.class);

  private static final char DECIMAL_POINT_CHAR = '.';
  private static final char MINUS_SIGN_CHAR = '-';
  private static final char PERCENT_SIGN_CHAR = '%';

  private static final int MINUS_SIGN_DECIMAL_POSITION = 0;

  private static final String PERCENT_FORMAT = "100.0%";

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
  public String format(final Document doc, final int offset, String text) throws InvalidTextFormatException {
    if (logger.isDebugEnabled()) {
      logger.debug("doc (" + doc.getClass().getName() + ")");
      logger.debug("offset = " + offset);
      logger.debug("text (" + text + ")");
    }

    try {
      final String currentPercent = doc.getText(0, doc.getLength());
      if (logger.isDebugEnabled()) {
        logger.debug("currentPercent (" + currentPercent + ")");
      }

      text = mutate(offset, doc.getLength(), text);
      if (logger.isDebugEnabled()) {
        logger.debug("text mutated (" + text + ")");
      }

      final String composedPercent = StringUtil.insert(currentPercent, text, offset);
      if (logger.isDebugEnabled()) {
        logger.debug("composedPercent (" + composedPercent + ")");
      }

      if (!isValidPercent(composedPercent)) {
        logger.warn("(" + composedPercent + ") is not a valid percent!");
        throw new InvalidTextFormatException("(" + composedPercent + ") is not a valid percent!");
      }
      return text;
    }
    catch (BadLocationException e) {
      logger.error("Failed to insert text (" + text + ") at offset (" + offset + ")!", e);
      throw new InvalidTextFormatException("Failed to insert text (" + text + ") at offset (" + offset + ")!", e);
    }
  }

  /**
   * Determines whether the specified String value is a valid percent format.
   * @param percent the String value representing the current percent value.
   * @return a boolean value indicating if the current String percent is a valid percent format.
   */
  private boolean isValidPercent(final String percent) {
    if (logger.isDebugEnabled()) {
      logger.debug("percent (" + percent + ")");
    }

    if (StringUtil.countOccurences(percent, String.valueOf(DECIMAL_POINT_CHAR)) > 1) {
      logger.warn("The percent value contained more than one decimal point!");
      return false;
    }

    final int END_POSITION = (percent.length() - 1);
    boolean valid = true; // innocent until proven guilty

    for (int index = 0, len = percent.length(); index < len && valid; index++) {
      final char c = percent.charAt(index);
      logger.debug("c = '" + c + "'");
      switch (index) {
        case MINUS_SIGN_DECIMAL_POSITION:
          valid &= (c == MINUS_SIGN_CHAR) || (c == DECIMAL_POINT_CHAR) || Character.isDigit(c);
          break;
        default:
          if (index == END_POSITION) {
            valid &= (c == PERCENT_SIGN_CHAR);
          }
          else {
            valid &= (c == DECIMAL_POINT_CHAR) || Character.isDigit(c);
          }
      }
    }

    return valid;
  }

  /**
   * Mutates the inserted value to conform to the percent format as defined by this TextFormat.
   * @param offset the offset into the document at which the value will be inserted, thus dictating
   * the type of format that is imposed on the value.
   * @param endPosition an integer value indicating the end of the document.
   * @param value the value being inserted into the document of the text component.
   * @return the modified value with percent formatting imposed.
   */
  private String mutate(final int offset, final int endPosition, String value) {
    if (logger.isDebugEnabled()) {
      logger.debug("offset = " + offset);
      logger.debug("value (" + value + ")");
    }
    if (!StringUtil.isEmpty(value)) {
      if ((offset == endPosition) && !value.endsWith(String.valueOf(PERCENT_SIGN_CHAR))) {
        value += PERCENT_SIGN_CHAR;
      }
    }
    return value;
  }

  /**
   * Returns a String description of this class.
   * @return a String value describing this class.
   */
  public String toString() {
    return getClass().getName() + "(" + PERCENT_FORMAT + ")";
  }

}
