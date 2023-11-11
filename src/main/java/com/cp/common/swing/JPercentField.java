/*
 * JPercentField.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.2
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

public final class JPercentField extends JTextField {

  private static final Logger logger = Logger.getLogger(JPercentField.class);

  private static final String PERCENT_SIGN = "%";
  private static final String DEFAULT_PERCENT_VALUE = "0.0" + PERCENT_SIGN;

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  /**
   * Creates an instance of the JPercentField class to represent percent values in a text field component.
   */
  public JPercentField() {
    this(DEFAULT_PERCENT_VALUE);
  }

  /**
   * Creates an instance of the JPercentField class to represent percent values in a text field component
   * initialized to the specified percent value.
   * @param percentage a String representation of the percent value used to initialize this percent field
   * component.
   */
  public JPercentField(final String percentage) {
    super(8);
    addKeyListener(new PercentFieldKeyListener());
    setText(percentage);
  }

  /**
   * Creates the default implementation of the model to be used at construction if one isn't explicitly given.
   * Returns an instance of PercentDocument.
   * @return the default javax.swing.text.Document object used to model percentages input this input field
   * component.
   */
  protected Document createDefaultModel() {
    return new PercentDocument();
  }

  /**
   * Returns a BigDecimal object containing a decimal value representing the percentage contained by this
   * percent field.
   * @return a java.math.BigDecimal value representing the percentage value of the percent field component
   * as a fractional decimal value.
   */
  public BigDecimal getPercentage() {
    final String percentValue = getText();
    // remove the percent sign
    final BigDecimal decimalValue = new BigDecimal(percentValue.substring(0, percentValue.length() - 1));
    // calculate the decimal value by dividing by 100%
    return decimalValue.divide(new BigDecimal(100.0), BigDecimal.ROUND_HALF_EVEN);
  }

  /**
   * The overridden setText method sets the content of this percent field component to the specified percentage value.
   * @param text a String specifying the percentage value to populate this text field component.
   */
  public void setText(String text) {
    if (logger.isDebugEnabled()) {
      logger.debug("text (" + text + ")");
    }
    if (StringUtil.isEmpty(text)) {
      text = DEFAULT_PERCENT_VALUE;
    }
    if (!text.trim().endsWith(PERCENT_SIGN)) {
      text = text.trim() + PERCENT_SIGN;
    }
    super.setText(text);
  }

  /**
   * The PercentDocument class is used by the JPercentField component to format the JTextField component
   * as a percentage input field.
   */
  private final class PercentDocument extends PlainDocument {

    private static final char DECIMAL_POINT_CHARACTER = '.';
    private static final char NEGATIVE_SIGN_CHARACTER = '-';
    private static final char PERCENT_SIGN_CHARACTER = '%';

    private static final String DECIMAL_POINT = ".";
    private static final String NEGATIVE_SIGN = "-";

    public void insertString(final int offset, final String value, final AttributeSet attrSet)
        throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
        logger.debug("attrSet ( " + attrSet + ")");
      }

      if (!isValidPercent(offset, value)) {
        TOOLKIT.beep();
      }
      else {
        super.insertString(offset, value, attrSet);
      }
    }

    /**
     * Determines whether the String object being inserted into this percent field with the current percent value
     * will maintain a valid percentage.
     * @param offset is an integer offset into the document.
     * @param value a String value to insert into the document providing validation criteria are met.
     * @throws javax.swing.text.BadLocationException if the insert position is not a valid position within the document.
     */
    private boolean isValidPercent(final int offset, final String value) throws BadLocationException {
      final String currentPercentValue = this.getText(0, getLength());
      if (logger.isDebugEnabled()) {
        logger.debug("currentPercentValue (" + currentPercentValue + ")");
      }

      if (StringUtil.countOccurences(value, DECIMAL_POINT) > 1) {
        return false;
      }
      if (StringUtil.contains(value, DECIMAL_POINT) && StringUtil.contains(currentPercentValue, DECIMAL_POINT)) {
        return false;
      }
      if (StringUtil.contains(value, PERCENT_SIGN) && StringUtil.contains(currentPercentValue, PERCENT_SIGN)) {
        return false;
      }

      boolean valid = true; // innocent until proven guilty
      for (int index = 0, len = value.length(); index < len && valid; index++) {
        final char c = value.charAt(index);
        logger.debug("c = '" + c + "'");
        switch (offset + index) {
          case 0:
            valid &= !StringUtil.contains(this.getText(0, 1), NEGATIVE_SIGN) &&
              ((c == NEGATIVE_SIGN_CHARACTER) || (c == DECIMAL_POINT_CHARACTER) || Character.isDigit(c));
            break;
          default:
            valid &= (((c == PERCENT_SIGN_CHARACTER) && (index == (len - 1)))
              || (c == DECIMAL_POINT_CHARACTER) || Character.isDigit(c));
        }
      }
      return valid;
    }

    public void remove(final int offset, final int length) throws BadLocationException {
      if (StringUtil.contains(this.getText(offset, length), PERCENT_SIGN)) {
        return;
      }
      super.remove(offset, length);
    }
  }

  /**
   * The PercentFieldKeyListener is used by the JPercentField component to track key events targeted at
   * editing and traversing the percentage field component.
   */
  public final class PercentFieldKeyListener extends KeyAdapter {

    /**
     * Invoked when a key has been pressed.  This method validates navigation for the JPercentField.
     */
    public void keyPressed(final KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_END:
          logger.debug("the END key was pressed");
          getCaret().setDot(getDocument().getLength() - 1);
          e.consume();
          break;
        case KeyEvent.VK_RIGHT:
          logger.debug("the RIGHT arrow key was presed");
          if (getCaret().getDot() == (getDocument().getLength() - 1)) {
            e.consume();
          }
          break;
      }
    }
  }

}
