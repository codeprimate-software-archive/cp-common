/*
 * Matte.java (c) 12 May 2002
 *
 * Matte paints a canvas similar to Adobe Photoshop for
 * displaying graphics and images.  The class uses double
 * bufferring for quick screen refreshes.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.8.12
 */

package com.cp.common.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

public class Matte extends JComponent {

  private static final Logger logger = Logger.getLogger(Matte.class);

  private static final Color TILE_COLOR = new Color(204, 204, 204);
  private static final Dimension TILE_SIZE = new Dimension(5, 5);

  // offscreen image buffer used for double buffering
  private BufferedImage imageBuffer;

  private Dimension previousMatteSize;

  /**
   * Creates an instance of the Matte class which represents a matte, or canvas, on which to paint
   * or draw images or other graphics primitives.
   */
  public Matte() {
    previousMatteSize = new Dimension(0, 0);
  }

  /**
   * Paints the UI of the matte, or canvas.
   * @param g is a Graphics object used to paint the UI of the matte.
   */
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);

    final Dimension currentMatteSize = getSize();

    if (!currentMatteSize.equals(previousMatteSize)) {
      previousMatteSize = currentMatteSize;
      imageBuffer = new BufferedImage(currentMatteSize.width, currentMatteSize.height, BufferedImage.TYPE_INT_ARGB);

      final Graphics2D g2 = imageBuffer.createGraphics();

      for (int y = 0; y < currentMatteSize.getHeight(); y += TILE_SIZE.height) {
        for (int x = 0; x < currentMatteSize.getWidth(); x += TILE_SIZE.width) {
          g2.setPaint((x % 2) == (y % 2) ? Color.white : TILE_COLOR);
          g2.fill(new Rectangle2D.Double(x, y, TILE_SIZE.getWidth(), TILE_SIZE.getHeight()));
        }
      }
    }

    g.drawImage(imageBuffer, 0, 0, this);
  }

}
