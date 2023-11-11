/*
 * CButtonAcceptanceTest.java (c) 7 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.CButton
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.CButton;
import com.cp.common.awt.ImageUtil;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class CButtonAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(100, 100);
  private static final String FRAME_TITLE = "CButton Test";

  public CButtonAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new GridLayout(1,1));
    getContentPane().add(new CButton(ImageUtil.getRightArrowImage(FRAME_SIZE)));
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new CButtonAcceptanceTest(FRAME_TITLE);
  }

}
