/*
 * ViewCardListenerAcceptanceTest.java (c) 19 January 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.ViewCardListener
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.ViewCardListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ViewCardListenerAcceptanceTest extends JFrame {

  private static final Dimension SIZE = new Dimension (640, 480);

  private static final String BLUE_CARD = "blue";
  private static final String FRAME_TITLE = "ViewCardListener Test";
  private static final String GREEN_CARD = "green";
  private static final String RED_CARD = "red";

  public ViewCardListenerAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(SIZE);
    final Container contentPane = getContentPane();
    final JPanel cardPanel = buildCardPanel();
    contentPane.add(cardPanel, BorderLayout.CENTER);
    contentPane.add(buildToolBar(cardPanel), BorderLayout.WEST);
    //setOrigin(WindowUtil.getDesktopLocation(SIZE));
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel buildCardPanel() {
    final JPanel cardPanel = new JPanel(new CardLayout());

    final JPanel redPanel = new JPanel(true);
    redPanel.setBackground(Color.red);
    cardPanel.add(RED_CARD, redPanel);

    final JPanel greenPanel = new JPanel(true);
    greenPanel.setBackground(Color.green);
    cardPanel.add(GREEN_CARD, greenPanel);

    final JPanel bluePanel = new JPanel(true);
    bluePanel.setBackground(Color.blue);
    cardPanel.add(BLUE_CARD, bluePanel);

    return cardPanel;
  }

  private JToolBar buildToolBar(final Container cardPanel) {
    final JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
    toolBar.setFloatable(false);

    final ButtonGroup group = new ButtonGroup();

    final JToggleButton redButton = new JToggleButton("Red", true);
    redButton.setForeground(Color.red);
    redButton.addActionListener(new ViewCardListener(cardPanel, RED_CARD));
    group.add(redButton);
    toolBar.add(redButton);
    toolBar.addSeparator();

    final JToggleButton greenButton = new JToggleButton("Green", false);
    greenButton.setForeground(Color.green);
    greenButton.addActionListener(new ViewCardListener(cardPanel, GREEN_CARD));
    group.add(greenButton);
    toolBar.add(greenButton);
    toolBar.addSeparator();

    final JToggleButton blueButton = new JToggleButton("Blue", false);
    blueButton.setForeground(Color.blue);
    blueButton.addActionListener(new ViewCardListener(cardPanel, BLUE_CARD));
    group.add(blueButton);
    toolBar.add(blueButton);

    return toolBar;
  }

  public static void main(final String[] args) {
    new ViewCardListenerAcceptanceTest();
  }

}
