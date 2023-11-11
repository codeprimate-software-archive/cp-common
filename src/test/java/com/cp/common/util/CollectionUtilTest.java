/*
 * CollectionUtilTest.java (c) 17 August 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.3.28
 * @see com.cp.common.util.CollectionUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import com.cp.common.lang.NumberUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Mockery;

public class CollectionUtilTest extends TestCase {

  private final Mockery context = new Mockery();

  public CollectionUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite testSuite = new TestSuite();
    testSuite.addTestSuite(CollectionUtilTest.class);
    //testSuite.addTest(new CollectionUtilTest("testName"));
    return testSuite;
  }

  public void testCombinations() throws Exception {
    final List<String> elementList = CollectionUtil.getList("A", "B", "C", "D");

    final List<List<String>> expectedCombinationList = new LinkedList<List<String>>();
    expectedCombinationList.add(CollectionUtil.getList("A"));
    expectedCombinationList.add(CollectionUtil.getList("B"));
    expectedCombinationList.add(CollectionUtil.getList("C"));
    expectedCombinationList.add(CollectionUtil.getList("D"));
    expectedCombinationList.add(CollectionUtil.getList("A", "B"));
    expectedCombinationList.add(CollectionUtil.getList("A", "C"));
    expectedCombinationList.add(CollectionUtil.getList("A", "D"));
    expectedCombinationList.add(CollectionUtil.getList("B", "C"));
    expectedCombinationList.add(CollectionUtil.getList("B", "D"));
    expectedCombinationList.add(CollectionUtil.getList("C", "D"));
    expectedCombinationList.add(CollectionUtil.getList("A", "B", "C"));
    expectedCombinationList.add(CollectionUtil.getList("A", "B", "D"));
    expectedCombinationList.add(CollectionUtil.getList("A", "C", "D"));
    expectedCombinationList.add(CollectionUtil.getList("B", "C", "D"));
    expectedCombinationList.add(CollectionUtil.getList("A", "B", "C", "D"));

    final List<List<String>> actualCombinationList = CollectionUtil.combinations(elementList);

    assertNotNull(actualCombinationList);
    assertEquals(expectedCombinationList, actualCombinationList);
  }

  public void testCopyStack() throws Exception {
    final Stack<String> expectedStack = new Stack<String>();
    expectedStack.push("1");
    expectedStack.push("2");
    expectedStack.push("3");

    final Stack<String> actualStack = CollectionUtil.copy(expectedStack);

    assertNotNull(actualStack);
    assertFalse(actualStack.isEmpty());
    assertEquals(expectedStack, actualStack);

    while (!actualStack.isEmpty()) {
      assertEquals(expectedStack.pop(), actualStack.pop());
    }
  }

  public void testEnumeration() throws Exception {
    final List<Integer> list = CollectionUtil.getList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    final Enumeration<Integer> enumeration = CollectionUtil.enumeration(list.iterator());

    assertNotNull(enumeration);

    for (final Integer value : list) {
      assertTrue(enumeration.hasMoreElements());
      assertEquals(value, enumeration.nextElement());
    }
  }

  public void testEnumerationWithNullIterator() throws Exception {
    try {
      CollectionUtil.enumeration(null);
      fail("Calling enumeration with a null Iterator object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Iterator object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling enumeration with a null Iterator object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindAllBy() throws Exception {
    final Filter<Integer> evenFilter = new Filter<Integer>() {
      public boolean accept(final Integer value) {
        return NumberUtil.isEven(value);
      }
    };

    final Collection<Integer> integers = CollectionUtil.getList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    final Collection<Integer> expectedIntegers = CollectionUtil.getList(0, 2, 4, 6, 8);
    final Collection<Integer> actualIntegers = CollectionUtil.findAllBy(integers, evenFilter);

    assertNotNull(actualIntegers);
    assertFalse(actualIntegers.isEmpty());
    assertEquals(expectedIntegers, actualIntegers);
  }

  public void testFindAllByReturnsEmptyCollection() throws Exception {
    final Filter<Integer> oddFilter = new Filter<Integer>() {
      public boolean accept(final Integer value) {
        return NumberUtil.isOdd(value);
      }
    };

    final Collection<Integer> integers = CollectionUtil.getList(0, 2, 4, 8, 16, 32, 64);
    final Collection<Integer> actualIntegers = CollectionUtil.findAllBy(integers, oddFilter);

    assertNotNull(actualIntegers);
    assertTrue(actualIntegers.isEmpty());
  }

  public void testFindAllByWithNullCollection() throws Exception {
    try {
      CollectionUtil.findAllBy(null, context.mock(Filter.class));
      fail("Calling findAllBy with a null Collection should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The collection of elements cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findAllBy with a null Collection threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindAllByWithNullFilter() throws Exception {
    try {
      CollectionUtil.findAllBy(Collections.emptyList(), null);
      fail("Calling findAllBy with a null Filter should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The filter object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findAllBy with a null Filter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindBy() throws Exception {
    final Filter<String> filter = new Filter<String>() {
      public boolean accept(final String value) {
        return "test".equals(value);
      }
    };

    final Collection<String> strings = CollectionUtil.getList("tester", "test", "testing", "tested");
    final String actualString = CollectionUtil.findBy(strings, filter);

    assertNotNull(actualString);
    assertEquals("test", actualString);
  }

  public void testFindByReturnsNull() throws Exception {
    final Filter<String> filter = new Filter<String>() {
      public boolean accept(final String value) {
        return "TEST".equals(value);
      }
    };

    final Collection<String> strings = CollectionUtil.getList("tester", "test", "testing", "tested");

    assertNull(CollectionUtil.findBy(strings, filter));
  }

  public void testFindByWithNullCollection() throws Exception {
    try {
      CollectionUtil.findBy(null, context.mock(Filter.class));
      fail("Calling findBy with a null Collection should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The collection of elements cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findBy with a null Collection threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindByWithNullFilter() throws Exception {
    try {
      CollectionUtil.findBy(Collections.emptyList(), null);
      fail("Calling findBy with a null Filter should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The filter object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findBy with a null Filter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetIterableForEnumeration() throws Exception {
    final Vector<String> vector = new Vector<String>(CollectionUtil.getList("test", "tester", "testing"));
    final Iterable<String> iterable = CollectionUtil.getIterable(vector.elements());
    final List<String> actualElements = new LinkedList<String>();

    for (final String element : iterable) {
      actualElements.add(element);
    }

    assertTrue(vector.containsAll(actualElements));
  }

  public void testGetIterableForIterator() throws Exception {
    final List<String> list = CollectionUtil.getList("test", "tester", "tseting");
    final Iterable<String> iterable = CollectionUtil.getIterable(list.iterator());
    final List<String> actualElements = new LinkedList<String>();

    for (final String element : iterable) {
      actualElements.add(element);
    }

    assertTrue(list.containsAll(actualElements));
  }

  public void testGetList() throws Exception {
    final List<Integer> expectedList = new LinkedList<Integer>();
    expectedList.add(0);
    expectedList.add(1);
    expectedList.add(2);
    expectedList.add(3);
    expectedList.add(4);

    final List<Integer> actualList = CollectionUtil.getList(0, 1, 2, 3, 4);

    assertNotNull(actualList);
    assertEquals(expectedList, actualList);
  }

  public void testGetSet() throws Exception {
    final Set<Integer> expectedSet = new HashSet<Integer>(5);
    expectedSet.add(0);
    expectedSet.add(1);
    expectedSet.add(2);
    expectedSet.add(3);
    expectedSet.add(4);

    final Set<Integer> actualSet = CollectionUtil.getSet(0, 1, 2, 3, 4);

    assertNotNull(actualSet);
    assertEquals(expectedSet, actualSet);
  }

  public void testIsEmpty() throws Exception {
    assertTrue(CollectionUtil.isEmpty((Collection) null));
    assertTrue(CollectionUtil.isEmpty(new ArrayList()));
    assertTrue(CollectionUtil.isEmpty(new ArrayList(10)));

    // test a List reference
    List<String> theList = null;
    assertTrue(CollectionUtil.isEmpty(theList));
    theList = new ArrayList<String>(29);
    assertTrue(CollectionUtil.isEmpty(theList));
    theList.add("Ardvark");
    theList.add("Baboon");
    theList.add("Chimpanzee");
    theList.add("Dog");
    theList.add("Elephant");
    theList.add("Ferret");
    assertFalse(CollectionUtil.isEmpty(theList));
    theList.clear();
    assertTrue(CollectionUtil.isEmpty(theList));
  }

  public void testIsFirstElement() throws Exception {
    final List<String> list = new ArrayList<String>(4);
    list.add("Jon");
    list.add("Jane");
    list.add("Bobby");
    list.add("Samantha");

    assertTrue(CollectionUtil.isFirstElement("Jon", list));
    assertTrue(!CollectionUtil.isFirstElement("Jane", list));
    assertTrue(!CollectionUtil.isFirstElement("Samantha", list));
    assertTrue(!CollectionUtil.isFirstElement("Doug", list));

    final List<Integer> list2 = new ArrayList<Integer>(7);
    Integer ONE = 1;
    list2.add(ONE);
    list2.add(2);
    list2.add(3);
    list2.add(5);
    list2.add(8);
    list2.add(13);
    list2.add(21);
    //...

    assertTrue(CollectionUtil.isFirstElement(ONE, list2));
    assertTrue(CollectionUtil.isFirstElement(1, list2));
    assertTrue(!CollectionUtil.isFirstElement(2, list2));
    assertTrue(!CollectionUtil.isFirstElement(21, list2));
    assertTrue(!CollectionUtil.isFirstElement(34, list2));
  }

  public void testIsLastElement() throws Exception {
    final List<String> list = new ArrayList<String>(4);
    list.add("Jon");
    list.add("Jane");
    list.add("Bobby");
    list.add("Samantha");

    assertTrue(CollectionUtil.isLastElement("Samantha", list));
    assertTrue(!CollectionUtil.isLastElement("Bobby", list));
    assertTrue(!CollectionUtil.isLastElement("Jon", list));
    assertTrue(!CollectionUtil.isLastElement("Belinda", list));

    final List<Integer> list2 = new ArrayList<Integer>(7);
    list2.add(1);
    list2.add(2);
    list2.add(3);
    list2.add(5);
    list2.add(8);
    list2.add(13);
    Integer TWENTYONE = 21;
    list2.add(TWENTYONE);
    //...

    assertTrue(CollectionUtil.isLastElement(TWENTYONE, list2));
    assertTrue(CollectionUtil.isLastElement(21, list2));
    assertTrue(!CollectionUtil.isLastElement(1, list2));
    assertTrue(!CollectionUtil.isLastElement(13, list2));
    assertTrue(!CollectionUtil.isLastElement(34, list2));
  }

  public void testIsNotEmpty() throws Exception {
    assertFalse(CollectionUtil.isNotEmpty((Collection) null));
    assertFalse(CollectionUtil.isNotEmpty(new ArrayList()));
    assertFalse(CollectionUtil.isNotEmpty(new ArrayList(10)));

    // test a List reference
    List<String> theList = null;
    assertFalse(CollectionUtil.isNotEmpty(theList));
    theList = new ArrayList<String>(29);
    assertFalse(CollectionUtil.isNotEmpty(theList));
    theList.add("Ardvark");
    theList.add("Baboon");
    theList.add("Chimpanzee");
    theList.add("Dog");
    theList.add("Elephant");
    theList.add("Ferret");
    assertTrue(CollectionUtil.isNotEmpty(theList));
    theList.clear();
    assertFalse(CollectionUtil.isNotEmpty(theList));
  }

  public void testIterator() throws Exception {
    final Vector<Integer> vector = new Vector<Integer>(CollectionUtil.getList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    final Iterator<Integer> iterator = CollectionUtil.iterator(vector.elements());

    assertNotNull(iterator);

    for (final Integer value : vector) {
      assertTrue(iterator.hasNext());
      assertEquals(value, iterator.next());
    }
  }

  public void testIteratorWithNullEnumeration() throws Exception {
    try {
      CollectionUtil.iterator(null);
      fail("Calling iterator with a null Enumeration object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Enumeration object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling iterator with a null Enumeration object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testSetDifference() throws Exception {
    // Prime Numbers
    final Set<Integer> primeNumbers = new HashSet<Integer>();
    primeNumbers.add(1);
    primeNumbers.add(2);
    primeNumbers.add(3);
    primeNumbers.add(5);
    primeNumbers.add(7);
    primeNumbers.add(11);
    primeNumbers.add(13);
    primeNumbers.add(17);
    primeNumbers.add(19);

    // Fibonacci Numbers
    final Set<Integer> fibonacciNumbers = new HashSet<Integer>();
    fibonacciNumbers.add(1);
    fibonacciNumbers.add(2);
    fibonacciNumbers.add(3);
    fibonacciNumbers.add(5);
    fibonacciNumbers.add(8);
    fibonacciNumbers.add(13);

    // Desired Result
    final Set<Integer> expectedResult = new HashSet<Integer>();
    expectedResult.add(7);
    expectedResult.add(11);
    expectedResult.add(17);
    expectedResult.add(19);

    // The Result
    final Set theResult = CollectionUtil.setDifference(primeNumbers, fibonacciNumbers);
    assertNotNull(theResult);
    assertFalse(theResult.isEmpty());
    assertEquals(expectedResult.size(), theResult.size());
    assertTrue(theResult.containsAll(expectedResult));
  }

  public void testSetIntersection() throws Exception {
    // Prime Numbers
    final Set<Integer> primeNumbers = new HashSet<Integer>();
    primeNumbers.add(1);
    primeNumbers.add(2);
    primeNumbers.add(3);
    primeNumbers.add(5);
    primeNumbers.add(7);
    primeNumbers.add(11);
    primeNumbers.add(13);
    primeNumbers.add(17);
    primeNumbers.add(19);

    // Fibonacci Numbers
    final Set<Integer> fibonacciNumbers = new HashSet<Integer>();
    fibonacciNumbers.add(1);
    fibonacciNumbers.add(2);
    fibonacciNumbers.add(3);
    fibonacciNumbers.add(5);
    fibonacciNumbers.add(8);
    fibonacciNumbers.add(13);

    // Desired Result
    final Set<Integer> expectedResult = new HashSet<Integer>();
    expectedResult.add(1);
    expectedResult.add(2);
    expectedResult.add(3);
    expectedResult.add(5);
    expectedResult.add(13);

    // The Result
    final Set theResult = CollectionUtil.setIntersection(primeNumbers, fibonacciNumbers);
    assertNotNull(theResult);
    assertFalse(theResult.isEmpty());
    assertEquals(expectedResult.size(), theResult.size());
    assertTrue(theResult.containsAll(expectedResult));
  }

  public void testSetUnion() throws Exception {
    // Prime Numbers
    final Set<Integer> primeNumbers = new HashSet<Integer>();
    primeNumbers.add(1);
    primeNumbers.add(2);
    primeNumbers.add(3);
    primeNumbers.add(5);
    primeNumbers.add(7);
    primeNumbers.add(11);
    primeNumbers.add(13);
    primeNumbers.add(17);
    primeNumbers.add(19);

    // Fibonacci Numbers
    final Set<Integer> fibonacciNumbers = new HashSet<Integer>();
    fibonacciNumbers.add(1);
    fibonacciNumbers.add(2);
    fibonacciNumbers.add(3);
    fibonacciNumbers.add(5);
    fibonacciNumbers.add(8);
    fibonacciNumbers.add(13);

    // Desired Result
    final Set<Integer> expectedResult = new HashSet<Integer>();
    expectedResult.add(1);
    expectedResult.add(2);
    expectedResult.add(3);
    expectedResult.add(5);
    expectedResult.add(7);
    expectedResult.add(8);
    expectedResult.add(11);
    expectedResult.add(13);
    expectedResult.add(17);
    expectedResult.add(19);

    // The Result
    final Set theResult = CollectionUtil.setUnion(primeNumbers, fibonacciNumbers);

    assertNotNull(theResult);
    assertFalse(theResult.isEmpty());
    assertEquals(expectedResult.size(), theResult.size());
    assertTrue(theResult.containsAll(expectedResult));
  }

  public void testSize() throws Exception {
    assertEquals(0, CollectionUtil.size(null));

    List<String> animalList = null;

    assertEquals(0, CollectionUtil.size(animalList));

    animalList = new ArrayList<String>();

    assertEquals(0, CollectionUtil.size(animalList));

    animalList.add("Cat");
    animalList.add("Dog");
    animalList.add("Rabbit");

    assertEquals(3, CollectionUtil.size(animalList));
  }

  public void testToString() throws Exception {
    // Construct a List of animals!
    final List<String> animalList = new ArrayList<String>();
    animalList.add("Ardvark");
    animalList.add("Baboon");
    animalList.add("Cat");
    animalList.add("Dog");
    animalList.add("Elephant");

    // The resulting String after calling CollectionUtil.toString on the List.
    final String resultString = "[Ardvark, Baboon, Cat, Dog, Elephant]";

    assertEquals(resultString, CollectionUtil.toString(animalList));
    assertEquals("[]", CollectionUtil.toString(new ArrayList()));
  }

  public void testUnmodifiableIterator() throws Exception {
    final List<String> list = new ArrayList<String>();
    list.add("Arizona");
    list.add("California");
    list.add("Idaho");
    list.add("Nevada");
    list.add("Oregon");
    list.add("Washington");

    // Make a copy of the List object.
    final List<String> listCopy = new ArrayList<String>(list);

    int count = 0;
    for (final Iterator it = CollectionUtil.unmodifiableIterator(list.iterator()); it.hasNext();) {
      final String state = (String) it.next();
      if ((count % 2) == 0) {
        try {
          it.remove();
          fail("Should not be able to remove an item from the List object with an unmodifiable Iterator!");
        }
        catch (UnsupportedOperationException ignore) {
          // If we get here, than the unmodifiableIterator is working correctly.
        }
      }
      count++;
    }

    assertTrue(list.containsAll(listCopy));
  }

  public void testUnmodifiableListIterator() throws Exception {
    final List<String> list = new ArrayList<String>();
    list.add("Africa");
    list.add("Canada");
    list.add("China");
    list.add("France");
    list.add("Germany");
    list.add("India");
    list.add("Japan");
    list.add("Korea");
    list.add("Mexico");
    list.add("Spain");
    list.add("Turkey");
    list.add("United States");

    // Make a copy of the List object.
    final List<String> listCopy = new ArrayList<String>(list);

    int count = 0;
    for (final ListIterator it = CollectionUtil.unmodifiableListIterator(list.listIterator()); it.hasNext();) {
      final String country = (String) it.next();
      try {
        if ((count % 2) == 0) { // All even indexed items.
          it.set("England");
        }
        else if ((count % 4) == 1) { // Every 5th item.
          it.add("Iceland");
        }
        else { // Everything else (odd items).
          it.remove();
        }
        fail("Should not be able to modify the List object with an unmodifiable ListIerator!");
      }
      catch (UnsupportedOperationException ignore) {
        // If we got here, then the unmodifiableListIterator method is working.
      }
      finally {
        count++;
      }
    }

    assertTrue(!list.contains("England"));
    assertTrue(!list.contains("Iceland"));
    assertTrue(list.contains("United States"));
    assertTrue(list.containsAll(listCopy));
  }

}
