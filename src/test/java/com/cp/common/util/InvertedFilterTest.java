/*
 * InvertedFilterTest.java (c) 29 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.29
 * @see com.cp.common.util.InvertedFilter
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class InvertedFilterTest extends TestCase {

  public InvertedFilterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(InvertedFilterTest.class);
    //suite.addTest(new InvertedFilterTest("testName"));
    return suite;
  }

  protected <T> Filter<T> getFilter(final boolean acceptResult) {
    return new Filter<T>() {
      public boolean accept(final T obj) {
        return acceptResult;
      }
    };
  }

  public void testAccept() throws Exception {
    Filter<Object> invertedFilter = new InvertedFilter<Object>(getFilter(true));

    assertFalse(invertedFilter.accept("test"));

    invertedFilter = new InvertedFilter<Object>(getFilter(false));

    assertTrue(invertedFilter.accept("test"));
  }

  public void testInstantiation() throws Exception {
    InvertedFilter<Object> invertedFilter = null;

    try {
      invertedFilter = new InvertedFilter<Object>(new MockFilter());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the InvertedFilter class with a non-null Filter object should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(invertedFilter);
    assertTrue(invertedFilter.getFilter() instanceof MockFilter);

    try {
      invertedFilter = null;
      invertedFilter = new InvertedFilter<Object>(null);
      fail("Instantiating an instance of hte InvertedFilter class with a null Filter object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The filter object cannot be null!", e.getMessage());
    }

    assertNull(invertedFilter);
  }

  protected static final class MockFilter implements Filter<Object> {

    public boolean accept(final Object obj) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
