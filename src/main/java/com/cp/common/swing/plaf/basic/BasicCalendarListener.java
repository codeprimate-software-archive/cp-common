/*
 * BasicCalendarListener.java (c) 9 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.4.16
 */

package com.cp.common.swing.plaf.basic;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.swing.JCalendar;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import org.apache.log4j.Logger;

public class BasicCalendarListener extends MouseInputAdapter implements PropertyChangeListener {

  private static final Logger logger = Logger.getLogger(BasicCalendarListener.class);

  private boolean repaint;

  private final EventListenerList mouseListenerList = new EventListenerList();

  private final JCalendar calendar;

  /**
   * Creates an instance of the BasicCalendarListener class initialized to the specified JCalendar Swing UI component.
   * The listener is responsible for receiving user input fields in the form of mouse or even keyboard events
   * transforming the user events into actions associated with the UI of the calendar component.
   * @param calendar the JCalendar Swing UI component generating input events for this listener to process.
   */
  public BasicCalendarListener(final JCalendar calendar) {
    if (ObjectUtil.isNull(calendar)) {
      logger.warn("The calendar for the BasicCalendarListener cannot be null!");
      throw new NullPointerException("The calendar for the BasicCalendarListener cannot be null!");
    }
    this.calendar = calendar;
  }

  /**
   * Returns the JCalendar Swing UI component registered with this listener to receive input events.
   * @return the JCalenar Swing UI component that this listener registered with to receive input events.
   */
  private JCalendar getCalendar() {
    return calendar;
  }

  /**
   * Returns a boolean value of the repaint property, which indicates whether the calendar UI needs to be refreshed
   * due to a user input event.  Note, the act of reading the repaint property value is a destructive read, resetting
   * it's value to false.
   * @return a boolean value indicating the current state of the repaint property value.
   */
  private boolean isRepaint() {
    final boolean oldRepaint = this.repaint;
    setRepaint(false);
    return oldRepaint;
  }

  /**
   * Sets the value fo the repaint property value to indicate whether a UI refresh operation on the calendar component
   * should occur.
   * @param repaint a boolean value indicating whether the calendar component UI should be refreshed.
   */
  private void setRepaint(final boolean repaint) {
    if (logger.isDebugEnabled()) {
      logger.debug("repaint (" + repaint + ")");
    }
    this.repaint = repaint;
  }

  /**
   * Registers a MouseListener for forwarding mouse input events to from this listener.
   * @param mouseListener the MouseListener object being registered with this listener.
   */
  public void addMouseInputListener(final MouseInputListener mouseListener) {
    if (ObjectUtil.isNotNull(mouseListener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding MouseInputListener (" + mouseListener + ")");
      }
      mouseListenerList.add(MouseInputListener.class, mouseListener);
    }
  }

  /**
   * Handles all mouse input events from the user in a standard way.
   * @param event the mouse event capturing the user's input.
   */
  private void handleMouseEvent(final MouseEvent event) {
    if (isRepaint()) {
      getCalendar().repaint();
    }
  }

  /**
   * Indicates that the user clicked the mouse over the calendar UI component.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mouseClicked(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mouseClicked(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Indicates that the user pressed and held the mouse button while dragging the mouse over the calendar UI component.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mouseDragged(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mouseDragged(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Indicates that the user moved the mouse from outside the calendar UI component to some position over
   * the calendar UI component.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mouseEntered(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mouseEntered(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Indicates that the user moved the mouse cursor from over the calendar UI component to outside the UI components
   * bounds.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mouseExited(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mouseExited(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Indicates that the user moved the mouse cursor over the calendar UI component.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mouseMoved(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mouseMoved(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Indicates that the user pressed a mouse button while in the calendar UI component's visable area.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mousePressed(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mousePressed(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Indicates that the user released the mouse button while in the calendar UI component's visible area.
   * @param event the MouseEvent capturing the user's input.
   */
  public void mouseReleased(final MouseEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator it = Arrays.asList(mouseListenerList.getListeners(MouseInputListener.class)).iterator(); it.hasNext(); ) {
          final MouseInputListener mouseListener = (MouseInputListener) it.next();
          mouseListener.mouseReleased(event);
        }
        handleMouseEvent(event);
      }
    });
  }

  /**
   * Receives property change events from the UI model components in the calendar's UI class.
   * @param event the PropertyChangeEvent object capturing the details of the property change.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    if (logger.isDebugEnabled()) {
      logger.debug("Received property change event on (" + event.getSource().getClass() + ") for property ("
        + event.getPropertyName() + ") which changed from (" + event.getOldValue() + ") to (" + event.getNewValue() + ")");
    }
    setRepaint(true);
  }

  /**
   * Removes a register MouseListener for receiving forwarded mouse events from this calendar UI listener.
   * @param mouseListener the MouseListener which was previously registered with this listener to receive
   * forward mouse events from the calendar component.
   */
  public void removeMouseInputListener(final MouseInputListener mouseListener) {
    if (ObjectUtil.isNotNull(mouseListener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing MouseInputListener (" + mouseListener + ")");
      }
      mouseListenerList.remove(MouseInputListener.class, mouseListener);
    }
  }

}
