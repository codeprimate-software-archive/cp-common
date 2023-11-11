/*
 * AbstractVisitableCollectionTest.java (c) 15 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.15
 * @see com.cp.common.util.AbstractVisitableCollection
 * @see com.cp.common.util.VisitableCollection
 */

package com.cp.common.util;

import com.cp.common.test.mock.MockVisitableObject;
import com.cp.common.test.mock.MockVisitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractVisitableCollectionTest extends TestCase {

  public AbstractVisitableCollectionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractVisitableCollectionTest.class);
    //suite.addTest(new AbstractVisitableCollectionTest("testName"));
    return suite;
  }

  public void testAddToList() throws Exception {
    final VisitableCollection<MockVisitableObject> visitableList = AbstractVisitableCollection.getVisitableList();

    assertNotNull(visitableList);
    assertEquals(0, visitableList.size());
    assertTrue(visitableList.add(new MockVisitableObject(0)));
    assertEquals(1, visitableList.size());
    assertTrue(visitableList.add(new MockVisitableObject(1)));
    assertEquals(2, visitableList.size());
    assertTrue(visitableList.add(new MockVisitableObject(1)));
    assertEquals(3, visitableList.size());
    assertFalse(visitableList.add(null));
    assertEquals(3, visitableList.size());
  }

  public void testAddToSet() throws Exception {
    final VisitableCollection<MockVisitableObject> visitableSet = AbstractVisitableCollection.getVisitableSet();

    assertNotNull(visitableSet);
    assertEquals(0, visitableSet.size());
    assertTrue(visitableSet.add(new MockVisitableObject(0)));
    assertEquals(1, visitableSet.size());
    assertTrue(visitableSet.add(new MockVisitableObject(1)));
    assertEquals(2, visitableSet.size());
    assertFalse(visitableSet.add(new MockVisitableObject(1)));
    assertEquals(2, visitableSet.size());
    assertFalse(visitableSet.add(null));
    assertEquals(2, visitableSet.size());
  }

  public void testContainsInList() throws Exception {
    final MockVisitableObject visitableObject = new MockVisitableObject(0);

    final VisitableCollection<MockVisitableObject> visitableList = AbstractVisitableCollection.getVisitableList();

    assertNotNull(visitableList);
    assertTrue(visitableList.isEmpty());
    assertTrue(visitableList.add(visitableObject));
    assertFalse(visitableList.isEmpty());
    assertTrue(visitableList.contains(visitableObject));
  }

  public void testContainsInSet() throws Exception {
    final MockVisitableObject visitableObject = new MockVisitableObject(0);
    final MockVisitableObject visitableObjectAgain = new MockVisitableObject(0);

    final VisitableCollection<MockVisitableObject> visitableSet = AbstractVisitableCollection.getVisitableSet();

    assertNotNull(visitableSet);
    assertTrue(visitableSet.isEmpty());
    assertTrue(visitableSet.add(visitableObject));
    assertFalse(visitableSet.isEmpty());
    assertTrue(visitableSet.contains(visitableObject));
    assertFalse(visitableSet.add(visitableObjectAgain));
    assertFalse(visitableSet.isEmpty());
    assertTrue(visitableSet.contains(visitableObjectAgain));
  }

  public void testRemoveFromList() throws Exception {
    final MockVisitableObject visitableObject = new MockVisitableObject(0);

    final VisitableCollection<MockVisitableObject> visitableList = AbstractVisitableCollection.getVisitableList();

    assertNotNull(visitableList);
    assertEquals(0, visitableList.size());
    assertTrue(visitableList.add(visitableObject));
    assertEquals(1, visitableList.size());
    assertFalse(visitableList.remove(new MockVisitableObject(1)));
    assertEquals(1, visitableList.size());
    assertTrue(visitableList.remove(visitableObject));
    assertEquals(0, visitableList.size());
  }

  public void testRemoveFromSet() throws Exception {
    final MockVisitableObject visitableObject = new MockVisitableObject(0);

    final VisitableCollection<MockVisitableObject> visitableSet = AbstractVisitableCollection.getVisitableSet();

    assertNotNull(visitableSet);
    assertEquals(0, visitableSet.size());
    assertTrue(visitableSet.add(visitableObject));
    assertEquals(1, visitableSet.size());
    assertFalse(visitableSet.remove(new MockVisitableObject(1)));
    assertEquals(1, visitableSet.size());
    assertTrue(visitableSet.remove(visitableObject));
    assertEquals(0, visitableSet.size());
  }

  public void testVisitableList() throws Exception {
    final MockVisitableObject visitableObjectZero = new MockVisitableObject(0);
    final MockVisitableObject visitableObjectOne = new MockVisitableObject(1);
    final MockVisitableObject visitableObjectTwo = new MockVisitableObject(2);
    final MockVisitableObject visitableObjectTwoAgain = new MockVisitableObject(2);

    final VisitableCollection<MockVisitableObject> visitableObjectCollection = AbstractVisitableCollection.getVisitableList(
      visitableObjectZero, visitableObjectOne, visitableObjectTwo, visitableObjectTwoAgain);

    assertNotNull(visitableObjectCollection);
    assertEquals(4, visitableObjectCollection.size());
    assertTrue(visitableObjectCollection.contains(visitableObjectZero));
    assertTrue(visitableObjectCollection.contains(visitableObjectOne));
    assertTrue(visitableObjectCollection.contains(visitableObjectTwo));
    assertTrue(visitableObjectCollection.contains(visitableObjectTwoAgain));
    assertFalse(visitableObjectZero.isVisited());
    assertFalse(visitableObjectOne.isVisited());
    assertFalse(visitableObjectTwo.isVisited());
    assertFalse(visitableObjectTwoAgain.isVisited());

    visitableObjectCollection.accept(new MockVisitor());

    assertTrue(visitableObjectZero.isVisited());
    assertTrue(visitableObjectOne.isVisited());
    assertTrue(visitableObjectTwo.isVisited());
    assertTrue(visitableObjectTwoAgain.isVisited());
  }

  public void testVisitableSet() throws Exception {
    final MockVisitableObject visitableObjectZero = new MockVisitableObject(0);
    final MockVisitableObject visitableObjectOne = new MockVisitableObject(1);
    final MockVisitableObject visitableObjectTwo = new MockVisitableObject(2);
    final MockVisitableObject visitableObjectTwoAgain = new MockVisitableObject(2);

    final VisitableCollection<MockVisitableObject> visitableObjectCollection = AbstractVisitableCollection.getVisitableSet(
      visitableObjectZero, visitableObjectOne, visitableObjectTwo, visitableObjectTwoAgain);

    assertNotNull(visitableObjectCollection);
    assertEquals(3, visitableObjectCollection.size());
    assertTrue(visitableObjectCollection.contains(visitableObjectZero));
    assertTrue(visitableObjectCollection.contains(visitableObjectOne));
    assertTrue(visitableObjectCollection.contains(visitableObjectTwo));
    assertTrue(visitableObjectCollection.contains(visitableObjectTwoAgain));
    assertFalse(visitableObjectZero.isVisited());
    assertFalse(visitableObjectOne.isVisited());
    assertFalse(visitableObjectTwo.isVisited());
    assertFalse(visitableObjectTwoAgain.isVisited());

    visitableObjectCollection.accept(new MockVisitor());

    assertTrue(visitableObjectZero.isVisited());
    assertTrue(visitableObjectOne.isVisited());
    assertTrue(visitableObjectTwo.isVisited());
    assertFalse(visitableObjectTwoAgain.isVisited());
  }

}
