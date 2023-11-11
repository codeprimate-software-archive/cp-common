/*
 * SortAscendingComparatorTest.java (c) 18 October 2003
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
import org.apache.log4j.Logger;

public class SortAscendingComparatorTest extends TestCase {

  private static final Logger logger = Logger.getLogger(SortAscendingComparatorTest.class);

  public SortAscendingComparatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SortAscendingComparatorTest.class);
    //suite.addTest(new SortAscendingComparatorTest(("testName")));
    return suite;
  }

  public void testSortAscedingComparator() throws Exception {
    final String[] animalArray =
    {
      "Ardvark",
      "Baboon",
      "Cat",
      "Dog",
      "Elephant"
    };

    final List animalList = new ArrayList();
    animalList.add("Cat");
    animalList.add("Elephant");
    animalList.add("Baboon");
    animalList.add("Ardvark");
    animalList.add("Dog");

    int index = 0;
    for (Iterator it = animalList.iterator(); it.hasNext(); ) {
      assertNotSame(animalArray[index++], it.next());
    }

    Collections.sort(animalList, SortAscendingComparator.getInstance());

    index = 0;
    for (Iterator it = animalList.iterator(); it.hasNext(); ) {
      assertEquals(animalArray[index++], it.next());
    }
  }

}
