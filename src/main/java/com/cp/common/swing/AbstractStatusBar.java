/*
 * AbstractStatusBar.java (c) 26 January 2005
 *
 * Abstract framework class for implementing status bar UI components (widgets) in GUI based applications.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.2.2
 */

package com.cp.common.swing;

import com.cp.common.beans.event.NumberOutOfBoundsVetoableChangeListener;
import com.cp.common.beans.event.RequiredFieldVetoableChangeListener;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.AbstractDataModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

public abstract class AbstractStatusBar extends JComponent implements PropertyChangeListener {

  private static final Logger logger = Logger.getLogger(AbstractStatusBar.class);

  private static final int DEFAULT_BAR_SPEED = 7;
  private static final int MAX_BAR_SPEED = 10;
  private static final int MAX_WAIT_TIME = 100;
  private static final int MIN_BAR_SPEED = 1;

  private static final Color DEFAULT_BACKGROUND_COLOR = Color.LIGHT_GRAY;
  private static final Color DEFAULT_BAR_COLOR = new Color(56, 73, 101); // dark gray

  private boolean paused = false;
  private boolean running = false;

  // determines the velocity of the oscillating bar on a scale of 1 to 10.
  private int barSpeed = DEFAULT_BAR_SPEED;

  private BufferedImage imageBuffer;

  private Dimension previousStatusBarSize;

  private Graphics2D graphicsBuffer;

  /**
   * Creates an instance of the AbstractStatusBar class.  Registers a VetoableChangeListener on the barSpeed property
   * to constrain the values between MIN_BAR_SPEED and MAX_BAR_SPEED.  Also sets the background color to light gray.
   */
  public AbstractStatusBar() {
    addVetoableChangeListener(new NumberOutOfBoundsVetoableChangeListener("barSpeed",
      new Integer(MIN_BAR_SPEED), new Integer(MAX_BAR_SPEED)));
    getBarModel().addPropertyChangeListener("move", this);
    getBarModel().addPropertyChangeListener("visible", this);
    setBackground(DEFAULT_BACKGROUND_COLOR);
  }

  /**
   * Returns the instance of the BarModel object used to model the bar displayed in the UI of the status bar.
   * @return the BarModel object model of the visual bar in the status bar.
   */
  protected abstract BarModel getBarModel();

  /**
   * Returns the color of the bar that moves (oscilates) within the status bar UI component.
   * @return a Color object specifying the color of bar within the status bar.
   */
  public Color getBarColor() {
    return getBarModel().getColor();
  }

  /**
   * Sets the color of the bar that moves (oscilates) within the status bar UI component.
   * @param color the Color object specifying the color of the bar within the status bar.
   */
  public void setBarColor(final Color color) {
    getBarModel().setColor(color);
  }

  /**
   * Returns the dimensions of the bar displayed in this status bar.
   * @return a Dimension object specifying the width and height of the bar within this status bar.
   */
  public Dimension getBarSize() {
    return getBarModel().getSize();
  }

  /**
   * Sets the dimensions of the bar displayed in this status bar.
   * @param barSize a Dimension object specifying the width and height of the bar within this status bar.
   */
  public void setBarSize(final Dimension barSize) {
    getBarModel().setSize(barSize);
  }

  /**
   * Returns the speed, or velocity, at which the bar moves (or oscillates) within this status bar UI component.
   * Note, speed is measured on a scale of 1 to 10.  The greater the number, the higher the speed.  The
   * speed value determines the number of milliseconds between movement requests that are sent to the bar.
   * @return an integer value specifying the speed of the bar within this status bar.
   */
  public final int getBarSpeed() {
    return barSpeed;
  }

