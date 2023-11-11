/*
 * MathUtil.java (c) 19 December 2006
 *
 * The MathUtil utility class contains several mathematical algorithms and calculations to encapsulate
 * common mathematical equations and operations in a single class.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.25
 * @see java.lang.Math
 * @see http://www.math.com
 */

package com.cp.common.lang;

import com.cp.common.util.CollectionUtil;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public final class MathUtil {

  /**
   * Default private constructor to prevent instantiation of the MathUtil utility class.
   */
  private MathUtil() {
  }

  /**
   * Computes the area of a circle.
   * @param radius a double value specifying the radius of the circle.
   * @return a double value specifying the area of a circle defined by it's radius.
   * @see MathUtil#ellipseArea(double, double)
   */
  public static double circleArea(final double radius) {
    return ellipseArea(radius, radius);
  }

  /**
   * Computes the circumference of a circle.
   * @param radius a double value specifying the radius of the circule.
   * @return a double value indicating the circle's circumference.
   */
  public static double circumference(final double radius) {
    return (2.0 * Math.PI * radius);
  }

  /**
   * Computes the compound interest for an inital deposit invested over a fixed number of years at a given
   * interest rate compounded over a fixed number of times per year.
   * @param initialDeposit a BigDecimal indicating the initial value of the deposit.
   * @param interestRate the interest rate expressed as a fraction.
   * @param timesPerYear the number of times per year that interest is compounded.
   * @param numberOfYears the number of years invested.
   * @return a BigDecimal indicating the future value of the initial deposite with compounded interest.
   */
  public static BigDecimal compoundInterest(final BigDecimal initialDeposit,
                                            final BigDecimal interestRate,
                                            final int timesPerYear,
                                            final int numberOfYears)
  {
    BigDecimal result = interestRate.divide(new BigDecimal(timesPerYear), MathContext.DECIMAL128);
    result = result.add(BigDecimal.ONE, MathContext.DECIMAL128);
    result = result.pow(timesPerYear * numberOfYears, MathContext.DECIMAL128);
    return initialDeposit.multiply(result).setScale(2, RoundingMode.HALF_EVEN);
  }

  /**
   * Computes the surface area of a cube.
   * @param side a double value specifying the length of one side of the cubic shape.
   * @return a double value indicating the surface area fo the cubic shape.
   */
  public static double cubeSurfaceArea(final double side) {
    return (6 * Math.pow(side, 2));
  }

  /**
   * Computes the volume of a cube.
   * @param side a double value specifying length of one side of the cubic shape.
   * @return a double value indicating the volume of the cubic shape.
   * @see MathUtil#rectangularPrismVolume(double, double, double)
   */
  public static double cubeVolume(final double side) {
    return rectangularPrismVolume(side, side, side);
  }

  /**
   * Computes the diameter of a circle.
   * @param radius a double value specifying the radius of the circle.
   * @return a double value specifying the diameter of a circle defined by it's radius.
   */
  public static double diameter(final double radius) {
    return (2.0 * radius);
  }

  /**
   * Computes the volume of an ellipsoid.
   * @param radiusWidth a double value specifying the radius width of the ellipsoid.
   * @param radiusHeight a double value specifying the radius height of the ellipsoid.
   * @param radiusLength a double value specifying the radius length of the ellipsoid.
   * @return a double value specifying the volume of an ellipsoid defined by the radius width, height and length.
   */
  public static double ellipsoidVolume(final double radiusWidth, final double radiusHeight, final double radiusLength) {
    return (4.0/3.0 * Math.PI * radiusWidth * radiusHeight * radiusLength);
  }

  /**
   * Computes the area of an ellipse.
   * @param radiusWidth a double value specifying the horizontal radius.
   * @param radiusHeight a double value specifying the verticle radius.
   * @return a double value specifying the area of the ellipse defined by two radii.
   */
  public static double ellipseArea(final double radiusWidth, final double radiusHeight) {
    return (Math.PI * radiusWidth * radiusHeight);
  }

  /**
   * Computes the factorial of an integral numerical value.
   * @param value the integer value used in the computation of the factorial.
   * @return the factorial of the integer value.
   * @throws IllegalArgumentException if the value is negative.
   */
  public static int factorial(int value) {
    Assert.isFalse(NumberUtil.isNegative(value), "Cannot compute the factorial of a negative number!");

    if (NumberUtil.isZero(value)) {
      return 1;
    }

    for (int multiplier = value; --multiplier > 0; ) {
      value *= multiplier;
    }

    return value;
  }

  /**
   * Determines the fibonacci number at the specified positional value.
   * @param n an integer value specifying the position in the sequence of Fibonacci numbers.
   * @return the fibonacci number at the specified positional value in the sequence of Fibonacci numbers.
   * @see MathUtil#fibonacciSequence(int)
   */
  public static int fibonacciNumber(final int n) {
    return CollectionUtil.getLastElement(fibonacciSequence(n));
  }

  /**
   * Determines the sequence of Fibonacci numbers for the specfied positional value.  The algorithm determining the
   * sequence of Fibonacci numbers is defined as the recurrence relation Fn = Fn-1 + Fn-2 with seed values
   * F0 = 0 and F1 = 1.
   * @param n an integer value specifying the position in the sequence of Fibonacci numbers.
   * @return a List of integral values containing the sequence of Fibonacci numbers for the specified positional value.
   * @see MathUtil#fibonacciNumber(int)
   * @see http://en.wikipedia.org/wiki/Fibonacci_sequence
   */
  public static List<Integer> fibonacciSequence(int n) {
    Assert.greaterThanEqual(n, 0, "The positional value n (" + n + ") must be greater than equal to zero!");

    final List<Integer> fibonaciSequence = new LinkedList<Integer>();

    int previousValue = 0;
    int currentValue = 1;
    int temp = currentValue;

    do {
      fibonaciSequence.add(previousValue);
      temp = currentValue;
      currentValue = previousValue + currentValue;
      previousValue = temp;
    }
    while (--n >= 0);

    return fibonaciSequence;
  }

  /**
   * Computes the remaining balance on a loan having a specified annual percentage rate, based on the amount borrowed,
   * taking into account the number a payments made to date and how much each payment is valued.
   * @param amountBorrowed the amount of the loan.
   * @param annualPercentageRate the APR value or interest rate.
   * @param numberOfPayments the number of payments at the specified amount already made.
   * @param paymentAmount the value amount of each payment.
   * @return a BigDecimal value indicating the remaining balance of the loan.
   */
  public static BigDecimal loanBalance(final BigDecimal amountBorrowed,
                                       final BigDecimal annualPercentageRate,
                                       final int numberOfPayments,
                                       final BigDecimal paymentAmount)
  {
    final BigDecimal monthlyInterestRate = annualPercentageRate.divide(new BigDecimal(1200), MathContext.DECIMAL128);
    final BigDecimal rate = monthlyInterestRate.add(BigDecimal.ONE).pow(numberOfPayments);
    final BigDecimal loanAmount = amountBorrowed.multiply(rate, MathContext.DECIMAL128);

    BigDecimal amountPaid = paymentAmount.divide(monthlyInterestRate, MathContext.DECIMAL128);
    amountPaid = amountPaid.multiply(rate.subtract(BigDecimal.ONE, MathContext.DECIMAL128));

    return loanAmount.subtract(amountPaid, MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_EVEN);
  }

  /**
   * Gets the maximum double value in an array of double values.
   * @param values the array of double values.
   * @return the maximum double value in the array of double values.
   * @see MathUtil#min(double...)
   */
  public static double max(final double... values) {
    double max = Double.NaN;

    if (ObjectUtil.isNotNull(values)) {
      for (final double value : values) {
        max = (Double.isNaN(max) ? value : Math.max(max, value));
      }
    }

    return max;
  }

  /**
   * Gets the minimum double value in an array of double values.
   * @param values the array of double values.
   * @return the minimum double value in the array of double values.
   * @see MathUtil#max(double...)
   */
  public static double min(final double... values) {
    double min = Double.NaN;

    if (ObjectUtil.isNotNull(values)) {
      for (final double value : values) {
        min = (Double.isNaN(min) ? value : Math.min(min, value));
      }
    }

    return min;
  }

  /**
   * Multiplies the series of integer values.
   * @param values the integer values to multiply together.
   * @return the multiplication of all the values.
   * @see MathUtil#sum(int...)
   */
  public static int multiply(final int... values) {
    int multiplication = 1;

    if (ObjectUtil.isNotNull(values)) {
      for (final int value : values) {
        multiplication *= value;
      }
    }

    return multiplication;
  }

  /**
   * Computes the volume of a pyramid.
   * @param base a double value specifying the base length of the pyramid.
   * @param height a double value specifying the height of the pyramid.
   * @return a double value specifying the volume of the pyramid defined by it's base and height.
   */
  public static double pyramidVolume(final double base, final double height) {
    return ((base * height) / 3.0);
  }

  /**
   * Computes the Pythagorean Theorem (c**2 = a**2 + b**2) to calculate the hypotenuse of a right triangle
   * (triangle with a 90 degree angle).
   * In any right triangle, the area of the square whose side is the hypotenuse (the side opposite the right angle)
   * is equal to the sum of the areas of the squares whose sides are the two legs (the two sides other than the
   * hypotenuse).
   * @param a the length of side A in the right triangle.
   * @param b the length of side B in the right triangle.
   * @return the length of the right triangle's hypotenuse.
   * @see Math#hypot(double, double)
   * @see http://en.wikipedia.org/wiki/Pythagorean_Theorem
   */
  public static double pythagoreanTheorem(final double a, final double b) {
    final double aSquared = (a * a);
    final double bSquared = (b * b);
    return Math.sqrt(aSquared + bSquared);
  }

  /**
   * Computes the area of a rectangle.
   * @param width a double value specifying the width of the rectangle.
   * @param height a double value specifying the height of the rectangle
   * @return a double value indicating the area of the rectangle (or rectangular area).
   */
  public static double rectangleArea(final double width, final double height) {
    return (width * height);
  }

  /**
   * Computes the surface area of a rectangular prism.
   * @param width a double value specifying the width of the rectangular prism.
   * @param height a double value specifying the height of the rectangular prism.
   * @param length a double value specifying the length of the rectangular prism.
   * @return a double value indicating the surface area of the rectangular prism.
   */
  public static double rectangularPrismSurfaceArea(final double width, final double height, final double length) {
    return ((2.0 * width * height) + (2.0 * height * length) + (2.0 * width * length));
  }

  /**
   * Computes the volume of a rectangular prism.
   * @param width a double value specifying the width of the rectangular prism.
   * @param height a double value specifying the height of the rectangular prism.
   * @param length a double value specifying the length of the rectangular prism.
   * @return a double value specifying the volume of the rectangular prism.
   */
  public static double rectangularPrismVolume(final double width, final double height, final double length) {
    return (width * height * length);
  }

  /**
   * Computes the surface area of a sphere.
   * @param radius a double value specifying the radius of the sphere.
   * @return a double value indicating the surface area of the sphere defined by the radius.
   */
  public static double sphereSurfaceArea(final double radius) {
    return (4.0 * Math.PI * Math.pow(radius, 2));
  }

  /**
   * Computes the volume of a sphere.
   * @param radius a double value specifying the radius of the sphere.
   * @return a double value indicating the volume of the sphere defined by the radius.
   * @see MathUtil#ellipsoidVolume(double, double, double)
   */
  public static double sphereVolume(final double radius) {
    return ellipsoidVolume(radius, radius, radius);
  }

  /**
   * Computes the area of a square.
   * @param side a double value specifying the length of a side.
   * @return a double vlaue indicating the area of the square (or square area).
   * @see MathUtil#rectangleArea(double,double)
   */
  public static double squareArea(final double side) {
    return rectangleArea(side, side);
  }

  /**
   * Sums the series of integer values.
   * @param values the integer values to add up.
   * @return the sum of all the integer values.
   * @see MathUtil#multiply(int...)
   */
  public static int sum(final int... values) {
    int sum = 0;

    if (ObjectUtil.isNotNull(values)) {
      for (final int value : values) {
        sum += value;
      }
    }

    return sum;
  }

  /**
   * Computes the area of a triangle
   * @param base a double value specifying the length of the base of a triangle.
   * @param height a double value specifying the height of a triangle.
   * @return a double value specifying the area of a triangle defined by it's base and height.
   */
  public static double triangleArea(final double base, final double height) {
    return (base * height) / 2;
  }

}
