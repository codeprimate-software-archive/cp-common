/*
 * MultithreadedQuickSortTest.java (c) 27 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.codeprimate.util.sort;

import com.cp.common.util.sort.AbstractSortTest;
import com.cp.common.util.sort.Sorter;
import com.codeprimate.util.sort.*;
import java.util.Comparator;
import junit.framework.Test;
import junit.framework.TestSuite;

public class MultithreadedQuickSortTest extends AbstractSortTest {

  private static final int NUMERIC_LIST_SIZE = 10000;

  public MultithreadedQuickSortTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MultithreadedQuickSortTest.class);
    //suite.addTest(new MultithreadedQuickSortTest("testNumberSort"));
    return suite;
  }

  protected int getNumericListSize() {
    return NUMERIC_LIST_SIZE;
  }

  protected com.cp.common.util.sort.Sorter getSorterImplementation(final Comparator orderBy) {
    return new com.codeprimate.util.sort.MultithreadedQuickSort(orderBy);
  }

}
