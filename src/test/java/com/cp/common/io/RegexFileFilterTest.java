/*
 * RegexFileFilterTest.java (c) 16 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.RegexFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class RegexFileFilterTest extends CommonIOTestCase {

  @Test
  public void testInstantiate() throws Exception {
    RegexFileFilter fileFilter = null;

    assertNull(fileFilter);

    try {
      fileFilter = new RegexFileFilter("test");
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the RegexFileFilter class with a regular expression of (test) threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(fileFilter);
    assertEquals("test", fileFilter.getPattern());
  }

  @Test
  public void testInstantiateWithEmptyPattern() throws Exception {
    try {
      new RegexFileFilter(" ");
      fail("Instantiating an instance of the RegexFileFilter class with an empty regular expression should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("( ) is not a valid regular expression!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the RegexFileFilter class with an empty regular expression threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testInstantiateWithNullPattern() throws Exception {
    try {
      new RegexFileFilter(null);
      fail("Instantiating an instance of the RegexFileFilter class with a null regular expression should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(null) is not a valid regular expression!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the RegexFileFilter class with a null regular expression threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testAccept() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("temp directory (" + TEMP_DIRECTORY + ")");
    }

    final File testFile0 = createNewFile("testFile.tst");
    assertFile(testFile0);

    final File testFile1 = createNewFile("readme.TXT");
    assertFile(testFile1);

    final File testFile2 = createNewFile("ezAs123.hlp");
    assertFile(testFile2);

    final File testFile3 = createNewFile("aTempFile");
    assertFile(testFile3);

    final File testFile4 = createNewFile("1.profile");
    assertFile(testFile4);

    final File testFile5 = createNewFile("configTest.");
    assertFile(testFile5);

    final File testFile6 = createNewFile("RegexFileFilterTest.java");
    assertFile(testFile6);

    // All files with only a 3 character extension.
    RegexFileFilter fileFilter = new RegexFileFilter("[a-zA-Z]*\\.[a-zA-Z]{3}");
    logger.debug("pattern 0 (" + fileFilter.getPattern() + ")");

    assertTrue(fileFilter.accept(testFile0));
    assertTrue(fileFilter.accept(testFile1));
    assertFalse(fileFilter.accept(testFile2));
    assertFalse(fileFilter.accept(testFile3));
    assertFalse(fileFilter.accept(testFile4));
    assertFalse(fileFilter.accept(testFile5));
    assertFalse(fileFilter.accept(testFile6));

    // All files with at least 2 characters before the extension and at least 3 characters in the extension.
    fileFilter = new RegexFileFilter("[a-zA-Z]{2,}\\.[a-zA-Z]{3,}");
    logger.debug("pattern 1 (" + fileFilter.getPattern() + ")");

    assertTrue(fileFilter.accept(testFile0));
    assertTrue(fileFilter.accept(testFile1));
    assertFalse(fileFilter.accept(testFile2));
    assertFalse(fileFilter.accept(testFile3));
    assertFalse(fileFilter.accept(testFile4));
    assertFalse(fileFilter.accept(testFile5));
    assertTrue(fileFilter.accept(testFile6));

    // All files with "test" in their name case-insensitive.
    fileFilter = new RegexFileFilter("\\w*[Tt]est\\w*\\.[a-zA-Z]*");
    logger.debug("pattern 2 (" + fileFilter.getPattern() + ")");

    assertTrue(fileFilter.accept(testFile0));
    assertFalse(fileFilter.accept(testFile1));
    assertFalse(fileFilter.accept(testFile2));
    assertFalse(fileFilter.accept(testFile3));
    assertFalse(fileFilter.accept(testFile4));
    assertTrue(fileFilter.accept(testFile5));
    assertTrue(fileFilter.accept(testFile6));

    // All files with numbers in their name.
    fileFilter = new RegexFileFilter("[a-zA-Z]*\\d+.*");
    logger.debug("pattern 3 (" + fileFilter.getPattern() + ")");

    assertFalse(fileFilter.accept(testFile0));
    assertFalse(fileFilter.accept(testFile1));
    assertTrue(fileFilter.accept(testFile2));
    assertFalse(fileFilter.accept(testFile3));
    assertTrue(fileFilter.accept(testFile4));
    assertFalse(fileFilter.accept(testFile5));
    assertFalse(fileFilter.accept(testFile6));
  }
}
