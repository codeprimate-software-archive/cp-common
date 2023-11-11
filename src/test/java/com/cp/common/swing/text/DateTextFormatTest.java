/*
 * DateTextFormatTest (c) 7 October 2004
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

public class DateTextFormatTest extends TestCase {

  public DateTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DateTextFormatTest.class);
    //suite.addTest(new DateTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new DateTextFormat());

    try {
      document.insertString(0, "10", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '10' should not have thrown an BadLocationException!");
    }

    assertEquals("10", document.getText(0, document.getLength()));

    try {
      document.insertString(2, "3", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '3' should not have thrown a BadLocationException!");
    }

    assertEquals("10/3", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "41/200", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '41/200' should not have thrown a BadLocationException!");
    }

    // NOTE: the DateTextFormat performs no Date validation, thus the 41st day of October in 2003 is legal
    // according to the DateTextFormat class.
    assertEquals("10/41/2003", document.getText(0, document.getLength()));

    try {
      document.replace(3, 2, "1", null); // 10/1/2003 is not a valid date format; must be formatted as MM/dd/yyyyy!
      fail("Replacing '41' with '1' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("10/41/2003", document.getText(0, document.getLength()));

    try {
      document.replace(3, 7, "012001", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '41/2003' with '012001' should not have thrown a BadLocationException!");
    }

    assertEquals("10/01/2001", document.getText(0, document.getLength()));

    try {
      document.remove(4, 7);
    }
    catch (Exception e) {
      fail("Removing '1/2001' should not have thrown an Exception!");
    }

    assertEquals("10/0", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "9", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '9' should not have thrown a BadLocationException!");
    }

    assertEquals("10/09", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "2004", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '2004', should not have thrown a BadLocationException!");
    }

    assertEquals("10/09/2004", document.getText(0, document.getLength()));

    // TODO: figure out how to handle this formatting issue, since the date is not valid!
    try {
      document.remove(4, 3);
    }
    catch (Exception e) {
      fail("Removing '9/2' should not have thrown an Exception!");
    }

    assertEquals("10/0004", document.getText(0, document.getLength()));
  }

}
