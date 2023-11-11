/*
 * JDirectoryChooserAcceptanceTest.java (c) 25 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JDirectoryChooser
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.JDirectoryChooser;
import com.cp.common.swing.XButton;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class JDirectoryChooserAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(300, 75);
  private static final String FRAME_TITLE = "JDirectoryChooser Test";

  public JDirectoryChooserAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    final XButton openDir = new XButton("Open Directory");
    openDir.setPreferredSize(new Dimension(100, 25));

    openDir.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent event) {
        final JDirectoryChooser dirChooser = new JDirectoryChooser(JDirectoryChooserAcceptanceTest.this);
        System.out.println(dirChooser.showDialog());
      }
    });

    getContentPane().add(openDir);

    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new JDirectoryChooserAcceptanceTest(FRAME_TITLE);
  }

}
