/*
 * NetscapeStatusBarAcceptanceTest.java (c) 31 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.NetscapeStatusBar
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.AbstractStatusBar;
import com.cp.common.swing.NetscapeStatusBar;
import com.cp.common.swing.XButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NetscapeStatusBarAcceptanceTest extends JFrame {

  private static final int DEFAULT_BAR_SPEED = 7;

  private static final Dimension BAR_SIZE = new Dimension(200, 25);
  private static final Dimension BUTTON_SIZE = new Dimension(75, 25);
  private static final Dimension FRAME_SIZE = new Dimension(480, 120);
  private static final Dimension STATUS_BAR_SIZE = new Dimension(FRAME_SIZE.width, 25);

  private static final String FRAME_TITLE = "NetscapeStatusBar Test";

  private NetscapeStatusBar netscapeStatusBar;

  public NetscapeStatusBarAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().add(buildForm(), BorderLayout.CENTER);
    getContentPane().add(buildStatusBar(), BorderLayout.SOUTH);
    setSize(FRAME_SIZE);
    setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel buildForm() {
    final JPanel form = new JPanel(new GridLayout(2, 1, 5, 5));

    final JSlider speedometer = new JSlider(1, 10, DEFAULT_BAR_SPEED);
    speedometer.setPaintLabels(true);
    speedometer.setPaintTicks(true);
    speedometer.setSnapToTicks(true);
    speedometer.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent event) {
        netscapeStatusBar.setBarSpeed(speedometer.getValue());
      }
    });

    form.add(speedometer);
    form.add(buildToolBar());

    return form;
  }

  private JToolBar buildToolBar() {
    final JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);

    toolbar.setBorder(BorderFactory.createEmptyBorder());
    toolbar.setFloatable(false);
    toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    final JButton start = new XButton("Start");
    start.setPreferredSize(BUTTON_SIZE);
    start.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        netscapeStatusBar.start();
      }
    });
    toolbar.add(start);

    final JButton pause = new XButton("Pause");
    pause.setPreferredSize(BUTTON_SIZE);
    pause.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        netscapeStatusBar.pause();
      }
    });
    toolbar.add(pause);

    final JButton stop = new XButton("Stop");
    stop.setPreferredSize(BUTTON_SIZE);
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        netscapeStatusBar.stop();
      }
    });
    toolbar.add(stop);

    return toolbar;
  }

  private AbstractStatusBar buildStatusBar() {
    netscapeStatusBar = new NetscapeStatusBar(BAR_SIZE);
    netscapeStatusBar.setBarSpeed(DEFAULT_BAR_SPEED);
    netscapeStatusBar.setPreferredSize(STATUS_BAR_SIZE);
    return netscapeStatusBar;
  }

  public static void main(final String[] args) {
    new NetscapeStatusBarAcceptanceTest(FRAME_TITLE);
  }

}
