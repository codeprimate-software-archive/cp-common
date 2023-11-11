/*
 * JStatusBar.java (c) 15 July 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.2.2
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import javax.swing.BorderFactory;

public final class JStatusBar extends AbstractTextStatusBar {

  private static final String DEFAULT_TEXT = "";

  private BarModel gradientBar;

  /**
   * Creates an instance of the JStatusBar UI component class initialized to the specified size.
   * @param barSize a Dimension object specifying the size (width and height) of the status bar.
   */
  public JStatusBar(final Dimension barSize) {
    this(barSize, DEFAULT_TEXT);
  }

  /**
   * Creates an instance of the JStatusBar UI component class initialized to the specified size.
   * @param barSize a Dimension object specifying the size (width and height) of the status bar.
   * @param text a String value specifying the message that will be displayed in the status bar.
   */
  public JStatusBar(final Dimension barSize, final String text) {
    setBarSize(barSize);
    setBorder(BorderFactory.createLoweredBevelBorder());
    setText(text);
  }

  /**
   * Returns the instance of the BarModel (GradientBarModel) object used to model the bar displayed in the UI
   * of the status bar.
   * @return the BarModel object model of the visual bar in the status bar.
   */
  protected synchronized BarModel getBarModel() {
    if (ObjectUtil.isNull(gradientBar)) {
      gradientBar = new GradientBarModel();
    }
    return gradientBar;
  }

  /**
   * The GradientBarModel class models an oscillating bar that fades down the bar opposite the direction in which
   * the bar is moving.
   */
  private final class GradientBarModel extends TextBarModel {

    private boolean forward = true;

    private Color getColor1() {
      return (forward ? getBackground() : getBarColor());
    }

    private Color getColor2() {
      return (forward ? getBarColor() : getBackground());
    }

    public void setSize(final Dimension size) {
      super.setSize(size);
      setOrigin(new Point(-size.width, 0));
    }

    protected void setVisible(final boolean visible) {
      if (isVisible() != visible) {
        setOrigin(new Point(-getSize().width, 0));
      }
      super.setVisible(visible);
    }

    public boolean contains(final Point position) {
      if (isVisible()) {
        final double x = position.getX();
        if (getOrigin().getX() <= x && (getOrigin().getX() + getSize().getWidth()) >= x) {
          final double y = position.getY();
          if (getOrigin().getY() <= y && (getOrigin().getY() + getSize().getHeight()) >= y) {
            return true;
          }
        }
        return false;
      }
      else {
        return false;
      }
    }

    public void move(final Dimension area) {
      if (forward) {
        getOrigin().x += getBarSpeed();
        forward = (Math.min(getOrigin().getX(), area.getWidth()) == getOrigin().getX());
      }
      else {
        getOrigin().x -= getBarSpeed();
        forward = (Math.min(getOrigin().getX(), -getSize().getWidth()) == getOrigin().getX());
      }
      firePropertyChange(new PropertyChangeEvent(this, "move", Boolean.FALSE, Boolean.TRUE));
    }

    public void paint(final Graphics2D g2) {
      if (isVisible()) {
        g2.setPaint(new GradientPaint(((float) getOrigin().getX()), 0.0f, getColor1(),
          ((float) getOrigin().getX() + getSize().width) , 0.0f, getColor2()));
        g2.fill(new Rectangle2D.Double(getOrigin().getX(), getOrigin().getY(), getSize().getWidth(), getSize().getHeight()));
      }
    }
  }

}
