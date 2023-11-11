/*
 * FileOnlyFileFilter.java (c) 10 July 2002
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

public class FileOnlyFileFilter extends AbstractFileFilter {

  /**
   * Creates an instance of the FileOnlyFileFilter class allowing only files to pass through the file filter.  
   * The default constructor allows the File object if and only if the File is a file and the File is not hidden
   * in the file system.
   */
  public FileOnlyFileFilter() {
  }

  /**
   * Creates an instance of the FileOnlyFileFilter class to allow only files with the specified hidden property value
   * set to pass through the file filter.
   * @param showHidden is a boolean value indicating whether to exclude or include hidden files within the file system.
   */
  public FileOnlyFileFilter(final boolean showHidden) {
    super(DEFAULT_INCLUSIVE_EXCLUSIVE, showHidden);
  }

  /**
   * Returns true if the File object represented by the pathname is a file (not a directory) and is not hidden
   * in the file system, or is a file and showHidden is true.
   * @param pathname is a File object used to determine if the file system object referenced by the pathname
   * is indeed a file.
   * @return a boolean value indicating whether the File object is indeed a file, false if it is a directory
   * or some other file system object.
   */
  public boolean accept(final File pathname) {
    if (logger.isDebugEnabled()) {
      logger.debug("file (" + pathname + ")");
    }
    return (pathname.isFile() && (isShowHidden() || !pathname.isHidden()));
  }

  /**
   * The operation is not supported by the FileOnlyFileFilter class.
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
