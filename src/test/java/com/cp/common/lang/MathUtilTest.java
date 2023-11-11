/*
 * MathUtilTest.java (c) 19 December 2006
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.25
 * @see com.cp.common.lang.MathUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import com.cp.common.util.CollectionUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MathUtilTest extends TestCase {

  public MathUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MathUtilTest.class);
    //suite.addTest(new MathUtilTest("testName"));
    return suite;
  }

  protected BigDecimal getBigDecimal(final double value) {
    return new BigDecimal(value).setScale(2, RoundingMode.FLOOR);
  }

  protected BigDecimal getBigDecimal(final int value) {
    return new BigDecimal(value);
  }

  protected BigDecimal getBigDecimal(final String value) {
    return new BigDecimal(value).setScale(2, RoundingMode.FLOOR);
  }

  public void testCircleArea() throws Exception {
    assertEquals(getBigDecimal("28.27"), getBigDecimal(MathUtil.circleArea(3)));
    assertEquals(getBigDecimal("50.26"), getBigDecimal(MathUtil.circleArea(4)));
    assertEquals(getBigDecimal("153.93"), getBigDecimal(MathUtil.circleArea(7)));
  }

  public void testCubeSurfaceArea() throws Exception {
    assertEquals(54.0, MathUtil.cubeSurfaceArea(3.0));
    assertEquals(96.0, MathUtil.cubeSurfaceArea(4.0));
    assertEquals(150.0, MathUtil.cubeSurfaceArea(5.0));
  }

  public void testCircumference() throws Exception {
    assertEquals(getBigDecimal("18.84"), getBigDecimal(MathUtil.circumference(3)));
    assertEquals(getBigDecimal("25.13"), getBigDecimal(MathUtil.circumference(4)));
    assertEquals(getBigDecimal("31.41"), getBigDecimal(MathUtil.circumference(5)));
  }

  public void testCompoundInterest() throws Exception {
    assertEquals(getBigDecimal("10600.00"), MathUtil.compoundInterest(getBigDecimal("10000.00"), getBigDecimal("0.06"), 1, 1));
    assertEquals(getBigDecimal("10609.00"), MathUtil.compoundInterest(getBigDecimal("10000.00"), getBigDecimal("0.06"), 2, 1));
    assertEquals(getBigDecimal("10613.64"), MathUtil.compoundInterest(getBigDecimal("10000.00"), getBigDecimal("0.06"), 4, 1));
    assertEquals(getBigDecimal("10616.78"), MathUtil.compoundInterest(getBigDecimal("10000.00"), getBigDecimal("0.06"), 12, 1));
    assertEquals(getBigDecimal("10618.00"), MathUtil.compoundInterest(getBigDecimal("10000.00"), getBigDecimal("0.06"), 52, 1));
    assertEquals(getBigDecimal("10618.31"), MathUtil.compoundInterest(getBigDecimal("10000.00"), getBigDecimal("0.06"), 365, 1));
  }

  public void testCubeVolume() throws Exception {
    assertEquals(27.0, MathUtil.cubeVolume(3));
    assertEquals(64.0, MathUtil.cubeVolume(4));
    assertEquals(125.0, MathUtil.cubeVolume(5));
    assertEquals(1000.0, MathUtil.cubeVolume(10));
  }

  public void testDiameter() throws Exception {
    assertEquals(6.0, MathUtil.diameter(3));
    assertEquals(8.0, MathUtil.diameter(4));
    assertEquals(10.0, MathUtil.diameter(5));
  }

  public void testEllipsoidVolume() throws Exception {
    assertEquals(getBigDecimal("251.32"), getBigDecimal(MathUtil.ellipsoidVolume(3, 4, 5)));
    assertEquals(getBigDecimal("268.08"), getBigDecimal(MathUtil.ellipsoidVolume(2, 4, 8)));
    assertEquals(getBigDecimal("1759.29"), getBigDecimal(MathUtil.ellipsoidVolume(5, 7, 12)));
  }

  public void testEllipseArea() throws Exception {
    assertEquals(getBigDecimal("37.69"), getBigDecimal(MathUtil.ellipseArea(3, 4)));
    assertEquals(getBigDecimal("37.69"), getBigDecimal(MathUtil.ellipseArea(4, 3)));
    assertEquals(getBigDecimal("150.79"), getBigDecimal(MathUtil.ellipseArea(4, 12)));
  }

  public void testFactorial() throws Exception {
    assertEquals(1, MathUtil.factorial(0));
    assertEquals(1, MathUtil.factorial(1));
    assertEquals(2, MathUtil.factorial(2));
    assertEquals(6, MathUtil.factorial(3));
    assertEquals(24, MathUtil.factorial(4));
    assertEquals(120, MathUtil.factorial(5));
    assertEquals(720, MathUtil.factorial(6));
    assertEquals(5040, MathUtil.factorial(7));
    assertEquals(40320, MathUtil.factorial(8));
    assertEquals(362880, MathUtil.factorial(9));
  }

  public void testFactorialOfNegativeNumber() throws Exception {
    try {
      MathUtil.factorial(-4);
      fail("Computing the factorial of a negative number should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Cannot compute the factorial of a negative number!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Computing the factorial of a negative number threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testFibonacciNumber() throws Exception {
    assertEquals(0, MathUtil.fibonacciNumber(0));
    assertEquals(1, MathUtil.fibonacciNumber(1));
    assertEquals(1, MathUtil.fibonacciNumber(2));
    assertEquals(2, MathUtil.fibonacciNumber(3));
    assertEquals(3, MathUtil.fibonacciNumber(4));
    assertEquals(5, MathUtil.fibonacciNumber(5));
    assertEquals(8, MathUtil.fibonacciNumber(6));
    assertEquals(13, MathUtil.fibonacciNumber(7));
    assertEquals(21, MathUtil.fibonacciNumber(8));
    assertEquals(34, MathUtil.fibonacciNumber(9));
    assertEquals(55, MathUtil.fibonacciNumber(10));
    assertEquals(89, MathUtil.fibonacciNumber(11));
    assertEquals(144, MathUtil.fibonacciNumber(12));
    assertEquals(233, MathUtil.fibonacciNumber(13));
    assertEquals(377, MathUtil.fibonacciNumber(14));
    assertEquals(610, MathUtil.fibonacciNumber(15));
    assertEquals(987, MathUtil.fibonacciNumber(16));
    assertEquals(1597, MathUtil.fibonacciNumber(17));
    assertEquals(2584, MathUtil.fibonacciNumber(18));
    assertEquals(4181, MathUtil.fibonacciNumber(19));
    assertEquals(6765, MathUtil.fibonacciNumber(20));
  }

  public void testFibonacciNumberWithNegativeValue() throws Exception {
    try {
      MathUtil.fibonacciNumber(-1);
      fail("Calling fibonacciNumber with a negative value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The positional value n (-1) must be greater than equal to zero!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling fibonacciNumber with a negative value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testFibonacciSequence() throws Exception {
    assertEquals(CollectionUtil.getList(0), MathUtil.fibonacciSequence(0));
    assertEquals(CollectionUtil.getList(0, 1), MathUtil.fibonacciSequence(1));
    assertEquals(CollectionUtil.getList(0, 1, 1), MathUtil.fibonacciSequence(2));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2), MathUtil.fibonacciSequence(3));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2, 3), MathUtil.fibonacciSequence(4));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2, 3, 5), MathUtil.fibonacciSequence(5));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2, 3, 5, 8), MathUtil.fibonacciSequence(6));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2, 3, 5, 8, 13), MathUtil.fibonacciSequence(7));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2, 3, 5, 8, 13, 21), MathUtil.fibonacciSequence(8));
    assertEquals(CollectionUtil.getList(0, 1, 1, 2, 3, 5, 8, 13, 21, 34), MathUtil.fibonacciSequence(9));
  }

  public void testFibonacciSequenceWithNegativeValue() throws Exception {
    try {
      MathUtil.fibonacciSequence(-1);
      fail("Calling fibonacciSequence with a negative value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The positional value n (-1) must be greater than equal to zero!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling fibonacciSequence with a negative value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testLoanBalance() throws Exception {
    assertEquals(getBigDecimal("100300.45"), MathUtil.loanBalance(new BigDecimal("150000.00"), new BigDecimal("4.875"),
      72, new BigDecimal("1205.00")).setScale(2, RoundingMode.HALF_EVEN));
  }

  public void testMax() throws Exception {
    assertTrue(Double.isNaN(MathUtil.max(null)));
    assertTrue(Double.isNaN(MathUtil.max(new double[0])));
    assertEquals(2.0, MathUtil.max(0.0, 1.0, 2.0));
    assertEquals(0.0, MathUtil.max(-2.0, -1.0, 0.0));
    assertEquals((double) Long.MAX_VALUE, MathUtil.max(Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE));
    assertEquals((double) Byte.MIN_VALUE, MathUtil.max(Byte.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE));
    assertEquals(Double.MAX_VALUE, MathUtil.max(Float.MAX_VALUE, Double.MAX_VALUE));
    //assertEquals(Float.MIN_VALUE, MathUtil.max(Float.MIN_VALUE, Double.MIN_VALUE));
  }

  public void testMin() throws Exception {
    assertTrue(Double.isNaN(MathUtil.min(null)));
    assertTrue(Double.isNaN(MathUtil.min(new double[0])));
    assertEquals(0.0, MathUtil.min(0.0, 1.0, 2.0));
    assertEquals(-2.0, MathUtil.min(-2.0, -1.0, 0.0));
    assertEquals((double) Long.MIN_VALUE, MathUtil.min(Byte.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE));
    assertEquals((double) Byte.MAX_VALUE, MathUtil.min(Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE));
    assertEquals(Double.MIN_VALUE, MathUtil.min(Float.MIN_VALUE, Double.MIN_VALUE));
    //assertEquals(Float.MAX_VALUE, MathUtil.min(Float.MAX_VALUE, Double.MAX_VALUE));
  }

  public void testMultiply() throws Exception {
    assertEquals(1, MathUtil.multiply(null));
    assertEquals(1, MathUtil.multiply(new int[0]));
    assertEquals(0, MathUtil.multiply(0));
    assertEquals(0, MathUtil.multiply(0, 0));
    assertEquals(0, MathUtil.multiply(0, 0, 0));
    assertEquals(0, MathUtil.multiply(1, 0));
    assertEquals(1, MathUtil.multiply(1, 1));
    assertEquals(2, MathUtil.multiply(1, 2));
    assertEquals(4, MathUtil.multiply(2, 2));
    assertEquals(6, MathUtil.multiply(1, 2, 3));
    assertEquals(24, MathUtil.multiply(1, 2, 3, 4));
    assertEquals(120, MathUtil.multiply(1, 2, 3, 4, 5));
    assertEquals(720, MathUtil.multiply(1, 2, 3, 4, 5, 6));
    assertEquals(5040, MathUtil.multiply(1, 2, 3, 4, 5, 6, 7));
    assertEquals(40320, MathUtil.multiply(1, 2, 3, 4, 5, 6, 7, 8));
    assertEquals(362880, MathUtil.multiply(1, 2, 3, 4, 5, 6, 7, 8, 9));
    assertEquals(0, MathUtil.multiply(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));
  }

  public void testPyramidVolume() throws Exception {
    assertEquals(4.0, MathUtil.pyramidVolume(3, 4));
    assertEquals(5.0, MathUtil.pyramidVolume(3, 5));
    assertEquals(8.0, MathUtil.pyramidVolume(4, 6));
    assertEquals(32.0, MathUtil.pyramidVolume(8, 12));
  }

  public void testPythagoreanTheorem() throws Exception {
    assertEquals(5.0, MathUtil.pythagoreanTheorem(3, 4));
  }

  public void testRectangleArea() throws Exception {
    assertEquals(0.0, MathUtil.rectangleArea(0, 0));
    assertEquals(0.0, MathUtil.rectangleArea(0, 1));
    assertEquals(1.0, MathUtil.rectangleArea(1, 1));
    assertEquals(2.0, MathUtil.rectangleArea(1, 2));
    assertEquals(4.0, MathUtil.rectangleArea(2, 2));
    assertEquals(8.0, MathUtil.rectangleArea(2, 4));
    assertEquals(10.0, MathUtil.rectangleArea(2, 5));
  }

  public void testRectangularPrismSurfaceArea() throws Exception {
    assertEquals(54.0, MathUtil.rectangularPrismSurfaceArea(3.0, 3.0, 3.0));
    assertEquals(94.0, MathUtil.rectangularPrismSurfaceArea(3.0, 4.0, 5.0));
    assertEquals(112.0, MathUtil.rectangularPrismSurfaceArea(2.0, 4.0, 8.0));
  }

  public void testRectangularPrismVolume() throws Exception {
    assertEquals(60.0, MathUtil.rectangularPrismVolume(3, 4, 5));
    assertEquals(64.0, MathUtil.rectangularPrismVolume(2, 4, 8));
    assertEquals(216.0, MathUtil.rectangularPrismVolume(3, 6, 12));
    assertEquals(1000000.0, MathUtil.rectangularPrismVolume(10, 100, 1000));
  }

  public void testSphereSurfaceArea() throws Exception {
    assertEquals(getBigDecimal("113.09"), getBigDecimal(MathUtil.sphereSurfaceArea(3.0)));
    assertEquals(getBigDecimal("201.06"), getBigDecimal(MathUtil.sphereSurfaceArea(4.0)));
    assertEquals(getBigDecimal("314.15"), getBigDecimal(MathUtil.sphereSurfaceArea(5.0)));
  }

  public void testSphereVolume() throws Exception {
    assertEquals(getBigDecimal("113.09"), getBigDecimal(MathUtil.sphereVolume(3)));
    assertEquals(getBigDecimal("268.08"), getBigDecimal(MathUtil.sphereVolume(4)));
    assertEquals(getBigDecimal("523.59"), getBigDecimal(MathUtil.sphereVolume(5)));
    assertEquals(getBigDecimal("4188.79"), getBigDecimal(MathUtil.sphereVolume(10)));
  }

  public void testSquareArea() throws Exception {
    assertEquals(0.0, MathUtil.squareArea(0));
    assertEquals(1.0, MathUtil.squareArea(1));
    assertEquals(4.0, MathUtil.squareArea(2));
    assertEquals(9.0, MathUtil.squareArea(3));
    assertEquals(16.0, MathUtil.squareArea(4));
    assertEquals(25.0, MathUtil.squareArea(5));
    assertEquals(36.0, MathUtil.squareArea(6));
    assertEquals(49.0, MathUtil.squareArea(7));
    assertEquals(64.0, MathUtil.squareArea(8));
    assertEquals(81.0, MathUtil.squareArea(9));
    assertEquals(100.0, MathUtil.squareArea(10));
  }

  public void testSum() throws Exception {
    assertEquals(0, MathUtil.sum(null));
    assertEquals(0, MathUtil.sum(new int[0]));
    assertEquals(0, MathUtil.sum(0));
    assertEquals(0, MathUtil.sum(0, 0));
    assertEquals(0, MathUtil.sum(0, 0, 0));
    assertEquals(1, MathUtil.sum(0, 1));
    assertEquals(2, MathUtil.sum(1, 1));
    assertEquals(3, MathUtil.sum(1, 1, 1));
    assertEquals(4, MathUtil.sum(2, 2));
    assertEquals(14, MathUtil.sum(2, 4, 8));
    assertEquals(3, MathUtil.sum(0, 1, 2));
    assertEquals(6, MathUtil.sum(0, 1, 2, 3));
    assertEquals(10, MathUtil.sum(0, 1, 2, 3, 4));
    assertEquals(15, MathUtil.sum(0, 1, 2, 3, 4, 5));
    assertEquals(21, MathUtil.sum(0, 1, 2, 3, 4, 5, 6));
    assertEquals(28, MathUtil.sum(0, 1, 2, 3, 4, 5, 6, 7));
    assertEquals(36, MathUtil.sum(0, 1, 2, 3, 4, 5, 6, 7, 8));
    assertEquals(45, MathUtil.sum(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
  }

  public void testTriangleArea() throws Exception {
    assertEquals(6.0, MathUtil.triangleArea(3, 4));
    assertEquals(6.0, MathUtil.triangleArea(2, 6));
    assertEquals(32.0, MathUtil.triangleArea(4, 16));
  }

}
