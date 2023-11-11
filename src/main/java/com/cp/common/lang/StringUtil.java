/*
 * StringUtil.java (c) 17 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.3.24
 * @see java.lang.String
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.BooleanUtil
 * @see com.cp.common.lang.CharacterUtil
 * @see com.cp.common.lang.NumberUtil
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.util.ArrayUtil
 * @see java.lang.String
 * @see java.util.StringTokenizer
 */

package com.cp.common.lang;

import com.cp.common.util.ArrayUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class StringUtil {

  private static final Log log = LogFactory.getLog(StringUtil.class);

  public static final String REPLACEMENT_TOKEN = "<REPLACEMENT_TOKEN>";
  private static final String COMMA_DELIMETER = ",";
  private static final String SPACE_DELIMETER = " ";

  /**
   * Default private constructor to prevent instantiation of the StringUtil utility class.
   */
  private StringUtil() {
  }

  /**
   * Gets the character at index in the specified String value.
   * @param value the String value from which to extract the character at the specified index.
   * @param index an integer value specifying the index within the specified String value to extract the character.
   * @return the character value at the specified index in the specified String value, or the null character
   * if the specified String value is null.
   * @throws IndexOutOfBoundsException if the index into the specified String value is not valid (negative
   * or greater than the length of the String value).
   */
  public static char charAt(final String value, final int index) {
    return (ObjectUtil.isNull(value) ? '\u0000' : value.charAt(index));
  }

  /**
   * Creates an Iterator to interate over the characters of the String value.
   * @param value the String value containing characters to be iterated over.
   * @return an Iterator of the characters in the String value.
   * @throws java.lang.NullPointerException if the String value is null.
   * @see java.text.StringCharacterIterator
   */
  public static Iterator charIterator(final String value) {
    Assert.notNull(value, "The String value cannot be null!");
    return ArrayUtil.asList(value.toCharArray()).iterator();
  }

  /**
   * Iterates the specified String value determining whether the String valiue contains a certain character or not.
   * @param value the String value in question for containment of a particular character.
   * @param containsStrategy the strategy for determining which type of character the String value should contain.
   * @return a boolean of true if the String value contains the particular character determined by the Strategy class,
   * false otherwise.
   */
  private static boolean contains(final String value, final ContainsStrategy containsStrategy) {
    Assert.notNull(value, "The String value cannot be null!");

    for (final char character : value.toCharArray()) {
      if (containsStrategy.accept(character)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Determines whether the sourceValue String contains the containedValue parameter.
   * @param sourceValue the containment String
   * @param containedValue the value contained by the sourceValue String.
   * @return a boolean indicating whether the containedValue is indeed contained in the sourceValue String.
   */
  public static boolean contains(final String sourceValue, final String containedValue) {
    return (!isEmpty(sourceValue) && (sourceValue.indexOf(containedValue) != -1));
  }

  /**
   * Determines whether the specified String value contains any Digit characters.
   * @param value the String value.
   * @return a boolean value indicating whether the specified String value contains any Digit characters.
   * @see StringUtil#contains(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean containsDigit(final String value) {
    return contains(value, ContainsDigitStrategy.INSTANCE);
  }

  /**
   * Determines whether the sourceValue String contains the containedValue parameter, ignoring case.
   * @param sourceValue the containment String
   * @param containedValue the value contained by the sourceValue String.
   * @return a boolean indicating whether the containedValue is indeed contained in the sourceValue String.
   */
  public static boolean containsIgnoreCase(String sourceValue, String containedValue) {
    sourceValue = ObjectUtil.getDefaultValue(sourceValue, "").toLowerCase();
    containedValue = ObjectUtil.getDefaultValue(containedValue, "").toLowerCase();
    return contains(sourceValue, containedValue);
  }

  /**
   * Determines whether the specified String value contains any Letter characters.
   * @param value the String value.
   * @return a boolean value indicating whether the specified String value contains any Letter characters.
   * @see StringUtil#contains(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean containsLetter(final String value) {
    return contains(value, ContainsLetterStrategy.INSTANCE);
  }

  /**
   * Determines whether the specified String value contains any Lowercase Letter characters.
   * @param value the String value.
   * @return a boolean value indicating whether the specified String value contains any Lowercase Letter characters.
   * @see StringUtil#contains(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean containsLowerCaseLetter(final String value) {
    return contains(value, ContainsLowerCaseLetterStrategy.INSTANCE);
  }

  /**
   * Determines whether the specified String value contains any Uppercase Letter characters.
   * @param value the String value.
   * @return a boolean value indicating whether the specified String value contains any Uppercase Letter characters.
   * @see StringUtil#contains(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean containsUpperCaseLetter(final String value) {
    return contains(value, ContainsUpperCaseLetterStrategy.INSTANCE);
  }

  /**
   * Determines whether the specified String value contains any whitespace characters.
   * @param value the String value.
   * @return a boolean value indicating whether the specified String value contains any whitespace characters.
   * @see StringUtil#contains(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean containsWhitespace(final String value) {
    return contains(value, ContainsWhitespaceStrategy.INSTANCE);
  }

  /**
   * Counts the number of characters, where the character value is determined by the ContainsStrategy object,
   * in the specified String value.
   * @param value the String value used to count characters detemined by the ContainsStrategy object.
   * @param containsStrategy the ContainsStrategy instance that determines the type of character to count in
   * the String value.
   * @return the count value of the specified character in the String value.
   */
  private static int count(final String value, final ContainsStrategy containsStrategy) {
    Assert.notNull(value, "The String value cannot be null!");

    int count = 0;

    for (final char character : value.toCharArray()) {
      if (containsStrategy.accept(character)) {
        count++;
      }
    }

    return count;
  }

  /**
   * Counts the number of Digit characters in the specified String value.
   * @param value the String value used to count Digit characters.
   * @return the number of Digit characters contained in the String value.
   * @see StringUtil#count(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static int countDigits(final String value) {
    return count(value, ContainsDigitStrategy.INSTANCE);
  }

  /**
   * Counts the number of Letter characters in the specified String value.
   * @param value the String value used to count Letter characters.
   * @return the number of Letter characters contained in the String value.
   * @see StringUtil#count(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static int countLetters(final String value) {
    return count(value, ContainsLetterStrategy.INSTANCE);
  }

  /**
   * Counts the number of Lowercase Letter characters in the specified String value.
   * @param value the String value used to count Lowercase Letter characters.
   * @return the number of Lowercase Letter characters contained in the String value.
   * @see StringUtil#count(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static int countLowerCaseLetters(final String value) {
    return count(value, ContainsLowerCaseLetterStrategy.INSTANCE);
  }

  /**
   * Counts the number of occurences of the contained String value in the sourceValue String.
   * @param sourceValue the String value to determine the number of occurences of containedValue.
   * @param containedValue the String value being counted for occurences in the sourceValue String.
   * @return the number of occurences of containedValue in sourceValue.
   */
  public static int countOccurences(String sourceValue, final String containedValue) {
    int count = 0;

    if (contains(sourceValue, containedValue)) {
      int index = -1;
      while ((index = sourceValue.indexOf(containedValue)) != -1) {
        count++;
        sourceValue = sourceValue.substring(index + 1);
      }
    }

    return count;
  }

  /**
   * Counts the number of Uppercase Letter characters in the specified String value.
   * @param value the String value used to count Uppercase Letter characters.
   * @return the number of Uppercase Letter characters contained in the String value.
   * @see StringUtil#count(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static int countUpperCaseLetters(final String value) {
    return count(value, ContainsUpperCaseLetterStrategy.INSTANCE);
  }

  /**
   * Counts the number of whitespace characters in the specified String value.
   * @param value the String value used to count whitespace characters.
   * @return the number of whitespace characters contained in the String value.
   * @see StringUtil#count(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static int countWhitespace(final String value) {
    return count(value, ContainsWhitespaceStrategy.INSTANCE);
  }

  /**
   * Compares two String values for equality.  The two String values are equal if both are null
   * or both contain the same characters in the String value regardless of the Strings case.  Thus
   * the equality comparison is case-insensitive.
   * @param str1 the first String value in the equality comparsion.
   * @param str2 the second String value in the equality comparsion.
   * @return a boolean value indicating if the String values are equal case insensitive.
   * @see String#equalsIgnoreCase(String)
   */
  public static boolean equalsIgnoreCase(final String str1, final String str2) {
    return (ObjectUtil.isNull(str1) ? ObjectUtil.isNull(str2) : str1.equalsIgnoreCase(str2));
  }

  /**
   * Returns only characters accepted by the ContainsStrategy from the specified String value.
   * @param value the String value to return characters from as accepted by the ContainsStrategy.
   * @param containsStrategy the ContainsStrategy instance used in determining the type of characters
   * from the specified String value that will be returned.
   * @return a String containing characters from the specified String value accepted by the ContainsStrategy.
   * @see StringUtil#getOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static String getCharactersOnly(final String value, final ContainsStrategy containsStrategy) {
    return getOnly(value, containsStrategy);
  }

  /**
   * Returns only the digit characters contained in the specified String value.
   * @param value the String from which only digit characters will be pulled.
   * @return a new String containing only digit characters from the original String.
   * @see StringUtil#getOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static String getDigitsOnly(final String value) {
    return getOnly(value, ContainsDigitStrategy.INSTANCE);
  }

  /**
   * Returns an empty String if the String value is null or returns the String value if not null.
   * @param value the String value evaluated.
   * @return an empty String if the String value is null.
   * @see StringUtil#getNullIfEmpty(String)
   */
  public static String getEmptyIfNull(final String value) {
    return (ObjectUtil.isNull(value) ? "" : value);
  }

  /**
   * Returns only the letter characters contained in the specified String value.
   * @param value the String from which only letter characters will be pulled.
   * @return a new String containing only letter characters from the original String.
   * @see StringUtil#getOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static String getLettersOnly(final String value) {
    return getOnly(value, ContainsLetterStrategy.INSTANCE);
  }

  /**
   * Returns null if the String value is null or an empty String, or returns the String value if neither.
   * @param value the String value evaluated.
   * @return null if the String value is null or an empty String.
   * @see StringUtil#getEmptyIfNull(String)
   */
  public static String getNullIfEmpty(final String value) {
    return ("".equals(trim(value)) ? null : value);
  }

  /**
   * Constructs a new String value from the specified String containing only characters determined by the
   * ContainsStrategy instance.  This method is meant to extract certain characters from a String value.
   * @param value the String value from which the characters determined by the ContainsStrategy will extract.
   * @param containsStrategy the ContainsStrategy instance that determines the type of character to extract
   * from the String value (digits, letters, etc).
   * @return a new String value containing only characters determined by the ContainsStrategy instance, or an
   * empty String if no such character exists in the String value.  If the original String value is null or empty,
   * it will return the String value.
   * @see StringUtil#getDigitsOnly
   * @see StringUtil#getLettersOnly
   */
  private static String getOnly(final String value, final ContainsStrategy containsStrategy) {
    if (isNotEmpty(value)) {
      final StringBuffer buffer = new StringBuffer();

      for (final char character : value.toCharArray()) {
        if (containsStrategy.accept(character)) {
          buffer.append(character);
        }
      }

      return buffer.toString();
    }
    else {
      return value;
    }
  }

  /**
   * Determines the first index of the specified character in the String value.
   * @param value the String value to search for the specified character in.
   * @param chr a String specifying the character to find in the specified String value.
   * @return an integer value indicating the index of the first occurrence of the character in the String value.
   * @see StringUtil#lastIndexOf(String, String)
   */
  public static int indexOf(final String value, final String chr) {
    return (isEmpty(value) ? -1 : value.indexOf(chr));
  }

  /**
   * Inserts the specified insertValue into the specified String value.
   * @param value the String value in which the insertValue will be inserted.
   * @param insertValue the value to insert into the String value.
   * @param offset the offset into the String value at which to insert the insertValue.
   * @return a String value with the insertValue inserted into the String value.
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if the offset is negative or greater than String length.
   * @see StringUtil#remove(String, int, int)
   */
  public static String insert(final String value, final String insertValue, final int offset) {
    Assert.notNull(value, "Cannot insert value (" + insertValue + ") into a null String!");
    Assert.isFalse(offset < 0 || offset > value.length(), "The offset (" + offset
      + ") cannot be negative or greater than the String value's length (" + value.length() + ")!");

    final StringBuffer buffer = new StringBuffer(value.substring(0, offset));
    buffer.append(insertValue);
    buffer.append(value.substring(offset));
    return buffer.toString();
  }

  /**
   * Determines whether the specified String contains only digit or letter characters.
   * @param value String to test for digits only.
   * @return a boolean value indicating whether the String consists of only digits and letters.
   * @see StringUtil#isOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean isAlphanumericOnly(final String value) {
    return isOnly(value, ContainsAlphanumericStrategy.INSTANCE);
  }

  /**
   * Determines whether the specified String value is blank.  A String is blank if it is null, is the empty String,
   * or contains spaces.
   * @param value the String value used in the determination of blankness.
   * @return a boolean value indicating whether the String value is blank.
   * @see StringUtil#isEmpty(String)
   * @see StringUtil#isNotBlank(String)
   */
  public static boolean isBlank(final String value) {
    if (log.isDebugEnabled()) {
      log.debug("value (" + value + ")");
    }

    return (ObjectUtil.isNull(value) || "".equals(value.trim()));
  }

  /**
   * Determines whether the specified String value contains characters accepted by the ContainsStrategy.
   * @param value the String value to test for containment as defined and accepted by the ContainsStrategy.
   * @param containsStrategy the ContainsStrategy instance used in determining the type fo character
   * that the String must ONLY contain to pass the test.
   * @return a boolean value indicating whether the specified String value only contains characters accepted
   * by the ContainsStrategy.
   * @see StringUtil#isOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean isCharatersOnly(final String value, final ContainsStrategy containsStrategy) {
    return isOnly(value, containsStrategy);
  }

  /**
   * Determines whether the specified String contains only digit characters.
   * @param value String to test for digits only.
   * @return a boolean value indicating whether the String consists of all digits.
   * @see StringUtil#isOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean isDigitsOnly(final String value) {
    return isOnly(value, ContainsDigitStrategy.INSTANCE);
  }

  /**
   * Detemines whether the specified String value is equal to the empty String.
   * @param value the String value used in the determination for emptiness.
   * @return a boolean value indicating whether the String value is empty.
   * @see StringUtil#isBlank(String)
   * @see StringUtil#isNotEmpty(String)
   */
  public static boolean isEmpty(final String value) {
    if (log.isDebugEnabled()) {
      log.debug("value (" + value + ")");
    }

    return "".equals(value);
  }

  /**
   * Determines whether the specified String contains only letter characters.
   * @param value String to test for letters only.
   * @return a boolean value indicating whether the String consists of all letters.
   * @see StringUtil#isOnly(String, com.cp.common.lang.StringUtil.ContainsStrategy)
   */
  public static boolean isLettersOnly(final String value) {
    return isOnly(value, ContainsLetterStrategy.INSTANCE);
  }

  /**
   * The inverse operation of the isBlank method, determining whether the specified String value is not blank.
   * @param value the String value used in the determination for non-blankness.
   * @return a boolean value indicating whether the String value is not blank.
   * @see StringUtil#isBlank(String)
   * @see StringUtil#isNotEmpty(String)
   */
  public static boolean isNotBlank(final String value) {
    return !isBlank(value);
  }

  /**
   * The inverse operation of the isEmpty method, determining whether the specified String value
   * is not the empty String.
   * @param value the String value used in the determination for non-emptiness.
   * @return a boolean value indicating whether the String value is not empty.
   * @see StringUtil#isEmpty(String)
   * @see StringUtil#isNotBlank(String)
   */
  public static boolean isNotEmpty(final String value) {
    return !isEmpty(value);
  }

  /**
   * Determines whether the specified String value contains ONLY instances of the specified character
   * determined by the ContainsStrategy object.
   * @param value String value tested for containment of only one type of character value.
   * @param containsStrategy the ContainsStrategy instance used in determining the type fo character
   * that the String must ONLY contain to pass the test.
   * @return a boolean value indicating that the String value contains ONLY instances of the specified
   * character value.
   */
  private static boolean isOnly(final String value, final ContainsStrategy containsStrategy) {
    if (ObjectUtil.isNotNull(value)) {
      for (final char character : value.toCharArray()) {
        if (!containsStrategy.accept(character)) {
          return false;
        }
      }

      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Determines the last index of the specified character in the String value.
   * @param value the String value to search for the specified character in.
   * @param chr a String specifying the character to find in the specified String value.
   * @return an integer value indicating the index of the last occurrence of the character in the String value.
   * @see StringUtil#indexOf(String, String)
   */
  public static int lastIndexOf(final String value, final String chr) {
    return (isEmpty(value) ? -1 : value.lastIndexOf(chr));
  }

  /**
   * Determines the length of the specified String value.  If the String is null or an empty String,
   * the method returns 0.
   * @param value the String value who's length is being determined.
   * @return an integer value specifying the length of the specified String value.
   */
  public static int length(final String value) {
    return (ObjectUtil.isNull(value) ? 0 : value.length());
  }

  /**
   * Pads the String value with whitespace characters up to totalLength, which equals the sum of value.length()
   * and (totalLength - value.length()).
   * @param value the String value to pad with whitespace.
   * @param totalLength the length of the String value + the length of whitespace should equal totalLength.
   * @return the String value padded with whitespace characters.
   * @throws java.lang.NullPointerException if the value is null.
   */
  public static String pad(final String value, final int totalLength) {
    Assert.notNull(value, "The String value to pad cannot be null!");

    final StringBuffer buffer = new StringBuffer(value);

    for (int index = (totalLength - value.length()); --index >= 0; ) {
      buffer.append(" ");
    }

    return buffer.toString();
  }

  /**
   * Parses a String of comma-separated values separating out the values into elements of a Collection object.
   * @param commaSeparatedValues a String containing comma-separated values.
   * @return a Collection of the values broken out of the comma delimeted String.
   * @see StringUtil#parseDelimiterSeparatedValues(String, String)
   */
  public static Collection<String> parseCommaSeparatedValues(final String commaSeparatedValues) {
    return parseDelimiterSeparatedValues(commaSeparatedValues, COMMA_DELIMETER);
  }

  /**
   * Parses a String of delimited (such as a comma or colon) values separating out the values into elements
   * of a Collection.
   * @param delimiterSeperatedValues a String containing delimited values as specified by the delimiter value.
   * @param delimiter a String specifying the delimiter, or value separator.
   * @return a Collection of String values from the String of delimited values.
   */
  public static Collection<String> parseDelimiterSeparatedValues(final String delimiterSeperatedValues, final String delimiter) {
    final Collection<String> values = new LinkedList<String>();

    if (isNotEmpty(delimiterSeperatedValues)) {
      final StringTokenizer parser = new StringTokenizer(delimiterSeperatedValues,
        ObjectUtil.getDefaultValue(delimiter, COMMA_DELIMETER));

      for ( ; parser.hasMoreTokens(); ) {
        values.add(parser.nextToken().trim());
      }
    }

    return values;
  }

  /**
   * Removes a portion of text from the source String.
   * @param source the source String from which the text will be removed.
   * @param offset an integer value specifying the offset into the source to start removing text.
   * @param length an integer value specifying the number of characters to remove from the source String.
   * @return the source String with the text between offset and offset + length removed.
   * @throws java.lang.NullPointerException if the source String is null.
   * @throws java.lang.IllegalArgumentException if the offset is less than 0 or greater than the source length.
   * @see StringUtil#insert(String, String, int)
   */
  public static String remove(final String source, final int offset, int length) {
    Assert.notNull(source, "Cannot remove text from a null String value!");
    Assert.isFalse(offset < 0 || offset > source.length(), "The offset must be between 0 and " + source.length() + "!");

    length = Math.min(length, (source.length() - offset));

    if (log.isDebugEnabled()) {
      log.debug("adjusted length (" + length + ")");
    }

    final StringBuffer buffer = new StringBuffer();
    buffer.append(source.substring(0, offset));
    buffer.append(source.substring(offset + length));
    return buffer.toString();
  }

  /**
   * Performs a replace operation by removing text between offset and offset + length from the source String
   * and inserting the specified value.
   * @param source the String value from which the text will be replaced.
   * @param offset the start index of the text to replace with value.
   * @param length the number of characters from the offset, constituting the text that will be replaced
   * from the source String with the specified value.
   * @param value the String value used to replace the text from the source String between offset and offset + length.
   * @return the source String with the text between offset and offset + length replaced with the specified String value.
   * @throws java.lang.NullPointerException if the source String is null.
   * @see StringUtil#insert(String, String, int)
   * @see StringUtil#remove(String, int, int)
   * @see StringUtil#replace(String, String, String)
   */
  public static String replace(final String source, final int offset, final int length, final String value) {
    return insert(remove(source, offset, length), value, offset);
    //return replace(source, source.substring(offset, length), value);
  }

  /**
   * Replaces all occurences of token with replacement in source String.
   * @param source the String containing tokens to replace.
   * @param token the Replacement token contained in the source String.
   * @param replacement the String used as the replacement for token.
   * @return a new String that contains replacement Strings for token Strings.
   * @throws java.lang.NullPointerException if the source String is null.
   * @see java.lang.String#replaceAll
   */
  public static String replace(String source, final String token, final String replacement) {
    Assert.notNull(source, "The source String cannot be null!");

    final StringBuffer buffer = new StringBuffer();

    for (int index = source.indexOf(token); index != -1; index = source.indexOf(token)) {
      buffer.append(source.substring(0, index));
      buffer.append(replacement);
      source = source.substring(index + token.length());
    }

    buffer.append(source);

    return buffer.toString();
  }

  /**
   * Single spaces tokens (words, numbers, special characters etc) in the source String.
   * @param source the String containing multiple spaces between it's tokens.
   * @return a new String with the tokens in the source String single spaced.
   */
  public static String singleSpaceTokens(final String source) {
    Assert.notNull(source, "The source String cannot be null!");

    final StringTokenizer parser = new StringTokenizer(source, SPACE_DELIMETER);
    final StringBuffer buffer = new StringBuffer();

    while (parser.hasMoreTokens()) {
      buffer.append(parser.nextToken());
      buffer.append(SPACE_DELIMETER);
    }

    return buffer.toString().trim();
  }

  /**
   * Gets a substring starting at the specified begin index from the String value.
   * @param value the String value to extract the substring from.
   * @param beginIndex an integer value specifying the begin index to start extracting the substring from
   * the specified String value.
   * @return a String value containing the substring from the String value starting at the begin index.
   * @see StringUtil#substring(String, int, int)
   */
  public static String substring(final String value, final int beginIndex) {
    return (ObjectUtil.isNull(value) ? null : value.substring(beginIndex));
  }

  /**
   * Gets a substring starting at the specified begin index and finishing at the specified end index from
   * the String value.
   * @param value the String value to extract the substring from.
   * @param beginIndex an integer value specifying the begin index to start extracting the substring from
   * the specified String value.
   * @param endIndex an integer value specifying the end index to finish extracting the substring from
   * the specified String value.
   * @return a String value containing the substring from the String value starting at the begin index and finishing
   * at the end index.
   * @see StringUtil#substring(String, int)
   */
  public static String substring(final String value, final int beginIndex, final int endIndex) {
    return (ObjectUtil.isNull(value) ? null : value.substring(beginIndex, endIndex));
  }

  /**
   * Converts the contents of the String value to lower case.
   * @param value a String value.
   * @return a String with the contents of the specified String value in lower case characters.
   * @see StringUtil#toUpperCase(String)
   */
  public static String toLowerCase(final String value) {
    return (isEmpty(value) ? value : value.toLowerCase());
  }

  /**
   * Returns the toString representation of the Object if the Object is not null, or an empty String
   * if the Object is null.
   * @param value Object value for which the String representation is returned.
   * @return a String representation of the Object value.
   * @see Object#toString()
   * @see ObjectUtil#toString()
   */
  public static String toString(final Object value) {
    return (ObjectUtil.isNull(value) ? "" : value.toString());
  }

  /**
   * Converts the contents of the String value to upper case.
   * @param value a String value.
   * @return a String with the contents of the specified String value in upper case characters.
   * @see StringUtil#toLowerCase(String)
   */
  public static String toUpperCase(final String value) {
    return (isEmpty(value) ? value : value.toUpperCase());
  }

  /**
   * Trims whitespace off the front and end of the String value.
   * @param value the String to trim.
   * @return a trimmed version of the String value.
   * @see String#trim()
   */
  public static String trim(final String value) {
    return (ObjectUtil.isNull(value) ? null : value.trim());
  }

  /**
   * Shortens the specified String value to be no larger than maxLength.
   * @param value the String to truncate.
   * @param maxLength the maximum number of characters from the String value that should be retained.
   * @return a truncated String value upto and including maxLength in characters, or null if the String value is null.
   * @see String#substring(int, int)
   */
  public static String truncate(final String value, final int maxLength) {
    Assert.greaterThanEqual(maxLength, 0, "The max length must be greater than or equal to 0!");
    return (ObjectUtil.isNull(value) ? null : value.substring(0, Math.min(maxLength, value.length())));
  }

  public static interface ContainsStrategy {

    public boolean accept(Character character);

    public boolean accept(char character);

  }

  public static abstract class AbstractContainsStrategy implements ContainsStrategy {

    public boolean accept(final Character character) {
      return accept(character.charValue());
    }

    public String toString() {
      return getClass().getName();
    }
  }

  /**
   * ContainsStrategy accepting only digits or letters.
   */
  private static class ContainsAlphanumericStrategy extends AbstractContainsStrategy {

    public static final ContainsAlphanumericStrategy INSTANCE = new ContainsAlphanumericStrategy();

    public boolean accept(final char character) {
      return (Character.isDigit(character) || Character.isLetter(character));
    }
  }

  /**
   * ContainsStrategy accepting only digits.
   */
  private static class ContainsDigitStrategy extends AbstractContainsStrategy {

    public static final ContainsDigitStrategy INSTANCE = new ContainsDigitStrategy();

    public boolean accept(final char character) {
      return Character.isDigit(character);
    }
  }

  /**
   * ContainsStrategy accepting only letters.
   */
  private static class ContainsLetterStrategy extends AbstractContainsStrategy {

    public static final ContainsLetterStrategy INSTANCE = new ContainsLetterStrategy();

    public boolean accept(final char character) {
      return Character.isLetter(character);
    }
  }

  /**
   * ContainsStrategy accepting only lowercase letters.
   */
  private static class ContainsLowerCaseLetterStrategy extends AbstractContainsStrategy {

    public static final ContainsLowerCaseLetterStrategy INSTANCE = new ContainsLowerCaseLetterStrategy();

    public boolean accept(final char character) {
      return (Character.isLetter(character) && Character.isLowerCase(character));
    }
  }

  /**
   * ContainsStrategy accepting only uppercase letters.
   */
  private static class ContainsUpperCaseLetterStrategy extends AbstractContainsStrategy {

    public static final ContainsUpperCaseLetterStrategy INSTANCE = new ContainsUpperCaseLetterStrategy();

    public boolean accept(final char character) {
      return (Character.isLetter(character) && Character.isUpperCase(character));
    }
  }

  /**
   * ContainsStrategy accepting only whitespace characters.
   */
  private static class ContainsWhitespaceStrategy extends AbstractContainsStrategy {

    public static final ContainsWhitespaceStrategy INSTANCE = new ContainsWhitespaceStrategy();

    public boolean accept(final char character) {
      return Character.isWhitespace(character);
    }
  }

  /**
   * ContainsStrategy accepting only currency values.
   */
  public static class ContainsCurrencyStrategy extends ContainsNumericStrategy {

    public static final ContainsCurrencyStrategy INSTANCE = new ContainsCurrencyStrategy();

    public boolean accept(final char character) {
      return (super.accept(character) || character == '$');
    }
  }

  /**
   * ContainsStrategy accepting only number values.
   */
  public static class ContainsNumericStrategy extends AbstractContainsStrategy {

    public static final ContainsNumericStrategy INSTANCE = new ContainsNumericStrategy();

    public boolean accept(final char character) {
      return (Character.isDigit(character) || character == '-' || character == '.');
    }
  }

  /**
   * ContainsStrategy accepting only percentage values.
   */
  public static class ContainsPercentageStrategy extends ContainsNumericStrategy {

    public static final ContainsPercentageStrategy INSTANCE = new ContainsPercentageStrategy();

    public boolean accept(final char character) {
      return (super.accept(character) || character == '%');
    }
  }

}
