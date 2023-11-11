/*
 * RelationalOperator.java (c) 15 February 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.13
 * @see com.cp.common.lang.LogicalOperator
 * @see java.lang.Comparable
 */

package com.cp.common.lang;

public abstract class RelationalOperator<T extends Comparable<T>> {

  private final String description;
  private final T expectedValue;

  /**
   * Creates in instance of the RelationalOperator class with the specified description
   * and no expected Comparable value.
   * @param description a String describing this relational operator.
   */
  protected RelationalOperator(final String description) {
    this.description = description;
    expectedValue = null;
  }

  /**
   * Creates in instance of the RelationalOperator class with the specified description
   * and expected Comparable value.
   * @param expectedValue the expected Comparable value in the relational operation.
   * @param description a String describing this relational operator.
   */
  protected RelationalOperator(final T expectedValue, final String description) {
    Assert.notNull(expectedValue, "The expected value cannot be null!");
    this.expectedValue = expectedValue;
    this.description = description;
  }

  /**
   * The expected Comparable value in the relational operation.
   * @return the expected Comparable value in the relational operation.
   */
  protected T getExpectedValue() {
    return expectedValue;
  }

  /**
   * Determines whether this RelationalOperator accepts the specified actual valu.
   * @param actualValue the actual Comparable value used in the relational operation with the expected Comparable value
   * of the same type.
   * @return a boolean value indicating whether the relational condition is true.
   */
  public abstract boolean accept(T actualValue);

  /**
   * Gets the String representation of this RelationalOperator.
   * @return the internal state of the RelationalOperator as a String value.
   */
  @Override
  public String toString() {
    return description;
  }

