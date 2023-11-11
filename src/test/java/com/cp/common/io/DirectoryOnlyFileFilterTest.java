/*
 * DirectoryOnlyFileFilterTest.java (c) 23 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.DirectoryOnlyFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class DirectoryOnlyFileFilterTest extends CommonIOTestCase {

  @Test
  public void testAcceptOnFile() throws Exception {
    final DirectoryOnlyFileFilter directoryOnlyFileFilter = new DirectoryOnlyFileFilter();
    final File tempFile = createTempFile("tmp.file");

    assertNotNull(directoryOnlyFileFilter);
    assertFalse(directoryOnlyFileFilter.isExclusive());
    assertTrue(directoryOnlyFileFilter.isInclusive());
    assertFalse(directoryOnlyFileFilter.isShowHidden());
    assertFile(tempFile);
    assertFalse(directoryOnlyFileFilter.accept(tempFile));
  }

  @Test
  public void testAcceptOnDirectory() throws Exception {
    final DirectoryOnlyFileFilter directoryOnlyFileFilter = new DirectoryOnlyFileFilter();

    assertNotNull(directoryOnlyFileFilter);
    assertFalse(directoryOnlyFileFilter.isExclusive());
    assertTrue(directoryOnlyFileFilter.isInclusive());
    assertFalse(directoryOnlyFileFilter.isShowHidden());

    for (final String directory : getDirectorySet()) {
      final File tempDirectory = new File(directory);
      assertDirectory(tempDirectory);
      assertTrue(directoryOnlyFileFilter.accept(tempDirectory));
    }
  }

  @Test
  public void testAcceptOnHiddenDirectory() throws Exception {
    final File hiddenTempDirectory = new File("/usr/local/hidden/directory/") {
      @Override
      public boolean isDirectory() {
        return true;
      }

      @Override
      public boolean isHidden() {
        return true;
      }
    };

    DirectoryOnlyFileFilter directoryOnlyFileFilter = new DirectoryOnlyFileFilter();

    assertNotNull(directoryOnlyFileFilter);
    assertFalse(directoryOnlyFileFilter.isExclusive());
    assertTrue(directoryOnlyFileFilter.isInclusive());
    assertFalse(directoryOnlyFileFilter.isShowHidden());
    assertFalse(directoryOnlyFileFilter.accept(hiddenTempDirectory));

    directoryOnlyFileFilter = new DirectoryOnlyFileFilter(true);

    assertNotNull(directoryOnlyFileFilter);
    assertFalse(directoryOnlyFileFilter.isExclusive());
    assertTrue(directoryOnlyFileFilter.isInclusive());
    assertTrue(directoryOnlyFileFilter.isShowHidden());
    assertTrue(directoryOnlyFileFilter.accept(hiddenTempDirectory));
  }

  @Test
  public void testSetInclusive() throws Exception {
    final DirectoryOnlyFileFilter directoryOnlyFileFilter = new DirectoryOnlyFileFilter();

    assertNotNull(directoryOnlyFileFilter);
    assertFalse(directoryOnlyFileFilter.isExclusive());
    assertTrue(directoryOnlyFileFilter.isInclusive());

    try {
      directoryOnlyFileFilter.setInclusive(false);
      fail("Calling setInclusive on the DirectoryOnlyFileFilter should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Operation Not Supported!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setInclusive on the DirectoryOnlyFileFilter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertFalse(directoryOnlyFileFilter.isExclusive());
    assertTrue(directoryOnlyFileFilter.isInclusive());
  }
}
