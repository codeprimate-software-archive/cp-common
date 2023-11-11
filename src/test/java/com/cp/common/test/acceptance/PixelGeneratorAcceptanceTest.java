/*
 * PixelGeneratorAcceptanceTest.java (c) 5 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.PixelGenerator
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.PixelGenerator;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.MemoryImageSource;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PixelGeneratorAcceptanceTest extends JFrame {

  private static final Dimension SIZE = new Dimension(480, 240);
  private static final String FRAME_TITLE = "PixelGenerator Test";

  public PixelGeneratorAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(SIZE);

    final Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(1, 3));
    contentPane.add(getLabel(getCircle()));
    contentPane.add(getLabel(getSquare()));
    contentPane.add(getLabel(getTriangle()));

    setLocationRelativeTo(null);
    setVisible(true);
  }

  private Shape getCircle() {
    return new Ellipse2D.Double(0.0, 0.0, 100.0, 100.0);
  }

  private Image getImage(final Shape shape) {
    final Rectangle bounds = shape.getBounds();
    final int[] pixels = PixelGenerator.getPixels(bounds, shape);
    return Toolkit.getDefaultToolkit().createImage(
        new MemoryImageSource(bounds.width, bounds.height, pixels, 0, bounds.width));
  }

  private JLabel getLabel(final Shape shape) {
    final JLabel label = new JLabel(new ImageIcon(getImage(shape)));
    label.setBorder(BorderFactory.createEtchedBorder());
    return label;
  }

  private Shape getSquare() {
    return new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);
  }

  private Shape getTriangle() {
    final GeneralPath path = new GeneralPath();
    path.moveTo(0, 100);
    path.lineTo(50, 0);
    path.lineTo(100, 100);
    path.closePath();
    return path;
  }

  public static void main(final String[] args) {
    new PixelGeneratorAcceptanceTest();
  }

}
