/*
 * CAboutDialogAcceptanceTest.java (c) 7 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.CAboutDialog
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.CAboutDialog;
import com.cp.common.awt.ImageUtil;
import com.cp.common.net.Browser;
import com.cp.common.util.AboutInfo;
import com.cp.common.util.AbstractAboutInfo;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;

public class CAboutDialogAcceptanceTest extends JFrame implements Browser {

  private static final Dimension FRAME_SIZE = new Dimension(100, 75);
  private static final String FRAME_TITLE = "CAboutDialog Test";

  private static final String COMPANY_NAME = "Code Primate";
  private static final String COMPANY_LOGO_IMAGE_FILE = "etc/content/images/cp.gif";
  private static final String LICENSEE = "John J. Blum";
  private static final String PRODUCT_LOGO_IMAGE_FILE = "etc/content/images/productLogo.gif";
  private static final String PRODUCT_NAME = "CAboutDialog.java";
  private static final String URL_STR = "http://www.codeprimate.com";
  private static final String VERSION_NUMBER = "1.0.0";

  public CAboutDialogAcceptanceTest(final String frameTitle) throws Exception {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new GridLayout(1, 1));

    final AboutInfo aboutInfo = getAboutInfo();
    aboutInfo.setCompanyLogo(ImageUtil.getImage(new File(COMPANY_LOGO_IMAGE_FILE), this));
    aboutInfo.setCompanyName(COMPANY_NAME);
    aboutInfo.setCompanyURL(new URL(URL_STR));
    aboutInfo.setLicensee(LICENSEE);
    aboutInfo.setProductLogo(ImageUtil.getImage(new File(PRODUCT_LOGO_IMAGE_FILE), this));
    aboutInfo.setProductName(PRODUCT_NAME);
    aboutInfo.setVersionNumber(VERSION_NUMBER);

    final CAboutDialog aboutDialog = new CAboutDialog(this, aboutInfo, this);

    final JButton openDialog = new JButton("Open Dialog");
    openDialog.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        aboutDialog.setVisible(true);
      }
    });

    getContentPane().add(openDialog);
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private AboutInfo getAboutInfo() {
    return new AbstractAboutInfo() {
      public String getCopyright() {
        final Calendar today = Calendar.getInstance();
        final StringBuffer buffer = new StringBuffer("Copyright ");
        buffer.append(AboutInfo.COPYRIGHT_SYMBOL);
        buffer.append(" ");
        buffer.append(today.get(Calendar.YEAR));
        buffer.append(", ");
        buffer.append(getCompanyName());
        buffer.append(" ");
        buffer.append(AboutInfo.ALL_RIGHTS_RESERVED);
        return buffer.toString();
      }

      public String getEULA() {
        return "End User License Agreement";
      }
    };
  }

  public void goToURL(final URL address) {
    System.out.println("URL (" + address.toExternalForm() + ")");
  }

  public static void main(final String[] args) throws Exception {
    new CAboutDialogAcceptanceTest(FRAME_TITLE);
  }

}
