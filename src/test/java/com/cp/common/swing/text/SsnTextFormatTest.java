/*
 * SsnTextFormatTest (c) 7 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.10
 */

package com.cp.common.swing.text;

import javax.swing.text.BadLocationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SsnTextFormatTest extends TestCase {

  public SsnTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SsnTextFormatTest.class);
    //suite.addTest(new SsnTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new SsnTextFormat());

    try {
      document.insertString(0, "1234", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '1234' should not have thrown a BadLocationException!");
    }

    assertEquals("123-4", document.getText(0, document.getLength()));

    try {
      document.insertString(1, "56-7", null); // forces a misplaced dash in the current ssn!
      fail("Inserting '56-7' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("123-4", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "5-67", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '5-67' should not have thrown a BadLocationException!");
    }

    assertEquals("123-45-67", document.getText(0, document.getLength()));

    try {
      document.insertString(7, "123", null); //exceeds length!
      fail("Inserting '123' exceeds length and should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("123-45-67", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "OO", null); // inserting letters is invalid!
      fail("Inserting letters 'OO' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("123-45-67", document.getText(0, document.getLength()));

    try {
      document.replace(2, 6, "1+00+1", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '1+00+1' should not have thrown a BadLocationException; the SsnTextFormat will format the value properly!");
    }

    assertEquals("121-00-17", document.getText(0, document.getLength()));

    // TODO: figure out how to handle this formatting issue, since the SSN is not valid!
    try {
      document.remove(3, 1);
    }
    catch (Exception e) {
      fail("Removing the first dash '-' should not have thrown an Exception!");
    }

    assertEquals("12100-17", document.getText(0, document.getLength()));
  }

}
