/*
 * DefaultFileFilterTest.java (c) 6 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.6.7
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.DefaultFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class DefaultFileFilterTest extends CommonIOTestCase {

  @Test
  public void testAccepting() throws Exception {
    final DefaultFileFilter filter = new DefaultFileFilter();

    assertNotNull(filter);
    assertTrue(filter.isAccepting());

    final File tempFile = createTempFile("tmp.file");

    assertTrue(filter.accept(tempFile));

    for (final String directory : getDirectorySet()) {
      final File tempDirectory = new File(directory);
      assertTrue(filter.accept(tempDirectory));
    }
  }

  @Test
  public void testNotAccepting() throws Exception {
    final DefaultFileFilter filter = new DefaultFileFilter(false);

    assertNotNull(filter);
    assertFalse(filter.isAccepting());

    final File tempFile = new File("tmp.file");

    assertFalse(filter.accept(tempFile));

    for (final String directory : getDirectorySet()) {
      final File tempDirectory = new File(directory);
      assertFalse(filter.accept(tempDirectory));
    }
  }

  @Test
  public void testSetExclusive() throws Exception {
    final DefaultFileFilter filter = new DefaultFileFilter();

    assertNotNull(filter);
    assertFalse(filter.isExclusive());

    try {
      filter.setExclusive(true);
      fail("Calling setExclusive on the DefaultFileFilter should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Operation Not Supported!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setExclusive on the DefaultFileFilter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertFalse(filter.isExclusive());
  }

  @Test
  public void testSetInclusive() throws Exception {
    final DefaultFileFilter filter = new DefaultFileFilter();

    assertNotNull(filter);
    assertTrue(filter.isInclusive());

    try {
      filter.setInclusive(false);
      fail("Calling setInclusive on the DefaultFileFilter should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Operation Not Supported!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setInclusive on the DefaultFileFilter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertTrue(filter.isInclusive());
  }

  @Test
  public void testShowHidden() throws Exception {
    final DefaultFileFilter filter = new DefaultFileFilter();

    assertNotNull(filter);
    assertEquals(DefaultFileFilter.DEFAULT_SHOW_HIDDEN, filter.isShowHidden());

    try {
      filter.setShowHidden(!filter.isShowHidden());
      fail("Calling setShowHidden on the DefaultFileFilter should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Operation Not Supported!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setShowHidden on the DefaultFileFilter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertEquals(DefaultFileFilter.DEFAULT_SHOW_HIDDEN, filter.isShowHidden());
  }
}
