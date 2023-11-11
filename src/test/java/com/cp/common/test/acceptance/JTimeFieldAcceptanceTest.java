/*
 * JTimeFieldAcceptanceTest (c) 13 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JTimeField
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.JTimeField;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class JTimeFieldAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(150, 70);
  private static final String FRAME_TITLE = "JTimeField Test";

  public JTimeFieldAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
    getContentPane().add(new JTimeField());
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new JTimeFieldAcceptanceTest(FRAME_TITLE);
  }

}
