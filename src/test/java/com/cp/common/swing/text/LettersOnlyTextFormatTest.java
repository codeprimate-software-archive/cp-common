/*
 * LettersOnlyTextFormatTest (c) 7 October 2004
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

public class LettersOnlyTextFormatTest extends TestCase {

  public LettersOnlyTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LettersOnlyTextFormatTest.class);
    //suite.addTest(new LettersOnlyTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new LettersOnlyTextFormat());

    try {
      document.insertString(0, "abc", null); // prepend abc
    }
    catch (BadLocationException e) {
      fail("Inserting 'abc' should not have thrown a BadLocationException!");
    }

    assertEquals("abc", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "ghi", null); // append ghi
    }
    catch (BadLocationException e) {
      fail("Inserting 'ghi' should not have thrown a BadLocationException!");
    }

    assertEquals("abcghi", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "def", null); // insert def
    }
    catch (BadLocationException e) {
      fail("Inserting 'def' should not have thrown a BadLocationException!");
    }

    assertEquals("abcdefghi", document.getText(0, document.getLength()));

    try {
      document.replace(2, 5, "ABC", null); // replace cdefg with ABC
    }
    catch (BadLocationException e) {
      fail("Replacing 'cdefg' with 'ABC' should not have thrown a BadLocationException!");
    }

    assertEquals("abABChi", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "$-123", null);
      fail("Inserting '$-123' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }
  }

}
