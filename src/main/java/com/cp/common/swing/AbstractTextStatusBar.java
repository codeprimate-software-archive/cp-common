/*
 * AbstractTextStatusBar.java (c) 26 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.2.1
 */

package com.cp.common.swing;

import com.cp.common.beans.event.RequiredFieldVetoableChangeListener;
import com.cp.common.lang.ObjectUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import javax.swing.text.Position;
import org.apache.log4j.Logger;

public abstract class AbstractTextStatusBar extends AbstractStatusBar {

  private static final Logger logger = Logger.getLogger(AbstractTextStatusBar.class);

  private static final Color DEFAULT_TEXT_COLOR = Color.black;
  private static final Color DEFAULT_TEXT_OVERLAY_COLOR = Color.white;

  private static final Font DEFAULT_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);

  private static final Position DEFAULT_TEXT_OFFSET = createPosition(5);

  private Color textColor = DEFAULT_TEXT_COLOR;
  private Color textOverlayColor = DEFAULT_TEXT_OVERLAY_COLOR;

  private Font textFont = DEFAULT_TEXT_FONT;

  private ITextSegment textSegment;

  private Position textOffset = DEFAULT_TEXT_OFFSET;

  private String text;

  /**
   * Default constructor used to create an instance of the AbstractTextStatusBar UI component class.  The
   * constructor also registers the TextPropertyChangeListener as an event handler for text changes events
   * on this component in order to update the view of text changes.
   */
  public AbstractTextStatusBar() {
    final TextPropertyChangeListener textChangeHandler = new TextPropertyChangeListener();
    addPropertyChangeListener("text", textChangeHandler);
    addPropertyChangeListener("textColor", textChangeHandler);
    addPropertyChangeListener("textFont", textChangeHandler);
    addPropertyChangeListener("textOffset", textChangeHandler);
    addVetoableChangeListener(new RequiredFieldVetoableChangeListener("text"));
  }

  /**
   * Constructs a Position object to denote an offset.
   * @param offset an integer value specifying the offset from some origin.
   * @return a Position object denoting an offset.
   */
  protected static Position createPosition(final int offset) {
    return new Position() {
      public int getOffset() {
        return offset;
      }
    };
  }

  /**
   * Factory method for constructing a ITextSegment object, representing the specified portion of text.
   * @param text the String value denoting the portion of text the segment will represent.
   * @param origin a Point value specifying the location of the text in the given context.
   * @param textColor the color of the specified text.
   * @return a ITextSegment object representing the text at the specified location (origin),
   * having the specified color.
   */
  protected static ITextSegment createTextSegment(final String text, final Point origin, final Color textColor) {
    return createTextSegment(text, origin, textColor, DEFAULT_TEXT_FONT);
  }

  /**
   * Factory method for constructing a ITextSegment object, representing the specified portion of text.
   * @param text the String value denoting the portion of text the segment will represent.
   * @param origin a Point value specifying the location of the text in the given context.
   * @param textColor the color of the specified text.
   * @param textFont the font of the specified text.
   * @return a ITextSegment object representing the text at the specified location (origin),
   * having the specified color.
   */
  protected static ITextSegment createTextSegment(final String text, final Point origin, final Color textColor, final Font textFont) {
    return new TextSegment(text, origin, textColor, textFont);
  }

  /**
   * Returns the text to display in the status bar.
   * @return a String value denoting the text displayed in the status bar.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the specified text to be displayed in this status bar.
   * @param text a String value denoting the text to be displayed in this status bar.
   */
  public void setText(final String text) {
    if (logger.isDebugEnabled()) {
      logger.debug("text (" + text + ")");
    }
    try {
      final String oldText = this.text;
      fireVetoableChange("text", oldText, text);
      this.text = text;
      firePropertyChange("text", oldText, text);
    }
    catch (PropertyVetoException e) {
      logger.error("The text (" + text + ") value was vetoed!", e);
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Returns the color of the text when not overlayed on the bar.
   * @return a Color object specifying the color of the text when not overlayed on the bar.
   */
  public Color getTextColor() {
    return textColor;
  }

  /**
   * Sets the color of the text when the text is NOT displayed over the bar.
   * @param textColor a Color object specifying the color of the text when the text is NOT displayed over the bar.
   */
  public void setTextColor(final Color textColor) {
    if (logger.isDebugEnabled()) {
      logger.debug("textColor (" + textColor + ")");
    }
    final Color oldTextColor = getTextColor();
    this.textColor = (Color) ObjectUtil.getDefaultValue(textColor, DEFAULT_TEXT_COLOR);
    firePropertyChange("textColor", oldTextColor, this.textColor);
  }

  /**
   * Returns the font used as the face for the text.
   * @return a Font object specifying the face of the font.
   */
  public Font getTextFont() {
    return textFont;
  }

  /**
   * Sets the font used as the face for the text.
   * @param textFont a Font object specifying the face of the font.
   */
  public void setTextFont(final Font textFont) {
    if (logger.isDebugEnabled()) {
      logger.debug("textFont (" + textFont + ")");
    }
    final Font oldTextFont = getTextFont();
    this.textFont = (Font) ObjectUtil.getDefaultValue(textFont, DEFAULT_TEXT_FONT);
    firePropertyChange("textFont", oldTextFont, this.textFont);
  }

  /**
   * Returns the offset in the specified context at which to begin displaying the text.
   * @return a Position object denoting the location (offset) at which to begin displaying the text.
   */
  public Position getTextOffset() {
    return textOffset;
  }

  /**
   * Sets the offset for the specified context at which to begin displaying the text.
   * @param textOffset a Position object denoting the location (offset) at which to begin displaying the text.
   */
  public void setTextOffset(final Position textOffset) {
    if (logger.isDebugEnabled()) {
      logger.debug("textOffset (" + textOffset + ")");
    }
    final Position oldTextOffset = getTextOffset();
    this.textOffset = (Position) ObjectUtil.getDefaultValue(textOffset, DEFAULT_TEXT_OFFSET);
    firePropertyChange("textOffset", oldTextOffset, this.textOffset);
  }

  /**
   * Returns the color of the text when the text is displayed over the bar.
   * @return a Color object specifying the color of the text when displayed over the bar.
   */
  public Color getTextOverlayColor() {
    return textOverlayColor;
  }

  /**
   * Sets the color of the text when the text is displayed over the bar.
   * @param textOverlayColor a Color object specifying the color of the text when displayed over the bar.
   */
  public void setTextOverlayColor(final Color textOverlayColor) {
    if (logger.isDebugEnabled()) {
      logger.debug("textOverlayColor (" + textOverlayColor + ")");
    }
    final Color oldTextOverlayColor = getTextOverlayColor();
    this.textOverlayColor = (Color) ObjectUtil.getDefaultValue(textOverlayColor, DEFAULT_TEXT_OVERLAY_COLOR);
    firePropertyChange("textOverlayColor", oldTextOverlayColor, this.textOverlayColor);
  }

  /**
   * Returns the composed text segment for the specified text used by the status bar.
   * @return a ITextSegment object composed of various ITextSegments representing portions of the text
   * displayed by this status bar.
   */
  public final ITextSegment getTextSegment() {
    if (ObjectUtil.isNull(textSegment)) {
      textSegment = createTextSegment(getText(), new Point(getTextOffset().getOffset(),
        (getSize().height - getFontMetrics(getTextFont()).getMaxDescent())), DEFAULT_TEXT_COLOR);
    }
    return textSegment;
  }

  /**
   * Paints the status bar with the text displayed over the bar.
   * @param g2 a Graphics object used to paint graphics primitives.
   */
  protected void paintStatusBar(final Graphics2D g2) {
    super.paintStatusBar(g2);
    getTextSegment().paint(g2);
  }

  /**
   * Receives model events from the BarModel to refresh the UI.
   * @param event the Event object indicating the model state change.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    recreateTextSegment();
    super.propertyChange(event);
  }

  /**
   * Recomposes the text segment representing the various portions of the text.
   */
  private void recreateTextSegment() {
    final FontMetrics fontMetrics = getFontMetrics(getTextFont());

    int x = getTextOffset().getOffset();
    int y = ((int) getSize().getHeight() - fontMetrics.getMaxDescent());

    ITextSegment previousTextSegment = null;
    final StringBuffer buffer = new StringBuffer();

    // initial peak
    boolean contains = ((TextBarModel) getBarModel()).contains(new Point(x + fontMetrics.charWidth(getText().charAt(0)), y));

    if (isRunning()) {
      for (CharacterIterator it = new StringCharacterIterator(getText()); it.current() != CharacterIterator.DONE; it.next()) {
        final char currentCharacter = it.current();

        // offset + current buffer width (characters contained in current iteration) + next character
        final boolean flag = ((TextBarModel) getBarModel()).contains(new Point(x + fontMetrics.stringWidth(buffer.toString())
          + fontMetrics.charWidth(currentCharacter), y));

        if (flag != contains) {
          // build text segment
          final ITextSegment tempTextSegment = createTextSegment(buffer.toString(), new Point(x, y),
            (contains ? getTextOverlayColor() : getTextColor()));

          // compose the text segments (composition)
          previousTextSegment = TextComposition.compose(previousTextSegment, tempTextSegment);

          // reset state...
          x += fontMetrics.stringWidth(buffer.toString());
          contains = flag;
          buffer.delete(0, buffer.length()); // clear the character buffer
          buffer.append(currentCharacter); // do not forget about the character just read!
        }
        else {
          buffer.append(currentCharacter);
        }
      }

      // append any remaining characters to the end of the text segment
      if (buffer.length() > 0) {
        final ITextSegment tempTextSegment = createTextSegment(buffer.toString(), new Point(x, y), (contains ? getTextOverlayColor() : getTextColor()));
        previousTextSegment = TextComposition.compose(previousTextSegment, tempTextSegment);
      }
    }
    else {
      previousTextSegment = createTextSegment(getText(), new Point(x, y), getTextColor());
    }

    textSegment = previousTextSegment;
  }

  /**
   * Interface to define a text segment, or some portion of fixed text.
   */
  public interface ITextSegment {
    public String getText();
    public void paint(Graphics2D g);
  }

  /**
   * Extended version of the BarModel class adding functionality to determine overlap between the text and the bar.
   */
  protected abstract class TextBarModel extends BarModel {
    public abstract boolean contains(Point position);
  }

  /**
   * TextComposition represents a composition of text segments, which is itself a text segment.
   */
  private static class TextComposition implements ITextSegment {

    private final ITextSegment textSegment0;
    private final ITextSegment textSegment1;

    private TextComposition(final ITextSegment textSegment0, final ITextSegment textSegment1) {
      this.textSegment0 = textSegment0;
      this.textSegment1 = textSegment1;
    }

    public static ITextSegment compose(final ITextSegment textSegment0, final ITextSegment textSegment1) {
      if (ObjectUtil.isNull(textSegment0)) {
        return textSegment1;
      }
      if (ObjectUtil.isNull(textSegment1)) {
        return textSegment0;
      }
      return new TextComposition(textSegment0, textSegment1);
    }

    public String getText() {
      return (textSegment0.getText() + textSegment1.getText());
    }

    public void paint(final Graphics2D g2) {
      textSegment0.paint(g2);
      textSegment1.paint(g2);
    }
  }

  /**
   * Listener for text property changes in the AbstractTextStatusBar component class.
   */
  private final class TextPropertyChangeListener implements PropertyChangeListener {
    public void propertyChange(final PropertyChangeEvent event) {
      if (!isRunning() && AbstractTextStatusBar.this.isVisible()) {
        repaint();
      }
    }
  }

  /**
   * TextSegment is used to represent different portions of the text for this status bar.
   */
  private static class TextSegment implements ITextSegment {

    private final Color textColor;
    private final Font textFont;
    private final Point origin;
    private final String text;

    public TextSegment(final String text, final Point origion, final Color textColor) {
      this(text, origion, textColor, DEFAULT_TEXT_FONT);
    }

    public TextSegment(final String text, final Point origin, final Color textColor, final Font textFont) {
      this.text = text;
      this.origin = origin;
      this.textColor = textColor;
      this.textFont = textFont;
    }

    public Point getOrigin() {
      return origin;
    }

    public String getText() {
      return text;
    }

    public Color getTextColor() {
      return textColor;
    }

    public Font getTextFont() {
      return textFont;
    }

    public void paint(final Graphics2D g2) {
      g2.setColor(getTextColor());
      g2.setFont(getTextFont());
      g2.drawString(getText(), (int) getOrigin().getX(), (int) getOrigin().getY());
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof TextSegment)) {
        return false;
      }

      final TextSegment that = (TextSegment) obj;

      return ObjectUtil.equals(getOrigin(), that.getOrigin())
        && ObjectUtil.equals(getText(), that.getText())
        && ObjectUtil.equals(getTextColor(), that.getTextColor())
        && ObjectUtil.equals(getTextFont(), that.getTextFont());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getOrigin());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getText());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getTextColor());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getTextFont());
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{origin = ");
      buffer.append(getOrigin());
      buffer.append(", text = ").append(getText());
      buffer.append(", textColor = ").append(getTextColor());
      buffer.append(", textFont = ").append(getTextFont());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
