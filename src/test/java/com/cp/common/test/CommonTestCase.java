/*
 * CommonTestCase.java (c) 6 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.6.14
 * @see com.cp.common.test.util.TestUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import com.cp.common.test.util.TestUtil;

import org.junit.Before;

public abstract class CommonTestCase {

  protected static final File PROJECT_DIRECTORY = new File(System.getProperty(TestUtil.PROJECT_DIR_SYSTEM_PROPERTY));
  protected static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

  /**
   * Sets up any data or other environmental properties for individual test cases.
   * @throws Exception if the set up operation fails.
   */
  @Before
  public void setup() throws Exception {
    assertTrue("Could not find project directory (" + PROJECT_DIRECTORY + ") in the file system!",
      PROJECT_DIRECTORY.exists());
    assertTrue("Could not find temporary directory (" + TEMP_DIRECTORY + ") in the file system!",
      TEMP_DIRECTORY.exists());
  }

  /**
   * Gets a File object based on the designated filename and the PROJECT_DIR System property.
   * @param filename a String value specifying the name of the file.
   * @return a File object with the given filename using the PROJECT_DIR System property as the
   * fully-qualified file system path of the file.
   */
  protected static File getFile(String filename) {
    return getFile(PROJECT_DIRECTORY, filename);
  }

  /**
   * Gets a File object based on the designated filename within the specified parent directory.
   * @param filename a String value specifying the name of the file.
   * @param parentDirectory a File object referencing the parent directory of the specified file to obtain.
   * @return a File object with the given filename using the given parent directory as the fully-qualified
   * file system path of the file.
   */
  protected static File getFile(File parentDirectory, final String filename) {
    return new File(parentDirectory, filename);
  }
}
