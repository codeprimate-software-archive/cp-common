/*
 * SortDescendingComparatorTest.java (c) 18 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.18
 */

package com.cp.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SortDescendingComparatorTest extends TestCase {

  public SortDescendingComparatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SortDescendingComparatorTest.class);
    //suite.addTest(new SortDescendingComparatorTest("testName"));
    return suite;
  }

  public void testSortDescendingComparator() throws Exception {
    final String[] animalArray =
    {
      "Elephant",
      "Dog",
      "Cat",
      "Baboon",
      "Ardvark"
    };

    final List animalList = new ArrayList();
    animalList.add("Cat");
    animalList.add("Ardvark");
    animalList.add("Baboon");
    animalList.add("Elephant");
    animalList.add("Dog");

    int index = 0;
    for (Iterator it = animalList.iterator(); it.hasNext(); ) {
      assertNotSame(animalArray[index++], it.next());
    }

    Collections.sort(animalList, SortDescendingComparator.getInstance());

    index = 0;
    for (Iterator it = animalList.iterator(); it.hasNext(); ) {
      assertEquals(animalArray[index++], it.next());
    }
  }

}
