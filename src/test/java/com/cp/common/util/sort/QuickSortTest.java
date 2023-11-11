/*
 * QuickSortTest.java (c) 21 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.util.sort.*;
import com.cp.common.util.sort.QuickSort;
import com.cp.common.util.SortAscendingComparator;
import com.cp.common.util.SortDescendingComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import junit.framework.Test;
import junit.framework.TestSuite;

public class QuickSortTest extends com.cp.common.util.sort.AbstractSortTest {

  private static final int NUMERIC_LIST_SIZE = 10000;

  public QuickSortTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(QuickSortTest.class);
    //suite.addTest(new QuickSortTest("testName"));
    return suite;
  }

  protected int getNumericListSize() {
    return NUMERIC_LIST_SIZE;
  }

  protected com.cp.common.util.sort.Sorter getSorterImplementation(final Comparator orderBy) {
    return new QuickSort(orderBy);
  }

  public void testQuickSort() throws Exception {
    final List numberList = new ArrayList(10);
    numberList.add(new Integer(20));
    numberList.add(new Integer(13));
    numberList.add(new Integer(18));
    numberList.add(new Integer(1));
    numberList.add(new Integer(11));
    numberList.add(new Integer(5));
    numberList.add(new Integer(16));
    numberList.add(new Integer(2));
    numberList.add(new Integer(9));
    numberList.add(new Integer(4));
    numberList.add(new Integer(15));
    numberList.add(new Integer(12));
    numberList.add(new Integer(7));
    numberList.add(new Integer(3));
    numberList.add(new Integer(17));
    numberList.add(new Integer(6));
    numberList.add(new Integer(0));
    numberList.add(new Integer(10));
    numberList.add(new Integer(8));
    numberList.add(new Integer(19));
    numberList.add(new Integer(14));
    numberList.add(new Integer(21));

    // Create a SortableList from the numberList!
    final SortableList sortableNumberList = new SortableList(numberList);

    // Sort the List in ascending order and assert the List has been sorted!
    com.cp.common.util.sort.Sorter quickSort = new QuickSort(SortAscendingComparator.getInstance(), 5);
    quickSort.sort(sortableNumberList);

    for (int index = 0, size = sortableNumberList.size(); index < size; index++) {
      assertEquals(new Integer(index), sortableNumberList.get(index));
    }

    // Sort the List in descending order and assert the List has been sorted!
    quickSort = new QuickSort(SortDescendingComparator.getInstance(), 12);
    quickSort.sort(sortableNumberList);

    for (int index = 0, size = sortableNumberList.size(); index < size; index++) {
      assertEquals(new Integer(size - index - 1), sortableNumberList.get(index));
    }
  }

}
