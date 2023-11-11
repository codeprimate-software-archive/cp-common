/*
 * AssertTest.java (c) 14 December 2006
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.10
 * @see com.cp.common.lang.Assert
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import com.cp.common.util.ConfigurationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AssertTest extends TestCase {

  public AssertTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AssertTest.class);
    //suite.addTest(new AssertTest("testName"));
    return suite;
  }

  public void testAssertBlank() throws Exception {
    try {
      Assert.blank(null, "test");
      Assert.blank("", "test");
      Assert.blank(" ", "test");
      Assert.blank("   ", "test");
    }
    catch (Throwable t) {
      fail("Asserting blank String values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertBlankWithNonBlankString() throws Exception {
    try {
      Assert.blank("blank", "The String value is not blank!");
      fail("Calling Assert.blank on a non-blank String should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value is not blank!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling Assert.blank on a non-blank String threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertDigitsOnly() throws Exception {
    try {
      Assert.digitsOnly("0", "test");
      Assert.digitsOnly("1", "test");
      Assert.digitsOnly("2", "test");
      Assert.digitsOnly("01", "test");
      Assert.digitsOnly("012", "test");
      Assert.digitsOnly("1010", "test");
      Assert.digitsOnly("0123456789", "test");
    }
    catch (Throwable t) {
      fail("Asserting String values containing only digits threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertDigitsOnlyWithHexidecimalNumbers() throws Exception {
    try {
      Assert.digitsOnly("0x0123456789abcdef", "The String value contains non-digit characters!");
      fail("Calling Assert.digitsOnly with a hexidecimal String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value contains non-digit characters!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling Assert.digitsOnly with a hexidecimal String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertDigitsOnlyWithLetters() throws Exception {
    try {
      Assert.digitsOnly("OlOlOl", "The String value contains non-digit characters!");
      fail("Calling Assert.digitsOnly with a character String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value contains non-digit characters!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling Assert.digitsOnly with a character String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertDigitsOnlyWithSignedNumber() throws Exception {
    try {
      Assert.digitsOnly("-123", "The String value contains non-digit characters!");
      fail("Calling Assert.digitsOnly with a signed number String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value contains non-digit characters!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling Assert.digitsOnly with a signed number String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyArray() throws Exception {
    try {
      Assert.empty((Object[]) null, "The array must be empty!");
      Assert.empty(new Object[0], "The array must be empty!");
    }
    catch (Throwable t) {
      fail("Asserting empty on a null Object array as well as an Object array of length 0 threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyWithNonEmptyArray() throws Exception {
    try {
      Assert.empty(new Object[] { "test" }, "The Object array must be empty!");
      fail("Asserting empty on a non-empty Object array should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Object array must be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting empty on a non-empty Object array threw an unexpected Throwable (" + t + ")");
    }
  }

  public void testAssertEmptyCollection() throws Exception {
    try {
      Assert.empty((Collection) null, "The Collection must be empty!");
      Assert.empty(Collections.emptyList(), "The Collection must be empty!");
      Assert.empty(Collections.emptySet(), "The Collection must be empty!");
    }
    catch (Throwable t) {
      fail("Asserting empty on null and empty Collections threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyWithNonEmptyCollection() throws Exception {
    try {
      final Collection<String> nonEmptyList = new ArrayList<String>(3);
      nonEmptyList.add("test");
      nonEmptyList.add("mock");
      nonEmptyList.add("assert");
      Assert.empty(nonEmptyList, "The Collection must be empty!");
      fail("Asserting empty on a non-empty Collection should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Collection must be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting empty on a non-empty Collection threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyMap() throws Exception {
    try {
      Assert.empty((Map) null, "The Map must be empty!");
      Assert.empty(Collections.emptyMap(), "The Map must be empty!");
      Assert.empty(new HashMap<Object, Object>(0), "The Map must be empty!");
    }
    catch (Throwable t) {
      fail("Asserting empty on null and empty Maps threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyWithNonEmptyMap() throws Exception {
    try {
      final Map<String, String> nonEmptyMap =  new HashMap<String, String>(3);
      nonEmptyMap.put("key", "value");
      Assert.empty(nonEmptyMap, "The Map must be empty!");
      fail("Asserting empty on a non-empty Map should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Map must be empty!", e.getMessage());
    }
    catch (Throwable t) {
    fail("Asserting empty on a non-empty Map threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyString() throws Exception {
    try {
      Assert.empty("", "The String must be empty!");
    }
    catch (Throwable t) {
      fail("Asserting empty on the empty String threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyWithNullString() throws Exception {
    try {
      Assert.empty((String) null, "The String must be empty!");
      fail("Asserting empty on a null String should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String must be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting empty on a null String threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyWithBlankString() throws Exception {
    try {
      Assert.empty("  ", "The String must be empty!");
      fail("Asserting empty on a blank String should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String must be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting empty on a blank String threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEmptyWithNonNullNonBlankString() throws Exception {
    try {
      Assert.empty("empty", "The String must be empty!");
      fail("Asserting empty on a non-null, non-blank String should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String must be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting empty on a non-null, non-blank String threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertEquals() throws Exception {
    try {
      Assert.equals(Boolean.TRUE, Boolean.TRUE, "The Objects must be equal!");
      Assert.equals(1, new Integer(1), "The Objects must be equal!");
      Assert.equals(Math.PI, new Double(Math.PI), "The Objects must be equal!");
      Assert.equals('C', 'C', "The Objects must be equal!");
      Assert.equals("test", "test", "The Objects must be equal!");
    }
    catch (Throwable t) {
      fail("Asserting equals on a set of equal Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNonEqualBooleans() throws Exception {
    try {
      Assert.equals(Boolean.TRUE, Boolean.FALSE, "The Objects must be equal!");
      fail("Asserting true == false should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects must be equal!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting true == false threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNonEqualCharacters() throws Exception {
    try {
      Assert.equals('C', 'c', "The Objects must be equal!");
      fail("Asserting C == c should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects must be equal!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting C == c an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNonEqualDoubles() throws Exception {
    try {
      Assert.equals(3.14159d, Math.PI, "The Objects must be equal!");
      fail("Asserting 3.14159 == Math.PI should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects must be equal!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting 3.14159 == Math.PI an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNonEqualIntegers() throws Exception {
    try {
      Assert.equals(1, 2, "The Objects must be equal!");
      fail("Asserting 1 == 2 should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects must be equal!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting 1 == 2 threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNonEqualStrings() throws Exception {
    try {
      Assert.equals("Q", "Queue", "The Objects must be equal!");
      fail("Asserting Q == Queue should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects must be equal!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Asserting Q == Queue an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThan() throws Exception {
    try {
      Assert.greaterThan(1, 0, "The actual value must be greater than minimum value!");
      Assert.greaterThan(1, -1, "The actual value must be greater than minimum value!");
      Assert.greaterThan(1, -10, "The actual value must be greater than minimum value!");
    }
    catch (Throwable t) {
      fail("Calling greater than with an actual value greater than the minimum values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanWithEqualValue() throws Exception {
    try {
      Assert.greaterThan(1.0, 1.0, "The actual value must be greater than minimum value!");
      fail("Calling greaterThan with two equal values should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than minimum value!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling a greater than with two equal values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanWithLesserValue() throws Exception {
    try {
      Assert.greaterThan(-10.0, 1.0, "The actual value must be greater than minimum value!");
      fail("Calling greaterThan with a lesser actual value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than minimum value!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThan with a lesser actual value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThan() throws Exception {
    try {
      Assert.greaterThanAndLessThan(3, 2, 4, "The actual value must be greater than the minimum value and less than the maximum value!");
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThan with a value between min and max threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThanWithValueLessThanMin() throws Exception {
    try {
      Assert.greaterThanAndLessThan(1.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than the maximum value!");
      fail("Calling greaterThanAndLessThan with an actual value less than min should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThan with an actual value less than min threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThanWithValueEqualToMin() throws Exception {
    try {
      Assert.greaterThanAndLessThan(2.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than the maximum value!");
      fail("Calling greaterThanAndLessThan with an actual value equal to min should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThan with an actual value equal to min threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThanWithValueEqualToMax() throws Exception {
    try {
      Assert.greaterThanAndLessThan(4.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than the maximum value!");
      fail("Calling greaterThanAndLessThan with an actual value equal to max should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThan with an actual value equal to max threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThanWithValueGreaterThanMax() throws Exception {
    try {
      Assert.greaterThanAndLessThan(5.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than the maximum value!");
      fail("Calling greaterThanAndLessThan with an actual value greater than max should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThan with an actual value greater than max threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThanEqual() throws Exception {
    try {
      Assert.greaterThanAndLessThanEqual(3, 2, 4, "The actual value must be greater than the minimum value and less than or equal to the maximum value!");
      Assert.greaterThanAndLessThanEqual(4, 2, 4, "The actual value must be greater than the minimum value and less than or equal to the maximum value!");
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThanEqual with a value between min and max inclusive threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanAndLessThanEqualWithValueLessThanMin() throws Exception {
    try {
      Assert.greaterThanAndLessThanEqual(1.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than or equal to the maximum value!");
      fail("Calling greaterThanAndLessThanEqual with an actual value less than min should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than or equal to the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThanEqual with an actual value less than min threw an unexpected Throwable (" + t + ")");
    }
  }

  public void testAssertGreaterThanAndLessThanEqualWithValueEqualToMin() throws Exception {
    try {
      Assert.greaterThanAndLessThanEqual(2.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than or equal to the maximum value!");
      fail("Calling greaterThanAndLessThanEqual with an actual value equal to min should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than or equal to the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThanEqual with an actual value equal to min threw an unexpected Throwable (" + t + ")");
    }
  }

  public void testAssertGreaterThanAndLessThanEqualWithValueGreaterThanMax() throws Exception {
    try {
      Assert.greaterThanAndLessThanEqual(5.0, 2.0, 4.0, "The actual value must be greater than the minimum value and less than or equal to the maximum value!");
      fail("Calling greaterThanAndLessThanEqual with an actual value greater than max should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than the minimum value and less than or equal to the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanAndLessThanEqual with an actual value greater than max threw an unexpected Throwable (" + t + ")");
    }
  }

  public void testAssertGreaterThanEqual() throws Exception {
    try {
      Assert.greaterThanEqual(2, 2, "The actual value must be greater than or equal to the minimum value!");
      Assert.greaterThanEqual(2, -4, "The actual value must be greater than or equal to the minimum value!");
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqual with a value greater than or equal to the minimum threw and unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualWithLesserValue() throws Exception {
    try {
      Assert.greaterThanEqual(-10.0, 2.0, "The actual value must be greater than or equal to the minimum value!");
      fail("Calling greaterThanEqual with an actual value less than the minimum value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than or equal to the minimum value!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqual with an actual value less than the minimum value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThan() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThan(3, 2, 4, "The actual value must greater than or equal to minimum value or less than maximum value!");
      Assert.greaterThanEqualAndLessThan(2, 2, 4, "The actual value must greater than or equal to minimum value or less than maximum value!");
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualAndLessThan with an actual value between mininum inclusive and maximum threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThanWithValueLessThanMin() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThan(1.0, 2.0, 4.0, "The actual value must greater than or equal to minimum value or less than maximum value!");
      fail("Calling greaterThanEqualAndLessThan with an actual value less than min should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must greater than or equal to minimum value or less than maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualAndLessThan with an actual value less than min threw a unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThanWithValueEqualToMax() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThan(4.0, 2.0, 4.0, "The actual value must greater than or equal to minimum value or less than maximum value!");
      fail("Calling greaterThanEqualAndLessThan with an actual value equal to max should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must greater than or equal to minimum value or less than maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualAndLessThan with an actual value equal to max threw a unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThanWithValueGreaterThanMax() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThan(5.0, 2.0, 4.0, "The actual value must greater than or equal to minimum value or less than maximum value!");
      fail("Calling greaterThanEqualAndLessThan with an actual value greater than max should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must greater than or equal to minimum value or less than maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualAndLessThan with an actual value greater than max threw a unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThanEqual() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThanEqual(3, 2, 4, "The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!");
      Assert.greaterThanEqualAndLessThanEqual(2, 2, 4, "The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!");
      Assert.greaterThanEqualAndLessThanEqual(4, 2, 4, "The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!");
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualAndLessThanEqual with an actual value between min and max inclusive threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThanEqualWithValueLessThanMin() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThanEqual(1.0, 2.0, 4.0, "The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!");
      fail("Calling greaterThanEqualLessThanEquals with an actual value less than min should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualLessThanEquals with an actual value less than min threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertGreaterThanEqualAndLessThanEqualWithValueGreaterThanMax() throws Exception {
    try {
      Assert.greaterThanEqualAndLessThanEqual(5.0, 2.0, 4.0, "The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!");
      fail("Calling greaterThanEqualLessThanEquals with an actual value greater than max should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be greater than or equal to the minimum value and less than or equal to the maximum value!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling greaterThanEqualLessThanEquals with an actual value greater than max threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsAssignableFrom() throws Exception {
    try {
      Assert.isAssignableFrom(Boolean.TRUE.getClass(), Boolean.class, "Expected Class type of Boolean!");
      Assert.isAssignableFrom(new Integer(1).getClass(), Number.class, "Expected Class type of Number!");
      Assert.isAssignableFrom(new Double(Math.PI).getClass(), Number.class, "Expected Class type of Number!");
      Assert.isAssignableFrom("test".getClass(), String.class, "Expected Class type of String!");
    }
    catch (Throwable t) {
      fail("Calling isAssignableFrom with valid values threw an unexpeced Throwable (" + t + "!)");
    }
  }

  public void testAssertIsAssignableFromWithNullActualClass() throws Exception {
    try {
      Assert.isAssignableFrom((Class) null, Object.class, "Expected Class type of Object!");
      fail("Calling isAssignableFrom with a null Class object should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected Class type of Object!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isAssignableFrom with a null Class object threw an unexpected Throwable (" + t+ ")!");
    }
  }

  public void testAssertIsAssignableFromWithInvalidActualClass() throws Exception {
    try {
      Assert.isAssignableFrom("2".getClass(), Number.class, "Expected Class type of Number!");
      fail("Calling isAssignableFrom with a String class when expecting a Number should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected Class type of Number!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isAssignableFrom with a String class when expecting a Number threw an unexpected Throwable (" + t+ ")!");
    }
  }

  public void testAssertIsFalse() throws Exception {
    try {
      Assert.isFalse(false, "Expected false, but was true!");
      Assert.isFalse(Boolean.FALSE, "Expected false, but was true!");
    }
    catch (Throwable t) {
      fail("Calling isFalse with false values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsFalseWithTrue() throws Exception {
    try {
      Assert.isFalse(true, "Expected false, but was true!");
      fail("Calling isFalse with a true value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected false, but was true!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isFalse with a true value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsInstanceOf() throws Exception {
    try {
      Assert.isInstanceOf(true, Boolean.class, "Expected instance of Boolean!");
      Assert.isInstanceOf('c', Character.class, "Expected instance of Character");
      Assert.isInstanceOf(1, Integer.class, "Expected instance of Integer!");
      Assert.isInstanceOf(Math.PI, Double.class, "Expected instance of Double!");
      Assert.isInstanceOf(100.00, Number.class, "Expected instance of Number!");
      Assert.isInstanceOf("test", String.class, "Expected instance of String!");
    }
    catch (Throwable t) {
      fail("Calling isInstanceOf with various values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsInstanceOfWithNonNumberNumber() throws Exception {
    try {
      Assert.isInstanceOf("3.14159", Number.class, "Expected instance of Number!");
      fail("Calling isInstanceOf with a String value when expected a Number should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected instance of Number!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isInstanceOf with a String value when expected a Number threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsInstanceOfWithNull() throws Exception {
    try {
      Assert.isInstanceOf(null, Object.class, "Expected instance of Object!");
      fail("Calling isInstanceOf with a null value when expecting an Object should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected instance of Object!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isInstanceOf with a null value when expecting an Object threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsNull() throws Exception {
    try {
      Assert.isNull(null, "Expected null, but was not null!");
    }
    catch (Throwable t) {
      fail("Calling isNull with a null value threw an unexpected Throwable t (" + t + ")!");
    }
  }

  public void testAssertIsNullWithNonNullValue() throws Exception {
    try {
      Assert.isNull("test", "Expected null, but was not null!");
      fail("Calling isNull with a non-null value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected null, but was not null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isNull with a non-null value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsTrue() throws Exception {
    try {
      Assert.isTrue(true, "Expected true but was false!");
      Assert.isTrue(Boolean.TRUE, "Expected true but was false!");
    }
    catch (Throwable t) {
      fail("Calling isTrue with true values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertIsTrueWithFalse() throws Exception {
    try {
      Assert.isTrue(false, "Expected true but was false!");
      fail("Calling isTrue with a false value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("Expected true but was false!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isTrue with a false value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThan() throws Exception {
    try {
      Assert.lessThan(1, 2, "The actual value must be less than the maximum value!");
      Assert.lessThan(-2, 2, "The actual value must be less than the maximum value!");
      Assert.lessThan(-10, 2, "The actual value must be less than the maximum value!");
    }
    catch (Throwable t) {
      fail("Calling lessThan with actual value less than the maximum value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanWithEqualValues() throws Exception {
    try {
      Assert.lessThan(2, 2, "The actual value must be less than the maximum value!");
      fail("Calling lessThan with equal values should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be less than the maximum value!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThan with equal values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanWithGreaterValue() throws Exception {
    try {
      Assert.lessThan(4, 2, "The actual value must be less than the maximum value!");
      fail("Calling lessThan with an actual value greater than the maximum value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be less than the maximum value!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThan with an actual value greater than the maximum value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThan() throws Exception {
    try {
      Assert.lessThanOrGreaterThan(1, 2, 4, "The actual value must fall outside the range!");
      Assert.lessThanOrGreaterThan(5, 2, 4, "The actual value must fall outside the range!");
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThan with actual values outside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThanWithValueEqualToLowerBound() throws Exception {
    try {
      Assert.lessThanOrGreaterThan(2.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanOrGreaterThan with an actual value equal to the lower bound in the range should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThan with an actual value equal to the lower bound in the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThanWithValueEqualToUpperBound() throws Exception {
    try {
      Assert.lessThanOrGreaterThan(4.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanOrGreaterThan with an actual value equal to the upper bound in the range should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThan with an actual value equal to the upper bound in the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThanWithValueInsideTheRange() throws Exception {
    try {
      Assert.lessThanOrGreaterThan(3.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanOrGreaterThan with an actual value inside the range should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThan with an actual value inside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThanEqual() throws Exception {
    try {
      Assert.lessThanOrGreaterThanEqual(1, 2, 4, "The actual value must fall outside the range!");
      Assert.lessThanOrGreaterThanEqual(4, 2, 4, "The actual value must fall outside the range!");
      Assert.lessThanOrGreaterThanEqual(5, 2, 4, "The actual value must fall outside the range!");
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThanEqual with actual values outside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThanEqualWithValueEqualToLowerBound() throws Exception {
    try {
      Assert.lessThanOrGreaterThanEqual(2.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanOrGreaterThan equal with an actual value equal to the lower bounded value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThan equal with an actual value equal to the lower bounded value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanOrGreaterThanEqualWithValueInsideTheRange() throws Exception {
    try {
      Assert.lessThanOrGreaterThanEqual(3.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanOrGreaterThan equal with an actual value inside the range should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanOrGreaterThan equal with an actual value inside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqual() throws Exception {
    try {
      Assert.lessThanEqual(1, 2, "The actual value must be less than or equal to the maximum value!");
      Assert.lessThanEqual(2, 2, "The actual value must be less than or equal to the maximum value!");
    }
    catch (Throwable t) {
      fail("Calling lessThanEqual with actual values less than or equal to the maximum value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqualWithGreaterValue() throws Exception {
    try {
      Assert.lessThanEqual(3.0, 2.0, "The actual value must be less than or equal to the maximum value!");
      fail("Calling lessThanEqual with an actual value greater than the maximum value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must be less than or equal to the maximum value!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanEqual with an actual value greater than the maximum value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqualOrGreaterThan() throws Exception {
    try {
      Assert.lessThanEqualOrGreaterThan(1.0, 2.0, 4.0, "The actual value must fall outside the range!");
      Assert.lessThanEqualOrGreaterThan(2.0, 2.0, 4.0, "The actual value must fall outside the range!");
      Assert.lessThanEqualOrGreaterThan(5.0, 2.0, 4.0, "The actual value must fall outside the range!");
    }
    catch (Throwable t) {
      fail("Calling lessThanEqualOrGreaterThan with actual values outside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqualOrGreaterThanWithValueEqualToUpperBound() throws Exception {
    try {
      Assert.lessThanEqualOrGreaterThan(4.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanEqualOrGreaterThan with an actual value equal to the upper bound should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanEqualOrGreaterThan with an actual value equal to the upper bound threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqualOrGreaterThanWithValueInsideRange() throws Exception {
    try {
      Assert.lessThanEqualOrGreaterThan(3.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanEqualOrGreaterThan with an actual value inside the range should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanEqualOrGreaterThan with an actual value inside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqualOrGreaterThanEqual() throws Exception {
    try {
      Assert.lessThanEqualOrGreaterThanEqual(1, 2, 4, "The actual value must fall outside the range!");
      Assert.lessThanEqualOrGreaterThanEqual(2, 2, 4, "The actual value must fall outside the range!");
      Assert.lessThanEqualOrGreaterThanEqual(4, 2, 4, "The actual value must fall outside the range!");
      Assert.lessThanEqualOrGreaterThanEqual(5, 2, 4, "The actual value must fall outside the range!");
    }
    catch (Throwable t) {
      fail("Calling lessThanEqualOrGreaterThanEqual with actual value outside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLessThanEqualOrGreaterThanEqualWithValueInsideTheRange() throws Exception {
    try {
      Assert.lessThanEqualOrGreaterThanEqual(3.0, 2.0, 4.0, "The actual value must fall outside the range!");
      fail("Calling lessThanEqualOrGreaterThanEqual with an actual value inside the range should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual value must fall outside the range!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lessThanEqualOrGreaterThanEqual with an actual value inside the range threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLettersOnly() throws Exception {
    try {
      Assert.lettersOnly("abcdefghijklmnopqrstuvwxyz", "The String value must contain only letters!");
      Assert.lettersOnly("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "The String value must contain only letters!");
    }
    catch (Throwable t) {
      fail("Calling lettersOnly with a character String containing only letters threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLettersOnlyWithNonLetterCharacters() throws Exception {
    try {
      Assert.lettersOnly("c0b01", "The String value must contain only letters!");
      fail("Calling lettersOnly with a character String containing letters and numbers should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value must contain only letters!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lettersOnly with a character String containing letters and numbers threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLettersOnlyWithNumbers() throws Exception {
    try {
      Assert.lettersOnly("0123456789", "The String value must contain only letters!");
      fail("Calling lettersOnly with a character String containing only numbers should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value must contain only letters!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lettersOnly with a character String containing only numbers threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertLettersOnlyWithSpecialCharacters() throws Exception {
    try {
      Assert.lettersOnly("!$@#%", "The String value must contain only letters!");
      fail("Calling lettersOnly with a character String containing special characters should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value must contain only letters!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling lettersOnly with a character String containing special characters threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNegative() throws Exception {
    try {
      Assert.negative(-0.0001, "The Number value must be negative!");
      Assert.negative(-1.0, "The Number value must be negative!");
    }
    catch (Throwable t) {
      fail("Calling negative with a negative numerical value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNegativeWithZero() throws Exception {
    try {
      Assert.negative(-0.0, "The Number value must be negative!");
      fail("Calling negative with zero should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Number value must be negative!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling negative with zero threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNegativeWithPositiveValue() throws Exception {
    try {
      Assert.negative(1.0, "The Number value must be negative!");
      fail("Calling negative with a positive value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Number value must be negative!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling negative with a positive value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotBlank() throws Exception {
    try {
      Assert.notBlank("null", "The String value cannot be blank!");
      Assert.notBlank("empty", "The String value cannot be blank!");
      Assert.notBlank("blank", "The String value cannot be blank!");
      Assert.notBlank("____", "The String value cannot be blank!");
      Assert.notBlank("0", "The String value cannot be blank!");
    }
    catch (Throwable t) {
      fail("Calling notBlank with non-blank String values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotBlankWithNullString() throws Exception {
    try {
      Assert.notBlank(null, "The String value cannot be blank!");
      fail("Calling notBlank with a null String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value cannot be blank!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notBlank with a null String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotBlankWithEmptyString() throws Exception {
    try {
      Assert.notBlank("", "The String value cannot be blank!");
      fail("Calling notBlank with an empty String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value cannot be blank!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notBlank with an empty String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotBlankWithBlankString() throws Exception {
    try {
      Assert.notBlank("   ", "The String value cannot be blank!");
      fail("Calling notBlank with a blank String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value cannot be blank!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notBlank with a blank String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyArray() throws Exception {
    try {
      Assert.notEmpty(new Object[] { "assert", "mock", "test" }, "The Object array cannot be empty!");
      Assert.notEmpty(new Object[10], "The Object array cannot be empty!");
    }
    catch (Throwable t) {
      fail("Calling notEmpty with non-empty Object arrays threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyArrayWithNullArray() throws Exception {
    try {
      Assert.notEmpty((Object[]) null, "The Object array cannot be empty!");
      fail("Calling notEmpty with a null Object array should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Object array cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with a null Object array threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyArrayWithEmptyArray() throws Exception {
    try {
      Assert.notEmpty(new Object[0], "The Object array cannot be empty!");
      fail("Calling notEmpty with an empty Object array should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Object array cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with an empty Object array threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyCollection() throws Exception {
    final Collection<String> collection = new ArrayList<String>(3);
    collection.add("assert");
    collection.add("mock");
    collection.add("test");

    assertFalse(collection.isEmpty());

    try {
      Assert.notEmpty(collection, "The Collection cannot be empty!");
    }
    catch (Throwable t) {
      fail("Calling notEmpty with a non-empty Collection threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testNotEmptyCollectionWithNullCollection() throws Exception {
    try {
      Assert.notEmpty((Collection) null, "The Collection cannot be empty!");
      fail("Calling notEmpty with a null Collection should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Collection cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with a null Collection threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testNotEmptyCollectionWithEmptyCollection() throws Exception {
    try {
      Assert.notEmpty(Collections.emptyList(), "The Collection cannot be empty!");
      fail("Calling notEmpty with an empty Collection should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Collection cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with an empty Collection threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyMap() throws Exception {
    final Map<String, Object> map = new HashMap<String, Object>(3);
    map.put("key0", "assert");
    map.put("key1", "mock");
    map.put("key2", "test");

    assertFalse(map.isEmpty());

    try {
      Assert.notEmpty(map, "The Map cannot be empty!");
    }
    catch (Throwable t) {
      fail("Calling notEmpty with a non-empty Map threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyMapWithNullMap() throws Exception {
    try {
      Assert.notEmpty((Map) null, "The Map cannot be empty!");
      fail("Calling notEmpty with a null Map should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Map cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with a null Map threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyMapWithEmptyMap() throws Exception {
    try {
      Assert.notEmpty(Collections.emptyMap(), "The Map cannot be empty!");
      fail("Calling notEmpty with an empty Map should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Map cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with an empty Map threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyString() throws Exception {
    try {
      Assert.notEmpty((String) null, "The String value cannot be empty!");
      Assert.notEmpty("null", "The String value cannot be empty!");
      Assert.notEmpty("empty", "The String value cannot be empty!");
      Assert.notEmpty("test", "The String value cannot be empty!");
      Assert.notEmpty(" ", "The String value cannot be empty!");
      Assert.notEmpty("_", "The String value cannot be empty!");
    }
    catch (Throwable t) {
      fail("Calling notEmpty with non-empty String values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEmptyStringWithEmptyString() throws Exception {
    try {
      Assert.notEmpty("", "The String value cannot be empty!");
      fail("Calling notEmpty with an empty String value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The String value cannot be empty!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEmpty with an empty String value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEqual() throws Exception {
    try {
      Assert.notEqual(null, null, "The Objects cannot be equal!");
      Assert.notEqual(Boolean.TRUE, Boolean.FALSE, "The Object cannot be equal!");
      Assert.notEqual('c', 'C', "The Object cannot be equal!");
      Assert.notEqual(0, 1, "The Object cannot be equal!");
      Assert.notEqual(3.14159d, Math.PI, "The Object cannot be equal!");
      Assert.notEqual("mock", "test", "The Object cannot be equal!");
      Assert.notEqual("TEST", "test", "The Object cannot be equal!");
      Assert.notEqual("", "empty", "The Object cannot be equal!");
      Assert.notEqual("", " ", "The Object cannot be equal!");
    }
    catch (Throwable t) {
      fail("Calling notEqual on non-equal Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotEqualWithEqualObjects() throws Exception {
    try {
      Assert.notEqual("test", "test", "The Objects cannot be equal!");
      fail("Calling notEqual on two equal Objects should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects cannot be equal!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notEqual on two equal Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotNull() throws Exception {
    try {
      Assert.notNull(new Object(), "The Object cannot be null!");
    }
    catch (Throwable t) {
      fail("Calling notNull with a non-null Object threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotNullWithNullObject() throws Exception {
    try {
      Assert.notNull(null, "The Object cannot be null!");
      fail("Calling notNull with a null Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notNull with a null Object threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotNullHavingRuntimeExceptionParameter() throws Exception {
    try {
      Assert.notNull(new Object(), new NullPointerException("The Object cannot be null!"));
    }
    catch (Throwable t) {
      fail("Calling notNull with a non-null Object threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotNullHavingRuntimeExceptionParameterWithNullObject() throws Exception {
    try {
      Assert.notNull(null, new ConfigurationException("The Object dependency was not properly initialized!"));
      fail("Calling notNull with a null Object argument should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("The Object dependency was not properly initialized!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notNull with a null Object argument threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotSame() throws Exception {
    try {
      Assert.notSame("test", new String("test"), "The Objects cannot be identical!");
    }
    catch (Throwable t) {
      fail("Calling notSame with non-identical Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertNotSameWithIdenticalObjects() throws Exception {
    try {
      Assert.notSame("test", "test", "The Objects cannot be identical!");
      fail("Calling notSame with identical Objects should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects cannot be identical!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling notSame with identical Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertPositive() throws Exception {
    try {
      Assert.positive(0.001, "The Number value must be positive!");
      Assert.positive(1.0, "The Number value must be positive!");
    }
    catch (Throwable t) {
      fail("Calling positive with a positive numerical value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertPositiveWithZero() throws Exception {
    try {
      Assert.positive(0.0d, "The Number value must be positive!");
      fail("Calling positive with zero should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Number value must be positive!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling positive with zero threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertPositiveWithNegativeValue() throws Exception {
    try {
      Assert.positive(-1.0, "The Number value must be positive!");
      fail("Calling positive with a negative value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Number value must be positive!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling positive with a negative value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertSame() throws Exception {
    try {
      Assert.same("test", "test", "The Objects must be identical!");
    }
    catch (Throwable t) {
      fail("Calling same on identical Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertSameWithNonIdenticalObjects() throws Exception {
    try {
      Assert.same("test", new String("test"), "The Objects must be identical!");
      fail("Calling same on non-identical Objects should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Objects must be identical!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling same on non-identical Objects threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertState() throws Exception {
    try {
      Assert.state(true, "The state of the condition must be true!");
      Assert.state(Boolean.TRUE, "The state of the condition must be true!");
      Assert.state(1 == 1, "The state of the condition must be true!");
    }
    catch (Throwable t) {
      fail("Calling state with true conditions threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertStateWithFalse() throws Exception {
    try {
      Assert.state(false, "The state of the condition must be true!");
      fail("Calling state with a false condition should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The state of the condition must be true!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling state with a false condition threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertStateHavingRuntimeExceptionParameter() throws Exception {
    try {
      Assert.state(true, new RuntimeException("test"));
    }
    catch (Throwable t) {
      fail("Calling state with a true condition threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertStateHavingRuntimeExceptionParameterWithFalse() throws Exception {
    try {
      Assert.state(false, new ConfigurationException("Not Configured!"));
      fail("Calling state with a false condition should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("Not Configured!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling state with a false condition threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertZero() throws Exception {
    try {
      Assert.zero(0.0d, "The numerical value must be zero!");
      Assert.zero(-0.0d, "The numerical value must be zero!");
    }
    catch (Throwable t) {
      fail("Calling zero with zero numerical values threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertZeroWithNegativeValue() throws Exception {
    try {
      Assert.zero(-1.0, "The numerical value must be zero!");
      fail("Calling zero with a negative value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The numerical value must be zero!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling zero with a negative value threw an unexpected Throwable (" + t + ")!");
    }
  }

  public void testAssertZeroWithPositiveValue() throws Exception {
    try {
      Assert.zero(1.0, "The numerical value must be zero!");
      fail("Calling zero with a positive value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The numerical value must be zero!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling zero with a positive value threw an unexpected Throwable (" + t + ")!");
    }
  }

}
