/*
 * IteratorToEnumerationTest.java (c) 15 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.28
 * @see com.cp.common.util.IteratorToEnumeration
 * @see junit.framework.TestCase
 * @see org.jmock.Mockery
 */

package com.cp.common.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class IteratorToEnumerationTest extends TestCase {

  private final Mockery context = new Mockery();

  public IteratorToEnumerationTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IteratorToEnumerationTest.class);
    //suite.addTest(new IteratorToEnumerationTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    Enumeration<Object> enumx = null;

    assertNull(enumx);

    try {
      enumx = new IteratorToEnumeration<Object>(context.mock(Iterator.class));
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the IteratorToEnumeration class with a non-null Iterator object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(enumx);
  }

  public void testInstantiateWithNullIterator() throws Exception {
    try {
      new IteratorToEnumeration<Object>(null);
      fail("Instantiating an instance of the IteratorToEnumeration class with a null Iterator should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Iterator cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the IteratorToEnumeration class with a null Iterator threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testHasMoreElementsWithEmptyIterator() throws Exception {
    final Iterator<Object> mockIterator = context.mock(Iterator.class);

    context.checking(new Expectations() {{
      one(mockIterator).hasNext();
      will(returnValue(false));
    }});

    final Enumeration<Object> enumx = new IteratorToEnumeration<Object>(mockIterator);

    assertNotNull(enumx);
    assertFalse(enumx.hasMoreElements());

    context.assertIsSatisfied();
  }

  public void testNextElement() throws Exception {
    final List<String> animalList = new ArrayList<String>(11);
    animalList.add("Antelope");
    animalList.add("Buffalo");
    animalList.add("Cougar");
    animalList.add("Dragonfly");
    animalList.add("Elephant");
    animalList.add("Fox");
    animalList.add("Gorrilla");
    animalList.add("Horse");

    // Create an instance of the Enumeration interface.
    final Enumeration<String> enumx = new IteratorToEnumeration<String>(animalList.iterator());

    assertNotNull(enumx);

    for (final String animal : animalList) {
      assertTrue(enumx.hasMoreElements());
      assertEquals(animal, enumx.nextElement());
    }
  }

}
