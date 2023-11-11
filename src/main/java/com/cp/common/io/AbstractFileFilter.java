/*
 * AbstractFileFilter.java (c) 17 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.7
 * @see com.cp.common.util.Filter
 * @see java.io.File
 * @see java.io.FileFilter
 */

package com.cp.common.io;

import com.cp.common.util.Filter;
import java.io.File;
import java.io.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractFileFilter implements FileFilter, Filter<File> {

  public static final boolean INCLUSIVE = true;
  public static final boolean EXCLUSIVE = false;

  protected static final boolean DEFAULT_INCLUSIVE_EXCLUSIVE = INCLUSIVE;
  protected static final boolean DEFAULT_SHOW_HIDDEN = false;

  // notice the inclusive instance variable is protected
  // (it is needed by subclases that override the setInclusive method)
  protected boolean inclusive = DEFAULT_INCLUSIVE_EXCLUSIVE;
  private boolean showHidden = DEFAULT_SHOW_HIDDEN;

  // Logger used to track and record the state of the properties and the actions of this filter.
  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the AbstractFileFilter class which excludes (filters) hidden files in the file system.
   */
  public AbstractFileFilter() {
    this(DEFAULT_INCLUSIVE_EXCLUSIVE, DEFAULT_SHOW_HIDDEN);
  }

  /**
   * Creates an instance of the AbstractFileFilter class initialized with the specified value for the
   * showHidden property, determining whether hidden files should be included or excluded by this file filter.
   * @param inclusive a boolean value configuring the file filter to include or exclude Files satisfying
   * the criteria of this file filter.
   * @param showHidden a boolean value indicating whether hidden files are included or excluded by this filter.
   */
  public AbstractFileFilter(final boolean inclusive, final boolean showHidden) {
    this.inclusive = inclusive;
    this.showHidden = showHidden;
  }

  /**
   * Determines whether Files are included or excluded based on the filter criteria.
   * @return a boolean value indicating whether Files are inclusive depending upon whether the File satisfies
   * this filter's criteria.
   * @see AbstractFileFilter#isInclusive()
   */
  public final boolean isExclusive() {
    return !isInclusive();
  }

  /**
   * Sets whether this FileFilter will include Files or exclude them based on the filter criteria.
   * @param exclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @see AbstractFileFilter#setInclusive(boolean)
   */
  public final void setExclusive(final boolean exclusive) {
    setInclusive(!exclusive);
  }

  /**
   * Determines whether Files are included or excluded based on the filter criteria.
   * @return a boolean value indicating whether Files are inclusive depending upon whether the File satisfies
   * this filter's criteria.
   * @see AbstractFileFilter#isExclusive()
   */
  public boolean isInclusive() {
    return inclusive;
  }

  /**
   * Sets whether this FileFilter will include Files or exclude them based on the filter criteria.
   * @param inclusive a boolean value configuring the file filter to include or exclude Files satisfying
   * the criteria of this file filter.
   * @see AbstractFileFilter#setExclusive(boolean)
   */
  public void setInclusive(final boolean inclusive) {
    this.inclusive = inclusive;
  }

  /**
   * Determines whether the filter should return hidden files in the file system.
   * @return a boolean value indicating whether hidden files are returned by this filter.
   */
  public boolean isShowHidden() {
    return showHidden;
  }

  /**
   * Sets whether hidden files are return by this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  public void setShowHidden(final boolean showHidden) {
    this.showHidden = showHidden;
  }

}
