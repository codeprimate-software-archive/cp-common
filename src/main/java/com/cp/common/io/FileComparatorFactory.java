/*
 * FileComparatorFactory.java (c) 17 April 2002
 *
 * Performs case-insensitive searches on various file attributes such as the
 * file's last modified date, location, name, size and type.
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.4
 * @see com.cp.common.util.DateUtil
 * @see com.cp.common.util.Order
 * @see java.io.File
 * @see java.util.Comparator
 */

package com.cp.common.io;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Order;
import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FileComparatorFactory {

  private static final Log logger = LogFactory.getLog(FileComparatorFactory.class);

  /**
   * Factory method to return a Comparator object that will compare File objects by type (file extension).
   * @param order an Order enumerated value specifying the order, either ascending or descending, of the comparison.
   * @return a java.util.Comparator object used to compare File objects by their type (file extension).
   */
  public static Comparator<File> getFileExtensionComparator(final Order order) {
    return new FileExtensionComparator(order);
  }

  /**
   * Factory method to return a Comparator object that will compare File objects by name.
   * @param order an Order enumerated value specifying the order, either ascending or descending, of the comparison.
   * @return a java.util.Comparator object used to compare File objects by their name.
   */
  public static Comparator<File> getFileNameComparator(final Order order) {
    return new FileNameComparator(order);
  }

  /**
   * Factory method to return a Comparator object that will compare File objects by absolute path (file system path).
   * @param order an Order enumerated value specifying the order, either ascending or descending, of the comparison.
   * @return a java.util.Comparator object used to compare File objects by their path.
   */
  public static Comparator<File> getFilePathComparator(final Order order) {
    return new FilePathComparator(order);
  }

  /**
   * Factory method to return a Comparator object that will compare File objects by size.
   * @param order an Order enumerated value specifying the order, either ascending or descending, of the comparison.
   * @return a java.util.Comparator object used to compare File objects by their size.
   */
  public static Comparator<File> getFileSizeComparator(final Order order) {
    return new FileSizeComparator(order);
  }

  /**
   * Factory method to return a Comparator object that will compare File objects by last modified date.
   * @param order an Order enumerated value specifying the order, either ascending or descending, of the comparison.
   * @return a java.util.Comparator object used to compare File objects by their last modified date.
   */
  public static Comparator<File> getLastModifiedComparator(final Order order) {
    return new LastModifiedComparator(order);
  }

  /**
   * Creates an instance of the FileSizeComparator class to order two File objects by their type (extension).
   */
  private static final class FileExtensionComparator implements Comparator<File> {

    private final Order order;

    public FileExtensionComparator(final Order order) {
      this.order = ObjectUtil.getDefaultValue(order, Order.ASCENDING);
    }

    public int compare(final File file1, final File file2) {
      final String fileExtension1 = FileUtil.getFileExtension(file1).toLowerCase();
      final String fileExtension2 = FileUtil.getFileExtension(file2).toLowerCase();

      if (logger.isDebugEnabled()) {
        logger.debug("file extension of file 1 (" + fileExtension1 + ")");
        logger.debug("file extension of file 2 (" + fileExtension2 + ")");
      }

      return (order.getOrderId() * fileExtension1.compareTo(fileExtension2));
    }

    @Override
    public String toString() {
      return "Comparing by file extension in " + order.getDescription() + " order.";
    }
  }

  /**
   * Creates an instance of the FileNameComparator class to order two File objects by name.
   */
  private static final class FileNameComparator implements Comparator<File> {

    private final Order order;

    public FileNameComparator(final Order order) {
      this.order = ObjectUtil.getDefaultValue(order, Order.ASCENDING);
    }

    public int compare(final File file1, final File file2) {
      final String filename1 = FileUtil.getFilename(file1);
      final String filename2 = FileUtil.getFilename(file2);

      if (logger.isDebugEnabled()) {
        logger.debug("name of file 1 (" + filename1 + ")");
        logger.debug("name of file 2 (" + filename2 + ")");
      }

      return (order.getOrderId() * filename1.compareTo(filename2));
    }

    @Override
    public String toString() {
      return "Comparing by filename in " + order.getDescription() + " order.";
    }
  }

  /**
   * Creates an instance of the FilePathComparator class to order two File objects by path
   */
  private static final class FilePathComparator implements Comparator<File> {

    private final Order order;

    public FilePathComparator(final Order order) {
      this.order = ObjectUtil.getDefaultValue(order, Order.ASCENDING);
    }

    public int compare(final File file1, final File file2) {
      final String filePath1 = FileUtil.getFileLocation(file1);
      final String filePath2 = FileUtil.getFileLocation(file2);

      if (logger.isDebugEnabled()) {
        logger.debug("absolute path (" + filePath1 + ") of file 1 (" + file1 + ")!");
        logger.debug("absolute path (" + filePath2 + ") of file 2 (" + file2 + ")!");
      }

      return (order.getOrderId() * filePath1.compareTo(filePath2));
    }

    @Override
    public String toString() {
      return "Comparing by the absolute pathname of the file in " + order.getDescription() + " order.";
    }
  }

  /**
   * Creates an instance of the FileSizeComparator class to order two File objects by size.
   * Yes, size is everything!
   */
  private static final class FileSizeComparator implements Comparator<File> {

    private final Order order;

    public FileSizeComparator(final Order order) {
      this.order = ObjectUtil.getDefaultValue(order, Order.ASCENDING);
    }

    public int compare(final File file1, final File file2) {
      final long fileSize1 = file1.length();
      final long fileSize2 = file2.length();

      if (logger.isDebugEnabled()) {
        logger.debug("size of file 1 (" + fileSize1 + ")");
        logger.debug("size of file 2 (" + fileSize2 + ")");
      }

      return (order.getOrderId() * Long.valueOf(fileSize1).compareTo(fileSize2));
    }

    @Override
    public String toString() {
      return "Comparing by file size in " + order.getDescription() + " order.";
    }
  }

  /**
   * Creates an instance of the LastModifiedComparator class to order two File objects by last modified date.
   */
  private static final class LastModifiedComparator implements Comparator<File> {

    private final Order order;

    public LastModifiedComparator(final Order order) {
      this.order = ObjectUtil.getDefaultValue(order, Order.ASCENDING);
    }

    /**
     * Compares two File objects by the lastModified property for ordering.
     * Note the commented out code.  I am not sure why this does not work as it retrieves the
     * number of miliseconds since the epoch for each File object calling lastModified method
     * and performing a subtraction.  If it is a negative value, then the first file object o1
     * is older (or create before) the second file object o2.  If the result of the subtraction
     * is zero, the file times are equal, and if the result of the subtraction is positive, the
     * file object o2 is older (create before) file object o1. Creating a Date object is the
     * only way I know how to successfully do the the comparison.
     */
    public int compare(final File file1, final File file2) {
      final Calendar lastModified1 = DateUtil.getCalendar(file1.lastModified());
      final Calendar lastModified2 = DateUtil.getCalendar(file2.lastModified());

      if (logger.isDebugEnabled()) {
        logger.debug("last modified date/time of file 1 (" + lastModified1 + ")");
        logger.debug("last modified date/time of file 2 (" + lastModified2 + ")");
      }

      return (order.getOrderId() * DateUtil.getCalendarComparator().compare(lastModified1, lastModified2));
    }

    @Override
    public String toString() {
      return "Comparing by the file's lastModified property value in " + order.getDescription() + " order.";
    }
  }

}
