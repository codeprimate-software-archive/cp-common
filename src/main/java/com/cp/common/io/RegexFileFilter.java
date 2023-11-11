/*
 * RegexFileFilter.java (c) 16 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see com.cp.common.io.FileOnlyFileFilter
 * @see java.util.regex.Pattern
 */

package com.cp.common.io;

import com.cp.common.lang.Assert;
import java.io.File;
import java.util.regex.Pattern;

public class RegexFileFilter extends FileOnlyFileFilter {

  private final Pattern pattern;

  /**
   * Creates an instance of the RegexFileFilter class using a regular expression to match files in the file system.
   * @param pattern a String value denoting the regular expression pattern used to match files in the file system.
   */
  public RegexFileFilter(final String pattern) {
    Assert.notEmpty(pattern, "(" + pattern + ") is not a valid regular expression!");
    this.pattern = Pattern.compile(pattern);
  }

  /**
   * Returns the regular expression pattern used to match files in the file system.
   * @return a String value denoting the regular expression pattern used by this FileFilter to match files
   * in the file system.
   */
  public String getPattern() {
    return pattern.pattern();
  }

  /**
   * Determines whether the specified File object satisifes the conditions of this FileFilter based on the use of
   * regular expressions to express the rules and criteria of this FileFilter.
   * @param pathname the File object to apply the filter to.
   * @return a boolean value indicating whether the File object satisfies the conditions of this FileFilter.
   */
  public boolean accept(final File pathname) {
    if (logger.isDebugEnabled()) {
      logger.debug("file (" + pathname + ")");
    }
    return (super.accept(pathname) && pattern.matcher(FileUtil.getFilename(pathname)).matches());
  }

  /**
   * Returns a String containing the internal state of this FileFilter.
   * @return a String representation of this FileFilter.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{pattern = ");
    buffer.append(getPattern());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