  /**
   * Determines whether the actual Comparable value is equal to the expected Comparable value.
   * @param expectedValue the expected Comparable value in the relational operation.
   * @return a RelationalOperator instance implementing the equality comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getEqualTo(final T expectedValue) {
    return new EqualToRelationalOperator<T>(expectedValue);
  }

  /**
   * Determines whether the actual Comparable value is greater than the expected Comparable value.
   * @param expectedValue the expected Comparable value in the relational operation.
   * @return a RelationalOperator instance implementing the greater than relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getGreaterThan(final T expectedValue) {
    return new GreaterThanRelationalOperator<T>(expectedValue);
  }

  /**
   * Determines whether the actual Comparable value is greater than some value and less than some other value.
   * @param greaterThanValue the exclusive lower bound in the relational comparison.
   * @param lessThanValue the exclusive upper bound in the relational comparison.
   * @return a RelationalOperator instance implementing the greater than less than relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getGreaterThanAndLessThan(final T greaterThanValue, final T lessThanValue) {
    return new GreaterThanAndLessThanRelationalOperator<T>(greaterThanValue, lessThanValue);
  }

  /**
   * Determines whether the actual Comparable value is greater than some value and less than or equal to some other value.
   * @param greaterThanValue the exclusive lower bound in the relational comparison.
   * @param lessThanEqualToValue the inclusive upper bound in the relational comparison.
   * @return a RelationalOperator instance implementing the greater than less than equal to relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getGreaterThanAndLessThanEqualTo(final T greaterThanValue, final T lessThanEqualToValue) {
    return new GreaterThanAndLessThanEqualToRelationalOperator<T>(greaterThanValue, lessThanEqualToValue);
  }

  /**
   * Determines whether the actual Comparable value is greater than or equal to the expected Comparable value.
   * @param expectedValue the expected Comparable value in the relational operation.
   * @return a RelationalOperator instance implementing the greater than equal to relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getGreaterThanEqualTo(final T expectedValue) {
    return new GreaterThanEqualToRelationalOperator<T>(expectedValue);
  }

  /**
   * Determines whether the actual Comparable value is greater than or equal to some value and less than some other value.
   * @param greaterThanEqualToValue the inclusive lower bound in the relational comparison.
   * @param lessThanValue the exclusive upper bound in the relational comparison.
   * @return a RelationalOperator instance implementing the greater than equal to less than relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getGreaterThanEqualToAndLessThan(final T greaterThanEqualToValue, final T lessThanValue) {
    return new GreaterThanEqualToAndLessThanRelationalOperator<T>(greaterThanEqualToValue, lessThanValue);
  }

  /**
   * Determines whether the actual Comparable value is greater than or equal to some value and less than or equal to some other value.
   * @param greaterThanEqualToValue the inclusive lower bound in the relational comparison.
   * @param lessThanEqualToValue the inclusive upper bound in the relational comparison.
   * @return a RelationalOperator instance implementing the greater than equal to less than equal to relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getGreaterThanEqualToAndLessThanEqualTo(final T greaterThanEqualToValue, final T lessThanEqualToValue) {
    return new GreaterThanEqualToAndLessThanEqualToRelationalOperator<T>(greaterThanEqualToValue, lessThanEqualToValue);
  }

  /**
   * Determines whether the actual Comparable value is less than the expected Comparable value.
   * @param expectedValue the expected Comparable value in the relational operation.
   * @return a RelationalOperator instance implementing the less than relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getLessThan(final T expectedValue) {
    return new LessThanRelationalOperator<T>(expectedValue);
  }

  /**
   * Determines whether the actual Comparable value is less than some value or greater than some other value.
   * @param lessThanValue the exclusive upper bound in the relational comparison.
   * @param greaterThanValue the exclusive lower bound in the relational comparison.
   * @return a RelationalOperator instance implementing the less than or greater than relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getLessThanOrGreaterThan(final T lessThanValue, final T greaterThanValue) {
    return new LessThanOrGreaterThanRelationalOperator<T>(lessThanValue, greaterThanValue);
  }

  /**
   * Determines whether the actual Comparable value is less than some value or greater than equal to some other value.
   * @param lessThanValue the exclusive upper bound in the relational comparison.
   * @param greaterThanEqualToValue the inclusive lower bound in the relational comparison.
   * @return a RelationalOperator instance implementing the less than or greater than equal to relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getLessThanOrGreaterThanEqualTo(final T lessThanValue, final T greaterThanEqualToValue) {
    return new LessThanOrGreaterThanEqualToRelationalOperator<T>(lessThanValue, greaterThanEqualToValue);
  }

  /**
   * Determines whether the actual Comparable value is less than or equal to the expected Comparable value.
   * @param expectedValue the expected Comparable value in the relational operation.
   * @return a RelationalOperator instance implementing the less than equal to relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getLessThanEqualTo(final T expectedValue) {
    return new LessThanEqualToRelationalOperator<T>(expectedValue);
  }

  /**
   * Determines whether the actual Comparable value is less than equal to some value or greater than some other value.
   * @param lessThanEqualToValue the inclusive upper bound in the relational comparison.
   * @param greaterThanValue the exclusive lower bound in the relational comparison.
   * @return a RelationalOperator instance implementing the less than equal to or greater than relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getLessThanEqualToOrGreaterThan(final T lessThanEqualToValue, final T greaterThanValue) {
    return new LessThanEqualToOrGreaterThanRelationalOperator<T>(lessThanEqualToValue, greaterThanValue);
  }

  /**
   * Determines whether the actual Comparable value is less than equal to some value or greater than equal to some other value.
   * @param lessThanEqualToValue the inclusive upper bound in the relational comparison.
   * @param greaterThanEqualToValue the inclusive lower bound in the relational comparison.
   * @return a RelationalOperator instance implementing the less than equal to or greater than equal to relational comparison.
   */
  public static <T extends Comparable<T>> RelationalOperator<T> getLessThanEqualToOrGreaterThanEqualTo(final T lessThanEqualToValue, final T greaterThanEqualToValue) {
    return new LessThanEqualToOrGreaterThanEqualToRelationalOperator<T>(lessThanEqualToValue, greaterThanEqualToValue);
  }

