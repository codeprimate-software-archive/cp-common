/*
 * JCalendarAcceptanceTest.java (c) 14 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JCalendar
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.JCalendar;
import com.cp.common.util.DateUtil;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JCalendarAcceptanceTest extends JFrame {

  private static final String FRAME_TITLE = "JCalendar Test";

  public JCalendarAcceptanceTest(final String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().add(getCalendarPanel(), BorderLayout.CENTER);
    setLocationRelativeTo(null);
    pack();
    setVisible(true);
  }

  private JPanel getCalendarPanel() {
    final JPanel calendarPanel = new JPanel(new GridLayout(1, 1, 5, 5));
    calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    calendarPanel.add(createCalendar());
    return calendarPanel;
  }

  private JCalendar createCalendar() {
    final JCalendar calendar = new JCalendar();
    calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
      public void propertyChange(final PropertyChangeEvent event) {
        System.out.println("calendar (" + DateUtil.DAY_MONTH_YEAR_FORMAT.format(((Calendar) event.getNewValue()).getTime()) + ")");
      }
    });
    return calendar;
  }

  public static void main(final String[] args) {
    new JCalendarAcceptanceTest(FRAME_TITLE);
  }

}
