/*
 * FileSizeFilterFactory.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.13
 * @see java.io.File
 * @see java.io.FileFilter
 */

package com.cp.common.io;

import com.cp.common.lang.RelationalOperator;
import java.io.File;
import java.io.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FileSizeFilterFactory {

  private static final Log logger = LogFactory.getLog(FileSizeFilterFactory.class);

  /**
   * Private constructor to enforce non-instantiability of the FileSizeFilterFactory class.
   * The factory contains methods for creating FileFilter objects that filter files based on their size.
   */
  private FileSizeFilterFactory() {
  }

  /**
   * Returns a FileFilter object used to filter File objects that do not equal the specified file size in bytes.
   * @param fileSize is a long value indicating that only File objects of the specified file size will be accepted
   * by this file filter.
   * @return a FileFilter object which filters files which do not match the specified file size.
   * @see FileSizeFilterFactory#getRelationalSizeFileFilter(com.cp.common.lang.RelationalOperator)
   */
  public static FileFilter getIsSizeFileFilter(final long fileSize) {
    return getRelationalSizeFileFilter(RelationalOperator.getEqualTo(fileSize));
  }

  /**
   * Returns a FileFilter object used to filter File objects based on the maximum file size in bytes.
   * @param maxFileSize is a long value indicating that only files less than or equal to the specified maximum file size
   * will be accepted by this file filter.
   * @return a FileFilter object excluding File objects having a file size greater than the specified maximum file size.
   * @see FileSizeFilterFactory#getRelationalSizeFileFilter(com.cp.common.lang.RelationalOperator)
   */
  public static FileFilter getMaxSizeFileFilter(final long maxFileSize) {
    return getRelationalSizeFileFilter(RelationalOperator.getLessThanEqualTo(maxFileSize));
  }

  /**
   * Returns a FileFilter object used to filter File objects based on the minimum file size in bytes.
   * @param minFileSize is a long value indicating that only files greater than or equal to the specified minimum
   * file size will be accepted by this file filter.
   * @return a FileFilter object excluding File objects having a file size less than the specified minimum file size.
   * @see FileSizeFilterFactory#getRelationalSizeFileFilter(com.cp.common.lang.RelationalOperator)
   */
  public static FileFilter getMinSizeFileFilter(final long minFileSize) {
    return getRelationalSizeFileFilter(RelationalOperator.getGreaterThanEqualTo(minFileSize));
  }

  /**
   * Returns a FileFilter object used to filter File objects based on both a minimum and maximum file sizes in bytes.
   * @param minFileSize the minimum file size of the File object in order to be accepted by this file filter.
   * @param maxFileSize the maximum file size of the File object in order to be accepted by this file filter.
   * @return a FileFilter that filters files who's file size falls outside the specified minimum and maximum file size
   * in bytes.
   * @see FileSizeFilterFactory#getRelationalSizeFileFilter(com.cp.common.lang.RelationalOperator)
   */
  public static FileFilter getRangeSizeFileFilter(final long minFileSize, final long maxFileSize) {
    return getRelationalSizeFileFilter(RelationalOperator.getGreaterThanEqualToAndLessThanEqualTo(minFileSize, maxFileSize));
  }

  /**
   * Returns a FileFilter object used to filter File objects based on the constraints of the specified
   * relational operator.
   * @param op the RelationalOperator object used in defining the criteria of the filter that the File objects
   * must satisfy.
   * @return a FileFilter object used to filter File objects based on the criteria defined in the relational operator.
   */
  public static FileFilter getRelationalSizeFileFilter(final RelationalOperator<Long> op) {
    return new FileFilter() {
      public boolean accept(final File pathname) {
        if (logger.isDebugEnabled()) {
          logger.debug("file size (" + pathname.length() + ")");
        }
        return op.accept(pathname.length());
      }

      @Override
      public String toString() {
        return "Accepts files having a size conforming to the relational operator (" + op.toString() + ")";
      }
    };
  }

}
