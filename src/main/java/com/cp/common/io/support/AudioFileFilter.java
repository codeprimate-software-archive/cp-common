/*
 * AudioFileFilter.java (c) 8 June 2009
 *
 * The AudioFileFilter class is used to determine whether or not the specified File object pathname
 * refers to an audio file based on file extension (type) and is accepted by this file filter.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.FileExtensionFileFilter
 */

package com.cp.common.io.support;

import com.cp.common.io.FileExtensionFileFilter;

public class AudioFileFilter extends FileExtensionFileFilter {

  // A String array containing the audio file extensions accepted by this file filter.
  private static final String[] SUPPORTED_AUDIO_FILE_EXTENSIONS = {
    "au",
    "m4a",
    "mid",
    "midi",
    "mp3",
    "raw",
    "wav",
    "wma",
  };

  /**
   * Creates an instance of the AudioFileFilter class to filter audio files from the file system.
   */
  public AudioFileFilter() {
    super(SUPPORTED_AUDIO_FILE_EXTENSIONS);
  }

  /**
   * Creates an instance of the AudioFileFilter class to filter audio files from the file system.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  public AudioFileFilter(final boolean inclusive, final boolean showHidden) {
    super(inclusive, showHidden, SUPPORTED_AUDIO_FILE_EXTENSIONS);
  }

}
