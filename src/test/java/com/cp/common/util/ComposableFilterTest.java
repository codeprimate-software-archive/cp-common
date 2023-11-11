/*
 * ComposableFilterTest.java (c) 25 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.25
 * @see com.cp.common.util.ComposableFilter
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComposableFilterTest extends TestCase {

  public ComposableFilterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ComposableFilterTest.class);
    //suite.addTest(new ComposableFilterTest("testName"));
    return suite;
  }

  protected List<Integer> getFilteredList(final List<Integer> sourceList, final Filter<Integer> filter) {
    Assert.notNull(sourceList, "The source list cannot be null!");
    Assert.notNull(filter, "The filter cannot be null!");

    final List<Integer> filteredList = new ArrayList<Integer>(sourceList.size());

    for (final Integer number : sourceList) {
      if (filter.accept(number)) {
        filteredList.add(number);
      }
    }

    return filteredList;
  }

  public void testAccept() throws Exception {
    final List<Integer> sourceList = CollectionUtil.getList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    Filter<Integer> greaterThanFilter = new GreaterThanFilter(5);
    Filter<Integer> lessThanFilter = new LessThanFilter(6);
    Filter<Integer> composedFilter = ComposableFilter.composeAnd(lessThanFilter, null);

    assertNotNull(composedFilter);
    assertSame(lessThanFilter, composedFilter);
    assertEquals(CollectionUtil.getList(0, 1, 2, 3, 4, 5), getFilteredList(sourceList, composedFilter));

    composedFilter = ComposableFilter.composeOr(null, greaterThanFilter);

    assertNotNull(composedFilter);
    assertSame(greaterThanFilter, composedFilter);
    assertEquals(CollectionUtil.getList(6, 7, 8, 9), getFilteredList(sourceList, composedFilter));

    greaterThanFilter = new GreaterThanFilter(2);
    lessThanFilter = new LessThanFilter(7);
    composedFilter = ComposableFilter.composeAnd(greaterThanFilter, lessThanFilter);

    assertNotNull(composedFilter);
    assertTrue(composedFilter instanceof ComposableFilter);
    assertEquals(CollectionUtil.getList(3, 4, 5, 6), getFilteredList(sourceList, composedFilter));

    composedFilter = ComposableFilter.composeOr(greaterThanFilter, lessThanFilter);

    assertNotNull(composedFilter);
    assertTrue(composedFilter instanceof ComposableFilter);
    assertEquals(sourceList, getFilteredList(sourceList, composedFilter));

    greaterThanFilter = new GreaterThanFilter(7);
    lessThanFilter = new LessThanFilter(2);
    composedFilter = ComposableFilter.composeOr(greaterThanFilter, lessThanFilter);

    assertNotNull(composedFilter);
    assertTrue(composedFilter instanceof ComposableFilter);
    assertEquals(CollectionUtil.getList(0, 1, 8, 9), getFilteredList(sourceList, composedFilter));

    composedFilter = ComposableFilter.composeAnd(greaterThanFilter, lessThanFilter);

    assertNotNull(composedFilter);
    assertTrue(composedFilter instanceof ComposableFilter);
    assertEquals(CollectionUtil.getList(), getFilteredList(sourceList, composedFilter));
  }

  public void testCompose() throws Exception {
    final Filter<Object> mockFilter = new MockFilter();
    final Filter<Object> testFilter = new TestFilter();

    assertNull(ComposableFilter.composeAnd(null, null));
    assertSame(mockFilter, ComposableFilter.composeAnd(mockFilter, null));
    assertSame(testFilter, ComposableFilter.composeOr(null, testFilter));
    assertTrue(ComposableFilter.compose(mockFilter, testFilter, null) instanceof ComposableFilter);
  }

  protected static class GreaterThanFilter implements Filter<Integer> {

    private final Integer lowerBound;

    public GreaterThanFilter(final Integer lowerBound) {
      Assert.notNull(lowerBound, "The lower bound cannot be null!");
      this.lowerBound = lowerBound;
    }

    protected Integer getLowerBound() {
      return lowerBound;
    }

    public boolean accept(final Integer obj) {
      Assert.notNull(obj, "The number to filter cannot be null!");
      return (obj > getLowerBound());
    }
  }

  protected static class LessThanFilter implements Filter<Integer> {

    private final Integer upperBound;

    public LessThanFilter(final Integer upperBound) {
      Assert.notNull(upperBound, "The upper bound cannot be null!");
      this.upperBound = upperBound;
    }

    protected Integer getUpperBound() {
      return upperBound;
    }

    public boolean accept(final Integer obj) {
      Assert.notNull(obj, "The number to filter cannot be null!");
      return (obj < getUpperBound());
    }
  }

  protected static final class MockFilter implements Filter<Object> {

    public boolean accept(final Object obj) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

  protected static final class TestFilter implements Filter<Object> {

    public boolean accept(final Object obj) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
