/*
 * SearchableSortableCollectionTest.java (c) 25 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 */

package com.cp.common.util;

import com.cp.common.util.search.BinarySearch;
import com.cp.common.util.search.BinarySearchFilter;
import com.cp.common.util.search.SearchException;
import com.cp.common.util.search.Searcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class SearchableSortableCollectionTest extends TestCase {

  private static final Logger logger = Logger.getLogger(SearchableSortableCollectionTest.class);

  public SearchableSortableCollectionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SearchableSortableCollectionTest.class);
    //suite.addTest(new SearchableSortableCollectionTest("testName"));
    return suite;
  }

  public BinarySearchFilter getBinarySearchFilter(final String value) {
    return new BinarySearchFilter() {
      public int compare(final Object obj) throws SearchException {
        if (logger.isDebugEnabled()) {
          logger.debug("obj (" + obj + ")");
          logger.debug("value (" + value + ")");
        }
        return value.compareTo((String) obj);
      }

      public boolean matches(Object obj) throws SearchException {
        if (logger.isDebugEnabled()) {
          logger.debug("obj (" + obj + ")");
          logger.debug("value (" + value + ")");
        }
        return value.equals(obj);
      }
    };
  }

  public void testSearchableSortableCollection() throws Exception {
    final String[] animalArray = {
      "Antelope",
      "Baboon",
      "Chimpanzee",
      "Deer",
      "Elephant",
      "Farret",
      "Gorilla",
      "Horse",
      "Iguana",
      "Jackel",
      "Kangaroo",
      "Lizard",
      "Monkey",
      "Newt",
      "Octopus",
      "Platapus",
      "Quail",
      "Raven",
      "Snail",
      "Turtle",
      "Urchin",
      "Vulture",
      "Wallrus"
    };

    final List animalList = new ArrayList(Arrays.asList(animalArray));
    Collections.shuffle(animalList);

    // SORT!

    // Assert that the List is not sorted!
    boolean assertOrdered = true;
    int index = 0;
    for (Iterator it = animalList.iterator(); it.hasNext(); ) {
      assertOrdered &= it.next().equals(animalArray[index++]);
    }

    logger.debug("assertOrdered = " + assertOrdered);
    assertFalse(assertOrdered);

    final SearchableSortableCollection animalCollection = new SearchableSortableCollection(animalList);

    // Sort the Collection & assert that it is sorted!
    new com.cp.common.util.sort.QuickSort().sort(animalCollection);
    index = 0;
    for (Iterator it = animalCollection.iterator(); it.hasNext(); ) {
      assertEquals(animalArray[index++], it.next());
    }

    logger.debug("Sorted!");

    // SEARCH!

    if (logger.isDebugEnabled()) {
      logger.debug("animalCollection: " + animalCollection);
    }
    Searcher binarySearch = new BinarySearch(getBinarySearchFilter("Elephant"));
    String searchResult = (String) binarySearch.search(animalCollection);
    logger.debug("searchResult = " + searchResult);
    assertNotNull(searchResult);
    assertEquals("Elephant", searchResult);
    logger.debug("Found Elephant!");

    binarySearch = new BinarySearch(getBinarySearchFilter("Wallrus"));
    searchResult = (String) binarySearch.search(animalCollection);
    logger.debug("searchResult = " + searchResult);
    assertNotNull(searchResult);
    assertEquals("Wallrus", searchResult);
    logger.debug("Found Wallrus!");

    binarySearch = new com.cp.common.util.search.BinarySearch(getBinarySearchFilter("Leach"));
    searchResult = (String) binarySearch.search(animalCollection);
    logger.debug("searchResult = " + searchResult);
    assertNull(searchResult);
    logger.debug("Did NOT find Leach!");
  }

}
