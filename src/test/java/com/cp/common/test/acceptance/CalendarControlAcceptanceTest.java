/*
 * CalendarControlAcceptanceTest.java (c) 16 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.CalendarControl
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.CalendarControl;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CalendarControlAcceptanceTest extends JFrame {

  private static final String FRAME_TITLE = "CalendarControl Test";

  public CalendarControlAcceptanceTest(final String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().add(getCalendarControlPanel(), BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel getCalendarControlPanel() {
    final JPanel calendarControlPanel = new JPanel(new GridLayout(1, 1, 5, 5));
    calendarControlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    calendarControlPanel.add(new CalendarControl());
    return calendarControlPanel;
  }

  public static void main(final String[] args) {
    new CalendarControlAcceptanceTest(FRAME_TITLE);
  }

}
