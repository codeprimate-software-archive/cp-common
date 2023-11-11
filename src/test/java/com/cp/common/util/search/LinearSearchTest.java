/*
 * LinearSearchTest.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.19
 */

package com.cp.common.util.search;

import com.cp.common.util.search.*;
import com.cp.common.util.search.LinearSearch;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import junit.framework.Test;
import junit.framework.TestSuite;

public class LinearSearchTest extends com.cp.common.util.search.AbstractSearchTest {

  public LinearSearchTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LinearSearchTest.class);
    //suite.addTest(new LinearSearchTest("testName"));
    return suite;
  }

  public void testLinearSearchOnNumbers() throws Exception {
    // Create a Set of search results!
    final Set resultSet = new HashSet();
    resultSet.add(new Integer(1));
    resultSet.add(new Integer(2));
    resultSet.add(new Integer(3));
    resultSet.add(new Integer(5));
    resultSet.add(new Integer(8));
    resultSet.add(new Integer(13));
    resultSet.add(new Integer(21));

    // Create a number List to search!
    final List numberList = new ArrayList();
    numberList.add(new Integer(12));
    numberList.add(new Integer(1));
    numberList.add(new Integer(3));
    numberList.add(new Integer(16));
    numberList.add(new Integer(17));
    numberList.add(new Integer(51));
    numberList.add(new Integer(5));
    numberList.add(new Integer(0));
    numberList.add(new Integer(88));
    numberList.add(new Integer(2));
    numberList.add(new Integer(77));
    numberList.add(new Integer(13));
    numberList.add(new Integer(21));
    numberList.add(new Integer(14));
    numberList.add(new Integer(6));
    numberList.add(new Integer(15));
    numberList.add(new Integer(32));
    numberList.add(new Integer(8));

    // Create a SearchList from the numberList!
    final SearchableList searchableNumberList = new SearchableList(numberList);

    // Create a Searcher!
    final Searcher linearSearch = new LinearSearch(new DefaultSearchFilter(resultSet));

    // Get Search Results!
    final Collection searchResults = (Collection) linearSearch.search(searchableNumberList);

    assertNotNull(searchResults);
    assertEquals(resultSet.size(), searchResults.size());
    assertTrue(searchResults.containsAll(resultSet));
  }

  public void testLinearSearchOnStrings() throws Exception {
    // Create result set!
    final Set fishResultSet = new HashSet();
    fishResultSet.add("Trout");
    fishResultSet.add("Bass");
    fishResultSet.add("Walleye");
    fishResultSet.add("Carp");

    // Create a List to search!
    final List animalList = new ArrayList();
    animalList.add("Cat");
    animalList.add("Trout");
    animalList.add("Dog");
    animalList.add("Turtle");
    animalList.add("Snake");
    animalList.add("Whale");
    animalList.add("Starfish");
    animalList.add("Walleye");
    animalList.add("Tiger");
    animalList.add("Giraff");
    animalList.add("Elephant");
    animalList.add("Zebra");
    animalList.add("Monkey");
    animalList.add("Carp");

    // Create a SearchableList from the animalList!
    final SearchableList searchableAnimalList = new SearchableList(animalList);
    Collections.sort(searchableAnimalList);

    // Create a Searcher
    final Searcher linearSearcher = new LinearSearch(new DefaultSearchFilter(fishResultSet));

    // Get the Search Results!
    final Collection searchResults = (Collection) linearSearcher.search(searchableAnimalList);

    assertNotNull(searchResults);
    assertEquals(3, searchResults.size());
    assertTrue(fishResultSet.containsAll(searchResults));
  }

}
