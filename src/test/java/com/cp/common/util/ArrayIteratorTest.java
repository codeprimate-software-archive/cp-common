/*
 * ArrayIteratorTest.java (c) 3 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.27
 * @see com.cp.common.util.ArrayIterator
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ArrayIteratorTest extends TestCase {

  public ArrayIteratorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ArrayIteratorTest.class);
    //suite.addTest(new ArrayIteratorTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    Iterator<Object> arrayIterator = null;

    assertNull(arrayIterator);

    try {
      arrayIterator = new ArrayIterator<Object>(new Object[] { "test", "tester", "testing" });
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ArrayIterator class with a non-null Object array threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(arrayIterator);
  }

  public void testInstantiateWithNullArray() throws Exception {
    try {
      new ArrayIterator<Object>(null);
      fail("Instantiating an instance of the ArrayIterator class with a null Object array should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The object array cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ArrayIterator class with a null Object array threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testHasNextWithEmptyArray() throws Exception {
    final Iterator<Object> arrayIterator = new ArrayIterator<Object>(new Object[0]);

    assertNotNull(arrayIterator);
    assertFalse(arrayIterator.hasNext());
  }

  public void testNext() throws Exception {
    final String[] animalArray = {
      "ardvark",
      "baboon",
      "cat",
      "dog",
      "elk",
      "ferret",
      "gerbal",
      "horse",
      "iguana",
      "jackel",
      "kangeroo",
      "lama",
      "mouse",
      "nothing",
      "octupus",
      "pika",
      "quale",
      "snake",
      "turtle",
      "usomething",
      "viper",
      "whale",
      "xwhat",
      "yack",
      "zebra"
    };

    final Iterator<String> animalArrayIterator = new ArrayIterator<String>(animalArray);

    assertNotNull(animalArrayIterator);

    for (final String animal : animalArray) {
      assertTrue(animalArrayIterator.hasNext());
      assertEquals(animal, animalArrayIterator.next());
    }
  }

  public void testRemove() throws Exception {
    final Iterator<Object> arrayIterator = new ArrayIterator<Object>(new Object[] { "test", "tester", "testing" });

    assertNotNull(arrayIterator);

    try {
      arrayIterator.remove();
      fail("Calling remove on the ArrayIterator class should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Not Implemented!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling remove on the ArrayIterator class threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    int count = 0;

    while (arrayIterator.hasNext()) {
      assertNotNull(arrayIterator.next());
      count++;
    }

    assertEquals(3, count);
  }

}
