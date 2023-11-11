/*
 * JAboutDialog.java (c) 17 April 2001
 *
 * This class implements a window to display information about a particular application.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.10
 * @see com.cp.common.awt.CAboutDialog
 * @see com.cp.common.net.Browser
 * @see com.cp.common.util.AboutInfo
 * @see javax.swing.JDialog
 */

package com.cp.common.swing;

import com.cp.common.net.Browser;
import com.cp.common.util.AboutInfo;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public final class JAboutDialog extends JDialog {

  private static final boolean DEFAULT_MODAL = true;
  private static final boolean DEFAULT_RESIZABLE = false;

  /**
   * Constructs an instance of the JAboutDialog Swing component class.
   * @param owner the JFrame container owning this dialog.
   * @param aboutInfo the AboutInfo instance containing the content information in the dialog.
   * @param browser the Browser object used to invoke a URL from the dialog.
   */
  public JAboutDialog(final JFrame owner, final AboutInfo aboutInfo, final Browser browser) {
    super(owner, "About " + aboutInfo.getProductName(), DEFAULT_MODAL);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setResizable(DEFAULT_RESIZABLE);
    getContentPane().add(getImageComponent(aboutInfo.getCompanyLogo()), BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(owner);
  }

  private JComponent getImageComponent(final Image img) {
    return new JLabel(new ImageIcon(img));
  }

}
