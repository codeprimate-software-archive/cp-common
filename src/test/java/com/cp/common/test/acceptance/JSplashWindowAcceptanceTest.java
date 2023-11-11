/*
 * JSplashWindowAcceptanceTest.java (c) 20 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JSplashWindow
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.ImageUtil;
import com.cp.common.swing.JSplashWindow;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JSplashWindowAcceptanceTest extends JFrame {

  private static final boolean RESIZABLE = false;

  private static final int NUMBER_OF_SECONDS = 5;
  private static final int PAUSE = 10000;

  private static final Dimension FRAME_SIZE = new Dimension(200, 100);

  private static final String EXIT_BUTTON_NAME = "EXIT JSplashWindow DEMO";
  private static final String FRAME_TITLE = "JSplashWindow Test";
  private static final String IMAGE_FILE = "etc/content/images/ape.gif";

  public JSplashWindowAcceptanceTest(final String frameTitle) throws FileNotFoundException {
    super(frameTitle);
    displaySplashWindowByOwner();
    //displaySplashWindowByThread();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final JButton exitButton = new JButton(EXIT_BUTTON_NAME);
    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        setVisible(false);
        dispose();
      }
    });

    getContentPane().setLayout(new GridLayout(1, 1));
    getContentPane().add(exitButton);
    setSize(FRAME_SIZE);
    setResizable(RESIZABLE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void displaySplashWindowByOwner() throws FileNotFoundException {
    new JSplashWindow(this, ImageUtil.getImage(new File(IMAGE_FILE), this));
    try {
      Thread.sleep(PAUSE);
    }
    catch (InterruptedException ignore) {
      // ignore the InterruptedException
    }
  }

  private void displaySplashWindowByThread() throws FileNotFoundException {
    new JSplashWindow(this, ImageUtil.getImage(new File(IMAGE_FILE), this), NUMBER_OF_SECONDS);
  }

  public static void main(final String[] args) throws Exception {
    new JSplashWindowAcceptanceTest(FRAME_TITLE);
  }

}
