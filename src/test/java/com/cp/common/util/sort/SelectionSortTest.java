/*
 * SelectionSortTest.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.util.sort.*;
import com.cp.common.util.sort.SelectionSort;
import java.util.Comparator;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SelectionSortTest extends com.cp.common.util.sort.AbstractSortTest {

  private static final int NUMERIC_LIST_SIZE = 1000;

  public SelectionSortTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SelectionSortTest.class);
    //suite.addTest(new SelectionSortTest("testName"));
    return suite;
  }

  protected int getNumericListSize() {
    return NUMERIC_LIST_SIZE;
  }

  protected Sorter getSorterImplementation(final Comparator orderBy) {
    return new SelectionSort(orderBy);
  }

}
