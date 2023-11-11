/*
 * ArchiveFileFilter.java (c) 7 June 2009
 *
 * The ArchiveFileFilter class is used to determine whether or not the specified File object pathname
 * refers to an archive file based on file extension (type) and is accepted by this file filter.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.FileExtensionFileFilter
 */

package com.cp.common.io.support;

import com.cp.common.io.FileExtensionFileFilter;

public class ArchiveFileFilter extends FileExtensionFileFilter {

  // A String array containing the archive file extensions accepted by this file filter.
  protected static final String[] SUPPORTED_ARCHIVE_FILE_EXTENSIONS = {
    "arc",
    "bz",
    "bz2",
    "bzip",
    "bzip2",
    "ear",
    "gzip",
    "jar",
    "pkg",
    "rar",
    "rpm",
    "tar",
    "war",
    "zip"
  };

  /**
   * Creates an instance of the ArchiveFileFilter class to filter archive files from the file system.
   */
  public ArchiveFileFilter() {
    super(SUPPORTED_ARCHIVE_FILE_EXTENSIONS);
  }

  /**
   * Creates an instance of the ArchiveFileFilter class to filter archive files from the file system.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  public ArchiveFileFilter(final boolean inclusive, final boolean showHidden) {
    super(inclusive, showHidden, SUPPORTED_ARCHIVE_FILE_EXTENSIONS);
  }

}
