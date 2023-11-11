/*
 * DigitsOnlyTextFormatTest (c) 7 October 2004
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

public class DigitsOnlyTextFormatTest extends TestCase {

  public DigitsOnlyTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DigitsOnlyTextFormatTest.class);
    //suite.addTest(new DigitsOnlyTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new DigitsOnlyTextFormat());

    try {
      document.insertString(0, "0123", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '0123' should not have thrown a BadLocationException!");
    }

    assertEquals("0123", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "IV", null); // try inserting a roman numeral!
      fail("Inserting 'IV' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("0123", document.getText(0, document.getLength()));

    try  {
      document.insertString(0, "456", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '456' should not have thrown a BadLocationException!");
    }

    assertEquals("4560123", document.getText(0, document.getLength()));

    try  {
      document.insertString(3, "789", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '789' should not have thrown a BadLocationException!");
    }

    assertEquals("4567890123", document.getText(0, document.getLength()));

    try {
      document.replace(0, document.getLength(), "lOlOOl", null); // try inserting letter binary!
      fail("Inserting 'lOlOOl' (letters, not number) should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("4567890123", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "$-", null); // try inserting symbols!
      fail("Inserting '$-' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("4567890123", document.getText(0, document.getLength()));

    try {
      document.insertString(3, ".", null); // try inserting a decimal point!
      fail("Inserting '.' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("4567890123", document.getText(0, document.getLength()));

    try {
      document.replace(0, document.getLength(), "10101", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '10101' should not have thrown a BadLocationException!");
    }

    assertEquals("10101", document.getText(0, document.getLength()));

    try  {
      document.insertString(1, "one", null); // trying inserting the text one
      fail("Inserting 'one' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("10101", document.getText(0, document.getLength()));
  }

}
