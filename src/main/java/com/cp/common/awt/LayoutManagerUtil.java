/*
 * LayoutManagerUtil.java (c) 23 February 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see java.awt.LayoutManager
 */

package com.cp.common.awt;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class LayoutManagerUtil {

  private static final Log logger = LogFactory.getLog(LayoutManagerUtil.class);

  /**
   * Default private constructor enforcing the non-instantiability property
   * on the LayoutManagerUtil class.  This class is a convenient utility class
   * meant for use during component layout management.
   */
  private LayoutManagerUtil() {
  }

  /**
   * Returns a GridBagConstraints object populated with the specified values.
   * @param gridx is an integer value indicating the cell at the left of the component's display area,
   * where the leftmost cell has gridx=0.
   * @param gridy is an integer value indicating the cell at the top of the component's display area,
   * where the topmost cell has gridy=0.
   * @param gridWidth is an integer value specifying the number of cells in a row for the component's display area.
   * @param gridHeight is an integer value specifying the number of cells in a column for the component's display area.
   * @param weightx is an integer value specifying how to distribute extra horizontal space.
   * @param weighty is an integer value specifying how to distribute extra vertical space.
   * @param anchor is used to determine which part of the cell the component should be placed.  The field is used
   * when the component is smaller than its display area. Possible values include: CENTER, NORTH, NORTHEAST, EAST,
   * SOUTHEAST, SOUTH, SOUTHWEST, WEST and NORTHWEST.
   * @param fill is an integer field is used when the component's display area is larger than the component's
   * requested size.
   * @param insets is a java.awt.Insets object field specifiing the external padding of the component, the minimum
   * amount of space between the component and the edges of its display area.
   * @param ipadx is an integer value field specifying the internal padding of the component, how much space to add
   * to the minimum width of the component.
   * @param ipady is an integer value This field specifying the internal padding, that is, how much space to add
   * to the minimum.
   * @return an instance of the GridBagConstraints object fully populated.
   * @see LayoutManagerUtil#setConstraints(java.awt.GridBagConstraints, int, int, int, int, int, int, int, int,
   * java.awt.Insets, int, int)
   */
  public static GridBagConstraints getConstraints(final int gridx,
                                                  final int gridy,
                                                  final int gridWidth,
                                                  final int gridHeight,
                                                  final int weightx,
                                                  final int weighty,
                                                  final int anchor,
                                                  final int fill,
                                                  final Insets insets,
                                                  final int ipadx,
                                                  final int ipady) {
    if (logger.isDebugEnabled()) {
      logger.debug("gridx = " + gridx);
      logger.debug("gridy = " + gridy);
      logger.debug("gridWidth = " + gridWidth);
      logger.debug("gridHeight = " + gridHeight);
      logger.debug("weightx = " + weightx);
      logger.debug("weighty = " + weighty);
      logger.debug("anchor = " + anchor);
      logger.debug("fill = " + fill);
      logger.debug("insets (" + insets + ")");
      logger.debug("ipadx = " + ipadx);
      logger.debug("ipady = " + ipady);
    }
    return new GridBagConstraints(gridx, gridy, gridWidth, gridHeight, weightx, weighty,
        anchor, fill, insets, ipadx, ipady);
  }

  /**
   * Sets the properties of the GridBagConstraints object used in determining the layout of components
   * in a container object using the GridBagLayout manager.
   * @param constraints is a java.awt.GridBagConstraints variable containing layout constraints.
   * @param gridx is an integer value indicating the cell at the left of the component's display area,
   * where the leftmost cell has gridx=0.
   * @param gridy is an integer value indicating the cell at the top of the component's display area,
   * where the topmost cell has gridy=0.
   * @param gridWidth is an integer value specifying the number of cells in a row for the component's display area.
   * @param gridHeight is an integer value specifying the number of cells in a column for the component's display area.
   * @param weightx is an integer value specifying how to distribute extra horizontal space.
   * @param weighty is an integer value specifying how to distribute extra vertical space.
   * @param anchor is used to determine which part of the cell the component should be placed.  The field is used
   * when the component is smaller than its display area. Possible values include: CENTER, NORTH, NORTHEAST, EAST,
   * SOUTHEAST, SOUTH, SOUTHWEST, WEST and NORTHWEST.
   * @param fill is an integer field is used when the component's display area is larger than the component's
   * requested size.
   * @param insets is a java.awt.Insets object field specifiing the external padding of the component, the minimum
   * amount of space between the component and the edges of its display area.
   * @param ipadx is an integer value field specifying the internal padding of the component, how much space to add
   * to the minimum width of the component.
   * @param ipady is an integer value This field specifying the internal padding, that is, how much space to add
   * to the minimum.
   * @see LayoutManagerUtil@getConstraints
   */
  public static void setConstraints(final GridBagConstraints constraints,
                                    final int gridx,
                                    final int gridy,
                                    final int gridWidth,
                                    final int gridHeight,
                                    final int weightx,
                                    final int weighty,
                                    final int anchor,
                                    final int fill,
                                    final Insets insets,
                                    final int ipadx,
                                    final int ipady) {
    if (logger.isDebugEnabled()) {
      logger.debug("constraints before (" + constraints + ")");
      logger.debug("gridx = " + gridx);
      logger.debug("gridy = " + gridy);
      logger.debug("gridWidth = " + gridWidth);
      logger.debug("gridHeight = " + gridHeight);
      logger.debug("weightx = " + weightx);
      logger.debug("weighty = " + weighty);
      logger.debug("anchor = " + anchor);
      logger.debug("fill = " + fill);
      logger.debug("insets (" + insets + ")");
      logger.debug("ipadx = " + ipadx);
      logger.debug("ipady = " + ipady);
    }

    constraints.gridx = gridx;
    constraints.gridy = gridy;
    constraints.gridwidth = gridWidth;
    constraints.gridheight = gridHeight;
    constraints.weightx = weightx;
    constraints.weighty = weighty;
    constraints.fill = fill;
    constraints.anchor = anchor;
    constraints.insets = insets;
    constraints.ipadx = ipadx;
    constraints.ipady = ipady;

    if (logger.isDebugEnabled()) {
      logger.debug("constraints after (" + constraints + ")");
    }
  }

  /**
   * Returns an instance of the VerticalFlowLayout manager with center alignment
   * and 5 pixel vertical and horizontal spacing between components.
   *
   * @return an instance of the VerticalFlowLayout manager with center alignment
   * and 5 pixels of padding between components in the UI.
   */
  public static VerticalFlowLayout getVerticalFlowLayout() {
    return new VerticalFlowLayout();
  }

  /**
   * Returns an instance of the VerticalFlowLayout manager with the specified
   * alignment and and 5 pixel vertical and horizontal spacing between
   * components.
   *
   * @param align is an integer value specifying the alignment of components
   * within their given space (CENTER, LEFT, RIGHT).
   * @return an instance of the VerticalFlowLayout manager with the specified
   * alignment and 5 pixels of padding between components in the UI.
   */
  public static VerticalFlowLayout getVerticalFlowLayout(final int align) {
    logger.debug("align = " + align);
    return new VerticalFlowLayout(align);
  }

  /**
   * Returns an instance of the VerticalFlowLayout manager with the specified
   * alignment, horizontal and vertical spacing betweeen components in the
   * UI.
   *
   * @param align is an integer value specifying the alignment of components
   * within their given space (CENTER, LEFT, RIGHT).
   * @param horizontalGap is an integer value specifying the number of pixels
   * between components horizontally.
   * @param verticalGap is an integer value specifying the number of pixels
   * between components vertically.
   * @return an instance of the VerticalFlowLayout manager with the specified
   * alignment, horizontalGap and verticalGap.
   */
  public static VerticalFlowLayout getVerticalFlowLayout(final int align,
                                                         final int horizontalGap,
                                                         final int verticalGap) {
    logger.debug("align = " + align);
    logger.debug("horizontalGap = " + horizontalGap);
    logger.debug("verticalGap = " + verticalGap);
    return new VerticalFlowLayout(align, horizontalGap, verticalGap);
  }

}
