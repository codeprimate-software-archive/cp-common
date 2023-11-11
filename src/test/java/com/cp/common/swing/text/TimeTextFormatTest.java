/*
 * TimeTextFormatTest (c) 7 October 2004
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

public class TimeTextFormatTest extends TestCase {

  public TimeTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TimeTextFormatTest.class);
    //suite.addTest(new TimeTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new TimeTextFormat());

    try {
      document.insertString(0, "1", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '1' should not have thrown a BadLocationException!");
    }

    assertEquals("1", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "2", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '2' should not have thrown a BadLocationException!");
    }

    assertEquals("12", document.getText(0, document.getLength()));

    try {
      document.replace(1, 1, "23059", null);
    }
    catch (BadLocationException e) {
      fail("Replacing '2' with '23059' should not have thrown a BadLocationException!");
    }

    assertEquals("12:30:59", document.getText(0, document.getLength()));

    try {
      document.insertString(3, "4", null);
      fail("Inserting minute '4' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("12:30:59", document.getText(0, document.getLength()));

    try {
      document.replace(3, 3, "153", null);
      fail("Replacing '30:' with '153' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("12:30:59", document.getText(0, document.getLength()));

    try {
      document.remove(4, 3);
    }
    catch (Exception e) {
      fail("Removing '0:5' should not have thrown an Exception!");
    }

    assertEquals("12:39", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "2000", null);
      fail("Inserting seconds '2000' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("12:39", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "72", null);
    }
    catch (BadLocationException e) {
      fail("Inserting seconds '72' should not have thrown a BadLocationException!");
    }

    // NOTE: the TimeTextFormat performs no Time validation, thus the 72nd second at 12:39 is legal
    // according to the TimeTextFormat class.
    assertEquals("12:39:72", document.getText(0, document.getLength()));

    // TODO: figure out how to handle this formatting issue, since the time is not valid!
    try {
      document.remove(5, 1);
    }
    catch (Exception e) {
      fail("Removing the colan ':' between minutes and seconds should not have thrown an Exception!");
    }

    assertEquals("12:3972", document.getText(0, document.getLength()));
  }

}