  /**
   * Sets the speed, or velocity, at which the bar moves (or oscillates) within the status bar UI component.
   * Note, speed is measured on a scale of 1 to 10.  The greater the number, the higher the speed.  The
   * speed value determines the number of milliseconds between movement requests that are sent to the bar.
   * @param barSpeed an integer value specifying the speed of movement of the bar within this status bar.
   */
  public final void setBarSpeed(final int barSpeed) {
    if (logger.isDebugEnabled()) {
      logger.debug("barSpeed (" + barSpeed + ")");
    }
    try {
      final int oldBarSpeed = getBarSpeed();
      fireVetoableChange("barSpeed", new Integer(oldBarSpeed), new Integer(barSpeed));
      this.barSpeed = barSpeed;
      firePropertyChange("barSpeed", new Integer(oldBarSpeed), new Integer(barSpeed));
    }
    catch (PropertyVetoException e) {
      logger.error("The barSpeed (" + barSpeed + ") value was vetoed!", e);
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Returns a Graphics object (off-screen memory buffer) in which to draw images and other graphics primitives.
   * @return a Graphics object used for double-buffering and painting the UI of the status bar.
   */
  protected final Graphics2D getGraphicsBuffer() {
    final Dimension currentStatusBarSize = getSize();

    if (logger.isDebugEnabled()) {
      logger.debug("previousStatusBarSize (" + previousStatusBarSize + ")");
      logger.debug("currentStatusBarSize (" + currentStatusBarSize + ")");
    }

    if (!currentStatusBarSize.equals(previousStatusBarSize)) {
      imageBuffer = new BufferedImage(currentStatusBarSize.width, currentStatusBarSize.height, BufferedImage.TYPE_INT_RGB);
      graphicsBuffer = imageBuffer.createGraphics();
      previousStatusBarSize = currentStatusBarSize;
    }

    return graphicsBuffer;
  }

  /**
   * Returns the image buffer associated with the graphics context (buffer) used to paint the status bar UI.
   * This image buffer is used to store a snapshot of the status bar in a moment of time.
   * @return an off-screen Image (context) containing the UI of the status bar.
   */
  protected final Image getImageBuffer() {
    return imageBuffer;
  }

  /**
   * Determines whether the status bar movement has been suspended.
   * @return a boolean value indicating whether the status bar movement has been suspended.
   */
  public final boolean isPaused() {
    return paused;
  }

  /**
   * Sets a condition specifying whether the status bar movement should be suspended.
   * @param paused a boolean value indicating if status bar movement should be suspended.
   */
  private void setPaused(final boolean paused) {
    if (logger.isDebugEnabled()) {
      logger.debug("paused (" + paused + ")");
    }
    this.paused = paused;
  }

  /**
   * Determines whether the status bar is running.  A status bar is considered running if the StatusBarRunner Thread
   * is actively updating the state of the bar, thus causing movement to occur.
   * @return a boolean value indicating if the status bar is running.
   */
  public final boolean isRunning() {
    return running;
  }

  /**
   * Sets the state of the status bar to either a running or non-running state.
   * @param running is a boolean value indicating if the status bar should be made active.
   */
  private void setRunning(final boolean running) {
    if (logger.isDebugEnabled()) {
      logger.debug("running (" + running + ")");
    }
    this.running = running;
  }

  /**
   * Returns the number of milliseconds that the StatusBarRunner Thread will sleep, thereby affecting the
   * velocity of the bar oscillating within the status bar.
   * @return an integer value specifying the number of milliseconds that the StatusBarRunner Thread will sleep.
   */
  private int getWaitTime() {
    return (MAX_WAIT_TIME - ((getBarSpeed() - 1) * 10));
  }

  /**
   * Paints the UI of the status bar component.
   * @param g the Graphic context in which to apply the paint operations.
   */
  protected final void paintComponent(final Graphics g) {
    super.paintComponent(g);

    final Graphics2D g2 = getGraphicsBuffer();

    g2.setColor(getBackground());
    g2.setClip(g.getClip());
    g2.fill(new Rectangle2D.Double(0.0, 0.0, getWidth(), getHeight()));
    paintStatusBar(g2); // paint the UI of the status bar
    g.drawImage(getImageBuffer(), 0, 0, null);
  }

  /**
   * Paints the UI of the status bar component.  This method is meant to be overridden to apply custom paint
   * operations depending upon the status bar implementation.
   * @param g2 a Graphics2D object context in which to apply the paint operations.
   */
  protected void paintStatusBar(final Graphics2D g2) {
    getBarModel().paint(g2);
  }

  /**
   * Receives model events from the BarModel signifying changes in position of the bar in order to refresh the UI
   * of the status bar.
   * @param event the Event object indicating the model state change of the bar.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    if (logger.isDebugEnabled()) {
      logger.debug("received event from (" + event.getSource().getClass().getName() + ")");
    }
    repaint();
  }

  /**
   * Suspends the state of the status bar.
   */
  public final synchronized void pause() {
    if (logger.isInfoEnabled()) {
      logger.info("pausing StatusBarRunner on status bar (" + getClass().getName() + ")");
    }
    if (isRunning()) {
      setPaused(true);
    }
  }

  /**
   * Invokes the status bar setting it to an active state and starting the StatusBarRunner to generate model
   * updates.
   */
  public final synchronized void start() {
    if (!isRunning()) {
      if (logger.isInfoEnabled()) {
        logger.info("starting StatusBarRunner on status bar (" + getClass().getName() + ")");
      }
      new Thread(new StatusBarRunner()).start();
      setPaused(false);
      setRunning(true);
    }
    else if (isPaused()) {
      setPaused(false);
      notifyAll();
    }
    if (logger.isInfoEnabled()) {
      logger.info("status bar (" + getClass().getName() + ") is running!");
    }
  }

  /**
   * Sets the status bar to an inactive state by stopping the StatusBarRunner from sending model updates.
   */
  public final synchronized void stop() {
    if (logger.isInfoEnabled()) {
      logger.info("stopping StatusBarRunner on status bar (" + getClass().getName() + ")");
    }
    setPaused(false);
    setRunning(false);
    notifyAll();
  }

  /**
   * Abstract Bean class modeling the visual bar that is displayed in the status bar.
   */
  protected abstract class BarModel extends AbstractDataModel {

    private boolean visible = false;
    private Color color = DEFAULT_BAR_COLOR;
    private Dimension size;
    private Point origin;

    /**
     * Creates an instance of the BarModel class.  Registers the origin and size properties as required fields.
     */
    public BarModel() {
      addVetoableChangeListener("origin", RequiredFieldVetoableChangeListener.INSTANCE);
      addVetoableChangeListener("size", RequiredFieldVetoableChangeListener.INSTANCE);
    }

    /**
     * Returns a Color object specifying the color of the bar.
     * @return a Color object specifying the color of the bar.
     */
    public Color getColor() {
      return color;
    }

    /**
     * Sets the color of the specified bar.
     * @param color a Color object used to paint the bar.
     */
    public void setColor(Color color) {
      if (logger.isDebugEnabled()) {
        logger.debug("color (" + color + ")");
      }
      final Color oldColor = getColor();
      this.color = (Color) ObjectUtil.getDefaultValue(color, DEFAULT_BAR_COLOR);
      firePropertyChange(new PropertyChangeEvent(this, "color", oldColor, color));
    }

    /**
     * Return a Point object specifying the origin of the bar in the coordinate system.
     * @return a Point object specifying the bar's location.
     */
    protected Point getOrigin() {
      return origin;
    }

    /**
     * Set the originating location of the bar in the coordinate system.
     * @param origin a Point specifying the originating location of the bar.
     */
    protected void setOrigin(final Point origin) {
      if (logger.isDebugEnabled()) {
        logger.debug("origin (" + origin + ")");
      }
      try {
        final PropertyChangeEvent event = new PropertyChangeEvent(this, "origin", getOrigin(), origin);
        fireVetoableChange(event);
        this.origin = origin;
        firePropertyChange(event);
      }
      catch (PropertyVetoException e) {
        logger.error("The origin (" + origin + ") value was vetoed!", e);
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    /**
     * Returns a Dimension object specifying the size of the bar.
     * @return a Dimension object specifying the width and heigth of the bar.
     */
    public Dimension getSize() {
      return size;
    }

    /**
     * Returns the size of the bar.
     * @param size a Dimension object specifying the width and height of the bar.
     */
    public void setSize(final Dimension size) {
      if (logger.isDebugEnabled()) {
        logger.debug("size (" + size + ")");
      }
      try {
        final PropertyChangeEvent event = new PropertyChangeEvent(this, "size", getSize(), size);
        fireVetoableChange(event);
        this.size = size;
        firePropertyChange(event);
      }
      catch (PropertyVetoException e) {
        logger.error("The size (" + size + ") value was vetoed!", e);
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    /**
     * Returns the value represented by this data model.
     * @return an Object containing the value represented by this data model.
     */
    public final Object getValue() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets the value to be represented by this data model.
     * @param value an Object containing the value to be represented by this data model.
     */
    public final void setValue(final Object value) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Determines whether the bar is visible.
     * @return a boolean value indicating if the bar is visible from the UI.
     */
    protected boolean isVisible() {
      return visible;
    }

    /**
     * Sets whether the bar is visible, thus, whether the bar should be painted.
     * @param visible a boolean value indicating if the bar should be made visible (painted).
     */
    protected void setVisible(final boolean visible) {
      if (logger.isDebugEnabled()) {
        logger.debug("visible (" + visible + ")");
      }
      final boolean oldVisible = isVisible();
      this.visible = visible;
      firePropertyChange(new PropertyChangeEvent(this, "visible", Boolean.valueOf(oldVisible), Boolean.valueOf(visible)));
    }

    /**
     * Causes the bar within the status bar to move one unit.  A unit can be defined as a 1 to N pixels in screen
     * coordinates, 1 to N using world units, or whatever the implementing class deems the value of the unit.
     * Note, move causes model events to be fired to update the view.
     * @param area a Dimension object specifying the area in which the status bar can move.
     */
    protected abstract void move(Dimension area);

    /**
     * Paints the face of the bar.
     * @param g the Graphics context used to perform the paint operations to draw the bar.
     */
    protected abstract void paint(Graphics2D g2);

    /**
     * Returns a String object describing this BarModel object.
     * @return a String value containing the state of this BarModel object.
     */
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{color = ");
      buffer.append(getColor());
      buffer.append(", origin = ").append(getOrigin());
      buffer.append(", size = ").append(getSize());
      buffer.append(", visible = ").append(isVisible());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * The StatusBarRunner class handles the animation (movement) of the status bar.
   */
  protected final class StatusBarRunner implements Runnable {

    public void run() {
      getBarModel().setVisible(true);
      while (isRunning()) {
        getBarModel().move(getSize());
        try {
          Thread.sleep(getWaitTime());
          while (isPaused()) {
            synchronized (AbstractStatusBar.this) {
              AbstractStatusBar.this.wait();
            }
          }
        }
        catch (InterruptedException e) {
          logger.warn("status bar (" + getClass().getName() + ") failed to sleep", e);
        }
      }
      getBarModel().setVisible(false);
    }
  }

}
