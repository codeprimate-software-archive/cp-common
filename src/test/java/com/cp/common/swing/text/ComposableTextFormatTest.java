/*
 * ComposableTextFormatTest (c) 7 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.13
 * @see com.cp.common.swing.text.ComposableTextFormat
 * @see junit.framework.TestCase
 */

package com.cp.common.swing.text;

import javax.swing.text.BadLocationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComposableTextFormatTest extends TestCase {

  public ComposableTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ComposableTextFormatTest.class);
    //suite.addTest(new ComposableTextFormatTest("testName"));
    return suite;
  }

  public void testBoundedLengthDigitsOnlyFormat() throws Exception {
    final int MAX_LENGTH = 5;
    final TextFormat textFormat = ComposableTextFormat.compose(
      new BoundedLengthTextFormat(MAX_LENGTH), new DigitsOnlyTextFormat());
    final MockDocument document = new MockDocument("", textFormat);

    try {
      document.insertString(0, "1235", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '1235' should not have thrown a BadLocationException!");
    }

    assertEquals("1235", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "4", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '4' should not have thrown a BadLocationException!");
    }

    assertEquals("12345", document.getText(0, document.getLength()));

    try {
      document.insertString(5, "67890", null); // exceeds length!
      fail("Inserting '67890' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("12345", document.getText(0, document.getLength()));

    try {
      document.remove(1, 4);
    }
    catch (Exception e) {
      fail("Removing '2345' should not have thrown an Exception!");
    }

    assertEquals("1", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "$-7.", null); // contains symbols!
      fail("Inserting '$-0l' should have thrown a BadLocationException; the value does not contain digits only!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("1", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "l.IV", null); // contains letters!
      fail("Inserting 'lO.VII' should have thrown a BadLocationException; the value does not contain digits only!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("1", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "12345", null); // exceeds length
      fail("Inserting '12345' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("1", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "0101", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '0101' should not have thrown a BadLocationException!");
    }

    assertEquals("01011", document.getText(0, document.getLength()));
  }

  public void testBoundedLengthLettersOnlyFormat() throws Exception {
    final int MAX_LENGTH = 5;
    final TextFormat textFormat = ComposableTextFormat.compose(
      new BoundedLengthTextFormat(MAX_LENGTH), new LettersOnlyTextFormat());
    final MockDocument document = new MockDocument("", textFormat);

    try {
      document.insertString(0, "abce", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'abce' should not have thrown a BadLocationException!");
    }

    assertEquals("abce", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "d", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'd' should not have thrown a BadLocationException!");
    }

    assertEquals("abcde", document.getText(0, document.getLength()));

    try {
      document.insertString(5, "fghij", null); // exceeds length!
      fail("Inserting 'fghij' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("abcde", document.getText(0, document.getLength()));

    try {
      document.remove(1, 4);
    }
    catch (BadLocationException e) {
      fail("Removing 'bcde' should not have thrown an Exception!");
    }

    assertEquals("a", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "$-X.", null); // contains symbols!
      fail("Inserting '$-0l' should have thrown a BadLocationException; the value does not contain letters only!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("a", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "1001", null); // contains numbers!
      fail("Inserting '1001' should have thrown a BadLocationException; the value does not contain letters only!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("a", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "abcde", null); // exceeds length
      fail("Inserting 'abcde' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("a", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "ABcD", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'ABcD' should not have thrown a BadLocationException!");
    }

    assertEquals("ABcDa", document.getText(0, document.getLength()));
  }

}
