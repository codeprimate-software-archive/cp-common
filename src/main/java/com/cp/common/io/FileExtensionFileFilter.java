/*
 * FileExtensionFileFilter.java (c) 16 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.7
 * @see com.cp.common.io.FileOnlyFileFilter
 * @see java.io.File
 */

package com.cp.common.io;

import com.cp.common.lang.Assert;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class FileExtensionFileFilter extends FileOnlyFileFilter {

  private final Set<String> fileExtensionSet;

  /**
   * Creates an instance of the FileExtensionFileFilter class to filter files in the file system by file type
   * for all file extensions expressed in the String array.
   * @param fileExtensions is a String array containing file extensions of files accepted by this filter.
   */
  public FileExtensionFileFilter(final String... fileExtensions) {
    this(DEFAULT_INCLUSIVE_EXCLUSIVE, DEFAULT_SHOW_HIDDEN, fileExtensions);
  }

  /**
   * Creates an instance of the FileExtensionFileFilter class to filter files in the file system by file type
   * for all file extensions expressed in the String array.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   * @param fileExtensions is a String array containing file extensions of files accepted by this filter.
   */
  public FileExtensionFileFilter(final boolean inclusive, final boolean showHidden, final String... fileExtensions) {
    Assert.notEmpty(fileExtensions, "At least one valid file extension must be specified!");

    fileExtensionSet = new TreeSet<String>();

    for (final String fileExtension : fileExtensions) {
      fileExtensionSet.add(getFileExtension(fileExtension));
    }

    setInclusive(inclusive);
    setShowHidden(showHidden);
  }

  /**
   * Returns the file extension formatted in lower case and trimmed (without whitespace characters).
   * @param fileExtension a String specifying the file extension.
   * @return a formatted String value of the specified file extension.
   */
  protected String getFileExtension(final String fileExtension) {
    Assert.notEmpty(fileExtension, "(" + fileExtension + ") is not a valid file extension!");
    return fileExtension.trim().toLowerCase();
  }

  /**
   * Returns the file extension set used by the accept method to filter File objects based on their extension (type).
   * @return a Set of file extensions used by this file filter.
   */
  protected Set<String> getFileExtensionSet() {
    return Collections.unmodifiableSet(fileExtensionSet);
  }

  /**
   * Sets whether this FileFilter will include Files or exclude them based on the filter criteria.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @see AbstractFileFilter#setExclusive(boolean)
   */
  @Override
  public void setInclusive(final boolean inclusive) {
    this.inclusive = inclusive;
  }

  /**
   * Called to filter the File object based on the file's extension (type).
   * @param pathname the File being filtered by this file filter.
   * @return a boolean value indicating if the File object is accepted by this file filter.
   */
  public boolean accept(final File pathname) {
    return (super.accept(pathname) && (isInclusive() == getFileExtensionSet().contains(
      FileUtil.getFileExtension(pathname).toLowerCase())));
  }

  /**
   * Returns a String listing all the supported, or unsupported file extensions as specified by the inclusive property
   * of this file filter.
   * @return a String representation of the supported or unsupported file extensions.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer(isInclusive() ? "Inclusive Set " : "Exclusive Set ");
    boolean condition = false;

    buffer.append("[");

    for (final String fileExtension : getFileExtensionSet()) {
      buffer.append(condition ? ", " : "").append(fileExtension);
      condition = true;
    }

    buffer.append("]:").append(getClass().getName());

    return buffer.toString();
  }

}
