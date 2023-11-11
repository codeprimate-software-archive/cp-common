/*
 * BinarySearchTest.java (c) 19 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.19
 */

package com.cp.common.util.search;

import com.cp.common.util.search.*;
import com.cp.common.util.search.BinarySearch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class BinarySearchTest extends com.cp.common.util.search.AbstractSearchTest {

  public BinarySearchTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BinarySearchTest.class);
    //suite.addTest(new BinarySearchTest("testName"));
    return suite;
  }

  public void testBinarySearch() throws Exception {
    // The desired result of the search.
    final Integer FIVE = new Integer(5);
    final Integer FIFTYONE = new Integer(51);

    // The List object to search.
    final List numberList = new ArrayList(8);
    numberList.add(new Integer(2));
    numberList.add(new Integer(34));
    numberList.add(new Integer(10));
    numberList.add(new Integer(101));
    numberList.add(FIFTYONE);
    numberList.add(new Integer(17));
    numberList.add(new Integer(42));
    numberList.add(FIVE);

    // Create a SearchableList from the numberList.
    final SearchableList searchableNumberList = new SearchableList(numberList);

    // Create a Searcher!
    Searcher binarySearch = new BinarySearch(new DefaultBinarySearchFilter(FIVE));

    // Check the unsorted List.
    assertNull(binarySearch.search(searchableNumberList));

    // Sort the List and find the results!
    Collections.sort(searchableNumberList);
    assertEquals(FIVE, binarySearch.search(searchableNumberList));
    binarySearch = new BinarySearch(new DefaultBinarySearchFilter(FIFTYONE));
    assertEquals(FIFTYONE, binarySearch.search(searchableNumberList));
  }

}
