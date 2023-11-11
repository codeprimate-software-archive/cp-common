/*
 * JFormattedTextFieldAcceptanceTest (c) 12 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.13
 * @see com.cp.common.swing.JFormattedTextField
 * @see javax.swing.JFrame
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.JFormattedTextField;
import com.cp.common.swing.text.BoundedLengthTextFormat;
import com.cp.common.swing.text.ComposableTextFormat;
import com.cp.common.swing.text.CurrencyTextFormat;
import com.cp.common.swing.text.DateTextFormat;
import com.cp.common.swing.text.DigitsOnlyTextFormat;
import com.cp.common.swing.text.LettersOnlyTextFormat;
import com.cp.common.swing.text.PercentTextFormat;
import com.cp.common.swing.text.PhoneNumberTextFormat;
import com.cp.common.swing.text.SsnTextFormat;
import com.cp.common.swing.text.TextFormat;
import com.cp.common.swing.text.TimeTextFormat;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JFormattedTextFieldAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(640, 400);
  private static final String FRAME_TITLE = "JFormattedTextField Test";

  public JFormattedTextFieldAcceptanceTest(final String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().add(buildUI(), BorderLayout.CENTER);
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JPanel buildUI() {
    final JPanel form = new JPanel(new GridLayout(10, 2, 5, 5));

    form.add(new JLabel("Bounded Length(10) Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new BoundedLengthTextFormat(10)));
    form.add(new JLabel("Currency Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new CurrencyTextFormat()));
    form.add(new JLabel("Date Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new DateTextFormat()));
    form.add(new JLabel("Digits Only Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new DigitsOnlyTextFormat()));
    form.add(new JLabel("Letters Only Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new LettersOnlyTextFormat()));
    form.add(new JLabel("Percent Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new PercentTextFormat()));
    form.add(new JLabel("Phone Number Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new PhoneNumberTextFormat()));
    form.add(new JLabel("SSN Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new SsnTextFormat()));
    form.add(new JLabel("Time Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(new TimeTextFormat()));

    final TextFormat textFormat = ComposableTextFormat.compose(new BoundedLengthTextFormat(5), new DigitsOnlyTextFormat());
    form.add(new JLabel("Bounded Length(5) Digits Only Formatted Field: ", JLabel.RIGHT));
    form.add(new JFormattedTextField(textFormat));

    return form;
  }

  public static void main(final String[] args) {
    new JFormattedTextFieldAcceptanceTest(FRAME_TITLE);
  }

}
