/*
 * ReflectionStatusBar.java (c) 15 July 2002
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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import javax.swing.BorderFactory;
import org.apache.log4j.Logger;

public final class ReflectionStatusBar extends AbstractStatusBar {

  private static final Logger logger = Logger.getLogger(ReflectionStatusBar.class);

  private BarModel reflectionBar;

  /**
   * Creates an instance of the ReflectionStatusBar UI component class.
   */
  public ReflectionStatusBar(final Dimension barSize) {
    setBarSize(barSize);
    setBorder(BorderFactory.createLoweredBevelBorder());
  }

  /**
   * Returns the instance of the BarModel (ReflectionBarModel) object used to model the bar displayed in the UI
   * of the status bar.
   * @return the BarModel object model of the visual bar in the status bar.
   */
  protected synchronized BarModel getBarModel() {
    if (ObjectUtil.isNull(reflectionBar)) {
      reflectionBar = new ReflectionBarModel(new LeftBarModel(), new RightBarModel());
    }
    return reflectionBar;
  }

  /**
   * The ReflectionBarModel is a composite BarModel object consisting of instances of the LeftBarModel
   * and RightBarModel classes.  The two independent, but complimentarty bars, Left... and Right..., form a
   * symmetry, or a reflection about the center of the status bar UI component.  This class implements the
   * BarModel abstract class and delegates to the LeftBarModel and RightBarModel classes.
   */
  private final class ReflectionBarModel extends BarModel {

    private BarModel leftBar;
    private BarModel rightBar;

    public ReflectionBarModel(final BarModel leftBar, final BarModel rightBar) {
      this.leftBar = leftBar;
      this.rightBar = rightBar;
    }

    public synchronized Color getColor() {
      return leftBar.getColor();
    }

    public synchronized void setColor(final Color color) {
      final Color oldColor = getColor();
      leftBar.setColor(color);
      rightBar.setColor(color);
      firePropertyChange(new PropertyChangeEvent(this, "color", oldColor, color));
    }

    protected Point getOrigin() {
      return leftBar.getOrigin();
    }

    protected void setOrigin(final Point origin) {
      try {
        final PropertyChangeEvent event = new PropertyChangeEvent(this, "origin", getOrigin(), origin);
        fireVetoableChange(event);
        leftBar.setOrigin(origin);
        rightBar.setOrigin(origin);
        firePropertyChange(event);
      }
      catch (PropertyVetoException e) {
        logger.error("The origin (" + origin + ") property change was vetoed!", e);
        throw new IllegalArgumentException("The origin (" + origin + ") property change was vetoed!");
      }
    }

    public Dimension getSize() {
      return leftBar.getSize();
    }

    public void setSize(final Dimension size) {
      try {
        final PropertyChangeEvent event = new PropertyChangeEvent(this, "size", getSize(), size);
        fireVetoableChange(event);
        leftBar.setSize(size);
        rightBar.setSize(size);
        firePropertyChange(event);
      }
      catch (PropertyVetoException e) {
        logger.error("The size (" + size + ") property change was vetoed!", e);
        throw new IllegalArgumentException("The size (" + size + ") property change was vetoed!");
      }
    }

    protected boolean isVisible() {
      return leftBar.isVisible();
    }

    protected void setVisible(final boolean visible) {
      final Boolean oldVisible = Boolean.valueOf(isVisible());
      leftBar.setVisible(visible);
      rightBar.setVisible(visible);
      firePropertyChange(new PropertyChangeEvent(this, "visible", oldVisible, Boolean.valueOf(visible)));
    }

    protected synchronized void move(final Dimension area) {
      leftBar.move(area);
      rightBar.move(area);
      firePropertyChange(new PropertyChangeEvent(this, "move", Boolean.FALSE, Boolean.TRUE));
    }

    protected synchronized void paint(final Graphics2D g2) {
      leftBar.paint(g2);
      rightBar.paint(g2);
    }
  }

  /**
   * Abstract BarModel class serving as the base class for the LeftBarModel and RightBarModel classes, pulling
   * common operations shared by both BarModel classes into this class.
   */
  private abstract class GradientBarModel extends BarModel {

    private boolean forward;

    protected GradientBarModel(final boolean forward) {
      setForward(forward);
    }

    private Color getColor1() {
      return (forward ? getBackground() : getBarColor());
    }

    private Color getColor2() {
      return (forward ? getBarColor() : getBackground());
    }

    public boolean isForward() {
      return forward;
    }

    public void setForward(final boolean forward) {
      this.forward = forward;
    }

    protected abstract Shape getClipShape();

    protected void paint(final Graphics2D g2) {
      if (isVisible()) {
        final Shape originalClip = g2.getClip();
        g2.setClip(getClipShape());
        g2.setPaint(new GradientPaint(((float) getOrigin().getX()), 0.0f, getColor1(),
          ((float) getOrigin().getX() + getSize().width) , 0.0f, getColor2()));
        g2.fill(new Rectangle2D.Double(getOrigin().getX(), getOrigin().getY(), getSize().getWidth(), getSize().getHeight()));
        g2.setClip(originalClip);
      }
    }
  }

  /**
   * LeftBarModel class represents the left bar that moves through the status bar UI component.
   */
  private final class LeftBarModel extends GradientBarModel {

    public LeftBarModel() {
      super(true);
    }

    protected Shape getClipShape() {
      final Dimension statusBarSize = ReflectionStatusBar.this.getSize();
      return new Rectangle2D.Double(0, 0, statusBarSize.getWidth() / 2, statusBarSize.getHeight());
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
      final int halfWidth = (int) (area.getWidth() / 2);
      if (isForward()) {
        getOrigin().x += getBarSpeed();
        setForward(Math.max(getOrigin().getX(), halfWidth) == halfWidth);
        if (!isForward()) {
          getOrigin().setLocation(halfWidth, 0);
        }
      }
      else {
        getOrigin().x -= getBarSpeed();
        setForward(Math.min(-getSize().getWidth(), getOrigin().getX()) == getOrigin().getX());
        if (isForward()) {
          getOrigin().setLocation(-getSize().getWidth(), 0);
        }
      }
    }
  }

  /**
   * RightBarModel class represents the right bar that moves through the status bar UI component.
   */
  private final class RightBarModel extends GradientBarModel {

    public RightBarModel() {
      super(false);
    }

    protected Shape getClipShape() {
      final Dimension statusBarSize = ReflectionStatusBar.this.getSize();
      final int halfWidth = (int) (statusBarSize.getWidth() / 2);
      return new Rectangle2D.Double(halfWidth, 0, halfWidth, statusBarSize.getHeight());
    }

    public void setSize(final Dimension size) {
      super.setSize(size);
      setOrigin(new Point(ReflectionStatusBar.this.getSize().width, 0));
    }

    protected void setVisible(final boolean visible) {
      if (isVisible() != visible) {
        setOrigin(new Point(ReflectionStatusBar.this.getSize().width, 0));
      }
      super.setVisible(visible);
    }

    protected void move(final Dimension area) {
      final int halfWidth = (int) (area.getWidth() / 2);
      if (isForward()) {
        getOrigin().x += getBarSpeed();
        setForward(Math.max(getOrigin().getX(), area.getWidth()) == area.getWidth());
        if (!isForward()) {
          getOrigin().setLocation(area.width, 0);
        }
      }
      else {
        getOrigin().x -= getBarSpeed();
        setForward(Math.min((halfWidth - getSize().getWidth()), getOrigin().getX()) == getOrigin().getX());
        if (isForward()) {
          getOrigin().setLocation((halfWidth - getSize().getWidth()), 0);
        }
      }
    }
  }

}
