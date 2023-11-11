/*
 * ComposableFilter.java (c) 24 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.2.5
 * @see com.cp.common.lang.LogicalOperator
 * @see com.cp.common.util.Filter
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.LogicalOperator;
import com.cp.common.lang.ObjectUtil;

public class ComposableFilter<T> implements Filter<T> {

  protected static final LogicalOperator DEFAULT_LOGICAL_OPERATOR = LogicalOperator.AND;

  private final Filter<T> leftFilter;
  private final Filter<T> rightFilter;

  private final LogicalOperator op;

  /**
   * Creates an instance of the ComposableFilter class initialized with both left and right filter objects.
   * @param leftFilter the left Filter object.
   * @param rightFilter the right Filter object.
   */
  protected ComposableFilter(final Filter<T> leftFilter, final Filter<T> rightFilter) {
    this(leftFilter, rightFilter, DEFAULT_LOGICAL_OPERATOR);
  }

  /**
   * Creates an instance of the ComposableFilter class initialized with both left and right filter objects
   * as well as the logical operator.
   * @param leftFilter the left Filter object.
   * @param rightFilter the right Filter object.
   * @param op the LogicalOperator used to join the conditions resulting from the left and right filters.
   */
  protected ComposableFilter(final Filter<T> leftFilter, final Filter<T> rightFilter, final LogicalOperator op) {
    Assert.notNull(leftFilter, "The left filter cannot be null!");
    Assert.notNull(rightFilter, "The right filter cannot be null!");
    this.leftFilter = leftFilter;
    this.rightFilter = rightFilter;
    this.op = ObjectUtil.getDefaultValue(op, DEFAULT_LOGICAL_OPERATOR);
  }

  /**
   * Composes two Filter objects using the specified binary logical operator (such as AND or OR).
   * @param leftFilter the left Filter object.
   * @param rightFilter the right Filter object.
   * @param op the LogicalOperator used to join the conditions resulting from the left and right filters.
   * @return an object implementing the Filter interface composed of the two Filter object parameters.
   * @see ComposableFilter#compose(LogicalOperator, Filter[])
   * @see ComposableFilter#composeAnd(Filter[])
   * @see ComposableFilter#composeOr(Filter[])
   */
  public static <T> Filter<T> compose(final Filter<T> leftFilter, final Filter<T> rightFilter, final LogicalOperator op) {
    return (ObjectUtil.isNull(leftFilter) ? rightFilter : (ObjectUtil.isNull(rightFilter) ? leftFilter
      : new ComposableFilter<T>(leftFilter, rightFilter, op)));
  }

  /**
   * Composes an array of Filter objects using the specified binary logical operator (such as AND or OR).
   * @param op the LogicalOperator used to join the conditions resulting from the array of Filter objects.
   * @param filters an array of Filter objects to be composed using the specified binary logical operator.
   * @return an object implementing the Filter interface composed of the array of Filter objects.
   * @see ComposableFilter#compose(Filter, Filter, LogicalOperator)
   * @see ComposableFilter#composeAnd(Filter[])
   * @see ComposableFilter#composeOr(Filter[])
   */
  public static <T> Filter<T> compose(final LogicalOperator op, final Filter<T>... filters) {
    Filter<T> currentFilter = null;

    if (ArrayUtil.isNotEmpty(filters)) {
      for (final Filter<T> filter : filters) {
        currentFilter = compose(currentFilter, filter, op);
      }
    }

    return currentFilter;
  }

  /**
   * Composes an array of Filter objects using the AND binary logical operator.
   * @param filters an array of Filter objects to be composed using the AND binary logical operator.
   * @return an object implementing the Filter interface composed of the array of Filter objects.
   * @see ComposableFilter#compose(Filter, Filter, LogicalOperator)
   * @see ComposableFilter#compose(LogicalOperator, Filter[])
   * @see ComposableFilter#composeOr(Filter[])
   */
  public static <T> Filter<T> composeAnd(final Filter<T>... filters) {
    return compose(LogicalOperator.AND, filters);
  }

  /**
   * Composes an array of Filter objects using the OR binary logical operator.
   * @param filters an array of Filter objects to be composed using the OR binary logical operator.
   * @return an object implementing the Filter interface composed of the array of Filter objects.
   * @see ComposableFilter#compose(Filter, Filter, LogicalOperator)
   * @see ComposableFilter#compose(LogicalOperator, Filter[])
   * @see ComposableFilter#composeAnd(Filter[])
   */
  public static <T> Filter<T> composeOr(final Filter<T>... filters) {
    return compose(LogicalOperator.OR, filters);
  }

  /**
   * Gets the left filter in the composed filters.
   * @return a Filter object on the left side of the tree.
   */
  protected Filter<T> getLeftFilter() {
    return leftFilter;
  }

  /**
   * Gets the logical operator used to join the conditions resulting from the left and right filters.
   * @return a LogicalOperator object used to join the conditions resulting from the left and right filters.
   */
  protected LogicalOperator getOp() {
    return op;
  }

  /**
   * Gets the right filter in the composed filters.
   * @return a Filter object on the right side of the tree.
   */
  protected Filter<T> getRightFilter() {
    return rightFilter;
  }

  /**
   * Determines whether the specified object satisfies the criteria of this filter.
   * @param obj the Object tested against the criteria of this filter.
   * @return a boolean value if the specified object passes the criteria of this filter.
   */
  public boolean accept(final T obj) {
    return getOp().op(getLeftFilter().accept(obj), getRightFilter().accept(obj));
  }

}
