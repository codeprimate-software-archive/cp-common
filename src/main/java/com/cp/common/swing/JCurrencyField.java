/*
 * JCurrencyField.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.9.19
 */

package com.cp.common.swing;

import com.cp.common.lang.StringUtil;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

public final class JCurrencyField extends JTextField {

  private static final Logger logger = Logger.getLogger(JCurrencyField.class);

  private static final char DECIMAL_POINT_CHARACTER = '.';
  private static final char DOLLAR_SIGN_CHARACTER = '$';
  private static final char NEGATIVE_SIGN_CHARACTER = '-';

  private static final String DECIMAL_POINT = ".";
  private static final String DOLLAR_SIGN = "$";
  private static final String NEGATIVE_SIGN = "-";

  private static final String DEFAULT_CURRENCY_VALUE = DOLLAR_SIGN + "0.00";

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  /**
   * Creates a new instance of the JCurrencyField class, which is an input component for currency in US dollars.
   */
  public JCurrencyField() {
    this(DOLLAR_SIGN);
  }

  /**
   * Creates a new instance of the JCurrencyField class input component initialized with the default monetary value
   * in US currency.
   * @param currency a String value specifyig the default monetary value to set the currency field component to.
   */
  public JCurrencyField(final String currency) {
    super(8);
    addKeyListener(new CurrencyFieldKeyListener());
    setText(currency);
    getCaret().setDot(1); // set the caret position past the dollar sign.
  }

  /**
   * Creates the default implementation of the model to be used at construction if one isn't explicitly given.
   * Returns an instance of CurrencyDocument.
   * @return the default javax.swing.text.Document object used to represent the content for this currency field
   * component.
   */
  protected Document createDefaultModel() {
    return new CurrencyDocument();
  }

  /**
   * Returns a BigDecimal representation of the monetary value contained in this currency field component.
   * @return a java.math.BigDecimal object containing the currency value represented by this currency field component.
   */
  public BigDecimal getCurrency() {
    return new BigDecimal(getText().substring(1)); // remember to remove the dollar sign
  }

  /**
   * The overridden setText method sets the content of this text field component to the specified String value
   * as if it were a currency value.
   * @param text a String value specifying the currency to populate this text field component.
   */
  public void setText(String text) {
    logger.debug("text (" + text + ")");
    if (StringUtil.isEmpty(text)) {
      text = DEFAULT_CURRENCY_VALUE;
    }
    if (!text.trim().startsWith(DOLLAR_SIGN)) {
      text = DOLLAR_SIGN + text.trim();
    }
    super.setText(text);
  }

  /**
   * The CurrencyDocument class is used by the JCurrencyField component to format the
   * JTextField component as a currency input field.
   * NOTE: on a text replacement, the AbstractDocument's (extended by PlainDocument) replace method
   * will call remove followed by insertString.
   */
  private final class CurrencyDocument extends PlainDocument {

    /**
     * Validates the String value being inserted into the CurrencyField and performs the insert.
     * @param offset
     * @param value
     * @param attrSet
     * @throws BadLocationException
     */
    public void insertString(final int offset, final String value, final AttributeSet attrSet)
        throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
        logger.debug("attrSet (" + attrSet + ")");
      }
      if (!isValidCurrency(offset, value))
        TOOLKIT.beep();
      else {
        super.insertString(offset, value, attrSet);
      }
    }

    /**
     * Determines whether the String value being inserted into this currency field maintains a valid monetary value.
     * @param offset is an integer offset into the document.
     * @param value the String value being inserted into the document providing the currency validation criteria is met.
     * @throws javax.swing.text.BadLocationException if the offset position is not a valid position within the document
     * to insert the String value.
     */
    private boolean isValidCurrency(final int offset, final String value)
        throws BadLocationException {

      final boolean containsDecimal = StringUtil.contains(this.getText(0, getLength()), DECIMAL_POINT);
      int decimalIndex = this.getText(0, getLength()).indexOf(DECIMAL_POINT);

      // check the number of digits after the decimal point
      if (containsDecimal && (decimalIndex < offset)) {
        final String digitsAfterDecimalPoint = this.getText(decimalIndex + 1, (getLength() - (decimalIndex + 1)));
        logger.debug("digits after decimal point (" + digitsAfterDecimalPoint + ")");
        if ((digitsAfterDecimalPoint.length() + value.length()) > 2) {
          return false;
        }
      }

      final boolean valueContainsDecimal = StringUtil.contains(value, DECIMAL_POINT);
      decimalIndex = value.indexOf(DECIMAL_POINT);

      // validate value decimal point
      if (valueContainsDecimal && containsDecimal) {
        return false;
      }
      if (StringUtil.countOccurences(value, DECIMAL_POINT) > 1) {
        return false;
      }
      if (valueContainsDecimal && value.substring(decimalIndex + 1).length() > 2) {
        return false;
      }

      // validate currency value
      boolean valid = true; // innocent until proven guilty
      for (int indx = 0, len = value.length(); indx < len && valid; indx++) {
        final char c = value.charAt(indx);
        switch (offset + indx) {
          case 0:
            final boolean haveDollarSign = StringUtil.contains(this.getText(0, getLength()), DOLLAR_SIGN);
            logger.debug("haveDollarSign = " + haveDollarSign);
            valid &= (c == DOLLAR_SIGN_CHARACTER && !haveDollarSign);
            break;
          case 1:
            final String firstChar = this.getText(1, 1);
            logger.debug("firstChar (" + firstChar + ")");
            valid &= ((c == NEGATIVE_SIGN_CHARACTER && !NEGATIVE_SIGN.equals(firstChar))
              || (c == DECIMAL_POINT_CHARACTER && !(NEGATIVE_SIGN.equals(firstChar) || DECIMAL_POINT.equals(firstChar)))
              || (Character.isDigit(c) && !NEGATIVE_SIGN.equals(firstChar)));
            break;
          default:
            valid &= (Character.isDigit(c) || (c == DECIMAL_POINT_CHARACTER));
        }
      }
      return valid;
    }

    /**
     * Handles the deletion of content in the currency field.
     * @param offset
     * @param length
     * @throws BadLocationException
     */
    public void remove(final int offset, final int length) throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("length = " + length);
      }
      if (offset == 0) {
        TOOLKIT.beep();
        return;
      }
      super.remove(offset, length);
    }
  }

  /**
   * The CurrencyFieldKeyListener is used by the JCurrencyField component to track key events that edit and/or
   * traverse the currency field component.
   */
  private final class CurrencyFieldKeyListener extends KeyAdapter {

    public void keyPressed(final KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_HOME:
          logger.debug("HOME key was pressed");
          getCaret().setDot(1); // do not allow the cursor before the dollar sign
          e.consume(); // consumer the key event since the cursor position was set manually
          break;
        case KeyEvent.VK_LEFT:
          logger.debug("LEFT arrow key was pressed");
          if (getCaret().getDot() == 1) {
            e.consume(); // cannot move before the dollar sign
          }
          break;
      }
    }
  }

}
