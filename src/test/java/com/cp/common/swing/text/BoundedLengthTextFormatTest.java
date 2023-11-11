/*
 * BoundedLengthTextFormatTest (c) 7 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.9
 */

package com.cp.common.swing.text;

import javax.swing.text.BadLocationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoundedLengthTextFormatTest extends TestCase {

  public BoundedLengthTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedLengthTextFormatTest.class);
    //suite.addTest(new BoundedLengthTextFormatTest("testName"));
    return suite;
  }

  public void testFormat0() throws Exception {
    final int MAX_LENGTH = 26;
    final MockDocument document = new MockDocument("", new BoundedLengthTextFormat(MAX_LENGTH));

    try {
      document.insertString(0, "def", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'def' should not have thrown a BadLocationException!");
    }

    assertEquals("def", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "abc", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'abc' should not have thrown a BadLocationException!");
    }

    assertEquals("abcdef", document.getText(0, document.getLength()));

    try {
      document.insertString(6, "ghijklmnop", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'ghijklmnop' should not have thrown a BadLocationException!");
    }

    assertEquals("abcdefghijklmnop", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "xyz", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'xyz' should not have thrown a BadLocationException!");
    }

    assertEquals("abcdefghijklmnopxyz", document.getText(0, document.getLength()));

    try {
      document.insertString(16, "qrstuvw", null);
    }
    catch (BadLocationException e) {
      fail("Inserting 'qrstuvw' should not have thrown a BadLocationException!");
    }

    assertEquals("abcdefghijklmnopqrstuvwxyz", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "123", null);
      fail("Inserting '123' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("abcdefghijklmnopqrstuvwxyz", document.getText(0, document.getLength()));

    try {
      document.replace(3, 3, "123456", null);
      fail("Replacing 'def' with '123456' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("abcdefghijklmnopqrstuvwxyz", document.getText(0, document.getLength()));

    try {
      document.replace(3, 20, "123", null);
    }
    catch (BadLocationException e) {
      fail("Replacing 'defghijklmnopqrstuvw' with '123' should not have thrown a BadLocationException!");
    }

    assertEquals("abc123xyz", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "01234567891011121314151617181920", null);
      fail("Inserting '01234567891011121314151617181920' should have thrown a BadLocationException; the value exceeds the maximu length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("abc123xyz", document.getText(0, document.getLength()));
  }

  public void testFormat1() throws Exception {
    final int MAX_LENGTH = 10;
    final MockDocument document = new MockDocument("", new BoundedLengthTextFormat(MAX_LENGTH));

    try {
      document.insertString(0, "0123456789", null); // insert max length value
    }
    catch (BadLocationException e) {
      fail("Inserting '0123456789' should not have thrown a BadLocationException; the value does not exceed the maximum length ("
        + MAX_LENGTH + ")!");
    }

    assertEquals("0123456789", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "", null); // insert empty String
    }
    catch (BadLocationException e) {
      fail("Inserting '' should not have thrown a BadLocationException; the value does not exceed the maximum length ("
        + MAX_LENGTH + ")!");
    }

    assertEquals("0123456789", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "101", null); // insert 101
      fail("Inserting '101' should have thrown a BadLocationException; the value exceeds the maximum length ("
        + MAX_LENGTH + ")!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("0123456789", document.getText(0, document.getLength()));

    try {
      document.replace(2, 8, "", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '23456789' with an empty String should not have thrown a BadLocationException!");
    }

    assertEquals("01", document.getText(0, document.getLength()));

    try {
      document.insertString(2, "101", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '101' should not have thrown a BadLocationException; the value does not exceed the maximum length ("
        + MAX_LENGTH + ")!");
    }

    assertEquals("01101", document.getText(0, document.getLength()));
  }

}
