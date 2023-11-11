/*
 * NetscapeStatusBar.java (c) 15 July 2002
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

public final class NetscapeStatusBar extends AbstractStatusBar {

  private BarModel netscapeBar;

  /**
   * Creates an instance of the NetscapeStatusBar UI component class initialized to the specified size.
   * @param barSize a Dimension object specifying the size (width and height) of the status bar.
   */
  public NetscapeStatusBar(final Dimension barSize) {
    setBarSize(barSize);
    setBorder(BorderFactory.createLoweredBevelBorder());
  }

  /**
   * Returns the instance of the BarModel (NetscapeBarModel) object used to model the bar displayed in the UI
   * of the status bar.
   * @return the BarModel object model of the visual bar in the status bar.
   */
  protected synchronized BarModel getBarModel() {
    if (ObjectUtil.isNull(netscapeBar)) {
      netscapeBar = new NetscapeBarModel();
    }
    return netscapeBar;
  }

  /**
   * The NetscapeBarModel class models the status bar in the Netscape Web Browser.
   */
  private final class NetscapeBarModel extends BarModel {

    private boolean forward = true;

    private double getBarExtent() {
      return (getOrigin().x + getSize().getWidth());
    }

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

    protected void move(final Dimension area) {
      if (forward) {
        getOrigin().x += getBarSpeed();
        forward = (Math.max(getBarExtent(), area.getWidth()) == area.getWidth());
        if (!forward) {
          getOrigin().setLocation(area.getWidth(), 0);
        }
      }
      else {
        getOrigin().x -= getBarSpeed();
        forward = (Math.min(getOrigin().getX(), 0) == getOrigin().getX());
        if (forward) {
          getOrigin().setLocation(-getSize().width, 0);
        }
      }
      firePropertyChange(new PropertyChangeEvent(this, "move", Boolean.FALSE, Boolean.TRUE));
    }

    protected void paint(final Graphics2D g2) {
      if (isVisible()) {
        g2.setPaint(new GradientPaint(((float) getOrigin().getX()), 0.0f, getColor1(),
          ((float) getOrigin().getX() + getSize().width) , 0.0f, getColor2()));
        g2.fill(new Rectangle2D.Double(getOrigin().getX(), getOrigin().getY(), getSize().getWidth(), getSize().getHeight()));
      }
    }
  }

}
