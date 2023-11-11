/*
 * CButton.java (c) 14 March 2001
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.27
 * @see com.cp.common.swing.XButton
 * @see java.awt.Button
 */

package com.cp.common.awt;

import com.cp.common.lang.ObjectUtil;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CButton extends Button {

  private static final int PAD_X = 4;
  private static final int PAD_Y = 4;

  private Dimension previousButtonSize = new Dimension(0, 0);

  private Image icon;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the CButton UI component.
   */
  public CButton() {
  }

  /**
   * Creates an instance of the CButton UI component initialized with the given label.
   * @param label a String value specifying the text used for the label of this button
   * component.
   */
  public CButton(final String label) {
    super(label);
  }

  /**
   * Creates an instance of the CButton UI component initialized with the given icon.
   * @param icon an Image object representing the icon to draw on the face of this button
   * component.
   */
  public CButton(final Image icon) {
    this.icon = icon;
  }

  /**
   * Returns the image painted on the face of this button component.
   * @return an Image object representing the content of the button icon.
   */
  public Image getIcon() {
    return icon;
  }

  /**
   * Sets the image of the icon painted on the button component face.
   * @param icon an Image object representing the content of the icon
   * being displayed on this button component.
   */
  public void setIcon(final Image icon) {
    this.icon = icon;
  }

  /**
   * Method called by the rendering context to display the button on screen.
   * @param g the Graphics object used to paint the UI of the button component.
   */
  public void paint(final Graphics g) {
    super.paint(g);
    paintIcon(g);
  }

  /**
   * Method called to paint the icon on the face of the button component.
   * @param g the Graphics object used to paint the icon.
   */
  private void paintIcon(final Graphics g) {
    if (ObjectUtil.isNotNull(getIcon()) && !getSize().equals(previousButtonSize)) {
      new IconRenderer(getIcon()).paint(g);
    }
  }

  /**
   * The IconRenderer handles the details of rendering, or painting, the icon image on the UI component.
   */
  private final class IconRenderer {

    private Dimension iconSize;
    private final Image icon;
    private Point iconLocation;

    public IconRenderer(final Image icon) {
      this.icon = icon;
      init();
    }

    private void init() {
      final double buttonWidth = (getSize().getWidth() - PAD_X);
      final double buttonHeight = (getSize().getHeight() - PAD_Y);
      if (logger.isDebugEnabled()) {
        logger.debug("buttonWidth = " + buttonWidth);
        logger.debug("buttonHeight = " + buttonHeight);
      }

      double iconWidth = getIcon().getWidth(CButton.this);
      double iconHeight = getIcon().getHeight(CButton.this);
      if (logger.isDebugEnabled()) {
        logger.debug("iconWidth = " + iconWidth);
        logger.debug("iconHeight = " + iconHeight);
      }

//      if (iconWidth > buttonWidth || iconHeight > buttonHeight) {
        if ((iconWidth / buttonWidth) > (iconHeight / buttonHeight)) {
          logger.debug("width");
          iconHeight = Math.min(buttonHeight, (iconHeight * (buttonWidth / iconWidth)));
          iconWidth = buttonWidth;
        }
        else {
          logger.debug("height");
          iconWidth = Math.min(buttonWidth, (iconWidth * (buttonHeight / iconHeight)));
          iconHeight = buttonHeight;
        }
//      }

      setIconLocation(new Point((int) ((buttonWidth - iconWidth) / 2), (int) ((buttonHeight - iconHeight) / 2)));
      setIconSize(new Dimension((int) iconWidth, (int) iconHeight));
    }

    private Image getIcon() {
      return icon;
    }

    private Point getIconLocation() {
      return iconLocation;
    }

    private void setIconLocation(final Point iconLocation) {
      this.iconLocation = iconLocation;
    }

    private Dimension getIconSize() {
      return iconSize;
    }

    private void setIconSize(final Dimension iconSize) {
      this.iconSize = iconSize;
    }

    public void paint(final Graphics g) {
      g.drawImage(getIcon(), (int) getIconLocation().getX(), (int) getIconLocation().getY(),
        (int) getIconSize().getWidth(), (int) getIconSize().getHeight(), CButton.this);
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{iconLocation = ");
      buffer.append(getIconLocation());
      buffer.append(", iconSize = ").append(getIconSize());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
