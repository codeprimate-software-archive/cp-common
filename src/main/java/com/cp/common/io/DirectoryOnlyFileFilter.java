/*
 * DirectoryOnlyFileFilter.java (c) 12 May 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.7
 * @see com.cp.common.io.AbstractFileFilter
 * @see java.io.File
 */

package com.cp.common.io;

import java.io.File;

public class DirectoryOnlyFileFilter extends AbstractFileFilter {

  /**
   * Creates an instance of the DirectoryOnlyFileFilter class allowing only directories to pass through the file filter.
   * The default constructor allows the File object if and only if the File is a directory and the File is not hidden
   * in the file system.
   */
  public DirectoryOnlyFileFilter() {
  }

  /**
   * Creates an instance of the DirectoryOnlyFileFilter class allowing only directories with the specified hidden bit
   * set to pass through the file filter.
   * @param showHidden is a boolean value indicating whether hidden directories within the file system should be
   * included or excluded by this file filter.
   */
  public DirectoryOnlyFileFilter(final boolean showHidden) {
    super(DEFAULT_INCLUSIVE_EXCLUSIVE, showHidden);
  }

  /**
   * Returns true if the File object represented by the pathname is a directory (not a file) and is not hidden
   * in the file system, or is a directory and showHidden is true.
   * @param pathname is a File object used to determine if the file system object referenced by pathname is indeed
   * a directory.
   * @return a boolean value indicating whether the File object is indeed a directory, false if it is a file
   * or some other file system object.
   */
  public boolean accept(final File pathname) {
    if (logger.isDebugEnabled()) {
      logger.debug("file (" + pathname + ")");
    }
    return (pathname.isDirectory() && (isShowHidden() || !pathname.isHidden()));
  }

  /**
   * The operation is not supported by the DirectoryOnlyFileFilter class.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @throws java.lang.UnsupportedOperationException as the inclusive operation is not supported by this
   * FileFilter implementation.
   */
  @Override
  public void setInclusive(final boolean inclusive) {
    throw new UnsupportedOperationException("Operation Not Supported!");
  }

}
