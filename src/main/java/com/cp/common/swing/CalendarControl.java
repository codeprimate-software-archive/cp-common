/*
 * CalendarControl.java (c) 30 March 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 * @see com.cp.common.swing.JCalendar
 * @see com.cp.common.swing.JDateField
 * @see com.cp.common.swing.JTimeField
 * @see javax.swing.JPanel
 */

package com.cp.common.swing;

import com.cp.common.awt.ImageUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.apache.log4j.Logger;

public final class CalendarControl extends JPanel {

  private static final Logger logger = Logger.getLogger(CalendarControl.class);

  private static final boolean LIGHTWEIGHT_POPUP_ENABLED = false;

  private static final Dimension BUTTON_SIZE = new Dimension(21, 21);

  private CalendarModel calendarModel;

  private JButton showCalendar;

  private JCalendar calendar;

  private JDateField dateField;

  /**
   * Creates a new instance of the CalendarControl component class.
   */
  public CalendarControl() {
    calendarModel = new DefaultCalendarModel();
    buildUI();
  }

  /**
   * Returns an AncestorListener responsible for listening to add, remove and move events from the calendar control.
   * @param popupMenu the popup menu that needs to be hidden if the arrangement, presence or absense of the calendar
   * component (popup) changes.
   * @return an AncestorListener for listening to events from the calendar control.
   */
  private AncestorListener getAncestorListener(final JPopupMenu popupMenu) {
    return new AncestorListener() {
      public void ancestorAdded(final AncestorEvent event) {
        logger.debug("ancestor added");
        popupMenu.setVisible(false);
      }

      public void ancestorRemoved(final AncestorEvent event) {
        logger.debug("ancestor removed");
        popupMenu.setVisible(false);
      }

      public void ancestorMoved(final AncestorEvent event) {
        logger.debug("ancestor moved");
        popupMenu.setVisible(false);
      }
    };
  }

  /**
   * Creates a FocusListener to listen for calendar focus lost events in order to hide the popup menu containing
   * the calendar component.
   * @param popupMenu the popup menu displaying the calendar component.
   * @return a FocusListener controling the display of the popup menu when the calendar loses focus.
   */
  private FocusListener getCalendarFocusListener(final JPopupMenu popupMenu) {
    return new FocusAdapter() {
      public void focusLost(final FocusEvent event) {
        if (logger.isDebugEnabled()) {
          logger.debug("focus lost on (" + event.getSource().getClass().getName() + ")");
        }
        popupMenu.setVisible(false);
      }
    };
  }

  /**
   * Returns the CalendarModel used by this calendar control to represent the date and time.
   * @return an instance of the CalendarModel class to store the date and time information for the
   * date field and calendar component.
   */
  public CalendarModel getModel() {
    return calendarModel;
  }

  /**
   * Returns an ActionListener for the showCalendar button of the calendar control used by the user to receive the
   * event to show the calendar component.
   * @param popupMenu the popup menu that we be made visible displaying the calendar component.
   * @return an ActionListener used to handle the event of showing the calendar component.
   */
  private ActionListener getShowCalendarActionListener(final JPopupMenu popupMenu) {
    return new ActionListener() {
      public void actionPerformed(final ActionEvent event) {
        if (!popupMenu.isVisible()) {
          logger.debug("popup menu was not visible");

          final Dimension calendarSize = calendar.getPreferredSize();
          final Dimension showCalendarButtonSize = showCalendar.getSize();
          final Point showCalendarButtonScreenLocation = showCalendar.getLocation();
          final Point popupMenuScreenLocation = new Point((showCalendarButtonScreenLocation.x - (calendarSize.width - showCalendarButtonSize.width)),
            (showCalendarButtonScreenLocation.y + showCalendarButtonSize.height));

          if (logger.isDebugEnabled()) {
            logger.debug("calendarSize (" + calendarSize + ")");
            logger.debug("showCalendarButtonSize (" + showCalendarButtonScreenLocation + ")");
            logger.debug("showCalendarButtonScreenLocation (" + showCalendarButtonScreenLocation + ")");
            logger.debug("popupMenuScreenLocation (" + popupMenuScreenLocation + ")");
          }

          popupMenu.show(CalendarControl.this, popupMenuScreenLocation.x, popupMenuScreenLocation.y);
          calendar.requestFocus();
        }
        else {
          logger.debug("popup menu was visisble");
          popupMenu.setVisible(false);
        }
      }
    };
  }

  /**
   * Constructs and lays out the visual UI components constituting the user interface to the calendar control.
   */
  private void buildUI() {
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

    dateField = (JDateField) add(new JDateField());
    dateField.setDataModel(getModel());

    calendar = new JCalendar();
    calendar.setModel(getModel());

    final JPopupMenu popupMenu = new JPopupMenu();
    popupMenu.add(calendar);
    popupMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    popupMenu.setLightWeightPopupEnabled(LIGHTWEIGHT_POPUP_ENABLED);
    popupMenu.pack();
    addAncestorListener(getAncestorListener(popupMenu));

    calendar.addFocusListener(getCalendarFocusListener(popupMenu));

    showCalendar = (XButton) add(new XButton(new ImageIcon(ImageUtil.getDownArrowImage(new Dimension(10, 10)))));
    showCalendar.setPreferredSize(BUTTON_SIZE);
    showCalendar.addActionListener(getShowCalendarActionListener(popupMenu));
  }

  /**
   * The overridden setEnabled method sets the CalendarControl component to enabled with a boolean value of true and
   * disabled with a boolean value of false.
   * @param enabled is a boolean value indicating true to enable this CalendarControl component, false to disable it.
   */
  public void setEnabled(final boolean enabled) {
    if (logger.isDebugEnabled()) {
      logger.debug("enabled (" + enabled + ")");
    }
    super.setEnabled(enabled);
    dateField.setEnabled(enabled);
    calendar.setEnabled(enabled);
    showCalendar.setEnabled(enabled);
  }

}
