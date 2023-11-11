/*
 * MockDocument (c) 9 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.9
 */

package com.cp.common.swing.text;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import org.apache.log4j.Logger;

public final class MockDocument implements Document {

  private static final Logger logger = Logger.getLogger(MockDocument.class);

  private String text;

  private TextFormat textFormat;

  public MockDocument(final String text) {
    this(text, DefaultTextFormat.INSTANCE);
  }

  public MockDocument(final String text,
                      final TextFormat textFormat) {
    logger.debug("text (" + text + ")");
    logger.debug("textFormat (" + textFormat + ")");

    if (ObjectUtil.isNull(text)) {
      logger.warn("The text cannot be null!");
      throw new NullPointerException("The text cannot be null!");
    }

    this.text = text;
    this.textFormat = (ObjectUtil.isNull(textFormat) ? DefaultTextFormat.INSTANCE : textFormat);
  }

  public void addDocumentListener(final DocumentListener listener) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void addUndoableEditListener(final UndoableEditListener listener) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Position createPosition(final int offset) throws BadLocationException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Element getDefaultRootElement() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public int getLength() {
    return text.length();
  }

  public Object getProperty(final Object key) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void putProperty(final Object key, final Object value) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Position getEndPosition() {
    return new Position() {
      public int getOffset() {
        return (text.length() - 1);
      }
    };
  }

  public Element[] getRootElements() {
    return new Element[0];
  }

  public Position getStartPosition() {
    return new Position() {
      public int getOffset() {
        return 0;
      }
    };
  }

  public String getText(final int offset, final int length) throws BadLocationException {
    try {
      return text.substring(offset, length);
    }
    catch (IndexOutOfBoundsException e) {
      logger.error("Failed to get " + length + " characters of text starting at " + offset + "!", e);
      throw new BadLocationException("Failed to get " + length + " characters of text starting at "
        + offset + "!", offset);
    }
  }

  public void getText(final int offset, final int length, final Segment txt) throws BadLocationException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void insertString(final int offset, final String value, final AttributeSet attrSet)
      throws BadLocationException {
    try {
      text = StringUtil.insert(text, textFormat.format(this, offset, value), offset);
    }
    catch (Exception e) {
      logger.error("Failed to insert text (" + value + ") at offset (" + offset + ")!", e);
      throw new BadLocationException("Failed to insert text (" + value + ") at offset (" + offset + ")!", offset);
    }
  }

  public void remove(final int offset, final int length) throws BadLocationException {
    try {
      text = StringUtil.remove(text, offset, length);
    }
    catch (Exception e) {
      logger.error("Failed to remove text between " + offset + " and " + length + "!", e);
      throw new BadLocationException("Failed to remove text between " + offset + " and " + length + "!", offset);
    }
  }

  public void replace(final int offset, final int length, final String value, final AttributeSet attrSet)
      throws BadLocationException {
    String replacedText = null;
    try {
      final String temp = text.substring(offset, offset + length);
      remove(offset, length);
      replacedText = temp;
      insertString(offset, value, attrSet);
    }
    catch (BadLocationException e) {
      if (!StringUtil.isEmpty(replacedText)) {
        text = StringUtil.insert(text, replacedText, offset);
      }
      throw e;
    }
  }

  public void removeDocumentListener(final DocumentListener listener) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void removeUndoableEditListener(final UndoableEditListener listener) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void render(final Runnable r) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public String toString() {
    return text;
  }

  private static final class DefaultTextFormat implements TextFormat {
    public static final DefaultTextFormat INSTANCE = new DefaultTextFormat();

    public String format(final Document doc, final int offset, final String text) throws InvalidTextFormatException {
      return text;
    }
  }

}
