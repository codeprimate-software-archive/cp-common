/*
 * BoundedAreaGrayFilterAcceptanceTest.java (c) 7 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.BoundedAreaGrayFilter
 */

package com.cp.common.test.acceptance;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.image.FilteredImageSource;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cp.common.awt.BoundedAreaGrayFilter;
import com.cp.common.awt.ImageUtil;

public class BoundedAreaGrayFilterAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(640, 480);
  private static final String FRAME_TITLE = "BoundedAreaGrayFilter Test";

  // NOTE: the use of a relative pathname.  Relative pathnames are resolved
  // relative to where the Java Virtual Machine was invoked, defined by the
  // user.dir System property.  See the FileContentFinderTest class for an
  // example of an absolute pathname.
  private static final String IMAGE_FILE = "etc/content/images/map.jpg";

  public BoundedAreaGrayFilterAcceptanceTest(final String frameTitle) throws Exception {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final Image mapImage = filterImage(ImageUtil.getImage(new File(IMAGE_FILE), this));

    getContentPane().add(new JScrollPane(getCanvas(mapImage), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private Image filterImage(Image image) {
    // NOTE: you can toggle the boolean parameter to affect fill on the filter.
    final BoundedAreaGrayFilter grayFilter = new BoundedAreaGrayFilter(getShape(), true);

    // Create a filterd image from the existing image.
    image = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), grayFilter));

    final MediaTracker tracker = new MediaTracker(this);
    tracker.addImage(image, 0);

    try {
      tracker.waitForID(0);
    }
    catch (InterruptedException e) {
      System.err.println("Failed to wait for filtered image to be generated!");
    }

    return image;
  }

  private JPanel getCanvas(final Image img) {
    final JPanel canvas = new JPanel(true) {
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
      }
    };

    final Dimension panelSize = new Dimension(img.getWidth(canvas), img.getHeight(canvas));
    canvas.setMaximumSize(panelSize);
    canvas.setPreferredSize(panelSize);
    canvas.setMinimumSize(panelSize);

    return canvas;
  }

  /**
   * Outline of the Code Primate mascot (ape).
   * @return a Shape object outline of the Code Primate mascot (ape).
   */
  public Shape getShape() {
    final GeneralPath path = new GeneralPath();
    path.moveTo(497.0f, 113.0f);
    path.lineTo(525.0f, 141.0f);
    path.lineTo(527.0f, 147.0f);
    path.lineTo(526.0f, 152.0f);
    path.lineTo(519.0f, 162.0f);
    path.lineTo(522.0f, 179.0f);
    path.lineTo(517.0f, 203.0f);
    path.lineTo(511.0f, 211.0f);
    path.lineTo(498.0f, 215.0f);
    path.lineTo(476.0f, 212.0f);
    path.lineTo(467.0f, 207.0f);
    path.lineTo(462.0f, 197.0f);
    path.lineTo(460.0f, 196.0f);
    path.lineTo(454.0f, 201.0f);
    path.lineTo(452.0f, 213.0f);
    path.lineTo(460.0f, 228.0f);
    path.lineTo(491.0f, 257.0f);
    path.lineTo(520.0f, 288.0f);
    path.lineTo(537.0f, 312.0f);
    path.lineTo(550.0f, 343.0f);
    path.lineTo(546.0f, 358.0f);
    path.lineTo(543.0f, 364.0f);
    path.lineTo(534.0f, 371.0f);
    path.lineTo(527.0f, 373.0f);
    path.lineTo(525.0f, 370.0f);
    path.lineTo(531.0f, 357.0f);
    path.lineTo(531.0f, 351.0f);
    path.lineTo(530.0f, 345.0f);
    path.lineTo(523.0f, 345.0f);
    path.lineTo(512.0f, 314.0f);
    path.lineTo(508.0f, 309.0f);
    path.lineTo(471.0f, 297.0f);
    path.lineTo(458.0f, 289.0f);
    path.lineTo(448.0f, 283.0f);
    path.lineTo(436.0f, 274.0f);
    path.lineTo(423.0f, 260.0f);
    path.lineTo(419.0f, 268.0f);
    path.lineTo(418.0f, 284.0f);
    path.lineTo(419.0f, 315.0f);
    path.lineTo(420.0f, 329.0f);
    path.lineTo(423.0f, 341.0f);
    path.lineTo(431.0f, 360.0f);
    path.lineTo(434.0f, 371.0f);
    path.lineTo(434.0f, 380.0f);
    path.lineTo(433.0f, 386.0f);
    path.lineTo(429.0f, 388.0f);
    path.lineTo(396.0f, 387.0f);
    path.lineTo(394.0f, 384.0f);
    path.lineTo(398.0f, 368.0f);
    path.lineTo(398.0f, 354.0f);
    path.lineTo(373.0f, 287.0f);
    path.lineTo(372.0f, 270.0f);
    path.lineTo(369.0f, 262.0f);
    path.lineTo(355.0f, 255.0f);
    path.lineTo(344.0f, 254.0f);
    path.lineTo(327.0f, 257.0f);
    path.lineTo(325.0f, 263.0f);
    path.lineTo(336.0f, 289.0f);
    path.lineTo(336.0f, 303.0f);
    path.lineTo(332.0f, 316.0f);
    path.lineTo(314.0f, 350.0f);
    path.lineTo(315.0f, 361.0f);
    path.lineTo(319.0f, 368.0f);
    path.lineTo(332.0f, 379.0f);
    path.lineTo(336.0f, 385.0f);
    path.lineTo(321.0f, 390.0f);
    path.lineTo(298.0f, 389.0f);
    path.lineTo(284.0f, 384.0f);
    path.lineTo(277.0f, 378.0f);
    path.lineTo(276.0f, 366.0f);
    path.lineTo(293.0f, 316.0f);
    path.lineTo(293.0f, 306.0f);
    path.lineTo(289.0f, 296.0f);
    path.lineTo(247.0f, 280.0f);
    path.lineTo(236.0f, 301.0f);
    path.lineTo(230.0f, 309.0f);
    path.lineTo(223.0f, 317.0f);
    path.lineTo(214.0f, 324.0f);
    path.lineTo(189.0f, 332.0f);
    path.lineTo(173.0f, 336.0f);
    path.lineTo(162.0f, 340.0f);
    path.lineTo(154.0f, 350.0f);
    path.lineTo(159.0f, 371.0f);
    path.lineTo(159.0f, 377.0f);
    path.lineTo(157.0f, 380.0f);
    path.lineTo(152.0f, 379.0f);
    path.lineTo(139.0f, 364.0f);
    path.lineTo(115.0f, 346.0f);
    path.lineTo(119.0f, 333.0f);
    path.lineTo(129.0f, 325.0f);
    path.lineTo(173.0f, 303.0f);
    path.lineTo(184.0f, 290.0f);
    path.lineTo(193.0f, 275.0f);
    path.lineTo(199.0f, 252.0f);
    path.lineTo(211.0f, 209.0f);
    path.lineTo(220.0f, 193.0f);
    path.lineTo(223.0f, 178.0f);
    path.lineTo(259.0f, 165.0f);
    path.lineTo(298.0f, 152.0f);
    path.lineTo(341.0f, 132.0f);
    path.lineTo(368.0f, 124.0f);
    path.lineTo(380.0f, 123.0f);
    path.lineTo(421.0f, 127.0f);
    path.lineTo(441.0f, 122.0f);
    path.lineTo(461.0f, 107.0f);
    path.lineTo(483.0f, 105.0f);
    path.lineTo(496.0f, 111.0f);
    path.closePath();
    return path;
  }

  public static void main(final String[] args) throws Exception {
    new BoundedAreaGrayFilterAcceptanceTest(FRAME_TITLE);
  }
}
