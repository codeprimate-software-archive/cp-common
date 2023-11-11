/*
 * HeapSortTest.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.util.sort.*;
import com.cp.common.util.sort.HeapSort;
import java.util.Comparator;
import junit.framework.Test;
import junit.framework.TestSuite;

public class HeapSortTest extends com.cp.common.util.sort.AbstractSortTest {

  private static final int NUMERIC_LIST_SIZE = 10000;

  public HeapSortTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(HeapSortTest.class);
    //suite.addTest(new HeapSortTest("testNumberSort"));
    return suite;
  }

  protected int getNumericListSize() {
    return NUMERIC_LIST_SIZE;
  }

  protected com.cp.common.util.sort.Sorter getSorterImplementation(final Comparator orderBy) {
    return new HeapSort(orderBy);
  }

}
