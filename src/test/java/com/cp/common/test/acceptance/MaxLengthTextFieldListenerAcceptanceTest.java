/*
 * MaxLengthTextFieldListenerAcceptanceTest.java (c) 5 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.MaxLengthTextFieldListener
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.MaxLengthTextFieldListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MaxLengthTextFieldListenerAcceptanceTest extends JFrame {

  private static final Dimension SIZE = new Dimension(320, 60);
  private static final String FRAME_TITLE = "MaxLengthTextFieldListener Test";

  public MaxLengthTextFieldListenerAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final Container contentPane = getContentPane();
    contentPane.add(new JLabel("propertyName", JLabel.CENTER), BorderLayout.WEST);

    final TextField propertyValueField = new TextField(40);
    propertyValueField.addKeyListener(new MaxLengthTextFieldListener(20));
    contentPane.add(propertyValueField, BorderLayout.CENTER);

    setSize(SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new MaxLengthTextFieldListenerAcceptanceTest();
  }

}
