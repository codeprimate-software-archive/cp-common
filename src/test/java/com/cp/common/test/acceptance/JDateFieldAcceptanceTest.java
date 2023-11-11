/*
 * JDateFieldAcceptanceTest (c) 12 September 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JDateField
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.JDateField;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class JDateFieldAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(150, 75);
  private static final String FRAME_TITLE = "JDateField Test";

  public JDateFieldAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
    getContentPane().add(new JDateField());
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new JDateFieldAcceptanceTest(FRAME_TITLE);
  }

}
