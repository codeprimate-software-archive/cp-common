/*
 * VerticalFlowLayoutAcceptanceTest.java (c) 24 January 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.VerticalFlowLayout
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VerticalFlowLayoutAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(640, 480);
  private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
  private static final String FRAME_TITLE = "VerticalFlowLayout Test";

  private final VerticalFlowLayout verticalFlowLayout = new VerticalFlowLayout(VerticalFlowLayout.TOP, 10, 10);

  public VerticalFlowLayoutAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final DefaultComboBoxModel model = new DefaultComboBoxModel();
    model.addElement(new AlignmentChoice(VerticalFlowLayout.TOP,  "TOP"));
    model.addElement(new AlignmentChoice(VerticalFlowLayout.MIDDLE, "MIDDLE"));
    model.addElement(new AlignmentChoice(VerticalFlowLayout.BOTTOM, "BOTTOM"));

    final JComboBox comboBox = new JComboBox(model);
    comboBox.addItemListener(new ItemListener() {
      public void itemStateChanged(final ItemEvent e) {
        final AlignmentChoice choice = (AlignmentChoice) e.getItem();
        verticalFlowLayout.setAlignment(choice.getAlignment());
      }
    });

    getContentPane().add(comboBox, BorderLayout.NORTH);
    getContentPane().add(buildUI(verticalFlowLayout), BorderLayout.CENTER);

    setSize(FRAME_SIZE);
    //setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel buildField(final String labelText, final JComponent inputComponent) {
    final JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    fieldPanel.setBorder(BorderFactory.createEtchedBorder());

    final JLabel label = new JLabel(labelText, JLabel.RIGHT);
    label.setPreferredSize(new Dimension(120, 35));
    fieldPanel.add(label);

    inputComponent.setPreferredSize(new Dimension(120,35));
    fieldPanel.add(inputComponent);

    return fieldPanel;
  }

  private JLabel buildLabel(final String text, final Dimension preferredSize, final Color color) {
    final JLabel label = new JLabel(text, JLabel.CENTER);
    label.setBackground(color);
    label.setBorder(BorderFactory.createEtchedBorder());
    label.setFont(LABEL_FONT);
    label.setPreferredSize(preferredSize);
    return label;
  }

  private JPanel buildUI(final LayoutManager layout) {
    final JPanel ui = new JPanel(layout);
    ui.add(buildLabel("One", new Dimension(120, 340), Color.BLUE));
    ui.add(buildLabel("Two", new Dimension(90, 90), Color.RED));
    ui.add(buildLabel("Three", new Dimension(160, 120), Color.WHITE));
    ui.add(buildLabel("Four", new Dimension(120, 140), Color.YELLOW));
    ui.add(buildLabel("Five", new Dimension(140, 120), Color.GREEN));
    ui.add(buildLabel("Six", new Dimension(100, 120), Color.CYAN));
    ui.add(buildLabel("Seven", new Dimension(130, 100), Color.BLACK));
    ui.add(buildLabel("Eight", new Dimension(120, 200), Color.ORANGE));
    ui.add(buildLabel("Nine", new Dimension(200, 180), Color.MAGENTA));
    return ui;
  }

  public static void main(final String[] args) {
    new VerticalFlowLayoutAcceptanceTest(FRAME_TITLE);
  }

  private static final class AlignmentChoice {

    private final int alignment;
    private final String description;

    public AlignmentChoice(final int alignment, final String description) {
      this.alignment = alignment;
      this.description = description;
    }

    public int getAlignment() {
      return alignment;
    }

    public String getDescription() {
      return description;
    }

    public String toString() {
      return getDescription();
    }
  }

}
