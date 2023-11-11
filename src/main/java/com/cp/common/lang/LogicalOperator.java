/*
 * LogicalOperator.java (c) 24 January 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.14
 * @see com.cp.common.lang.RelationalOperator
 */

package com.cp.common.lang;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class LogicalOperator {

  public static final LogicalOperator AND = new AndLogicalOperator();
  public static final LogicalOperator OR = new OrLogicalOperator();

  protected final Log log = LogFactory.getLog(getClass());

  /**
   * Gets the AND logical operator for anding two boolean conditions together.
   * @return the AND LogicalOperator.
   * @see LogicalOperator#getNot(LogicalOperator)
   * @see LogicalOperator#getOr()
   */
  public static LogicalOperator getAnd() {
    return AND;
  }

  /**
   * Gets an instance of the NOT logical operator wrapping the specified logical operator thereby negating
   * it's evaluation.
   * @param op the LogicalOperator object wrapped by an instance of the NOT logical operator to negate it's evaluation.
   * @return the NOT LogicalOperator.
   * @see LogicalOperator#getAnd()
   * @see LogicalOperator#getOr()
   */
  public static LogicalOperator getNot(final LogicalOperator op) {
    return new NotLogicalOperator(op);
  }

  /**
   * Gets the OR logical operator for oring two boolean conditions together.
   * @return the OR LogicalOperator.
   * @see LogicalOperator#getAnd()
   * @see LogicalOperator#getNot(LogicalOperator)
   */
  public static LogicalOperator getOr() {
    return OR;
  }

  /**
   * Determines whether this logical operator is a binary operator, or an operator taking two operands.
   * @return a boolean value indicating if this logical operator is a binary operator.
   * @see LogicalOperator#isTernary()
   * @see LogicalOperator#isUnary()
   */
  public abstract boolean isBinary();

  /**
   * Determines whether this logical operator is a ternary operator, or an operator taking three operands.
   * @return a boolean value indicating if this logical operator is a ternary operator.
   * @see LogicalOperator#isBinary()
   * @see LogicalOperator#isUnary()
   */
  public abstract boolean isTernary();

  /**
   * Determines whether this logical operator is a unary operator, or an operator taking only one operand.
   * @return a boolean value indicating if this logical operator is a unary operator.
   * @see LogicalOperator#isBinary()
   * @see LogicalOperator#isTernary()
   */
  public abstract boolean isUnary();

  /**
   * The logical operation applied to the two boolean conditions.
   * @param condition0 the first operand in the boolean condition.
   * @param condition1 the second operand in the boolean condition.
   * @return a boolean value indicating the result of applying the logical operator to the two boolean conditions.
   */
  public abstract boolean op(boolean condition0, boolean condition1);

  /**
   * The AND Binary LogicalOperator class.
   */
  public static class AndLogicalOperator extends LogicalOperator {

    @Override
    public boolean isBinary() {
      return true;
    }

    @Override
    public boolean isTernary() {
      return false;
    }

    @Override
    public boolean isUnary() {
      return false;
    }

    @Override
    public boolean op(final boolean condition0, final boolean condition1) {
      return (condition0 && condition1);
    }

    @Override
    public String toString() {
      return "AND";
    }
  }

  /**
   * The NOT Unary LogicalOperator class.
   */
  public static class NotLogicalOperator extends LogicalOperator {

    private final LogicalOperator op;

    public NotLogicalOperator(final LogicalOperator op) {
      Assert.notNull(op, "The logical operator used in this negation logical operator cannot be null!");
      this.op = op;
    }

    @Override
    public boolean isBinary() {
      return false;
    }

    protected LogicalOperator getOp() {
      return op;
    }

    @Override
    public boolean isTernary() {
      return false;
    }

    @Override
    public boolean isUnary() {
      return true;
    }

    @Override
    public boolean op(final boolean condition0, final boolean condition1) {
      return !getOp().op(condition0, condition1);
    }

    @Override
    public String toString() {
      return "NOT";
    }
  }

  /**
   * The OR Binary LogicalOperator class.
   */
  public static class OrLogicalOperator extends LogicalOperator {

    @Override
    public boolean isBinary() {
      return true;
    }

    @Override
    public boolean isTernary() {
      return false;
    }

    @Override
    public boolean isUnary() {
      return false;
    }

    @Override
    public boolean op(final boolean condition0, final boolean condition1) {
      return (condition0 || condition1);
    }

    @Override
    public String toString() {
      return "OR";
    }
  }

}
