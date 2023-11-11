/*
 * MatteAcceptanceTest.java (c) 12 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.Matte
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.Matte;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class MatteAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(640, 480);
  private static final String FRAME_TITLE = "Matte Test";

  public MatteAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().add(new Matte(), BorderLayout.CENTER);
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new MatteAcceptanceTest(FRAME_TITLE);
  }

}