  /**
   * Equal To Relational Operator
   */
  protected static class EqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    public EqualToRelationalOperator(final T expectedValue) {
      super(expectedValue, "equal to relational operator");
    }

    @Override
    public boolean accept(final T actualValue) {
      Assert.notNull(actualValue, "The actual value cannot be null!");
      return (actualValue.compareTo(getExpectedValue()) == 0);
    }
  }

  /**
   * Greater Than Relational Operator
   */
  protected static class GreaterThanRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    public GreaterThanRelationalOperator(final T expectedValue) {
      super(expectedValue, "greater than relational operator");
    }

    @Override
    public boolean accept(final T actualValue) {
      Assert.notNull(actualValue, "The actual value cannot be null!");
      return (actualValue.compareTo(getExpectedValue()) > 0);
    }
  }

  /**
   * Greater Than And Less Than Relational Operator
   */
  protected static class GreaterThanAndLessThanRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThan;
    private final RelationalOperator<T> lessThan;

    public GreaterThanAndLessThanRelationalOperator(final T greaterThanValue, final T lessThanValue) {
      super("greater than and less than relational operator");
      greaterThan = new GreaterThanRelationalOperator<T>(greaterThanValue);
      lessThan = new LessThanRelationalOperator<T>(lessThanValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (greaterThan.accept(actualValue) && lessThan.accept(actualValue));
    }
  }

  /**
   * Greater Than And Less Than Equal To Relational Operator
   */
  protected static class GreaterThanAndLessThanEqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThan;
    private final RelationalOperator<T> lessThanEqualTo;

    public GreaterThanAndLessThanEqualToRelationalOperator(final T greaterThanValue, final T lessThanEqualToValue) {
      super("greater than and less than equal to relational operator");
      greaterThan = new GreaterThanRelationalOperator<T>(greaterThanValue);
      lessThanEqualTo = new LessThanEqualToRelationalOperator<T>(lessThanEqualToValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (greaterThan.accept(actualValue) && lessThanEqualTo.accept(actualValue));
    }
  }

  /**
   * Greater Than Equal To Relational Operator
   */
  protected static class GreaterThanEqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    public GreaterThanEqualToRelationalOperator(final T expectedValue) {
      super(expectedValue, "greater than equal to relational operator");
    }

    @Override
    public boolean accept(final T actualValue) {
      Assert.notNull(actualValue, "The actual value cannot be null!");
      return (actualValue.compareTo(getExpectedValue()) >= 0);
    }
  }

  /**
   * Greater Than Equal To And Less Than Relational Operator
   */
  protected static class GreaterThanEqualToAndLessThanRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThanEqualTo;
    private final RelationalOperator<T> lessThan;

    public GreaterThanEqualToAndLessThanRelationalOperator(final T greaterThanEqualToValue, final T lessThanValue) {
      super("greater than equal to and less than relational operator");
      greaterThanEqualTo = new GreaterThanEqualToRelationalOperator<T>(greaterThanEqualToValue);
      lessThan = new LessThanRelationalOperator<T>(lessThanValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (greaterThanEqualTo.accept(actualValue) && lessThan.accept(actualValue));
    }
  }

  /**
   * Greater Than Equal To And Less Than Equal To Relational Operator
   */
  protected static class GreaterThanEqualToAndLessThanEqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThanEqualTo;
    private final RelationalOperator<T> lessThanEqualTo;

    public GreaterThanEqualToAndLessThanEqualToRelationalOperator(final T greaterThanEqualToValue, final T lessThanEqualToValue) {
      super("greater than equal to and less than equal to relational operator");
      greaterThanEqualTo = new GreaterThanEqualToRelationalOperator<T>(greaterThanEqualToValue);
      lessThanEqualTo = new LessThanEqualToRelationalOperator<T>(lessThanEqualToValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (greaterThanEqualTo.accept(actualValue) && lessThanEqualTo.accept(actualValue));
    }
  }

  /**
   * Less Than Relational Operator
   */
  protected static class LessThanRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    public LessThanRelationalOperator(final T expectedValue) {
      super(expectedValue, "less than relational operator");
    }

    @Override
    public boolean accept(final T actualValue) {
      Assert.notNull(actualValue, "The actual value cannot be null!");
      return (actualValue.compareTo(getExpectedValue()) < 0);
    }
  }

  /**
   * Less Than Or Greater Than Relational Operator
   */
  protected static class LessThanOrGreaterThanRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThan;
    private final RelationalOperator<T> lessThan;

    public LessThanOrGreaterThanRelationalOperator(final T lessThanValue, final T greaterThanValue) {
      super("less than or greater than relational operator");
      greaterThan = new GreaterThanRelationalOperator<T>(greaterThanValue);
      lessThan = new LessThanRelationalOperator<T>(lessThanValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (lessThan.accept(actualValue) || greaterThan.accept(actualValue));
    }
  }

  /**
   * Less Than Or Greater Than Equal To Relational Operator
   */
  protected static class LessThanOrGreaterThanEqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThanEqualTo;
    private final RelationalOperator<T> lessThan;

    public LessThanOrGreaterThanEqualToRelationalOperator(final T lessThanValue, final T greaterThanEqualToValue) {
      super("less than or greater than relational operator");
      greaterThanEqualTo = new GreaterThanEqualToRelationalOperator<T>(greaterThanEqualToValue);
      lessThan = new LessThanRelationalOperator<T>(lessThanValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (lessThan.accept(actualValue) || greaterThanEqualTo.accept(actualValue));
    }
  }

  /**
   * Less Than Equal To Relational Operator
   */
  protected static class LessThanEqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    public LessThanEqualToRelationalOperator(final T expectedValue) {
      super(expectedValue, "less than equal to relational operator");
    }

    @Override
    public boolean accept(final T actualValue) {
      Assert.notNull(actualValue, "The actual value cannot be null!");
      return (actualValue.compareTo(getExpectedValue()) <= 0);
    }
  }

  /**
   * Less Than Equal To Or Greater Than Relational Operator
   */
  protected static class LessThanEqualToOrGreaterThanRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThan;
    private final RelationalOperator<T> lessThanEqualTo;

    public LessThanEqualToOrGreaterThanRelationalOperator(final T lessThanEqualToValue, final T greaterThanValue) {
      super("less than equal to or greater than relational operator");
      greaterThan = new GreaterThanRelationalOperator<T>(greaterThanValue);
      lessThanEqualTo = new LessThanEqualToRelationalOperator<T>(lessThanEqualToValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (lessThanEqualTo.accept(actualValue) || greaterThan.accept(actualValue));
    }
  }

  /**
   * Less Than Equal To Or Greater Than Equal To Relational Operator
   */
  protected static class LessThanEqualToOrGreaterThanEqualToRelationalOperator<T extends Comparable<T>> extends RelationalOperator<T> {

    private final RelationalOperator<T> greaterThanEqualTo;
    private final RelationalOperator<T> lessThanEqualTo;

    public LessThanEqualToOrGreaterThanEqualToRelationalOperator(final T lessThanEqualToValue, final T greaterThanEqualToValue) {
      super("less than equal to or greater than relational operator");
      greaterThanEqualTo = new GreaterThanEqualToRelationalOperator<T>(greaterThanEqualToValue);
      lessThanEqualTo = new LessThanEqualToRelationalOperator<T>(lessThanEqualToValue);
    }

    @Override
    public boolean accept(final T actualValue) {
      return (lessThanEqualTo.accept(actualValue) || greaterThanEqualTo.accept(actualValue));
    }
  }

}
