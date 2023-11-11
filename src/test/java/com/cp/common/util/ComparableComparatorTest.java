/*
 * ComparableComparatorTest.java (c) 1 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.3.18
 * @see com.cp.common.test.util.TestUtil
 * @see com.cp.common.util.ComparableComparator
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import static com.cp.common.test.util.TestUtil.assertNegative;
import static com.cp.common.test.util.TestUtil.assertPositive;
import static com.cp.common.test.util.TestUtil.assertZero;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComparableComparatorTest extends TestCase {

  public ComparableComparatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ComparableComparatorTest.class);
    //suite.addTest(new ComparableComparatorTest("testName"));
    return suite;
  }

  public void testComparableComparator() throws Exception {
    assertNegative(ComparableComparator.<String>getInstance().compare(null, null));
    assertNegative(ComparableComparator.<String>getInstance().compare(null, "test"));
    assertNegative(ComparableComparator.<Character>getInstance().compare(null, 'A'));
    assertNegative(ComparableComparator.<Integer>getInstance().compare(null, 1));
    assertNegative(ComparableComparator.<Double>getInstance().compare(null, 3.14159));
    assertPositive(ComparableComparator.<String>getInstance().compare("test", null));
    assertPositive(ComparableComparator.<Character>getInstance().compare('A', null));
    assertPositive(ComparableComparator.<Integer>getInstance().compare(1, null));
    assertPositive(ComparableComparator.<Double>getInstance().compare(3.14159, null));
    assertNegative(ComparableComparator.<String>getInstance().compare("test", "testing"));
    assertPositive(ComparableComparator.<String>getInstance().compare("test", "abc"));
    assertZero(ComparableComparator.<String>getInstance().compare("test", "test"));
  }

}
