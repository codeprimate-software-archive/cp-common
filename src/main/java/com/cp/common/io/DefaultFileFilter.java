/*
 * DefaultFileFilter.java (c) 6 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.6.6
 * @see com.cp.common.io.AbstractFileFilter
 */

package com.cp.common.io;

import java.io.File;

public class DefaultFileFilter extends AbstractFileFilter {

  private final boolean accepting;

  /**
   * Creates an instance of the DefaultFileFilter class set to accept all File objects by default.
   */
  public DefaultFileFilter() {
    this(true);
  }

  /**
   * Creates an instance of the DefaultFileFilter class initialized to accept all File objects as specified
   * by the defaultAccept parameter.
   * @param accepting a boolean value indicating whether to accept or reject all File objects upon filtering.
   */
  public DefaultFileFilter(final boolean accepting) {
    this.accepting = accepting;
  }

  /**
   * Determines whether this file filter is accepting of all File objects.
   * @return a boolean value indicating whether this file filter is accepting of all file objects.
   */
  public boolean isAccepting() {
    return accepting;
  }

  /**
   * Sets whether this FileFilter will include Files or exclude them based on the filter criteria.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @see AbstractFileFilter#setExclusive(boolean)
   */
  @Override
  public void setInclusive(final boolean inclusive) {
    throw new UnsupportedOperationException("Operation Not Supported!");
  }

  /**
   * Sets whether hidden files are return by this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  @Override
  public void setShowHidden(final boolean showHidden) {
    throw new UnsupportedOperationException("Operation Not Supported!");
  }

  /**
   * Determines whether to accept or reject the specified File objcect.
   * @param pathname the File object being filtered by this file filter.
   * @return a boolean value indicating whether this file filter accepted or rejected the File object.
   */
  public boolean accept(final File pathname) {
    return isAccepting();
  }

}
