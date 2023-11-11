/*
 * GridCanvasAcceptanceTest.java (c) 12 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.GridCanvas
 */

package com.cp.common.test.acceptance;

import com.cp.common.swing.GridCanvas;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

public class GridCanvasAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(640, 480);
  private static final String FRAME_TITLE = "GridCanvas Test";

  public GridCanvasAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final GridCanvas grid = new GridCanvas();
    grid.addMouseListener(new MouseAdapter() {
      public void mousePressed(final MouseEvent e) {
        System.out.println("Pixel (" + e.getPoint() + ") = GridPoint (" + grid.getGridCoordinates(e.getPoint()) + ")");
      }
    });

    getContentPane().add(grid, BorderLayout.CENTER);
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(final String[] args) {
    new GridCanvasAcceptanceTest(FRAME_TITLE);
  }

}
