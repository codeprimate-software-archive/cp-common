/*
 * TreeSortTest.java (c) 25 August 2005
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

public class TreeSortTest extends AbstractSortTest {

  private static final int NUMERIC_LIST_SIZE = 1000;

  public TreeSortTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TreeSortTest.class);
    //suite.addTest(new TreeSortTest("testName"));
    return suite;
  }

  protected int getNumericListSize() {
    return NUMERIC_LIST_SIZE;
  }

  protected com.cp.common.util.sort.Sorter getSorterImplementation(final Comparator orderBy) {
    return new com.codeprimate.util.sort.TreeSort(orderBy);
  }

}
