/*
 * XButtonAcceptanceTest.java (c) 18 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.XButton
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.XButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class XButtonAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(150, 100);
  private static final String FRAME_TITLE = "XButton Test";

  public XButtonAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().add(getToolBar(), BorderLayout.CENTER);
    setSize(FRAME_SIZE);
    setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JToolBar getToolBar() {
    final JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
    toolBar.setBorder(BorderFactory.createEmptyBorder());
    toolBar.setFloatable(false);
    toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
    //toolBar.setRollover(true);

    final JButton button = (JButton) toolBar.add(new XButton("Test"));
    button.setPreferredSize(new Dimension(75, 25));
    //button.setRolloverEnabled(false);

    return toolBar;
  }

  public static void main(final String[] args) {
    new XButtonAcceptanceTest(FRAME_TITLE);
  }

}
