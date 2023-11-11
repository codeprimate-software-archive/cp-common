/*
 * UniqueListTest.java (c) 3 February 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.3
 */

package com.cp.common.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UniqueListTest extends TestCase {

  public UniqueListTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(UniqueListTest.class);
    //suite.addTest(new UniqueListTest("testName"));
    return suite;
  }

  public void testUniqueness() throws Exception {
    final List<String> animalList = new UniqueList<String>(new ArrayList<String>(5));
    animalList.add("Ape");
    animalList.add("Baboon");
    animalList.add("Chimpanzee");
    animalList.add("Orangutang");

    assertTrue(animalList.contains("Ape"));
    assertTrue(animalList.contains("Baboon"));
    assertTrue(animalList.contains("Chimpanzee"));
    assertTrue(animalList.contains("Orangutang"));

    try {
      animalList.add("Ape");
      fail("The List already contains 'Ape' and should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    final List<Integer> primeNumberList = new UniqueList<Integer>(new LinkedList<Integer>());
    primeNumberList.add(1);
    primeNumberList.add(2);
    primeNumberList.add(3);
    primeNumberList.add(5);
    primeNumberList.add(7);

    assertTrue(primeNumberList.contains(1));
    assertTrue(primeNumberList.contains(2));
    assertTrue(primeNumberList.contains(3));
    assertTrue(primeNumberList.contains(5));
    assertTrue(primeNumberList.contains(7));

    try {
      primeNumberList.set(0, 5);
      fail("The List already contains 5 and should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }
  }

}
