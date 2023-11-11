/*
 * EnumerationToIteratorTest.java (c) 15 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.28
 * @see com.cp.common.util.EnumerationToIterator
 * @see junit.framework.TestCase
 * @see org.jmock.Mockery
 */

package com.cp.common.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class EnumerationToIteratorTest extends TestCase {

  private final Mockery context = new Mockery();

  public EnumerationToIteratorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(EnumerationToIteratorTest.class);
    //suite.addTest(new EnumerationToIteratorTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    Iterator<Object> it = null;

    assertNull(it);

    try {
      it = new EnumerationToIterator<Object>(context.mock(Enumeration.class));
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the EnumerationToIterator class with a non-null Enumeration object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(it);
  }

  public void testInstantiateWithNullEnumeration() throws Exception {
    try {
      new EnumerationToIterator<Object>(null);
      fail("Instantiating an instance of the EnumerationToIterator class with a null Enumeration object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Enumeration cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the EnumerationToIterator class with a null Enumeration object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testHasNextWithEmptyEnumeration() throws Exception {
    final Enumeration mockEnumeration = context.mock(Enumeration.class);

    context.checking(new Expectations() {{
      oneOf(mockEnumeration).hasMoreElements();
      will(returnValue(false));
    }});

    final Iterator<Object> it = new EnumerationToIterator<Object>(mockEnumeration);

    assertNotNull(it);
    assertFalse(it.hasNext());

    context.assertIsSatisfied();
  }

  public void testNext() throws Exception {
    final Vector<String> animalList = new Vector<String>(11);
    animalList.add("Antelope");
    animalList.add("Buffalo");
    animalList.add("Cougar");
    animalList.add("Dragonfly");
    animalList.add("Elephant");
    animalList.add("Fox");
    animalList.add("Gorrilla");
    animalList.add("Horse");

    // Create an instance of the Enumeration interface.
    final Iterator<String> it = new EnumerationToIterator<String>(animalList.elements());

    for (final String animal : animalList) {
      assertTrue(it.hasNext());
      assertEquals(animal, it.next());
    }
  }

  public void testRemove() throws Exception {
    final Enumeration<Object> mockEnumeration = context.mock(Enumeration.class);

    context.checking(new Expectations() {{
      atLeast(1).of(mockEnumeration).hasMoreElements();
      will(returnValue(true));
    }});

    final Iterator<Object> it = new EnumerationToIterator<Object>(mockEnumeration);

    assertNotNull(it);
    assertTrue(it.hasNext());

    try {
      it.remove();
      fail("Calling remove on the EnumerationToIterator class should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Operation Not Supported!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling remove on the EnumerationToIterator class threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

}
