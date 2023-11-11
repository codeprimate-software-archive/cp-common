/*
 * MultithreadedQuickSortPerformanceTest.java (c) 28 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.29
 */

package com.codeprimate.util.sort;

import com.cp.common.util.sort.AbstractSortFactory;
import com.cp.common.util.SearchableSortableCollection;
import com.cp.common.util.sort.Sorter;
import com.cp.common.util.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MultithreadedQuickSortPerformanceTest extends TestCase {

  private static final int ARRAY_SIZE = 1000000;
  private static final int PRINT_COUNT = 10;

  public MultithreadedQuickSortPerformanceTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MultithreadedQuickSortPerformanceTest.class);
    //suite.addTest(new MultithreadedQuickSortPerformanceTest("testName"));
    return suite;
  }

  private void assertSorted(final Collection collection) {
    int previousValue = Integer.MIN_VALUE;
    for (Iterator it = collection.iterator(); it.hasNext(); ) {
      final int currentValue = ((Integer) it.next()).intValue();
      assertTrue(currentValue >= previousValue);
      previousValue = currentValue;
    }
  }

  private Integer[] generateIntegerArray() {
    final Integer[] array = new Integer[ARRAY_SIZE];
    final Random randomNumberGenerator = new Random(Calendar.getInstance().getTimeInMillis());

    for (int index = ARRAY_SIZE; --index >= 0; ) {
      array[index] = new Integer(randomNumberGenerator.nextInt(ARRAY_SIZE));
    }

    return array;
  }

  private String print(final Collection collection) {
    final StringBuffer buffer = new StringBuffer("[");
    int index = 0;
    for (Iterator it = collection.iterator(); it.hasNext() && index < PRINT_COUNT; index++) {
      buffer.append(it.next());
      buffer.append(it.hasNext() ? ", " : "");
    }
    buffer.append("...]");
    return buffer.toString();
  }

  public void testPeformance() throws Exception {
    final Integer[] integerArray = generateIntegerArray();

    // Sort using Quick Sort
    final SearchableSortableCollection quickSortList = new SearchableSortableCollection(
      new ArrayList(Arrays.asList(integerArray)));

    final Sorter quickSort = AbstractSortFactory.getInstance().getQuickSort();

    System.out.println("Sorting with Quick Sort " + print(quickSortList) + "...");
    long t0 = System.currentTimeMillis();
    quickSort.sort(quickSortList);
    long t1 = System.currentTimeMillis();

    final long quickSortDiff = (t1 - t0);

    System.out.println("Quick Sort sorted (" + ARRAY_SIZE + ") elements in ("
      + TimeUnit.convertTo(quickSortDiff, TimeUnit.MILLISECOND) + ") time!");

    // Sort using Multithreaded Quick Sort
    final SearchableSortableCollection multithreadedQuickSortList = new SearchableSortableCollection(
      new ArrayList(Arrays.asList(integerArray)));

    final Sorter multithreadedQuickSort = AbstractSortFactory.getInstance().getMultithreadedQuickSort();

    System.out.println("Sorting with Multithreaded Quick Sort " + print(multithreadedQuickSortList) + "...");
    t0 = System.currentTimeMillis();
    multithreadedQuickSort.sort(multithreadedQuickSortList);
    t1 = System.currentTimeMillis();

    final long multithreadedQuickSortDiff = (t1 - t0);

    System.out.println("Multithreaded Quick Sort sorted (" + ARRAY_SIZE + ") elements in ("
      + TimeUnit.convertTo(multithreadedQuickSortDiff, TimeUnit.MILLISECOND) + ") time!");

    // Sort using Collections.sort
    final List integerArrayList = new ArrayList(Arrays.asList(integerArray));

    System.out.println("Sorting with Collections.sort " + print(integerArrayList) + "...");
    t0 = System.currentTimeMillis();
    Collections.sort(integerArrayList);
    t1 = System.currentTimeMillis();

    final long collectionsSortDiff = (t1 - t0);

    System.out.println("Collections.sort sorted (" + ARRAY_SIZE + ") elements in ("
      + TimeUnit.convertTo(collectionsSortDiff, TimeUnit.MILLISECOND)+ ") time!");

    assertSorted(quickSortList);
    assertSorted(multithreadedQuickSortList);
    assertTrue("Multithreaded Quick Sort was not faster than Collections.sort!", multithreadedQuickSortDiff < collectionsSortDiff);
    assertTrue("Multithreaded Quick Sort was not faster than Quick Sort!", multithreadedQuickSortDiff < quickSortDiff);
  }

  public static void main(final String[] args) throws Exception {
    new MultithreadedQuickSortPerformanceTest("command-line test").testPeformance();
  }

}
