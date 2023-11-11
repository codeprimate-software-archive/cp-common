/*
 * ComposableFileFilter.java (c) 17 April 2002
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.13
 * @see com.cp.common.util.Filter
 * @see java.io.File
 * @see java.io.FileFilter
 */

package com.cp.common.io;

import com.cp.common.lang.Assert;
import com.cp.common.lang.LogicalOperator;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.Filter;
import java.io.File;
import java.io.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ComposableFileFilter implements FileFilter, Filter<File> {

  private static final Log logger = LogFactory.getLog(ComposableFileFilter.class);

  private static final LogicalOperator DEFAULT_OP = LogicalOperator.AND;

  private final FileFilter filter1;
  private final FileFilter filter2;

  private final LogicalOperator op;

  /**
   * Creates an instance of the FileFilterComposition class containing references to two FileFilter instances chaining
   * them together in a collective effort to filter files.
   * @param filter1 the first FileFilter object in the chain.
   * @param filter2 the second FileFilter object in the chain.
   * @param op the logical operator used to combine the conditions of the two file filters.
   * @throws java.lang.NullPointerException if either FileFilter object is null.
   */
  private ComposableFileFilter(final FileFilter filter1, final FileFilter filter2, final LogicalOperator op) {
    Assert.notNull(filter1, "The first FileFilter object cannot be null!");
    Assert.notNull(filter2, "The second FileFilter object cannot be null!");
    this.filter1 = filter1;
    this.filter2 = filter2;
    this.op = ObjectUtil.getDefaultValue(op, DEFAULT_OP);
  }

  /**
   * Adds the specified FileFilter object to the chain of FileFilters.
   * @param existingFilter an existing FileFilter object used to link together with a new FileFilter object.
   * @param newFilter a new FileFilter object to chain together with the existing FileFilter object.
   * @return a FileFilter object composition composed of the existing and new FilterFilter objects.
   */
  public static FileFilter add(final FileFilter existingFilter, final FileFilter newFilter) {
    return add(existingFilter, newFilter, DEFAULT_OP);
  }

  /**
   * Adds the specified FileFilter object to the chain of FileFilters.
   * @param existingFilter an existing FileFilter object used to link together with a new FileFilter object.
   * @param newFilter a new FileFilter object to chain together with the existing FileFilter object.
   * @param op the logical operator used to combine the conditions of the two file filters.
   * @return a FileFilter object composition composed of the existing and new FilterFilter objects.
   */
  public static FileFilter add(final FileFilter existingFilter, final FileFilter newFilter, final LogicalOperator op) {
    return (ObjectUtil.isNull(existingFilter) ? newFilter : (ObjectUtil.isNull(newFilter) ? existingFilter
      : new ComposableFileFilter(existingFilter, newFilter, op)));
  }

  /**
   * Determines whether the specified File referenced by pathname meets the criteria of this file filter.
   * @param pathname the File object being tested by this file filter.
   * @return a boolean value indicating whether the File meets the criteria of this file filter.
   */
  public boolean accept(final File pathname) {
    if (logger.isDebugEnabled()) {
      logger.debug("Determining whether file (" + pathname + ") is accepted by filter 1 (" + filter1.getClass()
        + ") " + op + " filter 2 (" + filter2.getClass() + ")");
    }
    return op.op(filter1.accept(pathname), filter2.accept(pathname));
  }

  /**
   * Determines whether the specified FileFilter object is a file filter in the composition of file filters
   * referenced by this FileFilterComposition object.  Note, a FileFilterComposition cannot contain itself.
   * Therefore, a call such as <code>fileFilterMultiAcceptor.contains(fileFilterMultiAcceptor);</code> returns false.
   * @param filter the FileFilter object tested for containment.
   * @return a boolean value indicating whether the specified FileFilter object is contained in the composition
   * of FileFilter objects referenced by this FileFilterComposition object.
   * @see ComposableFileFilter#contains(java.io.FileFilter, java.io.FileFilter)
   */
  public boolean contains(final FileFilter filter) {
    return (ObjectUtil.isNotNull(filter) && (filter1.equals(filter) || filter2.equals(filter)
      || contains(filter1, filter) || contains(filter2, filter)));
  }

  /**
   * Determines whether the containingFilter object is an instance of FileFilterComposition, and if so, determines
   * whether the containingFilter object is composed of the containedFilter object.
   * @param containingFilter a potential instance of FileFilterComposition.
   * @param containedFilter the FileFilter object being tested for whether the containingFilter is componsed
   * of the containedFilter.
   * @return a boolean value indicating whether the containingFilter object is composed of the containedFilter object.
   * @see ComposableFileFilter#contains(java.io.FileFilter)
   */
  private boolean contains(final FileFilter containingFilter, final FileFilter containedFilter) {
    return (containingFilter instanceof ComposableFileFilter)
      && ((ComposableFileFilter) containingFilter).contains(containedFilter);
  }

  /**
   * Removes the specified FileFilter object from the composition of FileFilters.
   * @param filterToRemove a FileFilter object to remove from the composition of FileFilters.
   * @return the FileFilter composition without the specified FileFilter object designated to be removed
   * from the FileFilter composition.
   * @see ComposableFileFilter#remove(java.io.FileFilter, com.cp.common.lang.LogicalOperator)
   */
  public FileFilter remove(final FileFilter filterToRemove) {
    return remove(filterToRemove, DEFAULT_OP);
  }

  /**
   * Removes the specified FileFilter object from the composition of FileFilters.
   * @param filterToRemove a FileFilter object to remove from the composition of FileFilters.
   * @param op the logical operator used to combine the conditions of the two file filters.
   * @return the FileFilter composition without the specified FileFilter object designated to be removed
   * from the FileFilter composition.
   * @see ComposableFileFilter#remove(java.io.FileFilter, java.io.FileFilter, com.cp.common.lang.LogicalOperator)
   */
  public FileFilter remove(final FileFilter filterToRemove, final LogicalOperator op) {
    if (ObjectUtil.isNotNull(filterToRemove)) {
      if (filter1.equals(filterToRemove)) {
        return filter2;
      }

      if (filter2.equals(filterToRemove)) {
        return filter1;
      }

      final FileFilter one = remove(filter1, filterToRemove, op);
      final FileFilter two = remove(filter2, filterToRemove, op);

      if (one.equals(filter1) && two.equals(filter2)) {
        // filterToRemove was not contained in the file filter composition
        return this;
      }
      else {
        return add(one, two, op);
      }
    }

    return this;
  }

  /**
   * Removes the FileFilter object referenced by the filterToRemove from the composition of FileFilters.
   * @param filterComposition a FileFilter object composition pointing to the beginning of the FileFilter composition.
   * @param filterToRemove a FileFilter object to remove from the FileFilter composition.
   * @param op the logical operator used to combine the conditions of the two file filters.
   * @return a reference to the FileFilter composition with the filterToRemove removed.
   * @see ComposableFileFilter#remove(java.io.FileFilter, com.cp.common.lang.LogicalOperator)
   */
  private FileFilter remove(final FileFilter filterComposition, final FileFilter filterToRemove, final LogicalOperator op) {
    if (filterComposition instanceof ComposableFileFilter) {
      return ((ComposableFileFilter) filterComposition).remove(filterToRemove, op);
    }
    return filterComposition;
  }

  /**
   * Returns a String representation of this FileFilterComposition object.
   * @return a String representation of this FileFilterComposition object.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{filter1 = ");
    buffer.append(filter1);
    buffer.append(", filter2 = ").append(filter2);
    buffer.append(", op = ").append(op);
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
