/*
 * VerticalFlowLayout.java (c) 17 April 2002
 *
 * A layout algorithm for laying out Components in a Container from top to bottom, left to right.
 * Note, the FlowLayout manager has similar behavior when the Locale varies and the ComponentOrientation
 * changes.
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see java.awt.LayoutManager
 * @see java.io.Serializable
 */

package com.cp.common.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class VerticalFlowLayout implements LayoutManager, Serializable {

  public static final int BOTTOM = 0;
  public static final int MIDDLE = 1;
  public static final int TOP = 2;

  private static final int DEFAULT_ALIGNMENT = MIDDLE;
  private static final int DEFAULT_HORIZONTAL_GAP = 5;
  private static final int DEFAULT_VERTICAL_GAP = 5;

  private int alignment = DEFAULT_ALIGNMENT;
  private int horizontalGap = DEFAULT_HORIZONTAL_GAP;
  private int verticalGap = DEFAULT_VERTICAL_GAP;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the VerticalFlowLayout, with defaults for alignment, horizontal gap and
   * vertical gap.
   */
  public VerticalFlowLayout() {
    this(DEFAULT_ALIGNMENT, DEFAULT_HORIZONTAL_GAP, DEFAULT_VERTICAL_GAP);
  }

  /**
   * Creates an instance of the VerticalFlowLayout class with the specified alignment, and defaults for the
   * horizontal gap and vertical gap.
   * @param alignment the alignment of the components in the container, TOP, MIDDLE and BOTTOM.
   */
  public VerticalFlowLayout(final int alignment) {
    this(alignment, DEFAULT_HORIZONTAL_GAP, DEFAULT_VERTICAL_GAP);
  }

  /**
   * Creates an instance of the VerticalFlowLayout class with the specified horizontal and vertical gaps,
   * and a default alignment.
   * @param horizontalGap the horizontal space between components layed out with this LayoutManager.
   * @param verticalGap the vertical space between components layed out with this LayoutManager.
   */
  public VerticalFlowLayout(final int horizontalGap, final int verticalGap) {
    this(DEFAULT_ALIGNMENT, horizontalGap, verticalGap);
  }

  /**
   * Creates an instance of the VerticalFlowLayout class with the specified alignment, horizontal gap
   * and vertical gap.
   * @param alignment the alignment of the components in the container, TOP, MIDDLE and BOTTOM.
   * @param horizontalGap the horizontal space between components layed out with this LayoutManager.
   * @param verticalGap the vertical space between components layed out with this LayoutManager.
   */
  public VerticalFlowLayout(final int alignment, final int horizontalGap, final int verticalGap) {
    setAlignment(alignment);
    setHorizontalGap(horizontalGap);
    setVerticalGap(verticalGap);
  }

  /**
   * Returns the alignment of the Components used by this LayoutManager to layout components in the Container.
   * @return an integer value specifying the alignment of Components in the Container.
   */
  public int getAlignment() {
    return alignment;
  }

  /**
   * Sets the specified alignment of the Components in this Container.
   * @param alignment an integer value specifying the alignment of Components in this Container.  Possible values
   * are TOP, MIDDLE and BOTTOM.
   * @throws IllegalArgumentException if the value of the alignment is not one of TOP, MIDDLE and BOTTOM.
   */
  public final void setAlignment(final int alignment) {
    if (logger.isDebugEnabled()) {
      logger.debug("alignment (" + alignment + ")");
    }
    switch (alignment) {
      case BOTTOM:
      case MIDDLE:
      case TOP:
        this.alignment = alignment;
        break;
      default:
        logger.warn("The alignment (" + alignment + ") is invalid!  Please specify a SwingConstants of TOP, CENTER or BOTTOM!");
        throw new IllegalArgumentException("The alignment (" + alignment
          + ") is invalid!  Please specify a SwingConstants of TOP, CENTER or BOTTOM!");
    }
  }

  /**
   * Returns the horizontal spacing used by this LayoutManager to layout Components in the Container.
   * @return an integer value specifying the horizontal spacing used by this LayoutManager to layout Components
   * in the Container.
   */
  public int getHorizontalGap() {
    return horizontalGap;
  }

  /**
   * Returns the horizontal spacing used by this LayoutManager to layout Components in the Container.
   * @param horizontalGap an integer value specifying the horizontal spacing used by this LayoutManager
   * to layout Components in the Container.
   */
  private void setHorizontalGap(final int horizontalGap) {
    if (logger.isDebugEnabled()) {
      logger.debug("horizontalGap (" + horizontalGap + ")");
    }
    this.horizontalGap = horizontalGap;
  }

  /**
   * Returns the vertical spacing used by this LayoutManager to layout Components in the Container.
   * @return an integer value specifying the horizontal spacing used by this LayoutManager to layout Components
   * in the Container.
   */
  public int getVerticalGap() {
    return verticalGap;
  }

  /**
   * Returns the vertical spacing used by this LayoutManager to layout Components in the Container.
   * @param verticalGap an integer value specifying the vertical spacing used by this LayoutManager
   * to layout Components in the Container.
   */
  private void setVerticalGap(final int verticalGap) {
    if (logger.isDebugEnabled()) {
      logger.debug("verticalGap (" + verticalGap + ")");
    }
    this.verticalGap = verticalGap;
  }

  /**
   * The implementation of this method for the VerticalFlowLayout manager does nothing.
   * @param name the string to be associated with the component.
   * @param comp the component to be added.
   */
  public void addLayoutComponent(final String name, final Component comp) {
    logger.warn("addLayoutComponent not implemented!");
  }

  /**
   * Lays out the Components in the Container according the VerticalFlowLayout manager's algorithm for laying out
   * Components.
   * @param parent the Container in which the Components will be layed out.
   */
  public void layoutContainer(final Container parent) {
    final Insets insets = parent.getInsets();

    if (logger.isDebugEnabled()) {
      logger.debug("insets (" + insets + ")");
    }

    final int maxWidth = (parent.getWidth() - (insets.left + insets.right));
    final int maxHeight = (parent.getHeight() - (insets.top + insets.bottom));

    if (logger.isDebugEnabled()) {
      logger.debug("maxWidth (" + maxWidth + ")");
      logger.debug("maxHeight (" + maxHeight + ")");
    }

    ColumnLayout colLayout = new ColumnLayout(insets.left);

    for (int index = 0, count = parent.getComponentCount(); index < count; index++) {
      final Component component = parent.getComponent(index);
      if (component.isVisible()) {
        component.setSize(component.getPreferredSize());
        if (colLayout.projectedHeight(component) < maxHeight) {
          colLayout.addComponent(component);
        }
        else {
          colLayout.layoutColumn(parent);
          colLayout = new ColumnLayout(colLayout.nextColumnPosition());
          colLayout.addComponent(component);
        }
      }
    }

    colLayout.layoutColumn(parent);
  }

  /**
   * Template method for implementing the minimumSize and preferredSize methods of the VerticalFlowLayout manager.
   * This method can be used to compute the minimum and preferred sizes of the Container.
   * @param parent the Container who's size is being determined based on the Components that the parent contains.
   * @param sizeDeterminator the size determination strategy for Components in the Container.
   * @return a Dimension object specifying the ideal size of the parent Container.
   */
  private Dimension layoutSize(final Container parent, final IComponentSizeDeterminator sizeDeterminator) {
    int width = 0;
    int height = 0;
    int numberOfVisibleComponents = 0;

    for (int index = parent.getComponentCount(); --index >= 0; ) {
      final Component component = parent.getComponent(index);
      if (component.isVisible()) {
        final Dimension componentSize = sizeDeterminator.getSize(component);
        if (logger.isDebugEnabled()) {
          logger.debug("componentSize (" + componentSize + ")");
        }

        width = (int) Math.max(width, componentSize.getWidth());
        height += componentSize.getHeight();
        numberOfVisibleComponents++;
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("numberOfVisibleComponents (" + numberOfVisibleComponents + ")");
    }

    final Insets containerInsets = parent.getInsets();
    if (logger.isDebugEnabled()) {
      logger.debug("containerInsets (" + containerInsets + ")");
    }

    width += (containerInsets.left + containerInsets.right);
    height += ((containerInsets.top + containerInsets.bottom) + (getVerticalGap() * (numberOfVisibleComponents - 1)));

    if (logger.isDebugEnabled()) {
      logger.debug("width (" + width + ")");
      logger.debug("height (" + height + ")");
    }

    return new Dimension(width, height);
  }

  /**
   * Returns the minimum size of the parent Container based on the minimum sizes of the Components contained within
   * the parent.
   * @param parent the Container who's minimum size is being determined based on the Components that the
   * parent contains.
   * @return a Dimension object specifying the minimum size of the parent Container.
   */
  public Dimension minimumLayoutSize(final Container parent) {
    return layoutSize(parent, MinimumComponentSizeDeterminator.INSTANCE);
  }

  /**
   * Returns the preferred size of the parent Container based on the minimum sizes of the Components contained within
   * the parent.
   * @param parent the Container who's preferred size is being determined based on the Components that the
   * parent contains.
   * @return a Dimension object specifying the preferred size of the parent Container.
   */
  public Dimension preferredLayoutSize(final Container parent) {
    return layoutSize(parent, PreferredComponentSizeDeterminator.INSTANCE);
  }

  /**
   * The implementation of this method for the VerticalFlowLayout manager does nothing.
   * @param comp the component to be added.
   */
  public void removeLayoutComponent(final Component comp) {
    logger.warn("removeLayoutComponent not implemented!");
  }

  /**
   * Returns a String description of this LayoutManager.
   * @return a String describing the current state of this LayoutManager.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{alignment = ");
    buffer.append(getAlignment());
    buffer.append(", horizontalGap = ").append(getHorizontalGap());
    buffer.append(", verticalGap = ").append(getVerticalGap());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * A separate class to specify the layout of a column in the vertical layout of the parent Container.
   */
  private final class ColumnLayout {

    private final int columnPosition;
    private int height;
    private int width;

    private final List componentQueue = new LinkedList();

    public ColumnLayout(final int columnPosition) {
      this.columnPosition = columnPosition;
    }

    public void addComponent(final Component component) {
      width = Math.max(width, component.getWidth());
      height += (componentQueue.isEmpty() ? component.getHeight() : (getVerticalGap() + component.getHeight()));
      componentQueue.add(component);
    }

    private int getColumnPosition() {
      return columnPosition;
    }

    public int getComponentCount() {
      return componentQueue.size();
    }

    public int getHeight() {
      return height;
    }

    private int getWidth() {
      return width;
    }

    private int getYOffset(final Container parent) {
      final Dimension parentSize = parent.getSize();
      final Insets parentInsets = parent.getInsets();

      switch (getAlignment()) {
        case BOTTOM:
          return (parentSize.height - parentInsets.bottom - getHeight());
        case MIDDLE:
          return ((parentSize.height - getHeight()) / 2);
        case TOP:
          return parentInsets.top;
        default:
          return 0;
      }
    }

    public void layoutColumn(final Container parent) {
      int yOffset = getYOffset(parent);
      for (Iterator it = componentQueue.iterator(); it.hasNext(); ) {
        final Component component = (Component) it.next();
        component.setLocation(getColumnPosition(), yOffset);
        yOffset += (component.getHeight() + getVerticalGap());
      }
    }

    public int nextColumnPosition() {
      return (getColumnPosition() + getWidth() + getHorizontalGap());
    }

    public int projectedHeight(final Component component) {
      return (getHeight() + (componentQueue.isEmpty() ? component.getHeight() : (getVerticalGap() + component.getHeight())));
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{columnPosition = ");
      buffer.append(getColumnPosition());
      buffer.append(", componentCount = ").append(getComponentCount());
      buffer.append(", height = ").append(getHeight());
      buffer.append(", width = ").append(getWidth());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * An interface to describe the size determination strategy for Components contained by their parent.
   */
  private interface IComponentSizeDeterminator {
    public Dimension getSize(Component component);
  }

  /**
   * The MinimumComponentSizeDeterminator class determines the minimum acceptable size of a Component in a
   * Container.
   */
  private static final class MinimumComponentSizeDeterminator implements IComponentSizeDeterminator {

    static final MinimumComponentSizeDeterminator INSTANCE = new MinimumComponentSizeDeterminator();

    public Dimension getSize(final Component component) {
      return component.getMinimumSize();
    }
  }

  /**
   * The PreferredComponentSizeDeterminator class determines the preferred size of a Component in a
   * Container.
   */
  private static final class PreferredComponentSizeDeterminator implements IComponentSizeDeterminator {

    static final PreferredComponentSizeDeterminator INSTANCE = new PreferredComponentSizeDeterminator();

    public Dimension getSize(final Component component) {
      return component.getPreferredSize();
    }
  }

}
