/*
 * CStatusBarAcceptanceTest.java (c) 7 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.CStatusBar
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.CStatusBar;
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
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class CStatusBarAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(480, 130);
  private static final String FRAME_TITLE = "CStatusBar Test";

  private final CStatusBar statusBar = new CStatusBar();

  private final JTextField textField = new JTextField("Testing CStatusBar...");

  public CStatusBarAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    statusBar.setSize(new Dimension((int) FRAME_SIZE.getWidth(), 25));
    statusBar.setBarLength(150);
    statusBar.setBarSpeed(95);

    getContentPane().add(buildUI(), BorderLayout.CENTER);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel buildField() {
    final JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    fieldPanel.add(new JLabel("Enter Status Bar Text: ", JLabel.CENTER));
    textField.setPreferredSize(new Dimension(240, 25));
    fieldPanel.add(textField);
    return fieldPanel;
  }

  private JToolBar buildToolBar() {
    final JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);

    toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
    toolbar.setFloatable(false);

    final JButton start = new JButton("Start");
    start.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        if (!statusBar.isPaused()) {
          statusBar.setText(textField.getText());
        }
        statusBar.start();
      }
    });
    toolbar.add(start);

    final JButton pause = new JButton("Pause");
    pause.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        statusBar.pause();
      }
    });
    toolbar.add(pause);

    final JButton stop = new JButton("Stop");
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        statusBar.stop();
      }
    });
    toolbar.add(stop);

    return toolbar;
  }

  private JPanel buildUI() {
    final JPanel form = new JPanel(new GridLayout(2, 1));
    form.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    form.add(buildField());
    form.add(buildToolBar());
    return form;
  }

  public static void main(final String[] args) {
    new CStatusBarAcceptanceTest();
  }

}
