/*
 * AbstractFileFilterTest.java (c) 7 June 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.6.7
 * @see com.cp.common.io.AbstractFileFilter
 * @see com.cp.common.io.CommonIOTestCase
 */

package com.cp.common.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AbstractFileFilterTest extends CommonIOTestCase {

  @Test
  public void testInstantiate() throws Exception {
    final AbstractFileFilter filter = new TestFileFilter(false, true);

    assertNotNull(filter);
    assertFalse(filter.isInclusive());
    assertTrue(filter.isShowHidden());
  }

  @Test
  public void testSetExclusive() throws Exception {
    final AbstractFileFilter filter = new TestFileFilter();

    assertNotNull(filter);
    assertFalse(filter.isExclusive());
    assertTrue(filter.isInclusive());

    filter.setExclusive(true);

    assertTrue(filter.isExclusive());
    assertFalse(filter.isInclusive());
  }

  @Test
  public void testSetInclusive() throws Exception {
    final AbstractFileFilter filter = new TestFileFilter();

    assertNotNull(filter);
    assertFalse(filter.isExclusive());
    assertTrue(filter.isInclusive());

    filter.setInclusive(false);

    assertTrue(filter.isExclusive());
    assertFalse(filter.isInclusive());
  }

  @Test
  public void testShowHidden() throws Exception {
    final AbstractFileFilter filter = new TestFileFilter();

    assertNotNull(filter);
    assertFalse(filter.isShowHidden());

    filter.setShowHidden(true);

    assertTrue(filter.isShowHidden());
  }

  private static final class TestFileFilter extends AbstractFileFilter {

    public TestFileFilter() {
    }

    public TestFileFilter(final boolean inclusive, final boolean showHidden) {
      super(inclusive, showHidden);
    }

    public boolean accept(final File pathname) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }
}
