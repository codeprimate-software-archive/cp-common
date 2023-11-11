/*
 * FileOnlyFileFilterTest.java (c) 23 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.FileOnlyFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class FileOnlyFileFilterTest extends CommonIOTestCase {

  @Test
  public void testAcceptOnDirectory() throws Exception {
    final FileOnlyFileFilter fileOnlyFileFilter = new FileOnlyFileFilter();

    assertNotNull(fileOnlyFileFilter);
    assertFalse(fileOnlyFileFilter.isExclusive());
    assertTrue(fileOnlyFileFilter.isInclusive());
    assertFalse(fileOnlyFileFilter.isShowHidden());

    for (final String directory : getDirectorySet()) {
      final File tempDirectory = new File(directory);
      assertDirectory(tempDirectory);
      assertFalse(fileOnlyFileFilter.accept(tempDirectory));
    }
  }

  @Test
  public void testAcceptOnFile() throws Exception {
    final FileOnlyFileFilter fileOnlyFileFilter = new FileOnlyFileFilter();

    assertNotNull(fileOnlyFileFilter);
    assertFalse(fileOnlyFileFilter.isExclusive());
    assertTrue(fileOnlyFileFilter.isInclusive());
    assertFalse(fileOnlyFileFilter.isShowHidden());

    final File tempFile = createTempFile("tmp.file");

    assertFile(tempFile);
    assertTrue(fileOnlyFileFilter.accept(tempFile));
  }

  @Test
  public void testAcceptOnHiddenFile() throws Exception {
    final File hiddenTempFile = new File("/usr/local/hidden/tmp.file") {
      @Override
      public boolean isFile() {
        return true;
      }

      @Override
      public boolean isHidden() {
        return true;
      }
    };

    FileOnlyFileFilter fileOnlyFileFilter = new FileOnlyFileFilter();

    assertNotNull(fileOnlyFileFilter);
    assertFalse(fileOnlyFileFilter.isExclusive());
    assertTrue(fileOnlyFileFilter.isInclusive());
    assertFalse(fileOnlyFileFilter.isShowHidden());
    assertFalse(fileOnlyFileFilter.accept(hiddenTempFile));

    fileOnlyFileFilter = new FileOnlyFileFilter(true);

    assertNotNull(fileOnlyFileFilter);
    assertFalse(fileOnlyFileFilter.isExclusive());
    assertTrue(fileOnlyFileFilter.isInclusive());
    assertTrue(fileOnlyFileFilter.isShowHidden());
    assertTrue(fileOnlyFileFilter.accept(hiddenTempFile));
  }

  @Test
  public void testSetInclusive() throws Exception {
    final FileOnlyFileFilter fileOnlyFileFilter = new FileOnlyFileFilter();

    assertNotNull(fileOnlyFileFilter);
    assertFalse(fileOnlyFileFilter.isExclusive());
    assertTrue(fileOnlyFileFilter.isInclusive());

    try {
      fileOnlyFileFilter.setInclusive(false);
      fail("Calling setInclusive on FileOnlyFileFilter should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Operation Not Supported!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setInclusive on FileOnlyFileFilter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    assertFalse(fileOnlyFileFilter.isExclusive());
    assertTrue(fileOnlyFileFilter.isInclusive());
  }
}
