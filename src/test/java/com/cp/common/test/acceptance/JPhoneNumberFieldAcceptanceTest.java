/*
 * JPhoneNumberFieldAcceptanceTest (c) 24 September 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JPhoneNumberField
 */

package com.cp.common.test.acceptance;

import com.cp.common.log4j.LoggingConfigurer;
import com.cp.common.swing.JPhoneNumberField;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class JPhoneNumberFieldAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(150, 75);
  private static final String FRAME_TITLE = "JPhoneNumberField Test";

  static {
    LoggingConfigurer.configure();
  }

  public JPhoneNumberFieldAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
    getContentPane().add(new JPhoneNumberField());
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new JPhoneNumberFieldAcceptanceTest(FRAME_TITLE);
  }

}
