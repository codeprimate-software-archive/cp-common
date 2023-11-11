/*
 * NumberUtilTest.java (c) 13 September 2003
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.29
 * @see com.cp.common.lang.NumberUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NumberUtilTest extends TestCase {

  public NumberUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NumberUtilTest.class);
    //suite.addTest(new NumberUtilTest("testByteValue"));
    return suite;
  }

  public void testByteValue() throws Exception {
    assertEquals(0, NumberUtil.byteValue(null));
    assertEquals(2, NumberUtil.byteValue(new Byte((byte) 2)));
    assertEquals(0, NumberUtil.byteValue(new Byte((byte) 0)));
    assertEquals(-2, NumberUtil.byteValue(new Byte((byte) -2)));
  }

  public void testShortValue() throws Exception {
    assertEquals(0, NumberUtil.shortValue(null));
    assertEquals(2, NumberUtil.shortValue(new Short((short) 2)));
    assertEquals(0, NumberUtil.shortValue(new Short((short) 0)));
    assertEquals(-2, NumberUtil.shortValue(new Short((short) -2)));
  }

  public void testIntValue() throws Exception {
    assertEquals(0, NumberUtil.intValue(null));
    assertEquals(2, NumberUtil.intValue(new Integer(2)));
    assertEquals(0, NumberUtil.intValue(new Integer(0)));
    assertEquals(-2, NumberUtil.intValue(new Integer(-2)));
  }

  public void testLongValue() throws Exception {
    assertEquals(0, NumberUtil.longValue(null));
    assertEquals(2, NumberUtil.longValue(new Long(2l)));
    assertEquals(0, NumberUtil.longValue(new Long(0l)));
    assertEquals(-2, NumberUtil.longValue(new Long(-2l)));
  }

  public void testFloatValue() throws Exception {
    assertTrue(0.0f == NumberUtil.floatValue(null));
    assertTrue(2.5f == NumberUtil.floatValue(new Float(2.5f)));
    assertTrue(0.0f == NumberUtil.floatValue(new Float(0.0f)));
    assertTrue(-2.8f == NumberUtil.floatValue(new Float(-2.8f)));
  }

  public void testDoubleValue() throws Exception {
    assertTrue(0.0 == NumberUtil.doubleValue(null));
    assertTrue(2.5 == NumberUtil.doubleValue(new Double(2.5d)));
    assertTrue(0.0 == NumberUtil.doubleValue(new Double(0.0d)));
    assertTrue(-2.8 == NumberUtil.doubleValue(new Double(-2.8d)));
  }

  public void testDetermineRadix() throws Exception {
    assertEquals(-1, NumberUtil.determineRadix(null));
    assertEquals(-1, NumberUtil.determineRadix(""));
    assertEquals(-1, NumberUtil.determineRadix(" "));
    assertEquals(2, NumberUtil.determineRadix("1010101"));
    assertEquals(8, NumberUtil.determineRadix("077"));
    assertEquals(10, NumberUtil.determineRadix("9"));
    assertEquals(16, NumberUtil.determineRadix("0xa1df"));
  }

  public void testParseByte() throws Exception {
    assertEquals(0, NumberUtil.parseByte(null));
    assertEquals(0, NumberUtil.parseByte(""));
    assertEquals(0, NumberUtil.parseByte(" "));
    assertEquals(2, NumberUtil.parseByte("02"));
    assertEquals(2, NumberUtil.parseByte("2"));
    assertEquals(2, NumberUtil.parseByte("2", 16));
    assertEquals(63, NumberUtil.parseByte("077"));
    assertEquals(102, NumberUtil.parseByte("102"));
    assertEquals(93, NumberUtil.parseByte("5d", 16));
    assertEquals(55, NumberUtil.parseByte(" 55 "));
  }

  public void testParseByteWithInvalidValue() throws Exception {
    try {
      NumberUtil.parseByte("lOlO");
      fail("Calling parseByte with value (lOlO) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling parseByte with value (lOlO) threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testParseShort() throws Exception {
    assertEquals(0, NumberUtil.parseShort(null));
    assertEquals(0, NumberUtil.parseShort(""));
    assertEquals(0, NumberUtil.parseShort(" "));
    assertEquals(2, NumberUtil.parseShort("02"));
    assertEquals(2, NumberUtil.parseShort("2"));
    assertEquals(2, NumberUtil.parseShort("2", 16));
    assertEquals(493, NumberUtil.parseShort("0755"));
    assertEquals(512, NumberUtil.parseShort("512"));
    assertEquals(23887, NumberUtil.parseShort("5d4f", 16));
    assertEquals(55, NumberUtil.parseShort(" 55 "));
  }

  public void testParseShortWithInvalidValue() throws Exception {
    try {
      NumberUtil.parseShort(" 5 5 ");
      fail("Calling parseShort with value ( 5 5 ) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling parseShort with value ( 5 5 ) threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testParseInt() throws Exception {
    assertEquals(0, NumberUtil.parseInt(null));
    assertEquals(0, NumberUtil.parseInt(""));
    assertEquals(0, NumberUtil.parseInt(" "));
    assertEquals(2, NumberUtil.parseInt("02"));
    assertEquals(2, NumberUtil.parseInt("2"));
    assertEquals(2, NumberUtil.parseInt("2", 16));
    assertEquals(29303, NumberUtil.parseInt("071167"));
    assertEquals(512500, NumberUtil.parseInt("512500"));
    assertEquals(6138396, NumberUtil.parseInt("5daa1c", 16));
    assertEquals(55, NumberUtil.parseInt(" 55 "));
  }

  public void testParseIntWithInvalidValue() throws Exception {
    try {
      NumberUtil.parseInt("five");
      fail("Calling parseInt with value (five) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling parseInt with value (five) threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testParseLong() throws Exception {
    assertEquals(0l, NumberUtil.parseLong(null));
    assertEquals(0l, NumberUtil.parseLong(""));
    assertEquals(0l, NumberUtil.parseLong(" "));
    assertEquals(2l, NumberUtil.parseLong("02"));
    assertEquals(2l, NumberUtil.parseLong("2"));
    assertEquals(2l, NumberUtil.parseLong("2", 16));
    assertEquals(241469l, NumberUtil.parseLong("0727475"));
    assertEquals(101248516l, NumberUtil.parseLong("101248516"));
    assertEquals(1568114625l, NumberUtil.parseLong("5d7787c1", 16));
    assertEquals(55l, NumberUtil.parseLong(" 55 "));
  }

  public void testParseLongWithInvalidValue() throws Exception {
    try {
      NumberUtil.parseLong("9x10**6");
      fail("Calling parseLong with value (9x10**6) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling parseLong with value (9x10**6) threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testParseFloat() throws Exception {
    assertEquals(0.0f, NumberUtil.parseFloat(null));
    assertEquals(0.0f, NumberUtil.parseFloat(""));
    assertEquals(0.0f, NumberUtil.parseFloat(" "));
    assertEquals(2.0f, NumberUtil.parseFloat("2"));
    assertEquals(1.01f, NumberUtil.parseFloat("1.01"));
    assertEquals(55.0f, NumberUtil.parseFloat(" 55 "));
  }

  public void testParseFloatWithInvalidValue() throws Exception {
    try {
      NumberUtil.parseFloat("root.beer");
      fail("Calling parseFloat with value (root.beer) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling parseFloat with value (root.beer) threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testParseDouble() throws Exception {
    assertEquals(0.0d, NumberUtil.parseDouble(null));
    assertEquals(0.0d, NumberUtil.parseDouble(""));
    assertEquals(0.0d, NumberUtil.parseDouble(" "));
    assertEquals(2.0d, NumberUtil.parseDouble("2"));
    assertEquals(Math.PI, NumberUtil.parseDouble(String.valueOf(Math.PI)));
    assertEquals(55.0d, NumberUtil.parseDouble(" 55 "));
  }

  public void testParseDoubleWithInvalidValue() throws Exception {
    try {
      NumberUtil.parseDouble("1.23.456");
      fail("Calling parseDouble with value (1.23.456) should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling parseDouble with value (1.23.456) threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testDecodeBigInteger() throws Exception {
    assertEquals(new BigInteger("0"), NumberUtil.decodeBigInteger("0"));
    assertEquals(new BigInteger("2"), NumberUtil.decodeBigInteger("2"));
    assertEquals(new BigInteger("10"), NumberUtil.decodeBigInteger("10"));
    assertEquals(new BigInteger("63"), NumberUtil.decodeBigInteger("077"));
    assertEquals(new BigInteger("-63"), NumberUtil.decodeBigInteger("-077"));
    assertEquals(new BigInteger("4660"), NumberUtil.decodeBigInteger("0x1234"));
    assertEquals(new BigInteger("45390"), NumberUtil.decodeBigInteger("#b14e"));
    assertEquals(new BigInteger("-11259375"), NumberUtil.decodeBigInteger("-0Xabcdef"));
  }

  public void testDecodeBigIntegerWithEmptyValue() throws Exception {
    try {
      NumberUtil.decodeBigInteger(" ");
      fail("Calling decodeBigInteger with an empty value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("A number must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling decodeBigInteger with an empty value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testDecodeBigIntegerWithNullValue() throws Exception {
    try {
      NumberUtil.decodeBigInteger(null);
      fail("Calling decodeBigInteger with a null value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("A number must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling decodeBigInteger with a null value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testValueOfAsByte() throws Exception {
    assertNull(NumberUtil.valueOf(null, Byte.class));
    assertNull(NumberUtil.valueOf("", Byte.class));
    assertNull(NumberUtil.valueOf(" ", Byte.class));
    assertEquals(new Byte((byte) 2), NumberUtil.valueOf("2", Byte.class));
    assertEquals(new Byte((byte) -10), NumberUtil.valueOf("-10", Byte.class));
    assertEquals(new Byte((byte) 63), NumberUtil.valueOf("077", Byte.class));
    assertEquals(new Byte((byte) -51), NumberUtil.valueOf("-063", Byte.class));
    assertEquals(new Byte((byte) 122), NumberUtil.valueOf("0x7a", Byte.class));
    assertEquals(new Byte((byte) -85), NumberUtil.valueOf("-#55", Byte.class));
  }

  public void testValueOfAsShort() throws Exception {
    assertNull(NumberUtil.valueOf(null, Short.class));
    assertNull(NumberUtil.valueOf("", Short.class));
    assertNull(NumberUtil.valueOf(" ", Short.class));
    assertEquals(new Short((short) 2), NumberUtil.valueOf("2", Short.class));
    assertEquals(new Short((short) -10), NumberUtil.valueOf("-10", Short.class));
    assertEquals(new Short((short) 490), NumberUtil.valueOf("0752", Short.class));
    assertEquals(new Short((short) -304), NumberUtil.valueOf("-0460", Short.class));
    assertEquals(new Short((short) 31420), NumberUtil.valueOf("0x7abc", Short.class));
    assertEquals(new Short((short) -85), NumberUtil.valueOf("-#55", Short.class));
  }

  public void testValueOfAsInteger() throws Exception {
    assertNull(NumberUtil.valueOf(null, Integer.class));
    assertNull(NumberUtil.valueOf("", Integer.class));
    assertNull(NumberUtil.valueOf(" ", Integer.class));
    assertEquals(new Integer(2), NumberUtil.valueOf("2", Integer.class));
    assertEquals(new Integer(-10), NumberUtil.valueOf("-10", Integer.class));
    assertEquals(new Integer(247296), NumberUtil.valueOf("0743000", Integer.class));
    assertEquals(new Integer(-85686), NumberUtil.valueOf("-0247266", Integer.class));
    assertEquals(new Integer(831463), NumberUtil.valueOf("0xcafe7", Integer.class));
    assertEquals(new Integer(-85), NumberUtil.valueOf("-#55", Integer.class));
  }

  public void testValueOfAsLong() throws Exception {
    assertNull(NumberUtil.valueOf(null, Long.class));
    assertNull(NumberUtil.valueOf("", Long.class));
    assertNull(NumberUtil.valueOf(" ", Long.class));
    assertEquals(new Long(2l), NumberUtil.valueOf("2", Long.class));
    assertEquals(new Long(-10l), NumberUtil.valueOf("-10", Long.class));
    assertEquals(new Long(230040l), NumberUtil.valueOf("0701230", Long.class));
    assertEquals(new Long(-77856l), NumberUtil.valueOf("-0230040", Long.class));
    assertEquals(new Long(3405691582l), NumberUtil.valueOf("0xCAFEBABE", Long.class));
    assertEquals(new Long(-85l), NumberUtil.valueOf("-#55", Long.class));
  }

  public void testValueOfAsFloat() throws Exception {
    assertNull(NumberUtil.valueOf(null, Float.class));
    assertNull(NumberUtil.valueOf("", Float.class));
    assertNull(NumberUtil.valueOf(" ", Float.class));
    assertEquals(new Float(0.0f), NumberUtil.valueOf("0", Float.class));
    assertEquals(new Float(-1.01f), NumberUtil.valueOf("-1.01", Float.class));
    assertEquals(new Float(Math.E), NumberUtil.valueOf(String.valueOf(Math.E), Float.class));
  }

  public void testValueOfAsDouble() throws Exception {
    assertNull(NumberUtil.valueOf(null, Double.class));
    assertNull(NumberUtil.valueOf("", Double.class));
    assertNull(NumberUtil.valueOf(" ", Double.class));
    assertEquals(new Double(0.0d), NumberUtil.valueOf("0", Double.class));
    assertEquals(new Double(Math.PI), NumberUtil.valueOf(String.valueOf(Math.PI), Double.class));
  }

  public void testValueOfAsBigDecimal() throws Exception {
    assertNull(NumberUtil.valueOf(null, BigDecimal.class));
    assertNull(NumberUtil.valueOf("", BigDecimal.class));
    assertNull(NumberUtil.valueOf(" ", BigDecimal.class));
    assertEquals(new BigDecimal("0"), NumberUtil.valueOf("0", BigDecimal.class));
    assertEquals(new BigDecimal("2.0"), NumberUtil.valueOf("2.0", BigDecimal.class));
    assertEquals(new BigDecimal("-9876543210.0123456789"), NumberUtil.valueOf("-9876543210.0123456789", BigDecimal.class));
    assertEquals(new BigDecimal(String.valueOf(Double.MAX_VALUE)), NumberUtil.valueOf(String.valueOf(Double.MAX_VALUE), BigDecimal.class));
    assertEquals(new BigDecimal(String.valueOf(Double.MIN_VALUE)), NumberUtil.valueOf(String.valueOf(Double.MIN_VALUE), BigDecimal.class));
  }

  public void testValueOfBigInteger() throws Exception {
    assertNull(NumberUtil.valueOf(null, BigInteger.class));
    assertNull(NumberUtil.valueOf("", BigInteger.class));
    assertNull(NumberUtil.valueOf(" ", BigInteger.class));
    assertEquals(new BigInteger("0"), NumberUtil.valueOf("0", BigInteger.class));
    assertEquals(new BigInteger("2"), NumberUtil.valueOf("2", BigInteger.class));
    assertEquals(new BigInteger("99876543210"), NumberUtil.valueOf("99876543210", BigInteger.class));
    assertEquals(new BigInteger(String.valueOf(Integer.MAX_VALUE)), NumberUtil.valueOf(String.valueOf(Integer.MAX_VALUE), BigInteger.class));
    assertEquals(new BigInteger(String.valueOf(Integer.MIN_VALUE)), NumberUtil.valueOf(String.valueOf(Integer.MIN_VALUE), BigInteger.class));
  }

  public void testValueOfWithInvalidValue() throws Exception {
    try {
      NumberUtil.valueOf(String.valueOf(Math.PI), Integer.class);
      fail("Calling valueOf with argument PI and the Integer class should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling valueOf with argument PI and the Integer class threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testValueOfWithNullNumberClass() throws Exception {
    try {
      NumberUtil.valueOf("0", null);
      fail("Calling value of with a null Number class specified should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The type of number cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling value of with a null Number class specified threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testValueOfWithOverflow() throws Exception {
    try {
      NumberUtil.valueOf("0xCAFEBABE", Integer.class);
      fail("Calling valueOf with value (0xCAFEBABE) and the Integer class specified should have thrown a NumberFormatException!");
    }
    catch (NumberFormatException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling valueOf with value (0xCAFEBABE) and the Integer class specified threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testIsBinaryNumber() throws Exception {
    assertTrue(NumberUtil.isBinaryNumber("0"));
    assertTrue(NumberUtil.isBinaryNumber("0000"));
    assertTrue(NumberUtil.isBinaryNumber("0001"));
    assertTrue(NumberUtil.isBinaryNumber("0101"));
    assertTrue(NumberUtil.isBinaryNumber("1101"));
    assertTrue(NumberUtil.isBinaryNumber("1111"));
    assertFalse(NumberUtil.isBinaryNumber(""));
    assertFalse(NumberUtil.isBinaryNumber(" "));
    assertFalse(NumberUtil.isBinaryNumber("IOIO"));
    assertFalse(NumberUtil.isBinaryNumber("-1010"));
    assertFalse(NumberUtil.isBinaryNumber("2"));
    assertFalse(NumberUtil.isBinaryNumber(String.valueOf(Math.PI)));
    assertFalse(NumberUtil.isBinaryNumber("0x1010"));
    assertFalse(NumberUtil.isBinaryNumber("0X10101"));
    assertFalse(NumberUtil.isBinaryNumber("#11011"));
    assertFalse(NumberUtil.isBinaryNumber("1234"));
    assertFalse(NumberUtil.isBinaryNumber("01234"));
  }

  public void testIsDecimalNumber() throws Exception {
    assertTrue(NumberUtil.isDecimalNumber("0"));
    assertTrue(NumberUtil.isDecimalNumber("-0"));
    assertTrue(NumberUtil.isDecimalNumber("0.0"));
    assertTrue(NumberUtil.isDecimalNumber("-0.0"));
    assertTrue(NumberUtil.isDecimalNumber("2"));
    assertTrue(NumberUtil.isDecimalNumber("10"));
    assertTrue(NumberUtil.isDecimalNumber("-10"));
    assertTrue(NumberUtil.isDecimalNumber("1101"));
    assertTrue(NumberUtil.isDecimalNumber("010"));
    assertTrue(NumberUtil.isDecimalNumber("077"));
    assertTrue(NumberUtil.isDecimalNumber("8092"));
    assertTrue(NumberUtil.isDecimalNumber("123456789"));
    assertTrue(NumberUtil.isDecimalNumber(String.valueOf(Integer.MAX_VALUE)));
    assertTrue(NumberUtil.isDecimalNumber(String.valueOf(Integer.MIN_VALUE)));
    assertTrue(NumberUtil.isDecimalNumber(String.valueOf(Math.PI)));
    assertTrue(NumberUtil.isDecimalNumber("-1.234567890"));
    assertTrue(NumberUtil.isDecimalNumber("0.123456789"));
    assertFalse(NumberUtil.isDecimalNumber(""));
    assertFalse(NumberUtil.isDecimalNumber(" "));
    assertFalse(NumberUtil.isDecimalNumber("O"));
    assertFalse(NumberUtil.isDecimalNumber("-O"));
    assertFalse(NumberUtil.isDecimalNumber("-0.O"));
    assertFalse(NumberUtil.isDecimalNumber("0.123.456"));
    assertFalse(NumberUtil.isDecimalNumber("lO"));
    assertFalse(NumberUtil.isDecimalNumber("0x1024"));
    assertFalse(NumberUtil.isDecimalNumber("0X1F2AD0"));
    assertFalse(NumberUtil.isDecimalNumber("#1234"));
    assertFalse(NumberUtil.isDecimalNumber("abc"));
  }

  public void testIsEqualTo() throws Exception {
    assertTrue(NumberUtil.isEqualTo(0, 0));
    assertTrue(NumberUtil.isEqualTo(0.0, 0.0));
    assertTrue(NumberUtil.isEqualTo(1.0, 1.0));
    assertTrue(NumberUtil.isEqualTo(-1, -1));
    assertFalse(NumberUtil.isEqualTo(-1, 1));
    assertFalse(NumberUtil.isEqualTo(Integer.MIN_VALUE, Integer.MAX_VALUE));
    assertFalse(NumberUtil.isEqualTo(Double.MIN_VALUE, Double.MAX_VALUE));
  }

  public void testIsEven() throws Exception {
    assertFalse(NumberUtil.isEven(-9));
    assertTrue(NumberUtil.isEven(-8));
    assertFalse(NumberUtil.isEven(-7));
    assertTrue(NumberUtil.isEven(-6));
    assertFalse(NumberUtil.isEven(-5));
    assertTrue(NumberUtil.isEven(-4));
    assertFalse(NumberUtil.isEven(-3));
    assertTrue(NumberUtil.isEven(-2));
    assertFalse(NumberUtil.isEven(-1));
    assertTrue(NumberUtil.isEven(0));
    assertFalse(NumberUtil.isEven(1));
    assertTrue(NumberUtil.isEven(2));
    assertFalse(NumberUtil.isEven(3));
    assertTrue(NumberUtil.isEven(4));
    assertFalse(NumberUtil.isEven(5));
    assertTrue(NumberUtil.isEven(6));
    assertFalse(NumberUtil.isEven(7));
    assertTrue(NumberUtil.isEven(8));
    assertFalse(NumberUtil.isEven(9));
    assertTrue(NumberUtil.isEven(10));
    assertTrue(NumberUtil.isEven(100));
    assertTrue(NumberUtil.isEven(1000));
    assertTrue(NumberUtil.isEven(1024));
    assertTrue(NumberUtil.isEven(2048));
    assertTrue(NumberUtil.isEven(1212));
    assertFalse(NumberUtil.isEven(11));
    assertFalse(NumberUtil.isEven(55));
    assertFalse(NumberUtil.isEven(127));
    assertFalse(NumberUtil.isEven(255));
  }

  public void testIsGreaterThan() throws Exception {
    assertTrue(NumberUtil.isGreaterThan(1.0, 3.0));
    assertTrue(NumberUtil.isGreaterThan(2, 3));
    assertFalse(NumberUtil.isGreaterThan(3.0, 3.0));
    assertFalse(NumberUtil.isGreaterThan(4, 3));
    assertFalse(NumberUtil.isGreaterThan(5.0, 3.0));
  }

  public void testIsGreaterThanAndLessThan() throws Exception {
    assertFalse(NumberUtil.isGreaterThanAndLessThan(2.0, 4.0, 1.0));
    assertFalse(NumberUtil.isGreaterThanAndLessThan(2, 4, 2));
    assertTrue(NumberUtil.isGreaterThanAndLessThan(2.0, 4.0, 3.0));
    assertFalse(NumberUtil.isGreaterThanAndLessThan(2, 4, 4));
    assertFalse(NumberUtil.isGreaterThanAndLessThan(2.0, 4.0, 5.0));
  }

  public void testIsGreaterThanAndLessThanEqualTo() throws Exception {
    assertFalse(NumberUtil.isGreaterThanAndLessThanEqualTo(2.0, 4.0, 1.0));
    assertFalse(NumberUtil.isGreaterThanAndLessThanEqualTo(2, 4, 2));
    assertTrue(NumberUtil.isGreaterThanAndLessThanEqualTo(2.0, 4.0, 3.0));
    assertTrue(NumberUtil.isGreaterThanAndLessThanEqualTo(2, 4, 4));
    assertFalse(NumberUtil.isGreaterThanAndLessThanEqualTo(2.0, 4.0, 5.0));
  }

  public void testIsGreaterThanEqualTo() throws Exception {
    assertTrue(NumberUtil.isGreaterThanEqualTo(1.0, 3.0));
    assertTrue(NumberUtil.isGreaterThanEqualTo(2, 3));
    assertTrue(NumberUtil.isGreaterThanEqualTo(3.0, 3.0));
    assertFalse(NumberUtil.isGreaterThanEqualTo(4, 3));
    assertFalse(NumberUtil.isGreaterThanEqualTo(5.0, 3.0));
  }

  public void testIsGreaterThanEqualToAndLessThan() throws Exception {
    assertFalse(NumberUtil.isGreaterThanEqualToAndLessThan(2.0, 4.0, 1.0));
    assertTrue(NumberUtil.isGreaterThanEqualToAndLessThan(2, 4, 2));
    assertTrue(NumberUtil.isGreaterThanEqualToAndLessThan(2.0, 4.0, 3.0));
    assertFalse(NumberUtil.isGreaterThanEqualToAndLessThan(2, 4, 4));
    assertFalse(NumberUtil.isGreaterThanEqualToAndLessThan(2.0, 4.0, 5.0));
  }

  public void testIsGreaterThanEqualToAndLessThanEqualTo() throws Exception {
    assertFalse(NumberUtil.isGreaterThanEqualToAndLessThanEqualTo(2.0, 4.0, 1.0));
    assertTrue(NumberUtil.isGreaterThanEqualToAndLessThanEqualTo(2, 4, 2));
    assertTrue(NumberUtil.isGreaterThanEqualToAndLessThanEqualTo(2.0, 4.0, 3.0));
    assertTrue(NumberUtil.isGreaterThanEqualToAndLessThanEqualTo(2, 4, 4));
    assertFalse(NumberUtil.isGreaterThanEqualToAndLessThanEqualTo(2.0, 4.0, 5.0));
  }

  public void testIsHexidecimalNumber() throws Exception {
    assertTrue(NumberUtil.isHexidecimalNumber("0x0"));
    assertTrue(NumberUtil.isHexidecimalNumber("0x1"));
    assertTrue(NumberUtil.isHexidecimalNumber("0x01"));
    assertTrue(NumberUtil.isHexidecimalNumber("0x12"));
    assertTrue(NumberUtil.isHexidecimalNumber("0x1234"));
    assertTrue(NumberUtil.isHexidecimalNumber("0xaa34"));
    assertTrue(NumberUtil.isHexidecimalNumber("0xFFaa"));
    assertTrue(NumberUtil.isHexidecimalNumber("0x10D2f9"));
    assertTrue(NumberUtil.isHexidecimalNumber("0X112F4a820BaCDf"));
    assertTrue(NumberUtil.isHexidecimalNumber("#ffbb77"));
    assertTrue(NumberUtil.isHexidecimalNumber("-0xaa"));
    assertFalse(NumberUtil.isHexidecimalNumber(""));
    assertFalse(NumberUtil.isHexidecimalNumber(" "));
    assertFalse(NumberUtil.isHexidecimalNumber("Ox1234"));
    assertFalse(NumberUtil.isHexidecimalNumber("0x0X1234"));
    assertFalse(NumberUtil.isHexidecimalNumber("-0xfg"));
    assertFalse(NumberUtil.isHexidecimalNumber("0001"));
    assertFalse(NumberUtil.isHexidecimalNumber("07"));
    assertFalse(NumberUtil.isHexidecimalNumber("2"));
    assertFalse(NumberUtil.isHexidecimalNumber(String.valueOf(Math.PI)));
    assertFalse(NumberUtil.isHexidecimalNumber("0xxx"));
    assertFalse(NumberUtil.isHexidecimalNumber("0xOI"));
    assertFalse(NumberUtil.isHexidecimalNumber("0xZzOO"));
  }

  public void testIsLessThan() throws Exception {
    assertFalse(NumberUtil.isLessThan(1.0, 3.0));
    assertFalse(NumberUtil.isLessThan(2, 3));
    assertFalse(NumberUtil.isLessThan(3.0, 3.0));
    assertTrue(NumberUtil.isLessThan(4, 3));
    assertTrue(NumberUtil.isLessThan(5.0, 3.0));
  }

  public void testIsLessThanOrGreaterThan() throws Exception {
    assertTrue(NumberUtil.isLessThanOrGreaterThan(2.0, 4.0, 1.0));
    assertFalse(NumberUtil.isLessThanOrGreaterThan(2, 4, 2));
    assertFalse(NumberUtil.isLessThanOrGreaterThan(2.0, 4.0, 3.0));
    assertFalse(NumberUtil.isLessThanOrGreaterThan(2, 4, 4));
    assertTrue(NumberUtil.isLessThanOrGreaterThan(2.0, 4.0, 5.0));
  }

  public void testIsLessThanOrGreaterThanEqualTo() throws Exception {
    assertTrue(NumberUtil.isLessThanOrGreaterThanEqualTo(2.0, 4.0, 1.0));
    assertFalse(NumberUtil.isLessThanOrGreaterThanEqualTo(2, 4, 2));
    assertFalse(NumberUtil.isLessThanOrGreaterThanEqualTo(2.0, 4.0, 3.0));
    assertTrue(NumberUtil.isLessThanOrGreaterThanEqualTo(2, 4, 4));
    assertTrue(NumberUtil.isLessThanOrGreaterThanEqualTo(2.0, 4.0, 5.0));
  }

  public void testIsLessThanEqualTo() throws Exception {
    assertFalse(NumberUtil.isLessThanEqualTo(1.0, 3.0));
    assertFalse(NumberUtil.isLessThanEqualTo(2, 3));
    assertTrue(NumberUtil.isLessThanEqualTo(3.0, 3.0));
    assertTrue(NumberUtil.isLessThanEqualTo(4, 3));
    assertTrue(NumberUtil.isLessThanEqualTo(5.0, 3.0));
  }

  public void testIsLessThanEqualToOrGreaterThan() throws Exception {
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThan(2.0, 4.0, 1.0));
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThan(2, 4, 2));
    assertFalse(NumberUtil.isLessThanEqualToOrGreaterThan(2.0, 4.0, 3.0));
    assertFalse(NumberUtil.isLessThanEqualToOrGreaterThan(2, 4, 4));
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThan(2.0, 4.0, 5.0));
  }

  public void testIsLessThanEqualToOrGreaterThanEqualTo() throws Exception {
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThanEqualTo(2.0, 4.0, 1.0));
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThanEqualTo(2, 4, 2));
    assertFalse(NumberUtil.isLessThanEqualToOrGreaterThanEqualTo(2.0, 4.0, 3.0));
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThanEqualTo(2, 4, 4));
    assertTrue(NumberUtil.isLessThanEqualToOrGreaterThanEqualTo(2.0, 4.0, 5.0));
  }

  public void testIsNegative() throws Exception {
    assertTrue(NumberUtil.isNegative(-1));
    assertTrue(NumberUtil.isNegative(-0.5));
    assertFalse(NumberUtil.isNegative(0));
    assertFalse(NumberUtil.isNegative(0.0));
    assertFalse(NumberUtil.isNegative(1));
    assertFalse(NumberUtil.isNegative(0.5));
  }

  public void testIsOctalNumber() throws Exception {
    assertTrue(NumberUtil.isOctalNumber("0"));
    assertTrue(NumberUtil.isOctalNumber("-0"));
    assertTrue(NumberUtil.isOctalNumber("01"));
    assertTrue(NumberUtil.isOctalNumber("-02"));
    assertTrue(NumberUtil.isOctalNumber("07"));
    assertTrue(NumberUtil.isOctalNumber("0123"));
    assertTrue(NumberUtil.isOctalNumber("-0777"));
    assertTrue(NumberUtil.isOctalNumber("07007"));
    assertFalse(NumberUtil.isOctalNumber(""));
    assertFalse(NumberUtil.isOctalNumber(" "));
    assertFalse(NumberUtil.isOctalNumber("002"));
    assertFalse(NumberUtil.isOctalNumber("08"));
    assertFalse(NumberUtil.isOctalNumber("09"));
    assertFalse(NumberUtil.isOctalNumber("018"));
    assertFalse(NumberUtil.isOctalNumber("0779"));
    assertFalse(NumberUtil.isOctalNumber("1101"));
    assertFalse(NumberUtil.isOctalNumber("10"));
    assertFalse(NumberUtil.isOctalNumber(String.valueOf(Math.PI)));
    assertFalse(NumberUtil.isOctalNumber("0x1"));
    assertFalse(NumberUtil.isOctalNumber("0x02"));
    assertFalse(NumberUtil.isOctalNumber("0x77"));
    assertFalse(NumberUtil.isOctalNumber("#2"));
    assertFalse(NumberUtil.isOctalNumber("-#77"));
    assertFalse(NumberUtil.isOctalNumber("O77"));
  }

  public void testIsOdd() throws Exception {
    assertTrue(NumberUtil.isOdd(-9));
    assertFalse(NumberUtil.isOdd(-8));
    assertTrue(NumberUtil.isOdd(-7));
    assertFalse(NumberUtil.isOdd(-6));
    assertTrue(NumberUtil.isOdd(-5));
    assertFalse(NumberUtil.isOdd(-4));
    assertTrue(NumberUtil.isOdd(-3));
    assertFalse(NumberUtil.isOdd(-2));
    assertTrue(NumberUtil.isOdd(-1));
    assertFalse(NumberUtil.isOdd(0));
    assertTrue(NumberUtil.isOdd(1));
    assertFalse(NumberUtil.isOdd(2));
    assertTrue(NumberUtil.isOdd(3));
    assertFalse(NumberUtil.isOdd(4));
    assertTrue(NumberUtil.isOdd(5));
    assertFalse(NumberUtil.isOdd(6));
    assertTrue(NumberUtil.isOdd(7));
    assertFalse(NumberUtil.isOdd(8));
    assertTrue(NumberUtil.isOdd(9));
    assertFalse(NumberUtil.isOdd(10));
    assertTrue(NumberUtil.isOdd(101));
    assertTrue(NumberUtil.isOdd(1011));
    assertTrue(NumberUtil.isOdd(1111));
    assertFalse(NumberUtil.isOdd(16));
    assertFalse(NumberUtil.isOdd(32));
    assertFalse(NumberUtil.isOdd(64));
    assertFalse(NumberUtil.isOdd(128));
    assertFalse(NumberUtil.isOdd(256));
    assertFalse(NumberUtil.isOdd(512));
  }

  public void testIsPositive() throws Exception {
    assertFalse(NumberUtil.isPositive(-1));
    assertFalse(NumberUtil.isPositive(-0.5));
    assertFalse(NumberUtil.isPositive(0));
    assertFalse(NumberUtil.isPositive(0.0));
    assertTrue(NumberUtil.isPositive(1));
    assertTrue(NumberUtil.isPositive(0.5));
  }

  public void testIsPrimeNumber() throws Exception {
    assertTrue(NumberUtil.isPrime(2));
    assertTrue(NumberUtil.isPrime(3));
    assertTrue(NumberUtil.isPrime(5));
    assertTrue(NumberUtil.isPrime(7));
    assertTrue(NumberUtil.isPrime(11));
    assertTrue(NumberUtil.isPrime(13));
    assertTrue(NumberUtil.isPrime(17));
    assertTrue(NumberUtil.isPrime(19));
    assertTrue(NumberUtil.isPrime(23));
    assertTrue(NumberUtil.isPrime(29));
    assertTrue(NumberUtil.isPrime(31));
    assertTrue(NumberUtil.isPrime(37));
    assertTrue(NumberUtil.isPrime(41));
    assertTrue(NumberUtil.isPrime(43));
    assertTrue(NumberUtil.isPrime(47));
    assertTrue(NumberUtil.isPrime(53));
    assertFalse(NumberUtil.isPrime(0));
    assertFalse(NumberUtil.isPrime(1));
    assertFalse(NumberUtil.isPrime(4));
    assertFalse(NumberUtil.isPrime(10));
    assertFalse(NumberUtil.isPrime(15));
    assertFalse(NumberUtil.isPrime(21));
    assertFalse(NumberUtil.isPrime(24));
    assertFalse(NumberUtil.isPrime(27));
    assertFalse(NumberUtil.isPrime(33));
    assertFalse(NumberUtil.isPrime(35));
    assertFalse(NumberUtil.isPrime(42));
    assertFalse(NumberUtil.isPrime(44));
    assertFalse(NumberUtil.isPrime(49));
    assertTrue(NumberUtil.isPrime(4441));
    assertFalse(NumberUtil.isPrime(31591));
  }

  public void testIsWholeNumber() throws Exception {
    assertTrue(NumberUtil.isWhole(0.0));
    assertTrue(NumberUtil.isWhole(1.0));
    assertTrue(NumberUtil.isWhole(2.0));
    assertTrue(NumberUtil.isWhole(-10.0));
    assertFalse(NumberUtil.isWhole(-10.1));
    assertFalse(NumberUtil.isWhole(-1.1));
    assertFalse(NumberUtil.isWhole(Math.PI));
  }

  public void testIsZero() throws Exception {
    assertFalse(NumberUtil.isZero(-1));
    assertFalse(NumberUtil.isZero(-0.5));
    assertTrue(NumberUtil.isZero(0));
    assertTrue(NumberUtil.isZero(0.0));
    assertFalse(NumberUtil.isZero(1));
    assertFalse(NumberUtil.isZero(0.5));
  }

}
