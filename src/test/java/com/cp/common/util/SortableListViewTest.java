/*
 * SortableListViewTest.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.4.25
 */
package com.cp.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class SortableListViewTest extends TestCase {

  private static final Logger logger = Logger.getLogger(SortableListViewTest.class);

  public SortableListViewTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SortableListViewTest.class);
    //suite.addTest(new SortableListViewTest("testName"));
    return suite;
  }

  public void testSortableListView() throws Exception {
    final Object[] fruitArray =
        {
          "banana",
          "pear",
          "apple",
          "watermelon",
          "canalope",
          "orange",
          "pineapple",
          "grapefruit",
          "grape",
          "strawberry"
        };

    final Object[] sortedFruitArray = new Object[fruitArray.length];
    System.arraycopy(fruitArray, 0, sortedFruitArray, 0, fruitArray.length);
    Arrays.sort(sortedFruitArray);

    final List fruitList = Arrays.asList(fruitArray);
    final List fruitListView = new SortableListView(fruitList);

    logger.debug("Size: " + fruitListView.size());

    // Sort the fruit list.
    Collections.sort(fruitListView);

    // Ensure that the order of elements in the original list are unaffeted (NOT sorted)!
    int index = 0;
    for (Iterator it = fruitList.iterator(); it.hasNext();) {
      assertEquals(fruitArray[index++], it.next());
    }

    /*for (Iterator it = fruitListView.iterator(); it.hasNext(); )
    {
        logger.debug(it.next());
    }*/

    index = 0;
    for (Iterator it = fruitListView.iterator(); it.hasNext();) {
      assertEquals(sortedFruitArray[index++], it.next());
    }
  }

}
