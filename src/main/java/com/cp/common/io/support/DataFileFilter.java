/*
 * DataFileFilter.java (c) 8 June 2009
 *
 * The DataFileFilter class is used to determine whether or not the specified File object pathname
 * refers to a data file based on file extension (type) and is accepted by this file filter.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.FileExtensionFileFilter
 */

package com.cp.common.io.support;

import com.cp.common.io.FileExtensionFileFilter;

public class DataFileFilter extends FileExtensionFileFilter {

  // A String array containing the data file extensions accepted by this file filter.
  private static final String[] SUPPORTED_EXECUTABLE_FILE_EXTENSIONS = {
    "dat",
    "xml",
  };

  /**
   * Creates an instance of the DataFileFilter class to filter data files from the file system.
   */
  public DataFileFilter() {
    super(SUPPORTED_EXECUTABLE_FILE_EXTENSIONS);
  }

  /**
   * Creates an instance of the DataFileFilter class to filter data files from the file system.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  public DataFileFilter(final boolean inclusive, final boolean showHidden) {
    super(inclusive, showHidden, SUPPORTED_EXECUTABLE_FILE_EXTENSIONS);
  }

}
