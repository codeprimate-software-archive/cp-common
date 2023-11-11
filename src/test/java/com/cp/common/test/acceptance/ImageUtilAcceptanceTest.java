/*
 * ImageUtilAcceptanceTest.java (c) 5 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.ImageUtil
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.ImageUtil;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageUtilAcceptanceTest extends JFrame {

  private static final String FRAME_TITLE = "ImageUtil Test";
  private static final Dimension IMAGE_SIZE = new Dimension(50, 50);
  private static final Dimension SIZE = new Dimension(600, 100);

  public ImageUtilAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(1, 6));
    contentPane.add(getLabel(ImageUtil.getDoubleLeftArrowImage(IMAGE_SIZE)));
    contentPane.add(getLabel(ImageUtil.getLeftArrowImage(IMAGE_SIZE)));
    contentPane.add(getLabel(ImageUtil.getDownArrowImage(IMAGE_SIZE)));
    contentPane.add(getLabel(ImageUtil.getUpArrowImage(IMAGE_SIZE)));
    contentPane.add(getLabel(ImageUtil.getRightArrowImage(IMAGE_SIZE)));
    contentPane.add(getLabel(ImageUtil.getDoubleRightArrowImage(IMAGE_SIZE)));

    setSize(SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JLabel getLabel(final Image img) {
    final JLabel label = new JLabel(new ImageIcon(img));
    label.setBorder(BorderFactory.createEtchedBorder());
    return label;
  }

  public static void main(final String[] args) {
    new ImageUtilAcceptanceTest();
  }

}
