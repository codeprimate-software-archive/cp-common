/*
 * ComposableTextFormat.java (c) 20 July 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.13
 * @see com.cp.common.swing.text.TextFormat
 * @see javax.swing.text.Document
 */

package com.cp.common.swing.text;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import javax.swing.text.Document;

public final class ComposableTextFormat implements TextFormat {

  private final TextFormat textFormat0;
  private final TextFormat textFormat1;

  /**
   * Creates an instance of the TextFormatComposition class to chain TextFormat objects together in order to
   * perform independent mutations and validations of text input to JFormattedField component.
   * @param textFormat0 a TextFormat object to format and validate text input.
   * @param textFormat1 a TextFormat object to format and validate text input.
   */
  private ComposableTextFormat(final TextFormat textFormat0, final TextFormat textFormat1) {
    Assert.notNull(textFormat0, "The first text format argument cannot be null!");
    Assert.notNull(textFormat1, "The second text format argument cannot be null!");
    this.textFormat0 = textFormat0;
    this.textFormat1 = textFormat1;
  }

  /**
   * Composes two TextFormat objects into one interface.
   * @param textFormat0 a TextFormat object to format and validate text input.
   * @param textFormat1 a TextFormat object to format and validate text input.
   * @return a TextFormat composition of the two TextFormat parameters.
   */
  public static TextFormat compose(final TextFormat textFormat0, final TextFormat textFormat1) {
    return (ObjectUtil.isNull(textFormat0) ? textFormat1 : (ObjectUtil.isNull(textFormat1) ? textFormat0
      : new ComposableTextFormat(textFormat0, textFormat1)));
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
    return textFormat1.format(doc, offset, textFormat0.format(doc, offset, text));
  }

}
