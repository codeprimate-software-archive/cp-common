/*
 * CollectionEquatorTest.java (c) 21 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.21
 */

package com.cp.common.util;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CollectionEquatorTest extends TestCase {

  public CollectionEquatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CollectionEquatorTest.class);
    //suite.addTest(new CollectionEquatorTest("testName"));
    return suite;
  }

  public void testCollectionEquator() throws Exception {
    // STRINGS
    final List upperCaseStringList = new ArrayList();
    upperCaseStringList.add("ARDVARK");
    upperCaseStringList.add("BABOON");
    upperCaseStringList.add("CAT");
    upperCaseStringList.add("DOG");
    upperCaseStringList.add("ELEPHANT");

    final List lowerCaseStringList = new ArrayList();
    lowerCaseStringList.add("ardvark");
    lowerCaseStringList.add("baboon");
    lowerCaseStringList.add("cat");
    lowerCaseStringList.add("dog");
    lowerCaseStringList.add("elephant");

    assertTrue(new CollectionEquator().areEqual(upperCaseStringList, upperCaseStringList));
    assertFalse(new CollectionEquator().areEqual(upperCaseStringList, lowerCaseStringList));
    assertTrue(new CollectionEquator(CaseInsensitiveStringEquator.getInstance()).areEqual(upperCaseStringList, lowerCaseStringList));

    // NUMBERS
    final List primeNumbers = new ArrayList();
    primeNumbers.add(new Integer(1));
    primeNumbers.add(new Integer(3));
    primeNumbers.add(new Integer(5));
    primeNumbers.add(new Integer(7));
    primeNumbers.add(new Integer(11));
    primeNumbers.add(new Integer(13));
    primeNumbers.add(new Integer(17));

    final List numberList = new ArrayList();
    numberList.add(new Integer(1));
    numberList.add("three");
    numberList.add("5");
    numberList.add(new Short((short) 7));
    numberList.add("11");
    numberList.add(new Integer(13));
    numberList.add("seventeen");

    final List primeNumbers2 = new ArrayList();
    primeNumbers2.add(new Integer(1));
    primeNumbers2.add(new Integer(3));
    primeNumbers2.add(new Integer(5));
    primeNumbers2.add(new Integer(7));
    primeNumbers2.add(new Integer(11));

    assertTrue(new CollectionEquator().areEqual(primeNumbers, primeNumbers));
    assertFalse(new CollectionEquator().areEqual(primeNumbers, primeNumbers2));
    assertFalse(new CollectionEquator().areEqual(primeNumbers, numberList));

    primeNumbers2.add(new Integer(13));
    primeNumbers2.add(new Integer(17));

    assertTrue(new CollectionEquator().areEqual(primeNumbers, primeNumbers2));
  }

}
