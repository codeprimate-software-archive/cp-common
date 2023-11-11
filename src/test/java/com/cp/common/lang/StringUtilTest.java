/*
 * StringUtilTest.java (c) 17 August 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.27
 * @see com.cp.common.lang.StringUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import com.cp.common.test.util.TestUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StringUtilTest extends TestCase {

  public StringUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(StringUtilTest.class);
    //suite.addTest(new StringUtilTest("testIsDigitsOnly"));
    return suite;
  }

  private String getDelimitedValues(final String[] valueArray, final String delimiter) {
    return getDelimitedValues(Arrays.asList(valueArray), delimiter);
  }

  private String getDelimitedValues(final List valueList, final String delimeter) {
    final StringBuffer buffer = new StringBuffer();
    for (Iterator it = valueList.iterator(); it.hasNext(); ) {
      buffer.append(it.next());
      buffer.append(it.hasNext() ? delimeter : "");
    }
    return buffer.toString();
  }

  public void testCharAt() throws Exception {
    assertEquals('\u0000', StringUtil.charAt(null, 0));
    assertEquals('t', StringUtil.charAt("test", 0));
    assertEquals('e', StringUtil.charAt("test", 1));
    assertEquals('s', StringUtil.charAt("test", 2));
    assertEquals('t', StringUtil.charAt("test", 3));
  }

  public void testCharAtThrowsIndexOutOfBoundsException() throws Exception {
    try {
      StringUtil.charAt("test", -1);
      fail("Calling charAt with a value of (test) and an index of -1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling charAt with a value of (test) and an index of -1 threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    try {
      StringUtil.charAt("test", 4);
      fail("Calling charAt with a value of (test) and an index of 4 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling charAt with a value of (test) and an index of 4 threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testCharIterator() throws Exception {
    final String testString = "abcdefghijklmnopqrstuvwxyz0123456789";
    int index = 0;
    for (Iterator it = StringUtil.charIterator(testString); it.hasNext(); ) {
      assertEquals(testString.charAt(index++), ((Character) it.next()).charValue());
    }
  }

  // testContains

  public void testContains() throws Exception {
    final String testString = "This is a test String.";
    assertTrue(StringUtil.contains(testString, "test"));
    assertTrue(!StringUtil.contains(testString, "Test"));
    assertTrue(StringUtil.contains(testString, "This is a "));
    assertTrue(!StringUtil.contains(testString, "This    is"));
    assertTrue(!StringUtil.contains(testString, "Apple"));
    assertTrue(!StringUtil.contains(null, "Word"));
  }

  public void testContainsDigit() throws Exception {
    assertTrue(StringUtil.containsDigit("1"));
    assertTrue(StringUtil.containsDigit(" 123"));
    assertTrue(StringUtil.containsDigit("J0b"));
    assertFalse(StringUtil.containsDigit("JOb"));
    assertFalse(StringUtil.containsDigit("lip"));
    assertTrue(StringUtil.containsDigit("1ip"));
    assertTrue(StringUtil.containsDigit("Rent2Own"));
    assertFalse(StringUtil.containsDigit("RentTwoOwn"));
    assertFalse(StringUtil.containsDigit("RentToOwn"));
    assertFalse(StringUtil.containsDigit("Ten"));
    assertTrue(StringUtil.containsDigit("Testing1..2..3.."));
  }

  public void testContainsIgnoreCase() throws Exception {
    final String testString = "This is another test String.";
    assertTrue(StringUtil.containsIgnoreCase(testString, "test"));
    assertTrue(StringUtil.containsIgnoreCase(testString, "Test"));
    assertTrue(StringUtil.containsIgnoreCase(testString, "is anotheR tEsT"));
    assertTrue(!StringUtil.containsIgnoreCase(testString, "is   another"));
    assertTrue(!StringUtil.containsIgnoreCase(testString, "Apple"));
    assertTrue(!StringUtil.containsIgnoreCase(null, "Word"));
  }

  public void testContainsLetter() throws Exception {
    assertTrue(StringUtil.containsLetter("A"));
    assertTrue(StringUtil.containsLetter("bcde"));
    assertTrue(StringUtil.containsLetter("AaBbCc"));
    assertTrue(StringUtil.containsLetter("e123"));
    assertTrue(StringUtil.containsLetter("123b456"));
    assertTrue(StringUtil.containsLetter("-?:{[h%@$"));
    assertFalse(StringUtil.containsLetter("123"));
    assertFalse(StringUtil.containsLetter("$%!1~"));
  }

  public void testContainsLowerCaseLetter() throws Exception {
    assertTrue(StringUtil.containsLowerCaseLetter("a"));
    assertTrue(StringUtil.containsLowerCaseLetter("Aa"));
    assertTrue(StringUtil.containsLowerCaseLetter("AaBbCc"));
    assertTrue(StringUtil.containsLowerCaseLetter("123a"));
    assertTrue(StringUtil.containsLowerCaseLetter("_-a"));
    assertTrue(StringUtil.containsLowerCaseLetter("******a!...123"));
    assertFalse(StringUtil.containsLowerCaseLetter("A"));
    assertFalse(StringUtil.containsLowerCaseLetter("ABC"));
    assertFalse(StringUtil.containsLowerCaseLetter("123"));
    assertFalse(StringUtil.containsLowerCaseLetter("ABC123"));
    assertFalse(StringUtil.containsLowerCaseLetter("~!#&123-_-N"));
  }

  public void testContainsUpperCaseLetter() throws Exception {
    assertTrue(StringUtil.containsUpperCaseLetter("A"));
    assertTrue(StringUtil.containsUpperCaseLetter("Aa"));
    assertTrue(StringUtil.containsUpperCaseLetter("AaBbCc"));
    assertTrue(StringUtil.containsUpperCaseLetter("123A"));
    assertTrue(StringUtil.containsUpperCaseLetter("_-A"));
    assertTrue(StringUtil.containsUpperCaseLetter("******A!...123"));
    assertFalse(StringUtil.containsUpperCaseLetter("a"));
    assertFalse(StringUtil.containsUpperCaseLetter("abc"));
    assertFalse(StringUtil.containsUpperCaseLetter("123"));
    assertFalse(StringUtil.containsUpperCaseLetter("abc123"));
    assertFalse(StringUtil.containsUpperCaseLetter("~!#&123-_-n"));
  }

  public void testContainsWhitespace() throws Exception {
    assertTrue(StringUtil.containsWhitespace(" "));
    assertTrue(StringUtil.containsWhitespace("    "));
    assertTrue(StringUtil.containsWhitespace("A aB"));
    assertTrue(StringUtil.containsWhitespace("1 "));
    assertTrue(StringUtil.containsWhitespace("1 2 3 "));
    assertTrue(StringUtil.containsWhitespace("-_ "));
    assertFalse(StringUtil.containsWhitespace("A"));
    assertFalse(StringUtil.containsWhitespace("Aa"));
    assertFalse(StringUtil.containsWhitespace("123abc"));
    assertFalse(StringUtil.containsWhitespace("1-abc...-#%"));
    assertFalse(StringUtil.containsWhitespace("Testing_1_2_3__"));
    assertFalse(StringUtil.containsWhitespace("_"));
  }

  // testCount

  public void testCountDigits() throws Exception {
    assertEquals(1, StringUtil.countDigits("1"));
    assertEquals(10, StringUtil.countDigits("0123456789"));
    assertEquals(6, StringUtil.countDigits("100101"));
    assertEquals(2, StringUtil.countDigits("05"));
    assertEquals(1, StringUtil.countDigits("0xAF"));
    assertEquals(0, StringUtil.countDigits(""));
    assertEquals(0, StringUtil.countDigits(" "));
    assertEquals(1, StringUtil.countDigits(" 2 "));
    assertEquals(0, StringUtil.countDigits("Ol"));
    assertEquals(0, StringUtil.countDigits("a"));
    assertEquals(0, StringUtil.countDigits("abc"));
    assertEquals(4, StringUtil.countDigits("0abc123"));
    assertEquals(2, StringUtil.countDigits(" a12z "));
    assertEquals(0, StringUtil.countDigits("~!()-"));
    assertEquals(3, StringUtil.countDigits("~!(123)-"));
  }

  public void testCountLetters() throws Exception {
    assertEquals(1, StringUtil.countLetters("l"));
    assertEquals(3, StringUtil.countLetters("abc"));
    assertEquals(2, StringUtil.countLetters("AB"));
    assertEquals(6, StringUtil.countLetters("AaBbCc"));
    assertEquals(0, StringUtil.countLetters(""));
    assertEquals(0, StringUtil.countLetters(" "));
    assertEquals(3, StringUtil.countLetters(" xXx "));
    assertEquals(0, StringUtil.countLetters("1"));
    assertEquals(0, StringUtil.countLetters("123"));
    assertEquals(3, StringUtil.countLetters("abc... 123... "));
    assertEquals(0, StringUtil.countLetters("!~***@$"));
    assertEquals(3, StringUtil.countLetters("$abc>>(...)-"));
  }

  public void testCountLowerCaseLetters() throws Exception {
    assertEquals(0, StringUtil.countLowerCaseLetters(""));
    assertEquals(0, StringUtil.countLowerCaseLetters(" "));
    assertEquals(1, StringUtil.countLowerCaseLetters(" z "));
    assertEquals(0, StringUtil.countLowerCaseLetters(" Z "));
    assertEquals(0, StringUtil.countLowerCaseLetters("ABC"));
    assertEquals(6, StringUtil.countLowerCaseLetters("abcdef"));
    assertEquals(3, StringUtil.countLowerCaseLetters("AaBbCc"));
    assertEquals(0, StringUtil.countLowerCaseLetters("123"));
    assertEquals(4, StringUtil.countLowerCaseLetters("a1b2c3d"));
    assertEquals(2, StringUtil.countLowerCaseLetters("1a2B3c"));
    assertEquals(0, StringUtil.countLowerCaseLetters("~`~!$"));
    assertEquals(0, StringUtil.countLowerCaseLetters("~`~!$(AB)"));
    assertEquals(1, StringUtil.countLowerCaseLetters("~`~!$(aB)"));
  }

  public void testCountOccurences() throws Exception {
    assertEquals(14, StringUtil.countOccurences("T*his ** is the ***** s*ur*e Str*ng***", "*"));
    assertEquals(2, StringUtil.countOccurences("This source String contains many characters.", "o"));
    assertEquals(6, StringUtil.countOccurences("  This is the source String", " "));
    assertEquals(1, StringUtil.countOccurences("  This is the source String", "  ")); // double space
    assertEquals(2, StringUtil.countOccurences("This is the source String; source String is the phrase occurence being counted!", "source String"));
  }

  public void testCountUpperCaseLetters() throws Exception {
    assertEquals(0, StringUtil.countUpperCaseLetters(""));
    assertEquals(0, StringUtil.countUpperCaseLetters(" "));
    assertEquals(0, StringUtil.countUpperCaseLetters(" z "));
    assertEquals(1, StringUtil.countUpperCaseLetters(" Z "));
    assertEquals(3, StringUtil.countUpperCaseLetters("ABC"));
    assertEquals(0, StringUtil.countUpperCaseLetters("abcdef"));
    assertEquals(3, StringUtil.countUpperCaseLetters("AaBbCc"));
    assertEquals(0, StringUtil.countUpperCaseLetters("123"));
    assertEquals(4, StringUtil.countUpperCaseLetters("A1B2C3D"));
    assertEquals(1, StringUtil.countUpperCaseLetters("1a2B3c"));
    assertEquals(0, StringUtil.countUpperCaseLetters("~`~!$"));
    assertEquals(2, StringUtil.countUpperCaseLetters("~`~!$(AB)"));
    assertEquals(1, StringUtil.countUpperCaseLetters("~`~!$(aB)"));
  }

  public void testCountWhitespace() throws Exception {
    assertEquals(0, StringUtil.countWhitespace(""));
    assertEquals(1, StringUtil.countWhitespace(" "));
    assertEquals(3, StringUtil.countWhitespace("   "));
    assertEquals(0, StringUtil.countWhitespace("abc"));
    assertEquals(0, StringUtil.countWhitespace("abc_def"));
    assertEquals(1, StringUtil.countWhitespace("abc def"));
    assertEquals(3, StringUtil.countWhitespace(" abc def "));
    assertEquals(0, StringUtil.countWhitespace("123"));
    assertEquals(2, StringUtil.countWhitespace("1 2 3"));
    assertEquals(0, StringUtil.countWhitespace("aBcDeF123"));
    assertEquals(0, StringUtil.countWhitespace("~''~!$@$!"));
    assertEquals(0, StringUtil.countWhitespace("~'abc'~!$123@$321!"));
    assertEquals(1, StringUtil.countWhitespace("~' '~!$@$!"));
    assertEquals(3, StringUtil.countWhitespace("~' '~!$@$!  "));
  }

  public void testEqualsIgnoreCase() throws Exception {
    assertTrue(StringUtil.equalsIgnoreCase(null, null));
    assertTrue(StringUtil.equalsIgnoreCase("test", "test"));
    assertTrue(StringUtil.equalsIgnoreCase("null", "null"));
    assertTrue(StringUtil.equalsIgnoreCase("TEST", "test"));
    assertTrue(StringUtil.equalsIgnoreCase("Null", "nuLL"));
    assertFalse(StringUtil.equalsIgnoreCase(null, "null"));
    assertFalse(StringUtil.equalsIgnoreCase("nill", "null"));
    assertFalse(StringUtil.equalsIgnoreCase("@", "at"));
    assertFalse(StringUtil.equalsIgnoreCase("q", "queue"));
  }

  public void testGetCharactersOnly() throws Exception {
    assertEquals("0", StringUtil.getCharactersOnly("0", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertEquals("0.0", StringUtil.getCharactersOnly("0.0", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertEquals("2", StringUtil.getCharactersOnly("2", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertEquals("3.14159", StringUtil.getCharactersOnly("3.14159", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertEquals("-0.02", StringUtil.getCharactersOnly("- 0 .  02  ", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertEquals("-0.02", StringUtil.getCharactersOnly("$- 0 .  (02) %  ", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertEquals("$-9.99", StringUtil.getCharactersOnly("($ -9. 99 )  ", StringUtil.ContainsCurrencyStrategy.INSTANCE));
    assertEquals("$-9.99", StringUtil.getCharactersOnly("($ -9. 99  %)  ", StringUtil.ContainsCurrencyStrategy.INSTANCE));
    assertEquals("5%", StringUtil.getCharactersOnly("5%", StringUtil.ContainsPercentageStrategy.INSTANCE));
    assertEquals("0.05%", StringUtil.getCharactersOnly("0.05%", StringUtil.ContainsPercentageStrategy.INSTANCE));
    assertEquals("-0.05%", StringUtil.getCharactersOnly("- $(0. 0  5%)  ", StringUtil.ContainsPercentageStrategy.INSTANCE));
  }

  public void testGetDigitsOnly() throws Exception {
    assertEquals(null, StringUtil.getDigitsOnly(null));
    assertEquals("", StringUtil.getDigitsOnly(""));
    assertEquals(" ", StringUtil.getDigitsOnly(" "));
    assertEquals("123456", StringUtil.getDigitsOnly("123456"));
    assertEquals("123", StringUtil.getDigitsOnly("1a2D3F"));
    assertEquals("1234", StringUtil.getDigitsOnly("1 2  3   4..."));
    assertEquals("10011", StringUtil.getDigitsOnly("10011"));
    assertEquals("111", StringUtil.getDigitsOnly("1OO11")); // Letter "O"
    assertEquals("123", StringUtil.getDigitsOnly("1$_-2#3%**-"));
    assertEquals("", StringUtil.getDigitsOnly("AaBbCc"));
    assertEquals("", StringUtil.getDigitsOnly("!-_()^;;'"));
    assertEquals("", StringUtil.getDigitsOnly("Zo &Ou-_(+=!~nI"));
  }

  public void testGetEmptyIfNull() throws Exception {
    assertEquals("", StringUtil.getEmptyIfNull(null));
    assertEquals("", StringUtil.getEmptyIfNull(""));
    assertEquals(" ", StringUtil.getEmptyIfNull(" "));
    assertEquals("null", StringUtil.getEmptyIfNull("null"));
    assertEquals("empty", StringUtil.getEmptyIfNull("empty"));
    assertEquals("0", StringUtil.getEmptyIfNull("0"));
    assertEquals("\0", StringUtil.getEmptyIfNull("\0"));
  }

  public void testGetLettersOnly() throws Exception {
    assertEquals(null, StringUtil.getLettersOnly(null));
    assertEquals("", StringUtil.getLettersOnly(""));
    assertEquals("  ", StringUtil.getLettersOnly("  "));
    assertEquals("xyz", StringUtil.getLettersOnly("xyz"));
    assertEquals("AaBbCc", StringUtil.getLettersOnly("AaBbCc"));
    assertEquals("AbC", StringUtil.getLettersOnly("1A234b56C7890"));
    assertEquals("abcabcUME", StringUtil.getLettersOnly("abc, 123 abcU&ME"));
    assertEquals("oOo", StringUtil.getLettersOnly("o0O0o"));
    assertEquals("LKY", StringUtil.getLettersOnly("L00K-_#Y0$ ^"));
    assertEquals("one", StringUtil.getLettersOnly("one"));
    assertEquals("dollarsign", StringUtil.getLettersOnly("dollar sign"));
    assertEquals("", StringUtil.getLettersOnly("12345"));
    assertEquals("", StringUtil.getLettersOnly("**$$$**-_=!+)("));
  }

  public void testGetNullIfEmpty() throws Exception {
    assertNull(StringUtil.getNullIfEmpty(null));
    assertNull(StringUtil.getNullIfEmpty(""));
    assertNull("", StringUtil.getNullIfEmpty(" "));
    assertNull("", StringUtil.getNullIfEmpty("   "));
    assertNull("", StringUtil.getNullIfEmpty("\n"));
    assertEquals("null", StringUtil.getNullIfEmpty("null"));
    assertEquals("empty", StringUtil.getNullIfEmpty("empty"));
    assertEquals("0", StringUtil.getNullIfEmpty("0"));
    assertNull(StringUtil.getNullIfEmpty("\0"));
  }

  public void testIndexOf() throws Exception {
    assertEquals(-1, StringUtil.indexOf(null, "*"));
    assertEquals(-1, StringUtil.indexOf("", "*"));
    assertEquals(-1, StringUtil.indexOf(" ", "*"));
    assertEquals(-1, StringUtil.indexOf("abc123", "*"));
    assertEquals(-1, StringUtil.indexOf("abc123star", "*"));
    assertEquals(0, StringUtil.indexOf("*123***", "*"));
    assertEquals(5, StringUtil.indexOf("01234**", "*"));
  }

  public void testInsert() throws Exception {
    assertEquals("123abc", StringUtil.insert("123", "abc", 3));
    assertEquals("123abc", StringUtil.insert("abc", "123", 0));
    assertEquals("a123bc", StringUtil.insert("abc", "123", 1));
    assertEquals("ab123c", StringUtil.insert("abc", "123", 2));
    assertEquals("abc", StringUtil.insert("abc", null, 1));
    assertEquals("abc", StringUtil.insert("abc", "", 2));

    try {
      StringUtil.insert(null, "123", 0);
      fail("The StringUtil.insert method should have thrown a NullPointerException for null 'value'");
    }
    catch (NullPointerException e) {
      // expected behavior!
    }

    try {
      StringUtil.insert("abc", "123", -1);
      fail("The StringUtil.insert method should have thrown a IllegalArgumentException for a negative offset!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    try {
      StringUtil.insert("abc", "123", 10);
      fail("The StringUtil.insert method should have thrown a IllegalArgumentException for an offset larger than value.length()!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }
  }

  public void testIsAlphanumericOnly() throws Exception {
    assertTrue(StringUtil.isAlphanumericOnly("0123"));
    assertTrue(StringUtil.isAlphanumericOnly("abc"));
    assertTrue(StringUtil.isAlphanumericOnly("0123abc"));
    assertFalse(StringUtil.isAlphanumericOnly("-123"));
    assertFalse(StringUtil.isAlphanumericOnly("@!("));
    assertFalse(StringUtil.isAlphanumericOnly("!23@b("));
  }

  public void testIsCharactersOnly() throws Exception {
    assertTrue(StringUtil.isCharatersOnly("-3.14159", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertFalse(StringUtil.isCharatersOnly("(3.14159)", StringUtil.ContainsNumericStrategy.INSTANCE));
    assertTrue(StringUtil.isCharatersOnly("$0.99", StringUtil.ContainsCurrencyStrategy.INSTANCE));
    assertFalse(StringUtil.isCharatersOnly("99 cents", StringUtil.ContainsCurrencyStrategy.INSTANCE));
    assertTrue(StringUtil.isCharatersOnly("100%", StringUtil.ContainsPercentageStrategy.INSTANCE));
    assertFalse(StringUtil.isCharatersOnly("100 percent", StringUtil.ContainsPercentageStrategy.INSTANCE));
  }

  public void testIsDigitsOnly() throws Exception {
    assertTrue(StringUtil.isDigitsOnly("1234567890"));
    assertFalse(StringUtil.isDigitsOnly("123-45-6789"));
    assertFalse(StringUtil.isDigitsOnly("123J456J7890B"));
    assertFalse(StringUtil.isDigitsOnly("123j456T6789-"));
    assertFalse(StringUtil.isDigitsOnly(" 12345"));
    assertFalse(StringUtil.isDigitsOnly("-12345"));
    assertFalse(StringUtil.isDigitsOnly("O123")); // The first character is the letter 'O', not zero.
    assertFalse(StringUtil.isDigitsOnly("'123'"));
  }

  public void testIsEmpty() throws Exception {
    assertTrue(StringUtil.isEmpty(null));
    assertTrue(StringUtil.isEmpty(""));
    assertTrue(StringUtil.isEmpty("   "));
    assertTrue(StringUtil.isEmpty("\n"));
    assertTrue(StringUtil.isEmpty("\t"));
    assertTrue(StringUtil.isEmpty("\0"));
    assertFalse(StringUtil.isEmpty("null"));
    assertFalse(StringUtil.isEmpty("A"));
    assertFalse(StringUtil.isEmpty("0"));
  }

  public void testIsLettersOnly() throws Exception {
    assertTrue(StringUtil.isLettersOnly("abc"));
    assertFalse(StringUtil.isLettersOnly("abc123"));
    assertFalse(StringUtil.isLettersOnly("@bc"));
    assertFalse(StringUtil.isLettersOnly(" abc"));
    assertFalse(StringUtil.isLettersOnly("1abc"));
    assertFalse(StringUtil.isLettersOnly("abc "));
    assertFalse(StringUtil.isLettersOnly("abc def"));
    assertFalse(StringUtil.isLettersOnly("j0B")); // The second character is the number zero, not the character "o".
    assertTrue(StringUtil.isLettersOnly("jOb"));
  }

  public void testIsNotEmpty() throws Exception {
    assertFalse(StringUtil.isNotEmpty(null));
    assertFalse(StringUtil.isNotEmpty(""));
    assertFalse(StringUtil.isNotEmpty("   "));
    assertFalse(StringUtil.isNotEmpty("\n"));
    assertFalse(StringUtil.isNotEmpty("\t"));
    assertFalse(StringUtil.isNotEmpty("\0"));
    assertTrue(StringUtil.isNotEmpty("null"));
    assertTrue(StringUtil.isNotEmpty("A"));
    assertTrue(StringUtil.isNotEmpty("0"));
  }

  // testIsOnly

  public void testLastIndexOf() throws Exception {
    assertEquals(-1, StringUtil.lastIndexOf(null, "*"));
    assertEquals(-1, StringUtil.lastIndexOf("", "*"));
    assertEquals(-1, StringUtil.lastIndexOf(" ", "*"));
    assertEquals(-1, StringUtil.lastIndexOf("abcdefghijklmnopqrstuvwxyz", "*"));
    assertEquals(0, StringUtil.lastIndexOf("*", "*"));
    assertEquals(4, StringUtil.lastIndexOf("*****", "*"));
    assertEquals(3, StringUtil.lastIndexOf("abc*123star", "*"));
  }

  public void testLength() throws Exception {
    assertEquals(0, StringUtil.length(null));
    assertEquals(0, StringUtil.length(""));
    assertEquals(1, StringUtil.length(" "));
    assertEquals(5, StringUtil.length("     "));
    assertEquals(1, StringUtil.length("a"));
    assertEquals(5, StringUtil.length("abcde"));
    assertEquals(3, StringUtil.length("101"));
    assertEquals(3, StringUtil.length("1 1"));
  }

  public void testPad() throws Exception {
    assertEquals("Test      ", StringUtil.pad("Test", 10));
    assertEquals("Test ", StringUtil.pad("Test", 5));
    assertEquals("Test", StringUtil.pad("Test", 4));
    assertEquals("Test", StringUtil.pad("Test", 0));
  }

  public void testParseCommaSeparatedValues() throws Exception {
    // Assert null comma-separated values.

    Collection<String> actualValues = StringUtil.parseCommaSeparatedValues(null);

    assertNotNull(actualValues);
    assertTrue(actualValues.isEmpty());

    // Assert empty String comma-separated values.
    actualValues = StringUtil.parseCommaSeparatedValues("");

    assertNotNull(actualValues);
    assertTrue(actualValues.isEmpty());

    actualValues = StringUtil.parseCommaSeparatedValues("test");

    assertNotNull(actualValues);
    assertEquals(1, actualValues.size());
    assertEquals("test", actualValues.iterator().next());

    actualValues = StringUtil.parseCommaSeparatedValues("test string");

    assertNotNull(actualValues);
    assertEquals(1, actualValues.size());
    assertEquals("test string", actualValues.iterator().next());

    // NUMBERS
    final String[] numbers = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

    String expectedValues = getDelimitedValues(numbers, " ");
    actualValues = StringUtil.parseCommaSeparatedValues(expectedValues);

    assertNotNull(actualValues);
    assertEquals(1, actualValues.size());
    assertEquals(expectedValues, actualValues.iterator().next());

    expectedValues = getDelimitedValues(Arrays.asList(numbers), ":");
    actualValues = StringUtil.parseCommaSeparatedValues(expectedValues);

    assertNotNull(actualValues);
    assertEquals(1, actualValues.size());
    assertEquals(expectedValues, actualValues.iterator().next());

    expectedValues = getDelimitedValues(Arrays.asList(numbers), ", ");
    actualValues = StringUtil.parseCommaSeparatedValues(expectedValues);

    assertNotNull(actualValues);
    TestUtil.assertEquals(numbers, actualValues.toArray());

    // ANIMALS
    final String[] animals = { "spider", "", "bat", "", "lizard" };

    expectedValues = getDelimitedValues(Arrays.asList(animals), ", ");
    actualValues = StringUtil.parseCommaSeparatedValues(expectedValues);

    assertNotNull(actualValues);
    assertEquals(5, actualValues.size());
    TestUtil.assertEquals(animals, actualValues.toArray());

    // COMMAS
    expectedValues = " , , ";
    actualValues = StringUtil.parseCommaSeparatedValues(expectedValues);

    assertNotNull(actualValues);
    assertEquals(3, actualValues.size());
    TestUtil.assertEquals(new String[] { "", "", "" }, actualValues.toArray());
  }

  public void testParseDelimiterSeparatedValues() throws Exception {
    String[] expectedValues = { "zero", "one", "two" };
    Collection<String> actualValues = StringUtil.parseDelimiterSeparatedValues(getDelimitedValues(expectedValues, " "), " ");

    assertNotNull(actualValues);
    TestUtil.assertEquals(expectedValues, actualValues.toArray());

    actualValues = StringUtil.parseDelimiterSeparatedValues(getDelimitedValues(expectedValues, ":"), ":");

    assertNotNull(actualValues);
    TestUtil.assertEquals(expectedValues, actualValues.toArray());

    actualValues = StringUtil.parseDelimiterSeparatedValues(getDelimitedValues(expectedValues, ","), ",");

    assertNotNull(actualValues);
    TestUtil.assertEquals(expectedValues, actualValues.toArray());

    actualValues = StringUtil.parseDelimiterSeparatedValues(getDelimitedValues(expectedValues, "\t"), "\t");

    assertNotNull(actualValues);
    TestUtil.assertEquals(expectedValues, actualValues.toArray());
  }

  public void testRemove() throws Exception {
    assertEquals("123789", StringUtil.remove("123456789", 3, 3));
    assertEquals("acdef", StringUtil.remove("abcdef", 1, 1));
    assertEquals("af", StringUtil.remove("abcdef", 1, 4));
    assertEquals("def", StringUtil.remove("abcdef", 0, 3));
    assertEquals("abc", StringUtil.remove("abcdef", 3, 3));
    assertEquals("bcdef", StringUtil.remove("abcdef", 0, 1));
    assertEquals("abcde", StringUtil.remove("abcdef", 5, 1));
    assertEquals("abcdef", StringUtil.remove("abcdef", 3, 0));
    assertEquals("", StringUtil.remove("abcdef", 0, 6));

    try {
      assertEquals("abcde", StringUtil.remove("abcdef", 5, 10));
    }
    catch (Exception e) {
      fail("Removing text from 'abcdef' calling StringUtil.remove with offset of 5 and length of 10 should not have thrown an Exception!");
    }

    try {
      StringUtil.remove(null, 1, 10);
      fail("Calling StringUtil.remove with a null String value should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      // expected behavior
    }

    try {
      StringUtil.remove("abcdef", -1, 10);
      fail("Calling StringUtil.remove with an offset of -1 should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }

    try {
      StringUtil.remove("abcdef", 10, 1);
      fail("Calling StringUtil.remove with an offset of 10 for String value 'abcdef' should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }
  }

  public void testReplace() throws Exception {
    final String CUSTOM_TOKEN = "<CUSTOM_TOKEN>";
    final String GOT_MILK_TOKEN = "<GOT_MILK_TOKEN>";

    final StringBuffer buffer = new StringBuffer();
    buffer.append("This a test String containing several replacement tokens, ");
    buffer.append(StringUtil.REPLACEMENT_TOKEN);
    buffer.append(", that we are going ");
    buffer.append(StringUtil.REPLACEMENT_TOKEN);
    buffer.append(" to replace and ");
    buffer.append(StringUtil.REPLACEMENT_TOKEN);
    buffer.append(" then test for.");
    buffer.append("  This String also contains a custom tag, ");
    buffer.append(CUSTOM_TOKEN);
    buffer.append(" that the StringUtil.replace method should not replace.");

    String result = StringUtil.replace(buffer.toString(), StringUtil.REPLACEMENT_TOKEN, GOT_MILK_TOKEN);
    assertNotNull(result);
    assertEquals(-1, result.indexOf(StringUtil.REPLACEMENT_TOKEN));
    assertTrue(result.indexOf(GOT_MILK_TOKEN) != -1);
    assertTrue(result.indexOf(CUSTOM_TOKEN) != -1);

    final StringBuffer buffer2 = new StringBuffer();
    buffer2.append("This is another test String ");
    buffer2.append(CUSTOM_TOKEN);
    buffer2.append(" containing yet another token.");

    String result2 = StringUtil.replace(buffer2.toString(), CUSTOM_TOKEN, "");
    assertNotNull(result2);
    assertEquals(-1, result2.indexOf(CUSTOM_TOKEN));
    assertEquals("This is another test String  containing yet another token.", result2);

    final StringBuffer buffer3 = new StringBuffer();
    buffer3.append("A final test String containing a partial token, ");
    buffer3.append(GOT_MILK_TOKEN.substring(5));
    buffer3.append(", to ensure StringUtil.replace ");
    buffer3.append(" doe not try to replace the token.");

    String result3 = StringUtil.replace(buffer3.toString(), GOT_MILK_TOKEN, CUSTOM_TOKEN);
    assertNotNull(result3);
    assertEquals(-1, result3.indexOf(CUSTOM_TOKEN));
    assertTrue(result3.indexOf(GOT_MILK_TOKEN.substring(5)) != -1);
  }

  public void testReplaceUsingIndexes() throws Exception {
    assertEquals("abcdefghi", StringUtil.replace("abc123ghi", 3, 3, "def"));
    assertEquals("0123456789", StringUtil.replace("0..9", 1, 2, "12345678"));
    assertEquals("a..z", StringUtil.replace("abcdefghijklmnopqrstuvwxyz", 1, 24, ".."));
    assertEquals("abcdef", StringUtil.replace("123456def", 0, 6, "abc"));
    assertEquals("123456789", StringUtil.replace("123def", 3, 3, "456789"));
  }

  public void testSingleSpaceTokens() throws Exception {
    final String testString = "        This is      a sentence that    contains  multiple                 spaces between it's   tokens. ";
    final String resultString = "This is a sentence that contains multiple spaces between it's tokens.";

    final String result = StringUtil.singleSpaceTokens(testString);
    assertNotNull(result);
    assertEquals(resultString, result);
  }

  public void testSubstring() throws Exception {
    assertNull(StringUtil.substring(null, 10));
    assertEquals("", StringUtil.substring("", 0));
    assertEquals(" ", StringUtil.substring(" ", 0, 1));
    assertEquals("test", StringUtil.substring("testing", 0, 4));
    assertEquals("sting", StringUtil.substring("testing", 2));
  }

  public void testSubstringWithIndexOutOfBounds() throws Exception {
    try {
      StringUtil.substring("", 1, 2);
      fail("Calling substring on an empty String with begin index of 1 and end index of 2 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling substring on an empty String with begin index of 1 and end index of 2 threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    try {
      StringUtil.substring("test", -1, 2);
      fail("Calling substring on String value (test) with a negative begin index should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected
    }
    catch (Throwable t) {
      fail("Calling substring on String value (test) with a negative begin index threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    try {
      StringUtil.substring("test", 2, 1);
      fail("Calling substring on String value (test) with a begin index after the end index should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling substring on String value (test) with a begin index after the end index threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    try {
      StringUtil.substring("test", 0, 10);
      fail("Calling substring on String value (test) when the end index is greater than the length of the String value should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // // expected behavior!
    }
    catch (Throwable t) {
      fail("Calling substring on String value (test) when the end index is greater than the length of the String value threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testToLowerCase() throws Exception {
    assertNull(StringUtil.toLowerCase(null));
    assertEquals("", StringUtil.toLowerCase(""));
    assertEquals(" ", StringUtil.toLowerCase(" "));
    assertEquals("null", StringUtil.toLowerCase("null"));
    assertEquals("null", StringUtil.toLowerCase("NULL"));
    assertEquals("null", StringUtil.toLowerCase("Null"));
    assertEquals("null", StringUtil.toLowerCase("NuLl"));
  }

  public void testToString() throws Exception {
    assertEquals("Hello", StringUtil.toString("Hello"));
    assertEquals("1", StringUtil.toString(1));
    assertEquals("10.5", StringUtil.toString(10.50)); // Note: the Double class removes the end zero.
    assertEquals("true", StringUtil.toString(Boolean.TRUE));
    assertEquals("C", StringUtil.toString('C'));
    assertEquals("", StringUtil.toString(null));
    assertEquals("", StringUtil.toString(""));
  }

  public void testToUpperCase() throws Exception {
    assertNull(StringUtil.toUpperCase(null));
    assertEquals("", StringUtil.toUpperCase(""));
    assertEquals(" ", StringUtil.toUpperCase(" "));
    assertEquals("NULL", StringUtil.toUpperCase("NULL"));
    assertEquals("NULL", StringUtil.toUpperCase("null"));
    assertEquals("NULL", StringUtil.toUpperCase("Null"));
    assertEquals("NULL", StringUtil.toUpperCase("NuLl"));
  }

  public void testTrim() throws Exception {
    assertNull(StringUtil.trim(null));
    assertEquals("", StringUtil.trim(""));
    assertEquals("", StringUtil.trim(" "));
    assertEquals("test", StringUtil.trim("test"));
    assertEquals("test", StringUtil.trim("test "));
    assertEquals("test", StringUtil.trim(" test"));
    assertEquals("test", StringUtil.trim(" test "));
    assertEquals("_test_", StringUtil.trim("_test_"));
  }

  public void testTruncate() throws Exception {
    assertEquals("Testing", StringUtil.truncate("Testing", 7));
    assertEquals("Testing", StringUtil.truncate("Testing", 10));
    assertEquals("Test", StringUtil.truncate("Testing", 4));
    assertEquals("T", StringUtil.truncate("Testing", 1));
    assertEquals("", StringUtil.truncate("Testing", 0));
  }

}
