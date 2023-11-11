/*
 * FormatUtilTest.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.20
 * @see com.cp.common.text.FormatUtil
 */

package com.cp.common.text;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FormatUtilTest extends TestCase {

  public FormatUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(FormatUtilTest.class);
    //suite.addTest(new FormatUtilTest("testName"));
    return suite;
  }

  public void testFormatBoolean() throws Exception {
    assertEquals("true", FormatUtil.format(Boolean.TRUE));
    assertEquals("false", FormatUtil.format(Boolean.FALSE));
    assertEquals("yes", FormatUtil.format(true, "yes", "no"));
    assertEquals("no", FormatUtil.format(false, "yes", "no"));
    assertEquals("1", FormatUtil.format(true, "1", "0"));
    assertEquals("0", FormatUtil.format(false, "1", "0"));
  }

  public void testFormatPhoneNumber() throws Exception {
    assertEquals("", FormatUtil.formatPhoneNumber(null));
    assertEquals("", FormatUtil.formatPhoneNumber(""));
    assertEquals("", FormatUtil.formatPhoneNumber("   "));
    assertEquals("(503) 253-2256", FormatUtil.formatPhoneNumber("5032532256"));
    assertEquals("(503) 253-2256", FormatUtil.formatPhoneNumber("503-253-2256"));
    assertEquals("(503) 253-2256", FormatUtil.formatPhoneNumber("503.253.2256"));
    assertEquals("(503) 253-2256", FormatUtil.formatPhoneNumber("(503) 253-2256"));
    assertEquals("503-504-8657", FormatUtil.formatPhoneNumber("(503) 504-8657", "###-###-####"));
    assertEquals("(503) 577-5796 x4000", FormatUtil.formatPhoneNumber("50357757964000", "(###) ###-#### x####"));

    try {
      FormatUtil.formatPhoneNumber("273-4317", "###-###-####");
    }
    catch (IllegalFormatException e) {
      assertEquals("The phone number (273-4317) cannot be formatted using pattern (###-###-####)!", e.getMessage());
    }
  }

  public void testFormatSsn() throws Exception {
    assertEquals("", FormatUtil.formatSsn(null));
    assertEquals("", FormatUtil.formatSsn(""));
    assertEquals("", FormatUtil.formatSsn("   "));
    assertEquals("010-10-1010", FormatUtil.formatSsn("010101010"));
    assertEquals("333-22-4444", FormatUtil.formatSsn("333 22 4444"));
    assertEquals("333-22-4444", FormatUtil.formatSsn("333-22-4444"));

    try {
      FormatUtil.formatSsn("12-34-5678");
      fail("23-45-6789 is not a valid SSN!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }

    try {
      FormatUtil.formatSsn("123-4-5678");
      fail("123-1-4567 is not a valid SSN!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }

    try {
      FormatUtil.formatSsn("123-45-6");
      fail("123-45-6 is not a valid SSN format!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }

    try {
      FormatUtil.formatSsn("12-3-456");
      fail("123-4-567 is not a valid SSN!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }

    try {
      FormatUtil.formatSsn("   -  -    ");
      fail("'   -  -    ' is not a valid SSN!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }

    try {
      FormatUtil.formatSsn("--");
      fail("-- is not a valid SSN!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }

    try {
      FormatUtil.formatSsn("@!1-%7_*7(2");
      fail("@!1-%7_*7(2 is not a valid SSN!");
    }
    catch (IllegalArgumentException ignore) {
      // expected behavior!
    }
  }

}
