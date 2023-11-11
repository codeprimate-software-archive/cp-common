/*
 * PhoneNumberTextFormatTest (c) 7 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.12
 */

package com.cp.common.swing.text;

import com.cp.common.lang.StringUtil;
import javax.swing.text.BadLocationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PhoneNumberTextFormatTest extends TestCase {

  public PhoneNumberTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PhoneNumberTextFormatTest.class);
    //suite.addTest(new PhoneNumberTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new PhoneNumberTextFormat());

    try {
      document.insertString(0, "503-123-1234", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '503-123-1234' should not have thrown a BadLocationException!");
    }

    assertEquals("(503)123-1234", document.getText(0, document.getLength()));

    try {
      document.remove(0, document.getLength());
    }
    catch (Exception e) {
      fail("Removing the entire contents of the phone number should not have thrown as Exception!");
    }

    assertTrue(StringUtil.isEmpty(document.getText(0, document.getLength())));

    try {
      document.insertString(0, "(319)556-1722", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '(319)-556-1722' should not have thrown a BadLocationException!");
    }

    assertEquals("(319)556-1722", document.getText(0, document.getLength()));

    try {
      document.replace(5, 8, "0987654", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '556-1722' with '0987654' should not have thrown a BadLocationException!");
    }

    assertEquals("(319)098-7654", document.getText(0, document.getLength()));

    try {
      document.replace(1, 7, "555-678", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '319)098' with '555-678' should not have thrown a BadLocationException!");
    }

    assertEquals("(555)678-7654", document.getText(0, document.getLength()));

    try {
      document.remove(10, 3);
    }
    catch (BadLocationException e) {
      fail("Removing '654' should not have thrown a BadLocationException!");
    }

    assertEquals("(555)678-7", document.getText(0, document.getLength()));

    try {
      document.replace(3, 1, "0123", null); // creates phone number with invalid format
      fail("Replacing '5' nearest the right paren with '0123' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("(555)678-7", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "1234", null); // exceeds length
      fail("Inserting '1234' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("(555)678-7", document.getText(0, document.getLength()));

    try {
      document.replace(1, 9, "(503) 504-8657", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '555)678-7' with '(503) 504-8657' should not have thrown a BadLocationException!");
    }

    assertEquals("(503)504-8657", document.getText(0, document.getLength()));

    try {
      document.remove(5, 3);
    }
    catch (BadLocationException e) {
      fail("Removing '504' should not have thrown a BadLocationException!");
    }

    assertEquals("(503)-8657", document.getText(0, document.getLength()));
  }

}
