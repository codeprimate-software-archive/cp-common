/*
 * BoundedLengthTextFormat.java (c) 20 July 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.5
 * @see com.cp.common.swing.text.TextFormat
 */

package com.cp.common.swing.text;

import javax.swing.text.Document;
import org.apache.log4j.Logger;

public final class BoundedLengthTextFormat implements TextFormat {

  private static final Logger logger = Logger.getLogger(BoundedLengthTextFormat.class);

  private final int maxLength;

  /**
   * Creates an instance of the BoundedLengthTextFormat class to restrict the length of text entered in the
   * JFormattedField component to the specified maximum length.
   * @param maxLength is a integer value specifying the maximum number of characters (length of text) that can be
   * inserted into the JFormattedField component.
   */
  public BoundedLengthTextFormat(final int maxLength) {
    logger.debug("maxLength = " + maxLength);
    this.maxLength = maxLength;
  }

  /**
   * Returns the maximum number characters allowed by this TextFormat.
   * @return a integer value specifying the maximum number characters allowed by this TextFormat.
   */
  public int getMaxLength() {
    return maxLength;
  }

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
  public String format(final Document doc, final int offset, final String text) throws InvalidTextFormatException {
    if (logger.isDebugEnabled()) {
      logger.debug("doc (" + doc.getClass().getName() + ")");
      logger.debug("offset = " + offset);
      logger.debug("text (" + text + ")");
    }
    if ((doc.getLength() + text.length()) > getMaxLength()) {
      logger.warn("The length of the text to be inserted plus the current text length exceeds the maximum length of "
        + getMaxLength());
      throw new InvalidTextFormatException("The length of the text to be inserted plus the current text length exceeds the maximum length of "
        + getMaxLength());
    }
    return text;
  }

  /**
   * Returns a String description of this class.
   * @return a String value describing this class.
   */
  public String toString() {
    return "BoundedLengthTextFormat (" + getMaxLength() + " characters)";
  }

}
