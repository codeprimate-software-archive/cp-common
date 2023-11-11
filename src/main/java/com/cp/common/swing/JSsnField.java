/*
 * JSnnField.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.5
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

public final class JSsnField extends JTextField {

  private static final Logger logger = Logger.getLogger(JSsnField.class);

  private static final int END_POSITION = 11;
  private static final int LEFT_DASH_POSITION = 6;
  private static final int RIGHT_DASH_POSITION = 3;

  private static final Color SELECTION_COLOR = new Color(10, 36, 106);

  private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 12);

  private static final String DEFAULT_SSN = "   -  -    ";

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  private boolean typeOver;

  /**
   * Creates an instance of the JSsnField component class to represent social security numbers in a text field.
   */
  public JSsnField() {
    super(12);
    addKeyListener(new SsnFieldKeyListener());
    setFont(DEFAULT_FONT);
    setSelectedTextColor(Color.white);
    setSelectionColor(SELECTION_COLOR);
    setText(DEFAULT_SSN);
  }

  /**
   * Creates the default implementation of the model to be used at construction if one isn't explicitly given.
   * Returns an instance of SsnDocument.
   * @return a javax.swing.text.Document model implementation for the ssn field, a basic text field component
   * enforcing an ssn format using the SsnDocument model.
   */
  protected Document createDefaultModel() {
    return new SsnDocument();
  }

  /**
   * Determines whether the user pressed the insert key and activated text over write.
   * @return a boolean value indicating if text over write has been activated.
   */
  public boolean isTypeOver() {
    return typeOver;
  }

  /**
   * Sets whether the user has pressed the insert key activating the text over write.
   * @param typeOver a boolean value indicating if the user activated text over write by pressing the insert key.
   */
  public void setTypeOver(final boolean typeOver) {
    logger.debug("typeOver = " + typeOver);
    this.typeOver = typeOver;
  }

  /**
   * Sets the current caret position for type overs.
   * @param caret is an integer index representing the offset into the document for the ssn field.
   */
  private void setCaret(int caret) {
    logger.debug("caret = " + caret);
    if (isTypeOver()) {
      switch (caret) {
        case LEFT_DASH_POSITION:
        case RIGHT_DASH_POSITION:
        case END_POSITION:
          setSelectionStart(caret - 1);
          setSelectionEnd(caret);
          break;
        default:
          setSelectionStart(caret);
          setSelectionEnd(caret + 1);
      }
    }
    else {
      switch (caret) {
        case LEFT_DASH_POSITION:
        case RIGHT_DASH_POSITION:
          caret++;
          break;
      }
      getCaret().setDot(caret);
    }
  }

  /**
   * Sets the caret position for type overs to the left of the current caret position.
   * @param caret is an integer index representing the offset into the document for this ssn field.
   */
  private void setCaretLeft(int caret) {
    logger.debug("caret = " + caret);
    if (isTypeOver()) {
      if (caret == 5 || caret == 8) {
        setSelectionStart(caret - 3);
        setSelectionEnd(caret - 2);
      }
      else {
        setSelectionStart(caret - 2);
        setSelectionEnd(caret - 1);
      }
    }
    else {
      caret--;
      switch (caret) {
        case LEFT_DASH_POSITION:
        case RIGHT_DASH_POSITION:
          caret--;
          break;
      }
      getCaret().setDot(caret);
    }
  }

  /**
   * Sets the caret position for type overs to the right of the current caret position.
   * @param caret is an integer index representing the offset into the document for this ssn field.
   */
  private void setCaretRight(int caret) {
    logger.debug("caret = " + caret);
    if (isTypeOver()) {
      if ((caret == LEFT_DASH_POSITION) || (caret == RIGHT_DASH_POSITION)) {
        setSelectionStart(caret + 1);
        setSelectionEnd(caret + 2);
      }
      else if (caret == END_POSITION) {
        setSelectionStart(END_POSITION - 1);
        setSelectionEnd(END_POSITION);
      }
      else {
        setSelectionStart(caret);
        setSelectionEnd(caret + 1);
      }
    }
    else {
      caret++;
      switch (caret) {
        case LEFT_DASH_POSITION:
        case RIGHT_DASH_POSITION:
          caret++;
          break;
      }
      getCaret().setDot(caret);
    }
  }

  /**
   * The SsnDocument class is the default document used by the JSsnField component to model social security numbers
   * in a standard JTextField component.
   */
  private final class SsnDocument extends PlainDocument {

    private static final char DASH_CHARACTER = '-';
    private static final int MAX_NUMBER_OF_SSN_DIGITS = 9;

    private boolean replacing = false;

    private String replaceText = null;

    /**
     * The replaceText property keeps track of the text that was removed for replacement on a replace operation.
     * This text will be needed if the inserted text is an invalid ssn format.
     * @return a String value representing the replaced text.
     */
    private String getReplaceText() {
      return replaceText;
    }

    /**
     * Sets the replaced text upon removal for a replace operation.
     * @param replaceText the text that will be removed and replaced during the replace operation.
     */
    private void setReplaceText(final String replaceText) {
      if (logger.isDebugEnabled()) {
        logger.debug("replaceText (" + replaceText + ")");
      }
      this.replaceText = replaceText;
    }

    /**
     * Determines if a replace operation is occurring.
     * @return a boolean value indicating if the replace operation is occurring.
     */
    private boolean isReplacing() {
      return replacing;
    }

    /**
     * Sets the replacing property during a replace operation.
     * @param replacing a boolean value indicating if a replace operation has occurred.
     */
    private void setReplacing(final boolean replacing) {
      logger.debug("replacing = " + replacing);
      this.replacing = replacing;
    }

    /**
     * Formats the specified String value starting at offset according to the ssn format imposed by this JSsnField
     * component class.
     * @param offset the offset into the document of this JSsnField component class.
     * @param value the value to format.
     * @return the formatted value.
     */
    private String format(final int offset, final String value) {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
      }

      final String valueDigits = StringUtil.getDigitsOnly(value);
      if (logger.isDebugEnabled()) {
        logger.debug("valueDigits (" + valueDigits + ")");
      }

      if (!StringUtil.isEmpty(valueDigits)) {
        final StringBuffer buffer = new StringBuffer();
        for (int position = offset, index = 0, len = valueDigits.length();  index < len; position++) {
          switch (position) {
            case LEFT_DASH_POSITION:
            case RIGHT_DASH_POSITION:
              buffer.append(DASH_CHARACTER);
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
     * Inserts the value into this document starting at offset formatted according to the AttributeSet.
     * @param offset the offset with the document of this JSsnField component class to insert the specified value.
     * @param value the value being inserted into this document.
     * @param attrSet the AttributeSet used to markup the value.
     * @throws BadLocationException if the insert position for the value within this document is not valid.
     */
    public void insertString(final int offset, String value, final AttributeSet attrSet)
        throws BadLocationException {
      try {
        if (logger.isDebugEnabled()) {
          logger.debug("offset = " + offset);
          logger.debug("value (" + value + ")");
          logger.debug("attrSet (" + attrSet + ")");
        }

        if (!isValidSsn(offset, value)) {
          if (ObjectUtil.isNotNull(getReplaceText())) {
            if (logger.isDebugEnabled()) {
              logger.debug("replaceText (" + getReplaceText() + ")");
            }
            super.insertString(offset, getReplaceText(), attrSet);
          }
          logger.warn("The value (" + value + ") is not a valid SSN!");
          TOOLKIT.beep();
        }
        else {
          if (!isTypeOver()) {
            value = shiftRight(offset, value);
          }
          super.insertString(offset, value, attrSet);
          setCaret(offset + value.length());
        }
      }
      finally {
        setReplaceText(null);
        setReplacing(false);
      }
    }

    /**
     * Verifies that the user inputed value constitute a valid social security number.
     * @param offset is an integer value indicating the offset into document object for the social security number
     * validating that the content (value) conforms to the rules and format of a valid ssn.
     * @param value the String content containing a full/partial social security number.
     */
    public boolean isValidSsn(final int offset, final String value) throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
      }

      // if the value is equal to the default SSN, then the value is valid
      if (DEFAULT_SSN.equals(value)) {
        return true;
      }

      // verify the the number of digits in the current ssn and value do not exceed the maximum number of digits
      // in a valid ssn
      final int numSsnDigits = StringUtil.countDigits(this.getText(0, getLength()));
      final int numValueDigits = StringUtil.countDigits(value);
      if (numValueDigits > StringUtil.countWhitespace(DEFAULT_SSN.substring(offset))
        || ((numSsnDigits + numValueDigits) > MAX_NUMBER_OF_SSN_DIGITS)) {
        logger.debug("invalid length!");
        return false;
      }

      // if the value contains only digits and does not violate length, then the value is valid and the format
      // method will adjust the value according to the phone number format
      if (StringUtil.isDigitsOnly(value)) {
        return true;
      }

      boolean valid = true; // innocent until proven guilty
      for (int index = 0, len = value.length(); index < len && valid; index++) {
        switch (offset + index) {
          case LEFT_DASH_POSITION:
          case RIGHT_DASH_POSITION:
            valid &= (value.charAt(index) == DASH_CHARACTER);
            break;
          default:
            valid &= Character.isDigit(value.charAt(index));
        }
      }
      return valid;
    }

    /**
     * Removes a portion of the content (ssn) from this document.  If a replace operation is not occuring,
     * this will cause the digits after offset to be shifted left, retaining the format of the ssn.
     * @param offset the offset from the begining >= 0
     * @param length the number of characters to remove >= 0
     */
    public void remove(final int offset, final int length) throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("length = " + length);
      }

      // do not remove the ssn formatting
      int adjustedOffset = offset;
      switch (offset) {
        case LEFT_DASH_POSITION:
        case RIGHT_DASH_POSITION:
          adjustedOffset--;
          break;
      }

      // if a replace operation is occurring, then record the removed text
      if (isReplacing()) {
        setReplaceText(this.getText(offset, length));
      }

      super.remove(adjustedOffset, length);

      // adjust the caret position
      if (!isReplacing()) {
        shiftLeft(adjustedOffset);
        setCaret(adjustedOffset);
      }
    }

    /**
     * Replaces the text between offset and offset + length with the specified text.
     * @param offset an integer value specifying the begin index into this document to replace text.
     * @param length an integer value specifying the amount of text to replace.
     * @param text the text replacement.
     * @param attrs the cosmetic attributes for the text.
     * @throws BadLocationException
     */
    public void replace(final int offset, final int length, final String text, final AttributeSet attrs)
        throws BadLocationException {
      setReplacing(true);
      super.replace(offset, length, text, attrs);
    }

    /**
     * Called by the remove operation to shift all digit characters to the left introducing a space characters
     * at the end of the document as needed according to the ssn format.
     * @param offset is an integer value indicating an offset into the document of this JSsnField component class
     * to left shift digits of the ssn.
     * @throws javax.swing.text.BadLocationException if the given delete position is not a valid position
     * within the document.
     */
    private void shiftLeft(final int offset) throws BadLocationException {
      logger.debug("offset = " + offset);

      final String currentSsn = this.getText(0, getLength());
      if (logger.isDebugEnabled()) {
        logger.debug("currentSsn (" + currentSsn + ")");
      }

      final int beginIndex = Math.min(currentSsn.length(), offset);
      if (logger.isDebugEnabled()) {
        logger.debug("beginIndex = " + beginIndex);
      }

      String shiftedDigits = StringUtil.getDigitsOnly(currentSsn.substring(beginIndex));
      if (logger.isDebugEnabled()) {
        logger.debug("unformatted shiftedDigits (" + shiftedDigits + ")");
      }

      shiftedDigits = format(offset, shiftedDigits);
      if (logger.isDebugEnabled()) {
        logger.debug("formatted shiftedDigits (" + shiftedDigits + ")");
      }

      final StringBuffer buffer = new StringBuffer();
      buffer.append(shiftedDigits);
      buffer.append(DEFAULT_SSN.substring(offset + shiftedDigits.length()));

      super.remove(offset, getLength() - offset);
      super.insertString(offset, buffer.toString(), null);
    }

    /**
     * Shifts all digit characters to the right accounting for the ssn format by formatting the value and the
     * remaining portion of the current ssn as needed.
     * @param offset is an integer value indicating an offset into the document of this JSsnField component to
     * right shift digits of the current ssn.
     * @throws javax.swing.text.BadLocationException if the given insert position is not a valid position
     * within the document.
     */
    private String shiftRight(final int offset, String value) throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("unformatted value (" + value + ")");
      }

      value = format(offset, value);
      if (logger.isInfoEnabled()) {
        logger.debug("formatted value (" + value + ")");
      }

      final String currentSsn = this.getText(0, getLength());
      if (logger.isDebugEnabled()) {
        logger.debug("currentSsn (" + currentSsn + ")");
      }

      final String digitsAfterOffset = StringUtil.getDigitsOnly(currentSsn.substring(offset));
      if (logger.isDebugEnabled()) {
        logger.debug("digitsAfterOffset (" + digitsAfterOffset + ")");
      }

      final int endIndex = Math.min(digitsAfterOffset.length(), (DEFAULT_SSN.length() - offset - value.length()));
      logger.debug("endIndex = " + endIndex);

      String shiftedDigits = digitsAfterOffset.substring(0, endIndex);
      if (logger.isDebugEnabled()) {
        logger.debug("unformatted shiftedDigits (" + shiftedDigits + ")");
      }

      shiftedDigits = format(offset + value.length(), shiftedDigits);
      if (logger.isDebugEnabled()) {
        logger.debug("formatted shiftedDigits (" + shiftedDigits + ")");
      }

      final StringBuffer buffer = new StringBuffer();
      buffer.append(shiftedDigits);
      buffer.append(DEFAULT_SSN.substring(offset + value.length() + shiftedDigits.length()));

      super.remove(offset, getLength() - offset);
      super.insertString(offset, buffer.toString(), null);

      return value;
    }
  }

  /**
   * The SsnFieldKeyListener is used by the JSsnField component class to track key events targeted at editing
   * and traversing the social security number.
   */
  public final class SsnFieldKeyListener extends KeyAdapter {

    public void keyPressed(final KeyEvent e) {
      final int dot = getCaret().getDot();
      logger.debug("dot = " + dot);
      switch (e.getKeyCode()) {
        case KeyEvent.VK_INSERT:
          setTypeOver(!isTypeOver());
          if (isTypeOver()) {
            setCaret(dot);
          }
          else {
            setCaret(dot - 1);
          }
          e.consume();
          break;
        case KeyEvent.VK_LEFT:
          setCaretLeft(dot);
          e.consume();
          break;
        case KeyEvent.VK_RIGHT:
          setCaretRight(dot);
          e.consume();
          break;
        default:
          logger.debug("keyPressed (" + e.getKeyChar() + ")");
      }
    }
  }

}
