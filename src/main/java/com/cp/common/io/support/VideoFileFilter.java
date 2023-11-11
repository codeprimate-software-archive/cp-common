/*
 * VideoFileFilter.java (c) 12 May 2003
 *
 * The VideoFileFilter class is used to determine whether or not the specified File object pathname
 * refers to a video file based on file extension (type) and is accepted by this file filter.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.FileExtensionFileFilter
 */

package com.cp.common.io.support;

import com.cp.common.io.FileExtensionFileFilter;

public class VideoFileFilter extends FileExtensionFileFilter {

  // A String array containing the video file extensions accepted by this file filter.
  protected static final String[] SUPPORTED_VIDEO_FILE_EXTENSIONS = {
    "avi",
    "divx",
    "mov",
    "mpeg",
    "mpg",
    "qt",
    "swf",
    "tivo",
    "wmv",
  };

  /**
   * Creates an instance of the VideoFileFilter class to filter video files from the file system.
   */
  public VideoFileFilter() {
    super(SUPPORTED_VIDEO_FILE_EXTENSIONS);
  }

  /**
   * Creates an instance of the VideoFileFilter class to filter video files from the file system.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  public VideoFileFilter(final boolean inclusive, final boolean showHidden) {
    super(inclusive, showHidden, SUPPORTED_VIDEO_FILE_EXTENSIONS);
  }

}
