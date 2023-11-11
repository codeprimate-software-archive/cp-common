/*
 * GridCanvas.java (c) 12 December 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.8.12
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

public class GridCanvas extends JComponent {

  private static final Logger logger = Logger.getLogger(GridCanvas.class);

  // default grid cell size (size assumes that the grid cell is a square area
  // and the value constitutes the lenght of one side)
  private static final int DEFAULT_GRID_CELL_SIZE = 10;
  // default line thickness within the grid
  private static final int DEFAULT_GRID_LINE_WIDTH = 1;

  private int gridCellSize = DEFAULT_GRID_CELL_SIZE;
  private int gridLineWidth = DEFAULT_GRID_LINE_WIDTH;

  private Color gridLineColor = Color.lightGray;

  /**
   * Constructs an instance of the GridCanvas class, which is a UI component representing graph paper to
   * plot coordinate data.  The grid canvas background color is defaulted to white.
   */
  public GridCanvas() {
    setBackground(Color.white);
  }

  /**
   * Returns the value of the grid cell size, which refers to the length of one of the grid cell's sides.
   * Note, the grid cell is assumed to be a square area.
   * @return an integer value representing the length of one side of a cell in the grid.  A grid cell
   * is a defined as a square unit area in the grid.
   */
  public int getGridCellSize() {
    return gridCellSize;
  }

  /**
   * Sets the length of one side of a cell in the grid, which is defined as a square unit area in the
   * grid.
   * @param size an integer value specifying the side length of a cell in the grid.
   */
  public void setGridCellSize(final int size) {
    logger.debug("size = " + size);
    if (size < gridLineWidth) {
      logger.warn("The grid cell size must be greater than or equal to grid line width: "
        + gridLineWidth);
      throw new IllegalArgumentException("The grid cell size must be greater than or equal to grid line width: "
        + gridLineWidth);
    }
    gridCellSize = size;
  }

  /**
   * Returns a grid point refering to a grid cell for the given (x, y) pixel coordinates in the grid.
   * @param pt is a java.awt.Point object refering to the pixel coordinates in the grid.
   * @return a GridPoint refering to the grid cell in the grid for the given pixel coordinates.
   */
  public GridPoint getGridCoordinates(final Point pt) {
    return new GridPoint((int) (pt.getX() / getGridCellSize()), (int) (pt.getY() / getGridCellSize()));
  }

  /**
   * Returns the color used to paint the lines in the grid.
   * @return a Color object refering to the color used to paint the grid lines.
   */
  public Color getGridLineColor() {
    return gridLineColor;
  }

  /**
   * Sets the color used to paint the lines in the grid.
   * @param gridLineColor is a Color object refering to the color used to paint the lines in the grid.
   */
  public void setGridLineColor(final Color gridLineColor) {
    if (ObjectUtil.isNull(gridLineColor)) {
      logger.warn("The color used to paint the grid line cannot be null!");
      throw new NullPointerException("The color used to paint the grid line cannot be null!");
    }
    this.gridLineColor = gridLineColor;
  }

  /**
   * Returns the width of the grid lines in the grid.
   * @return an integer value specifying the width in pixels of the grid lines in the grid.
   */
  public int getGridLineWidth() {
    return gridLineWidth;
  }

  /**
   * Sets the width of the grid lines in pixels.
   * @param lineWidth is an integer value specifying the width of the grid line in pixels.
   */
  public void setGridLineWidth(final int lineWidth) {
    logger.debug("lineWidth = " + lineWidth);
    if (lineWidth < 1) {
      logger.warn("The grid line width must be greater than " + DEFAULT_GRID_LINE_WIDTH);
      throw new IllegalArgumentException("The grid line width must be greater than " + DEFAULT_GRID_LINE_WIDTH);
    }
    gridLineWidth = lineWidth;
  }

  /**
   * Paints the UI of the grid canvas.
   * @param g is the Graphics object used to paint the UI of the grid canvas.
   */
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);

    final Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(new BasicStroke(getGridLineWidth()));
    g2.setColor(getGridLineColor());

    paintBackground(g2);

    final int height = getHeight();
    final int width = getWidth();

    for (int index = getGridCellSize(), dim = Math.max(width, height); index < dim; index += getGridCellSize()) {
      // increase x, draw verticle line
      g2.draw(new Line2D.Double(index, 0, index, height));
      // increase y, draw horizontal line
      g2.draw(new Line2D.Double(0, index, width, index));
    }
  }

  /**
   * Paints, or fills in, the background of the GridCanvas component.
   * @param g2 the Graphics object used to paint the background of the GridCanvas.
   */
  private void paintBackground(final Graphics2D g2) {
    final Color theColor = g2.getColor();
    g2.setColor(Color.white);
    g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    g2.setColor(theColor);
  }

  /**
   * The GridPoint class used to represent the cells as (x, y) coordinates in the grid canvas.
   */
  public static final class GridPoint {
    private final int x;
    private final int y;

    private GridPoint(final int x, final int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof GridPoint)) {
        return false;
      }

      final GridPoint gp = (GridPoint) obj;

      return (gp.getX() == getX() && gp.getY() == getY());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + getX();
      hashValue = 37 * hashValue + getY();
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("(");
      buffer.append(getX()).append(", ").append(getY());
      buffer.append(")");
      return buffer.toString();
    }
  }

}
