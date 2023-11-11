/*
 * PercentTextFormatTest (c) 8 October 2004
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

public class PercentTextFormatTest extends TestCase {

  public PercentTextFormatTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PercentTextFormatTest.class);
    //suite.addTest(new PercentTextFormatTest("testName"));
    return suite;
  }

  public void testFormat() throws Exception {
    final MockDocument document = new MockDocument("", new PercentTextFormat());

    try {
      document.insertString(0, "50", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '50' as a percent should not have thrown a BadLocationException!");
    }

    assertEquals("50%", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "0.12", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '0.12' should not have thrown a BadLocationException!");
    }

    assertEquals("0.1250%", document.getText(0, document.getLength()));

    try {
      document.insertString(document.getLength(), "20%", null); // inserting values after the percent sign is not a valid percent format
      fail("Inserting '20%' after the % sign should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("0.1250%", document.getText(0, document.getLength()));

    try {
      document.replace(0, 1, "76", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '76' should not have thrown a BadLocationException!");
    }

    assertEquals("76.1250%", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "0.", null);
      fail("Inserting '0.' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behvavior!
    }

    assertEquals("76.1250%", document.getText(0, document.getLength()));

    try {
      document.insertString(0, "-1", null);
    }
    catch (BadLocationException e) {
      fail("Inserting '-1' should not have thrown a BadLocationException!");
    }

    assertEquals("-176.1250%", document.getText(0, document.getLength()));

    try {
      document.replace(3, 1, "%", null);
      fail("Replacing '6' with '%' should have thrown a BadLocationException!");
    }
    catch (BadLocationException e) {
      // expected behavior!
    }

    assertEquals("-176.1250%", document.getText(0, document.getLength()));
  }

}
