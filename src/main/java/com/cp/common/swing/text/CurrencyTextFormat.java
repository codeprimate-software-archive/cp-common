/*
 * CurrencyTextFormat (c) 8 October 2004
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

public class CurrencyTextFormat implements TextFormat {

  private static final Logger logger = Logger.getLogger(CurrencyTextFormat.class);

  private static final char DECIMAL_POINT_CHAR = '.';
  private static final char DOLLAR_SIGN_CHAR = '$';
  private static final char MINUS_SIGN_CHAR = '-';

  private static final int DOLLAR_SIGN_POSITION = 0;
  private static final int MINUS_SIGN_DECIMAL_POSITION = 1;

  private static final String CURRENCY_FORMAT = "$0.00";

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
      final String currentCurrency = doc.getText(0, doc.getLength());
      if (logger.isDebugEnabled()) {
        logger.debug("currentCurrency (" + currentCurrency + ")");
      }

      text = mutate(offset, text);
      if (logger.isDebugEnabled()) {
        logger.debug("text mutated (" + text + ")");
      }

      final String composedCurrency = StringUtil.insert(currentCurrency, text, offset);
      if (logger.isDebugEnabled()) {
        logger.debug("composedCurrency (" + composedCurrency + ")");
      }

      if (!isValidCurrency(composedCurrency)) {
        logger.warn("(" + composedCurrency + ") is not a valid currency!");
        throw new InvalidTextFormatException("(" + composedCurrency + ") is not a valid currency!");
      }
      return text;
    }
    catch (BadLocationException e) {
      logger.error("Failed to insert text (" + text + ") at offset (" + offset + ")!", e);
      throw new InvalidTextFormatException("Failed to insert text (" + text + ") at offset (" + offset + ")!", e);
    }
  }

  /**
   * Determines whether the specified String value is a valid currency format.
   * @param currency the String value representing the current currency value.
   * @return a boolean value indicating if the current String currency is a valid currency format.
   */
  private boolean isValidCurrency(final String currency) {
    if (logger.isDebugEnabled()) {
      logger.debug("currency (" + currency + ")");
    }

    if (StringUtil.countOccurences(currency, String.valueOf(DECIMAL_POINT_CHAR)) > 1) {
      logger.warn("The currency value contained more than one decimal point!");
      return false;
    }

    final int decimalPointIndex = currency.indexOf(DECIMAL_POINT_CHAR);
    logger.debug("decimalPointIndex = " + decimalPointIndex);
    if ((decimalPointIndex > -1) && currency.substring(decimalPointIndex + 1).length() > 2) {
      logger.warn("The currency value has too many digits after the decimal point!");
      return false;
    }

    boolean valid = true; // innocent until proven guilty
    for (int index = 0, len = currency.length(); index < len && valid; index++) {
      final char c = currency.charAt(index);
      logger.debug("c = '" + c + "'");
      switch (index) {
        case DOLLAR_SIGN_POSITION:
          valid &= (c == DOLLAR_SIGN_CHAR);
          break;
        case MINUS_SIGN_DECIMAL_POSITION:
          valid &= (c == MINUS_SIGN_CHAR) || (c == DECIMAL_POINT_CHAR) || Character.isDigit(c);
          break;
        default:
          valid &= (c == DECIMAL_POINT_CHAR) || Character.isDigit(c);
      }
    }
    return valid;
  }

  /**
   * Mutates the inserted value to conform to the currency format as defined by this TextFormat.
   * @param offset the offset into the document at which the value will be inserted, thus dictating
   * the type of format that is imposed on the value.
   * @param value the value being inserted into the document of the text component.
   * @return the modified value with currency formatting imposed.
   */
  private String mutate(final int offset, String value) {
    if (logger.isDebugEnabled()) {
      logger.debug("offset = " + offset);
      logger.debug("value (" + value + ")");
    }
    if (!StringUtil.isEmpty(value)) {
      if ((offset == DOLLAR_SIGN_POSITION) && !value.startsWith(String.valueOf(DOLLAR_SIGN_CHAR))) {
        logger.debug("prepending dollar sign");
        value = DOLLAR_SIGN_CHAR + value;
      }
    }
    return value;
  }

  /**
   * Returns a String description of this class.
   * @return a String value describing this class.
   */
  public String toString() {
    return getClass().getName() + "(" + CURRENCY_FORMAT + ")";
  }

}
