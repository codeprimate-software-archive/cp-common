/*
 * FontStyleTest.java (c) 8 August 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.8.8
 * @see com.cp.common.awt.FontStyle
 * @see junit.framework.TestCase
 */

package com.cp.common.awt;

import java.awt.Font;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FontStyleTest extends TestCase {

  public FontStyleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(FontStyleTest.class);
    //suite.addTest(new FontStyleTest("testName"));
    return suite;
  }

  public void testBoldFontStyle() throws Exception {
    assertEquals("bold", FontStyle.BOLD.getDescription());
    assertEquals(Font.BOLD, FontStyle.BOLD.getStyle());
  }

  public void testItalicFontStyle() throws Exception {
    assertEquals("italic", FontStyle.ITALIC.getDescription());
    assertEquals(Font.ITALIC, FontStyle.ITALIC.getStyle());
  }

  public void testPlainFontStyle() throws Exception {
    assertEquals("plain", FontStyle.PLAIN.getDescription());
    assertEquals(Font.PLAIN, FontStyle.PLAIN.getStyle());
  }

  public void testGetByDescription() throws Exception {
    assertNull(FontStyle.getByDescription(null));
    assertNull(FontStyle.getByDescription(""));
    assertNull(FontStyle.getByDescription(" "));
    assertNull(FontStyle.getByDescription("B"));
    assertNull(FontStyle.getByDescription("Italian"));
    assertNull(FontStyle.getByDescription("Simple"));
    assertEquals(FontStyle.BOLD, FontStyle.getByDescription("bold"));
    assertEquals(FontStyle.ITALIC, FontStyle.getByDescription("italic"));
    assertEquals(FontStyle.PLAIN, FontStyle.getByDescription("plain"));
    assertEquals(FontStyle.PLAIN, FontStyle.getByDescription("Plain"));
    assertEquals(FontStyle.PLAIN, FontStyle.getByDescription("PLAIN"));
  }

  public void testGetByStyle() throws Exception {
    assertNull(FontStyle.getByStyle(-1));
    assertEquals(FontStyle.BOLD, FontStyle.getByStyle(Font.BOLD));
    assertEquals(FontStyle.ITALIC, FontStyle.getByStyle(Font.ITALIC));
    assertEquals(FontStyle.PLAIN, FontStyle.getByStyle(Font.PLAIN));
  }

}
