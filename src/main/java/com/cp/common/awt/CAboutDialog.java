/*
 * CAboutDialog.java (c) 20 July 2002
 *
 * This class implements a dialog to display information "about" a particular application.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see com.cp.common.swing.JAboutDialog
 * @see java.awt.Dialog
 */

package com.cp.common.awt;

import com.cp.common.net.Browser;
import com.cp.common.net.URLListener;
import com.cp.common.util.AboutInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CAboutDialog extends Dialog {

  private static final boolean MODAL = true;

  private static final Color NAVY_BLUE = new Color(0, 0, 176);

  private static final Dimension DIALOG_SIZE = new Dimension(400, 300);

  private static final Font BOLD_ARIAL_16 = new Font("Arial", Font.BOLD, 16);
  private static final Font BOLD_ARIAL_12 = new Font("Arial", Font.BOLD, 12);

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates a new instance of the CAboutDialog class initalized with information about the company
   * and procduct and the frame from which this dialog was derived.  A reference to a browser object
   * responsible for directing the user to the company website, specified by the URL, is also passed
   * to the constructor.
   * @param owner is a reference to the Frame of the application that opened this dialog.
   * @param aboutInfo is an AbstractAboutInfo object containing informatiou about the name of the product,
   * the product logo, the product version, copyright information, and company logo
   * @param browser is an interface reference to a browser object responsible for directing the
   * user to the company URL.
   */
  public CAboutDialog(final Frame owner, final AboutInfo aboutInfo, final Browser browser) {
    super(owner, "About " + aboutInfo.getProductName(), MODAL);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        dispose();
      }
    });

    setResizable(false);
    setSize(DIALOG_SIZE);
    setLocationRelativeTo(owner);
    buildUI(aboutInfo, browser);
  }

  /**
   * Manages the layout & presentation of the user interface for the about dialog.  The buildUI method
   * constructs a border around the content of the about dialog.
   */
  private void buildUI(final AboutInfo aboutInfo, final Browser browser) {
    setBackground(Color.white);
    setLayout(new GridBagLayout());
    add(buildAboutInfoPanel(aboutInfo, browser), getConstraints());
  }

  /**
   * Handles the about dialog layout.
   */
  private Panel buildAboutInfoPanel(final AboutInfo aboutInfo, final Browser browser) {
    final Panel aboutInfoPanel = new Panel(new BorderLayout(5,5));

    final Label imageLabel = new CompanyLogoLabel(aboutInfo.getCompanyLogo());
    imageLabel.setBackground(Color.white);
    aboutInfoPanel.add(imageLabel, BorderLayout.WEST);

    aboutInfoPanel.add(buildInfoPanel(aboutInfo, browser), BorderLayout.CENTER);

    return aboutInfoPanel;
  }

  /**
   * Constructs the interface for the textual information in the about dialog.
   */
  private Panel buildInfoPanel(final AboutInfo aboutInfo, final Browser browser) {
    final Panel infoPanel = new Panel(new GridLayout(7, 1));

    infoPanel.setBackground(Color.lightGray);
    infoPanel.setSize(new Dimension(300, 300));

    Label tempLabel = null;

    // Product Logo
    tempLabel = (Label) infoPanel.add(new ProductLogoLabel(aboutInfo.getProductLogo()));
    tempLabel.setFont(BOLD_ARIAL_16);

    // Product Name
    tempLabel = (Label) infoPanel.add(new Label(aboutInfo.getProductName(), Label.LEFT));
    tempLabel.setFont(BOLD_ARIAL_16);

    // Product Version Number
    tempLabel = (Label) infoPanel.add(new Label("Version " + aboutInfo.getVersionNumber(), Label.LEFT));
    tempLabel.setFont(BOLD_ARIAL_12);

    // Add spacer...
    infoPanel.add(new Label(" <<<<<<<<<< - >>>>>>>>>> ", Label.CENTER));

    // Copyright
    tempLabel = (Label) infoPanel.add(new Label(aboutInfo.getCopyright(), Label.LEFT));
    tempLabel.setFont(BOLD_ARIAL_12);

    // Company URL
    tempLabel = (Label) infoPanel.add(new Label(aboutInfo.getCompanyURL().toExternalForm(), Label.LEFT));
    tempLabel.setFont(BOLD_ARIAL_12);
    tempLabel.setForeground(NAVY_BLUE);
    tempLabel.addMouseListener(new URLListener(this, browser, aboutInfo.getCompanyURL()));

    // Licensee
    tempLabel = (Label) infoPanel.add(new Label("Licensed to " + aboutInfo.getLicensee(), Label.LEFT));
    tempLabel.setFont(BOLD_ARIAL_12);

    return infoPanel;
  }

  /**
   * Returns constraints for the about dialog used create a border around the dialog container.
   * @return
   */
  private GridBagConstraints getConstraints() {
    return new GridBagConstraints(0, 0, 1, 1, 100, 100, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(10, 10, 10, 10), 10, 10);
  }

  private static final class CompanyLogoLabel extends ImageLabel {
    public CompanyLogoLabel(final Image img) {
      super("               ", img);
    }

    public int getImageX() {
      return ((getSize().width - getImageWidth()) / 2);
    }

    public int getImageY() {
      return (getSize().height - getImageHeight());
    }
  }

  private static final class ProductLogoLabel extends ImageLabel {
    public ProductLogoLabel(final Image img) {
      super("                              ", img);
    }

    public int getImageX() {
      return 0;
    }

    public int getImageY() {
      return ((getSize().height - getImageHeight()) / 2);
    }
  }

  private static abstract class ImageLabel extends Label {
    private int imageHeight;
    private int imageWidth;

    private final Image img;

    public ImageLabel(final String text, final Image img) {
      super(text);
      this.img = img;
    }

    private void init() {
      final double imgWidth = getImage().getWidth(this);
      final double imgHeight = getImage().getHeight(this);

      if ((imgWidth / getSize().width) > (imgHeight / getSize().height)) {
        setImageHeight(Math.min(getSize().height, (int) (imgHeight * (getSize().getWidth() / imgWidth))));
        setImageWidth(getSize().width);
      }
      else {
        setImageWidth(Math.min(getSize().width, (int) (imgWidth * (getSize().getHeight() / imgHeight))));
        setImageHeight(getSize().height);
      }
    }

    public Image getImage() {
      return img;
    }

    public int getImageHeight() {
      return imageHeight;
    }

    private void setImageHeight(final int imageHeight) {
      this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
      return imageWidth;
    }

    private void setImageWidth(final int imageWidth) {
      this.imageWidth = imageWidth;
    }

    public abstract int getImageX();

    public abstract int getImageY();

    public void paint(final Graphics g) {
      super.paint(g);
      // Note, init needs to be called in the paint method since the size property for label is not set until the
      // Label component is rendered to screen.
      init();
      g.drawImage(getImage(), getImageX(), getImageY(), getImageWidth(), getImageHeight(), this);
    }
  }

}
