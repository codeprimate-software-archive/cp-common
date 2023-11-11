/*
 * CommonIOTestCase.java (c) 20 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see com.cp.common.test.CommonTestCase
 */

package com.cp.common.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.cp.common.lang.Assert;
import com.cp.common.test.CommonTestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CommonIOTestCase extends CommonTestCase {

  protected static final String[] SYSTEM_PROPERTY_DIRECTORIES = {
    System.getProperty("java.home"),
    System.getProperty("java.io.tmpdir"),
    System.getProperty("java.ext.dirs"),
    System.getProperty("user.home"),
    System.getProperty("user.dir")
  };


  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Asserts that the specified File is NOT null and is a directory.
   * @param testDirectory the test directory.
   */
  protected void assertDirectory(File testDirectory) {
    assertNotNull(testDirectory);
    assertTrue(testDirectory.isDirectory());
  }

  /**
   * Asserts that the specified File is NOT null and is a file.
   * @param testFile the test file.
   */
  protected void assertFile(File testFile) {
    assertNotNull(testFile);
    assertTrue(testFile.isFile());
  }

  /**
   * Creates a File object with the given filename.
   * @param filename a String value specifying the name of the file.
   * @return a File object with the given filename.
   */
  protected static File createFile(String filename) {
    return new File(filename) {
      @Override
      public boolean isFile() {
        return true;
      }
    };
  }

  /**
   * Creates a new file in the temp directory of file system on the local host with the specified filename.
   * @param filename the name of the file to create in the temp directory of the file system on the local host.
   * @return a File object representing the newly created file.
   * @throws IOException if an I/O error occurs while creating the new file.
   */
  public static File createNewFile(String filename) throws Exception {
    return createNewFile(filename, true);
  }

  /**
   * Creates a new file in the temp directory of file system on the local host with the specified filename.
   * @param filename the name of the file to create in the temp directory of the file system on the local host.
   * @param deleteOnExit a boolean value indicating whethe the newly created file should be removed from the file system
   * on the localhost when the Java VM exits.
   * @return a File object representing the newly created file.
   * @throws IOException if an I/O error occurs while creating the new file.
   */
  public static File createNewFile(String filename, boolean deleteOnExit) throws Exception {
    final File newFile = new File(TEMP_DIRECTORY, filename);

    if (newFile.createNewFile()) {
      if (deleteOnExit) {
        newFile.deleteOnExit();
      }

      return newFile;
    }

    return null;
  }

  /**
   * Creates a temporary file in the temp directory of file system on the local host with the specified filename.
   * @param filename the name of the file to create in the temp directory of the file system on the local host.
   * @return a File object representing the newly created temporary file.
   * @throws IOException if an I/O error occurs while creating the temporary file.
   */
  protected static File createTempFile(String filename) throws IOException {
    return createTempFile(TEMP_DIRECTORY, filename);
  }

  /**
   * Creates a temporary file in the specified directory of the file system with the given filename.
   * Note, the File's deleteOnExit method is called to cleanup resources after the test is run.
   * @param directory a File object referring to the directory where the temporary file should be created.
   * @param filename the name of the file to create in the temp directory of the file system on the local host.
   * @return a File object representing the newly created temporary file.
   * @throws IOException if an I/O error occurs while creating the temporary file.
   */
  protected static File createTempFile(File directory, String filename) throws IOException {
    final File tempFile = File.createTempFile(getFilename(filename), getFileExtension(filename), directory);
    tempFile.deleteOnExit();
    return tempFile;
  }

  /**
   * Returns a predefined Set of directories in the file system based on the Java System properties.
   * @return a Set of directories contained in the file system on the local host.
   */
  protected Set<String> getDirectorySet() {
    return Collections.unmodifiableSet(new HashSet<String>(Arrays.<String>asList(SYSTEM_PROPERTY_DIRECTORIES)));
  }

  /**
   * Returns the file extension of the file given the filename as determined by the dot preappended to the extension.
   * @param filename the name of the file for which the extension will be returned.
   * @return the file extension in the following format ".ext".
   */
  private static String getFileExtension(String filename) {
    Assert.notEmpty(filename, "(" + filename + ") is not a valid filename!");
    return ("." + FileUtil.getFileExtension(filename));
  }

  /**
   * Returns the name of a file based on the String representation of its filename. The filename is usually in
   * the form "name.extension".
   * @param filename the name of the file as a String.
   * @return a String value specifying just the name portion of the filename, excluding extension if it exists.
   */
  private static String getFilename(String filename) {
    Assert.notEmpty(filename, "(" + filename + ") is not a valid filename!");
    // if index is a -1, the filename may not have an extension.
    final int index = filename.indexOf(".");
    return (index > -1 ? filename.substring(0, index) : filename);
  }
}
