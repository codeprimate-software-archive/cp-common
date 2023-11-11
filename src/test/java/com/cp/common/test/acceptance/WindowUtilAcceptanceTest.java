/*
 * WindowUtilAcceptanceTest.java (c) 11 January 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.awt.WindowUtil
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.WindowUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class WindowUtilAcceptanceTest extends JFrame {

  private static final Dimension DIALOG_SIZE = new Dimension(160, 120);
  private static final Dimension FRAME_SIZE = new Dimension(320, 240);

  private static final String BUTTON_TITLE = "Show Dialog";
  private static final String DIALOG_TITLE = "WindowUtil Test - Dialog";
  private static final String FRAME_TITLE = "WindowUtil Test";

  public WindowUtilAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final JButton showDialog = new JButton(getDialogAction());
    showDialog.setText(BUTTON_TITLE);
    getContentPane().add(showDialog, BorderLayout.SOUTH);
    setSize(FRAME_SIZE);;
    setLocation(WindowUtil.getDesktopLocation(getSize()));
    setVisible(true);
  }

  private Action getDialogAction() {
    return new AbstractAction() {
      public void actionPerformed(final ActionEvent e) {
        final JDialog dialog = new JDialog(WindowUtilAcceptanceTest.this, DIALOG_TITLE, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(DIALOG_SIZE);
        dialog.setLocation(WindowUtil.getDialogLocation(WindowUtilAcceptanceTest.this, dialog.getSize()));
        //dialog.setLocationRelativeTo(WindowUtilAcceptanceTest.this);
        dialog.setVisible(true);
      }
    };
  }

  public static void main(final String[] args) {
    new WindowUtilAcceptanceTest();
  }

}
