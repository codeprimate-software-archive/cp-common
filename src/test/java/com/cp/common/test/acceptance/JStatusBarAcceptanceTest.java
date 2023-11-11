/*
 * JStatusBarAcceptanceTest.java (c) 30 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JStatusBar
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.AbstractStatusBar;
import com.cp.common.swing.JStatusBar;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JStatusBarAcceptanceTest extends JFrame {

  private static final int DEFAULT_BAR_SPEED = 7;

  private static final Dimension BAR_SIZE = new Dimension(200, 25);
  private static final Dimension BUTTON_SIZE = new Dimension(75, 25);
  private static final Dimension FRAME_SIZE = new Dimension(480, 150);
  private static final Dimension STATUS_BAR_SIZE = new Dimension(FRAME_SIZE.width, 25);
  private static final Dimension TEXT_FIELD_SIZE = new Dimension(240, 25);

  private static final String FRAME_TITLE = "JStatusBar Test";

  private JStatusBar statusBar;

  private JTextField textField;

  public JStatusBarAcceptanceTest(final String frameTitle) {
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
    final JPanel form = new JPanel(new BorderLayout(5, 5));
    form.add(buildInputForm(), BorderLayout.CENTER);
    form.add(buildToolBar(), BorderLayout.SOUTH);
    return form;
  }

  private JPanel buildInputForm() {
    final JPanel inputForm = new JPanel(new GridLayout(2, 1, 5, 5));

    inputForm.add(buildField("Status Bar Text: ", "Testing JStatusBar..."));

    final JSlider speedometer = new JSlider(1, 10, DEFAULT_BAR_SPEED);
    speedometer.setPaintLabels(true);
    speedometer.setPaintTicks(true);
    speedometer.setSnapToTicks(true);
    speedometer.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent event) {
        statusBar.setBarSpeed(speedometer.getValue());
      }
    });

    inputForm.add(speedometer);

    return inputForm;
  }

  private JPanel buildField(final String label, final String defaultText) {
    final JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    fieldPanel.add(new JLabel(label, JLabel.CENTER));
    textField = new JTextField(defaultText);
    textField.setPreferredSize(TEXT_FIELD_SIZE);
    fieldPanel.add(textField);

    return fieldPanel;
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
        if (!statusBar.isPaused()) {
          statusBar.setText(textField.getText());
        }
        statusBar.start();
      }
    });
    toolbar.add(start);

    final JButton pause = new XButton("Pause");
    pause.setPreferredSize(BUTTON_SIZE);
    pause.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        statusBar.pause();
      }
    });
    toolbar.add(pause);

    final JButton stop = new XButton("Stop");
    stop.setPreferredSize(BUTTON_SIZE);
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        statusBar.stop();
      }
    });
    toolbar.add(stop);

    return toolbar;
  }

  private AbstractStatusBar buildStatusBar() {
    statusBar = new JStatusBar(BAR_SIZE, "Testing JStatusBar...");
    //statusBar.setBarColor(new Color(119, 0, 0));
    statusBar.setBarSpeed(DEFAULT_BAR_SPEED);
    statusBar.setPreferredSize(STATUS_BAR_SIZE);
    return statusBar;
  }

  public static void main(final String[] args) {
    new JStatusBarAcceptanceTest(FRAME_TITLE);
  }

}
