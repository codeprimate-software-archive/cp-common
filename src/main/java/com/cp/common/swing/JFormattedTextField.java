/*
 * JFormattedTextField.java (c) 20 July 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.12
 * @see javax.swing.JTextField
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.swing.text.InvalidTextFormatException;
import com.cp.common.swing.text.TextFormat;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

public final class JFormattedTextField extends JTextField {

  private static final Logger logger = Logger.getLogger(JFormattedTextField.class);

  private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 12);

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  private TextFormat textFormat;

  /**
   * Creates an instance of the JFormattedField component class with a default text format used to format
   * and verify input into this text field component.
   */
  public JFormattedTextField() {
    this(DefaultTextFormat.INSTANCE);
  }

  /**
   * Creates an instance of the JFormattedField component class initialized with the specified TextFormat object
   * used to format and validate text input to this text field component.
   * @param textFormat a TextFormat object specifying the format and validation scheme used when inserting text
   * into this formatted text field.
   */
  public JFormattedTextField(final TextFormat textFormat) {
    setTextFormat(textFormat);
    setFont(DEFAULT_FONT);
  }

  /**
   * Creates the default implementation of the model to be used at construction if one isn't explicitly given.
   * Returns an instance of SchemaDocument.
   * @return a default Ljavax.swing.text.Document object used to format and verify the content for this
   * formatted text field component.
   */
  protected Document createDefaultModel() {
    return new SchemaDocument();
  }

  /**
   * Returns the TextFormat class used by this text compenent to format and valid input.
   * @return a TextFormat class representing the format enforced by this text component.
   */
  public TextFormat getTextFormat() {
    return textFormat;
  }

  /**
   * Sets the specified text format enforced by this text component to format and validate input.
   * @param textFormat the TextFormat object used to constrain input.
   */
  public void setTextFormat(TextFormat textFormat) {
    if (logger.isDebugEnabled()) {
      logger.debug("textFormat (" + textFormat + ")");
    }
    if (ObjectUtil.isNull(textFormat)) {
      logger.info("The textFormat was null; setting default.");
      textFormat = DefaultTextFormat.INSTANCE;
    }
    setText(""); // clear the current text
    this.textFormat = textFormat;
  }

  /**
   * The DefaultTextFormat class requires no special format and performs no validation on the text input.  The class
   * is merely an adapter for the TextFormat interface.
   */
  private static final class DefaultTextFormat implements TextFormat {

    private static final DefaultTextFormat INSTANCE = new DefaultTextFormat();

    public String format(final Document doc, final int offset, final String text) throws InvalidTextFormatException {
      return text;
    }
  }

  /**
   * The SchemaDocument class is used by the JFormattedTextField component class to model text input via formatting
   * and validation.
   */
  public final class SchemaDocument extends PlainDocument {

    private String replacedText = null;

    /**
     * Determines whether a replace operation is occurring.
     * @return a boolean value indicating if a replace operation is occurring.
     */
    private boolean isReplacing() {
      return ObjectUtil.isNotNull(getReplacedText());
    }

    /**
     * The replaceText property keeps track of the text that was removed for replacement on a replace operation.
     * This text will be needed if the inserted text is an invalid ssn format.
     * @return a String value representing the replaced text.
     */
    private String getReplacedText() {
      return replacedText;
    }

    /**
     * Sets the replaced text upon removal for a replace operation.
     * @param replacedText the text that will be removed and replaced during the replace operation.
     */
    private void setReplacedText(final String replacedText) {
      if (logger.isDebugEnabled()) {
        logger.debug("replacedText (" + replacedText + ")");
      }
      this.replacedText = replacedText;
    }

    public void insertString(final int offset, final String value, final AttributeSet attrSet)
        throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
        logger.debug("attrSet (" + attrSet + ")");
      }
      try {
        super.insertString(offset, textFormat.format(this, offset, value), attrSet);
      }
      catch (InvalidTextFormatException itf) {
        if (ObjectUtil.isNotNull(getReplacedText())) {
          if (logger.isDebugEnabled()) {
            logger.debug("reinserting replacedText (" + getReplacedText() + ")");
          }
          super.insertString(offset, getReplacedText(), attrSet);
        }
        logger.warn("The value (" + value + ") is not valid format!");
        TOOLKIT.beep();
      }
    }

    public void replace(final int offset, final int length, final String value, final AttributeSet attrs)
        throws BadLocationException {
      setReplacedText(getText(offset, length));
      super.replace(offset, length, value, attrs);
    }
  }

}
