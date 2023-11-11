/*
 * JAboutDialogAcceptanceTest.java (c) 21 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.JAboutDialog
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.ImageUtil;
import com.cp.common.swing.JAboutDialog;
import com.cp.common.util.AboutInfo;
import com.cp.common.util.AbstractAboutInfo;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JAboutDialogAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(200, 150);

  private static final String FRAME_TITLE = "JAboutDialog Test!";
  private static final String IMAGE_FILE = "etc/content/images/cp-common-about.gif";
  private static final String PRODUCT_NAME = "cp-common API & Framework";

  public JAboutDialogAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new GridLayout(1, 1));

    final AboutInfo aboutInfo = getAboutInfo();

    final JAboutDialog aboutDialog = new JAboutDialog(this, aboutInfo, null);

    final JButton showAboutDialog = new JButton("Show About Dialog");
    showAboutDialog.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        aboutDialog.setVisible(true);
      }
    });

    getContentPane().add(showAboutDialog);
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private AboutInfo getAboutInfo() {
    return new AbstractAboutInfo() {
      public Image getCompanyLogo() {
        Image companyLogo = null;

        try {
          companyLogo = ImageUtil.getImage(new File(IMAGE_FILE), JAboutDialogAcceptanceTest.this);
        }
        catch (FileNotFoundException e) {
          System.err.println("Failed to find image file (" + IMAGE_FILE + ")!");
        }

        return companyLogo;
      }

      public String getCopyright() {
        return null;
      }

      public String getProductName() {
        return PRODUCT_NAME;
      }

      public String getEULA() {
        return null;
      }
    };
  }

  public static void main(final String[] args) {
    new JAboutDialogAcceptanceTest(FRAME_TITLE);
  }

}
