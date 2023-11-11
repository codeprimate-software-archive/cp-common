/*
 * AbstractSearchTest.java (c) 19 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.util.search.Searcher
 * @see com.cp.common.util.search.BinarySearch
 * @see com.cp.common.util.search.BinarySearchFilter
 * @see java.util.List
 */

package com.cp.common.util.search;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Searchable;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;

public class AbstractSearchTest extends TestCase {

  public AbstractSearchTest(final String testName) {
    super(testName);
  }

  protected static final class DefaultBinarySearchFilter implements BinarySearchFilter {
    private final Comparable result;

    public DefaultBinarySearchFilter(final Comparable result) {
      if (ObjectUtil.isNull(result)) {
        throw new NullPointerException("The result cannot be null!");
      }
      this.result = result;
    }

    public int compare(final Object obj) throws SearchException {
      return result.compareTo(obj);
    }

    public boolean matches(final Object obj) throws SearchException {
      return result.equals(obj);
    }
  }

  protected static final class DefaultSearchFilter implements SearchFilter {
    private final Set results;

    public DefaultSearchFilter(final Set results) {
      if (ObjectUtil.isNull(results)) {
        throw new NullPointerException("The results cannot be null!");
      }
      this.results = results;
    }

    public boolean matches(final Object obj) throws SearchException {
      return results.contains(obj);
    }
  }

  protected static final class SearchableList extends AbstractList implements Searchable {
    private final List theList;

    public SearchableList(final List theList) {
      this.theList = theList;
    }

    public void add(int index, Object element) {
      theList.add(index, element);
    }

    public Object get(int index) {
      return theList.get(index);
    }

    public Iterator iterator() {
      return theList.iterator();
    }

    public Object remove(int index) {
      return theList.remove(index);
    }

    public Object set(int index, Object element) {
      return theList.set(index, element);
    }

    public int size() {
      return theList.size();
    }
  }

}
