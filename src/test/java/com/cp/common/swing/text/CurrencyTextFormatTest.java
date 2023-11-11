/*
 * CurrencyTextFormatTest (c) 8 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.12
 */

package com.cp.common.swing.text;

import javax.swing.text.BadLocationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CurrencyTextFormatTest extends TestCase {

  public CurrencyTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CurrencyTextFormatTest.class);
    //suite.addTest(new CurrencyTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new CurrencyTextFormat());

    try {
      document.insertString(0, "101.50", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '101.50' should not have thrown a BadLocationException!");
    }

    assertEquals("$101.50", document.getText(0, document.getLength()));

    try {
      document.replace(5, 2, "145", null); // exceeds the maximum allowed digits after the decimal point
      fail("Replacing '50' cents with '145' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("$101.50", document.getText(0, document.getLength()));

    try {
      document.remove(6, 1);
    }
    catch (BadLocationException e) {
      fail("Removing '0' from the 50 should not have thrown a BadLocationException!");
    }

    assertEquals("$101.5", document.getText(0, document.getLength()));

    try {
      document.replace(3, 3, ".10", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '1.5' with '.10' should not have thrown a BadLocationException!");
    }

    assertEquals("$10.10", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), ".50", null);
      fail("Inserting '.50' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior
    }

    assertEquals("$10.10", document.getText(0, document.getLength()));

    try {
      document.insertString(1, "-", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '-' minus sign should not have thrown a BadLocationException!");
    }

    assertEquals("$-10.10", document.getText(0, document.getLength()));

    try {
      document.remove(0, document.getLength());
    }
    catch (BadLocationException e) {
      fail("Removing the entire contents of the currency should not have thrown a BadLocationException!");
    }

    assertEquals("", document.getText(0, document.getLength()));

    try {
      document.insertString(0, ".-50", null);
      fail("Inserting '.-50' should have thrown a BadLocationException as the minus sign must come before the decimal!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "-.45", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '-.45' should not have thrown a BadLocationException!");
    }

    assertEquals("$-.45", document.getText(0, document.getLength()));

    try {
      document.replace(1, 4, ".99", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '-.45' with '.99' should not have thrown a BadLocationException!");
    }

    assertEquals("$.99", document.getText(0, document.getLength()));
  }

}
