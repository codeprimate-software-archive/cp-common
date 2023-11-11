/*
 * GraphicsUtilAcceptanceTest.java (c) 8 February 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.GraphicsUtil
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.GraphicsUtil;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GraphicsUtilAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(900, 100);
  private static final Dimension ICON_SIZE = new Dimension(40, 30);
  private static final Dimension NEW_DOCUMENT_ICON_SIZE = new Dimension(35, 40);

  private static final String FRAME_TITLE = "GraphicsUtil Test";

  public GraphicsUtilAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    final JLabel label0 = getLabel();
    final JLabel label1 = getLabel();
    final JLabel label2 = getLabel();
    final JLabel label3 = getLabel();
    final JLabel label4 = getLabel();
    final JLabel label5 = getLabel();
    final JLabel label6 = getLabel();
    final JLabel label7 = getLabel();
    final JLabel label8 = getLabel();
    final JLabel label9 = getLabel();
    final JLabel label10 = getLabel();
    final JLabel label11 = getLabel();
    final JLabel label12 = getLabel();
    final JLabel label13 = getLabel();
    final JLabel label14 = getLabel();
    final JLabel label15 = getLabel();
    final JLabel label16 = getLabel();
    final JLabel label17 = getLabel();

    final Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(2, 9));
    contentPane.add(label0);
    contentPane.add(label1);
    contentPane.add(label2);
    contentPane.add(label3);
    contentPane.add(label4);
    contentPane.add(label5);
    contentPane.add(label6);
    contentPane.add(label7);
    contentPane.add(label8);
    contentPane.add(label9);
    contentPane.add(label10);
    contentPane.add(label11);
    contentPane.add(label12);
    contentPane.add(label13);
    contentPane.add(label14);
    contentPane.add(label15);
    contentPane.add(label15);
    contentPane.add(label16);
    contentPane.add(label17);

    setSize(FRAME_SIZE);
    pack();

    label0.setIcon(new ImageIcon(GraphicsUtil.fillDoubleLeftArrow(GraphicsUtil.getViewableArea(label0))));
    label1.setIcon(new ImageIcon(GraphicsUtil.fillLeftArrow((GraphicsUtil.getViewableArea(label1)))));
    label2.setIcon(new ImageIcon(GraphicsUtil.fillUpArrow((GraphicsUtil.getViewableArea(label2)))));
    label3.setIcon(new ImageIcon(GraphicsUtil.fillDownArrow((GraphicsUtil.getViewableArea(label3)))));
    label4.setIcon(new ImageIcon(GraphicsUtil.fillRightArrow((GraphicsUtil.getViewableArea(label4)))));
    label5.setIcon(new ImageIcon(GraphicsUtil.fillDoubleRightArrow((GraphicsUtil.getViewableArea(label5)))));
    //label6.setIcon(GraphicsUtil.getNewDocumentIcon(GraphicsUtil.getViewableArea(label6)));
    label6.setIcon(GraphicsUtil.getNewDocumentIcon(NEW_DOCUMENT_ICON_SIZE));
    //label7.setIcon(GraphicsUtil.getOpenFolderIcon(GraphicsUtil.getViewableArea(label7)));
    label7.setIcon(GraphicsUtil.getOpenFolderIcon(ICON_SIZE));
    //label8.setIcon(GraphicsUtil.getDisketteIcon(GraphicsUtil.getViewableArea(label8)));
    label8.setIcon(GraphicsUtil.getDisketteIcon(ICON_SIZE));
    label9.setIcon(new ImageIcon(GraphicsUtil.drawDoubleLeftArrow(GraphicsUtil.getViewableArea(label9))));
    label10.setIcon(new ImageIcon(GraphicsUtil.drawLeftArrow((GraphicsUtil.getViewableArea(label10)))));
    label11.setIcon(new ImageIcon(GraphicsUtil.drawUpArrow((GraphicsUtil.getViewableArea(label11)))));
    label12.setIcon(new ImageIcon(GraphicsUtil.drawDownArrow((GraphicsUtil.getViewableArea(label12)))));
    label13.setIcon(new ImageIcon(GraphicsUtil.drawRightArrow((GraphicsUtil.getViewableArea(label13)))));
    label14.setIcon(new ImageIcon(GraphicsUtil.drawDoubleRightArrow((GraphicsUtil.getViewableArea(label14)))));

    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JLabel getLabel() {
    final JLabel label = new JLabel();
    label.setBorder(BorderFactory.createEtchedBorder());
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setPreferredSize(new Dimension(80, FRAME_SIZE.height));
    return label;
  }

  public static void main(final String[] args) {
    new GraphicsUtilAcceptanceTest(FRAME_TITLE);
  }

}
