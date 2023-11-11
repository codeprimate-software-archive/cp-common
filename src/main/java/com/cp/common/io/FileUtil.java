/*
 * FileUtil.java (c) 12 May 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.10
 * @see java.io.File
 */

package com.cp.common.io;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FileUtil {

  private static final Log logger = LogFactory.getLog(FileUtil.class);

  /**
   * Private contructor used to enforce non-instantiability.  The FileUtil class is a utility class.
   */
  private FileUtil() {
  }

  /**
   * Returns the file's extension (type), such as "java", "class", etc.
   * @param file the File object for which the file extension (file type) will be determined.
   * @return a String value specifying the file's extension (type) or an empty String if the file has
   * no extension (type).
   * @throws java.lang.NullPointerException if the File object parameter is null.
   * @see FileUtil#getFileExtension(String)
   */
  public static String getFileExtension(final File file) {
    Assert.notNull(file, "The file cannot be null!");
    return getFileExtension(file.getName());
  }

  /**
   * Returns a file's extension (type), such as "java", "class", etc, given a String representation of the file's name.
   * @param filename is the name of the file for which the file extension (file type) will be determined.
   * @return a String value speicfying the file's extension (type), or an empty String if the file has
   * no extension (type).
   * @throws java.lang.NullPointerException if the filename parameter is null.
   * @see FileUtil#getFileExtension(File)
   */
  public static String getFileExtension(final String filename) {
    Assert.notNull(filename, "The name of the file cannot be null!");

    try {
      int index = filename.lastIndexOf(".");
      return (index == -1 ? "" : filename.substring(index + 1).trim());
    }
    catch (IndexOutOfBoundsException ignore) {
      logger.warn("Empty file extension (" + filename + ").");
      return "";
    }
  }

  /**
   * Returns a file's absolute/relative location in the file system.
   * @param file is the specified File object for which the location will be determined.
   * @return a String value indicating the location of the file in the file system, or an empty String if the file
   * does not exists in the file system.
   * @throws java.lang.NullPointerException if the File object parameter is null.
   * @see FileUtil#getFileLocation(String)
   */
  public static String getFileLocation(final File file) {
    Assert.notNull(file, "The file cannot be null!");
    return getFileLocation(file.getAbsolutePath());
  }

  /**
   * Returns a file's absolute/relative location in the file system.
   * @param pathname a String value indicating the absolute or relative path and name of the file in the file system.
   * @return a String value indicating the location of the file in the file system, or an empty String if the file
   * does not exists in the file system.
   * @throws java.lang.NullPointerException if the File object parameter is null.
   * @see FileUtil#getFileLocation(java.io.File)
   */
  public static String getFileLocation(final String pathname) {
    Assert.notNull(pathname, "The pathname of the file cannot be null!");
    int index = pathname.lastIndexOf(File.separator);
    index = (index != -1 ? index : pathname.lastIndexOf("/"));
    return (index == -1 ? "" : pathname.substring(0, index + 1));
  }

  /**
   * Gets the name of the file excluding pathname or returns an empty String if the File object is null.
   * @param file the File object for which the name is determined.
   * @return a String value specifying the name of the file excluding pathname.
   */
  public static String getFilename(final File file) {
    return (ObjectUtil.isNull(file) ? "" : file.getName());
  }

}
