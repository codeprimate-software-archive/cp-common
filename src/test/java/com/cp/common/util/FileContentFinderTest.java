/*
 * FileContentFinderTest.java (c) 16 January 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.7
 * @see com.cp.common.test.CommonTestCase
 * @see com.cp.common.util.FileContentFinder
 */

package com.cp.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import com.cp.common.test.CommonTestCase;

import org.junit.Test;

public class FileContentFinderTest extends CommonTestCase {

  @Test
  public void testFileContentFinder() throws Exception {
    final File testFile = getFile("/etc/test/fileContentFinderTestFile.txt");

    // Cache the contents of the test file.
    FileContentFinder.cacheFileContents(true);

    if (testFile.exists()) {
      assertEquals(10, FileContentFinder.getLineNumber(testFile, "Never leave a human to do a machines job"));
      assertEquals(15, FileContentFinder.getLineNumber(testFile, "Hobbitzies"));
      assertEquals(6, FileContentFinder.getLineNumber(testFile, "computer"));
      assertEquals(-1, FileContentFinder.getLineNumber(testFile, "NOT FOUND"));
      assertEquals(1, FileContentFinder.getLineNumber(testFile, "the"));
    }
    else {
      fail("File Not Found: " + testFile);
    }
  }
}
