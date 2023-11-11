/*
 * InsertionSortTest.java (c) 19 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.util.sort.*;
import com.cp.common.util.sort.InsertionSort;
import com.cp.common.util.SortAscendingComparator;
import com.cp.common.util.SortDescendingComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class InsertionSortTest extends com.cp.common.util.sort.AbstractSortTest {

  private static final int NUMERIC_LIST_SIZE = 1000;

  public InsertionSortTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(InsertionSortTest.class);
    //suite.addTest(new InsertionSortTest("testName"));
    return suite;
  }

  protected int getNumericListSize() {
    return NUMERIC_LIST_SIZE;
  }

  protected com.cp.common.util.sort.Sorter getSorterImplementation(final Comparator orderBy) {
    return new InsertionSort(orderBy);
  }

  public void testInsertionSort() throws Exception {
    final List numberList = new ArrayList(10);
    numberList.add(new Integer(1));
    numberList.add(new Integer(5));
    numberList.add(new Integer(2));
    numberList.add(new Integer(9));
    numberList.add(new Integer(4));
    numberList.add(new Integer(7));
    numberList.add(new Integer(3));
    numberList.add(new Integer(6));
    numberList.add(new Integer(0));
    numberList.add(new Integer(8));

    // Create a SortableList from the numberList!
    final SortableList sortableNumberList = new SortableList(numberList);

    // Sort the List in ascending order and assert the List has been sorted!
    com.cp.common.util.sort.Sorter insertionSort = new InsertionSort(SortAscendingComparator.getInstance());
    insertionSort.sort(sortableNumberList);

    for (int index = 0, size = sortableNumberList.size(); index < size; index++) {
      assertEquals(new Integer(index), sortableNumberList.get(index));
    }

    // Sort the List in descending order and assert the List has been sorted!
    insertionSort = new InsertionSort(SortDescendingComparator.getInstance());
    insertionSort.sort(sortableNumberList);

    for (int index = 0, size = sortableNumberList.size(); index < size; index++) {
      assertEquals(new Integer(size - index - 1), sortableNumberList.get(index));
    }
  }

}
